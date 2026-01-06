import mysql.connector
import requests
from bs4 import BeautifulSoup
import os
import time
import random
import re

# Database configuration
db_config = {
    "host": "localhost",
    "user": "root",
    "password": "ebrim@CJ@lloh25",
    "database": "_backend"
}

upload_dir = "Backend/uploads/movies"
if not os.path.exists(upload_dir):
    os.makedirs(upload_dir)

def slugify(text):
    text = text.lower()
    text = re.sub(r'[^\w\s-]', '', text)
    text = re.sub(r'[\s_-]+', '_', text)
    text = re.sub(r'^-+|-+$', '', text)
    return text

def get_poster_url(title, year):
    search_url = f"https://www.themoviedb.org/search?query={title.replace(' ', '+')}"
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
    }
    
    try:
        response = requests.get(search_url, headers=headers, timeout=10)
        if response.status_code != 200:
            return None
        
        soup = BeautifulSoup(response.text, 'html.parser')
        results = soup.select('.results .card')
        if not results:
            return None
        
        img = results[0].select_one('img.poster')
        if img and img.get('src'):
            src = img.get('src')
            if '/t/p/' in src:
                return re.sub(r'/t/p/w[^/]+/', '/t/p/w500/', src)
            return src
    except Exception as e:
        print(f"Error searching for {title}: {e}")
    return None

def download_image(url, filename):
    try:
        headers = {"User-Agent": "Mozilla/5.0"}
        response = requests.get(url, headers=headers, stream=True, timeout=10)
        if response.status_code == 200:
            with open(os.path.join(upload_dir, filename), 'wb') as f:
                for chunk in response.iter_content(1024):
                    f.write(chunk)
            return True
    except Exception as e:
        print(f"Error downloading {url}: {e}")
    return False

def main():
    conn = mysql.connector.connect(**db_config)
    cursor = conn.cursor(dictionary=True)
    
    # Fix the first 60 movies (up to Transformers: Revenge of the Fallen)
    # We'll use the title as the filename as suggested
    cursor.execute("SELECT id, title, release_year FROM movies WHERE id <= 60 ORDER BY id ASC")
    movies = cursor.fetchall()
    
    print(f"Fixing first {len(movies)} movies...")
    
    for movie in movies:
        title = movie['title']
        year = movie['release_year']
        movie_id = movie['id']
        
        # Create a safe filename from the title
        safe_title = slugify(title)
        filename = f"{safe_title}_{movie_id}.jpg"
        
        print(f"Processing: {title} ({year}) -> {filename}")
        
        poster_url = get_poster_url(title, year)
        if poster_url:
            if not poster_url.startswith('http'):
                poster_url = "https://www.themoviedb.org" + poster_url
            
            if download_image(poster_url, filename):
                # Update database
                cursor.execute("UPDATE movies SET image_url = %s, image_filename = %s WHERE id = %s", (filename, filename, movie_id))
                conn.commit()
                print(f"Successfully fixed {title}")
            else:
                print(f"Failed to download image for {title}")
        else:
            print(f"No poster found for {title}")
        
        time.sleep(random.uniform(1, 2))
    
    cursor.close()
    conn.close()
    print("Done fixing movies!")

if __name__ == "__main__":
    main()
