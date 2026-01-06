import mysql.connector
import os

# Database configuration
db_config = {
    "host": "localhost",
    "user": "root",
    "password": "ebrim@CJ@lloh25",
    "database": "_backend"
}

def pair_movies():
    print("Pairing movies...")
    conn = mysql.connector.connect(**db_config)
    cursor = conn.cursor(dictionary=True)
    
    cursor.execute("SELECT id, title FROM movies ORDER BY id ASC")
    movies = cursor.fetchall()
    
    for movie in movies:
        movie_id = movie['id']
        filename = f"movie_{movie_id}.jpg"
        
        # Update DB to use the standard filename
        cursor.execute("UPDATE movies SET image_url = %s, image_filename = %s WHERE id = %s", 
                       (filename, filename, movie_id))
    
    conn.commit()
    cursor.close()
    conn.close()
    print(f"Updated {len(movies)} movie records.")

def pair_music():
    print("Pairing music...")
    conn = mysql.connector.connect(**db_config)
    cursor = conn.cursor(dictionary=True)
    
    cursor.execute("SELECT id, title FROM music ORDER BY id ASC")
    music_list = cursor.fetchall()
    
    for i, song in enumerate(music_list):
        song_id = song['id']
        # We pair them chronologically by ID
        # If the scraper saved them as music_1, music_2... based on loop index,
        # and the IDs are 1, 2, 3... then music_{id}.jpg is correct.
        filename = f"music_{song_id}.jpg"
        
        cursor.execute("UPDATE music SET image_url = %s, image_filename = %s WHERE id = %s", 
                       (filename, filename, song_id))
    
    conn.commit()
    cursor.close()
    conn.close()
    print(f"Updated {len(music_list)} music records.")

if __name__ == "__main__":
    pair_movies()
    pair_music()
    print("All assets paired successfully!")
