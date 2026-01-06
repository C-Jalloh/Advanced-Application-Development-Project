import mysql.connector
import requests
from bs4 import BeautifulSoup
import os
import time
from datetime import datetime

# Database configuration
db_config = {
    "host": "localhost",
    "user": "root",
    "password": "ebrim@CJ@lloh25",
    "database": "_backend"
}

upload_dir = "Backend/uploads/music"
if not os.path.exists(upload_dir):
    os.makedirs(upload_dir)

def download_image(url, filename):
    if not url or not url.startswith('http'):
        return None
    try:
        headers = {"User-Agent": "Mozilla/5.0"}
        response = requests.get(url, headers=headers, stream=True, timeout=10)
        if response.status_code == 200:
            path = os.path.join(upload_dir, filename)
            with open(path, 'wb') as f:
                for chunk in response.iter_content(1024):
                    f.write(chunk)
            return filename
    except Exception as e:
        print(f"Error downloading {url}: {e}")
    return None

def scrape_billboard():
    url = "https://www.billboard.com/charts/hot-100/"
    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36"
    }
    
    response = requests.get(url, headers=headers)
    if response.status_code != 200:
        print(f"Failed to fetch Billboard: {response.status_code}")
        return []
    
    soup = BeautifulSoup(response.text, 'html.parser')
    tracks = []
    
    # Billboard structure is a bit complex, but usually items are in .o-chart-results-list-row-container
    items = soup.select('.o-chart-results-list-row-container')
    
    for item in items:
        try:
            # Title is in an h3 with id title-of-a-story
            title_tag = item.select_one('h3#title-of-a-story')
            if not title_tag: continue
            title = title_tag.get_text(strip=True)
            
            # Artist is in the next span with class c-label
            artist_tag = item.select_one('span.c-label.a-no-trucate')
            artist = artist_tag.get_text(strip=True) if artist_tag else "Unknown Artist"
            
            # Image
            img_tag = item.select_one('img.c-lazy-image__img')
            img_url = img_tag.get('data-lazy-src') or img_tag.get('src') if img_tag else None
            
            tracks.append({
                "title": title,
                "artist": artist,
                "imageUrl": img_url,
                "releaseYear": datetime.now().year,
                "duration": 180 + (len(title) * 2) % 60, # Fake duration
                "genre": "Pop" # Default for Hot 100
            })
        except Exception as e:
            print(f"Error parsing item: {e}")
            
    return tracks

def main():
    print("Scraping Billboard Hot 100...")
    tracks = scrape_billboard()
    print(f"Found {len(tracks)} tracks.")
    
    if not tracks:
        return

    conn = mysql.connector.connect(**db_config)
    cursor = conn.cursor()
    
    # Clear existing music to avoid duplicates during this import (optional)
    # cursor.execute("DELETE FROM music")
    # conn.commit()

    for i, track in enumerate(tracks):
        print(f"Importing: {track['title']} by {track['artist']}...")
        
        # Download image
        image_filename = f"music_{i+1}.jpg"
        saved_filename = download_image(track['imageUrl'], image_filename)
        
        # Insert into music table
        # Note: The MusicService will handle normalization if we use the API, 
        # but here we are inserting directly into DB. 
        # To trigger normalization, we should either:
        # 1. Use the API (slower)
        # 2. Manually handle normalization here (better for bulk)
        
        # Let's handle normalization here to be consistent with 3NF
        
        # 1. Artist
        cursor.execute("SELECT id FROM artists WHERE name = %s", (track['artist'],))
        res = cursor.fetchone()
        if res:
            artist_id = res[0]
        else:
            cursor.execute("INSERT INTO artists (name) VALUES (%s)", (track['artist'],))
            artist_id = cursor.lastrowid
            
        # 2. Album (Billboard doesn't give album, so we'll use "Single" or Title)
        album_title = f"{track['title']} - Single"
        cursor.execute("SELECT id FROM albums WHERE title = %s AND artist_id = %s", (album_title, artist_id))
        res = cursor.fetchone()
        if res:
            album_id = res[0]
        else:
            cursor.execute("INSERT INTO albums (title, artist_id, release_year) VALUES (%s, %s, %s)", 
                           (album_title, artist_id, track['releaseYear']))
            album_id = cursor.lastrowid
            
        # 3. Genre
        cursor.execute("SELECT id FROM music_genres WHERE name = %s", (track['genre'],))
        res = cursor.fetchone()
        if res:
            genre_id = res[0]
        else:
            cursor.execute("INSERT INTO music_genres (name) VALUES (%s)", (track['genre'],))
            genre_id = cursor.lastrowid
            
        # 4. Music
        sql = """INSERT INTO music (title, artist, album, genre, release_year, duration, image_url, image_filename, album_id, created_at) 
                 VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"""
        values = (
            track['title'],
            track['artist'],
            album_title,
            track['genre'],
            track['releaseYear'],
            track['duration'],
            saved_filename,
            saved_filename,
            album_id,
            datetime.now()
        )
        cursor.execute(sql, values)
        music_id = cursor.lastrowid
        
        # 5. Link Genre
        cursor.execute("INSERT INTO music_to_genres (music_id, genre_id) VALUES (%s, %s)", (music_id, genre_id))
        
        conn.commit()

    cursor.close()
    conn.close()
    print("Done!")

if __name__ == "__main__":
    main()
