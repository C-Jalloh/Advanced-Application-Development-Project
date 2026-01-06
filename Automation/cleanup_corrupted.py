import mysql.connector
import os

# Database configuration
db_config = {
    "host": "localhost",
    "user": "root",
    "password": "ebrim@CJ@lloh25",
    "database": "_backend"
}

def cleanup_corrupted_movies(end_id):
    """Delete corrupted movie poster files and clear DB references"""
    conn = mysql.connector.connect(**db_config)
    cursor = conn.cursor()
    
    # Delete files 1 through end_id
    for i in range(1, end_id + 1):
        filepath = f"Backend/uploads/movies/movie_{i}.jpg"
        if os.path.exists(filepath):
            os.remove(filepath)
            print(f"Deleted {filepath}")
    
    # Update DB to clear image_url for these records
    cursor.execute("UPDATE movies SET image_url = NULL, image_filename = NULL WHERE id <= %s", (end_id,))
    conn.commit()
    
    print(f"Cleared image references in DB for movies 1-{end_id}")
    cursor.close()
    conn.close()

if __name__ == "__main__":
    cleanup_corrupted_movies(59)
    print("Done!")
