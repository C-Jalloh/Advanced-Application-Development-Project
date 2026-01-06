import mysql.connector
import os
import time

# Database configuration
db_config = {
    "host": "localhost",
    "user": "root",
    "password": "ebrim@CJ@lloh25",
    "database": "_backend"
}

def pair_chronologically(table_name, folder_path, prefix):
    print(f"Pairing {table_name} chronologically...")
    
    # 1. Get files sorted by creation time
    files = [os.path.join(folder_path, f) for f in os.listdir(folder_path) if os.path.isfile(os.path.join(folder_path, f))]
    # Sort by modification time (closest to creation time on Linux for new files)
    files.sort(key=os.path.getmtime)
    
    # 2. Get records sorted by ID
    conn = mysql.connector.connect(**db_config)
    cursor = conn.cursor(dictionary=True)
    cursor.execute(f"SELECT id FROM {table_name} ORDER BY id ASC")
    records = cursor.fetchall()
    
    count = min(len(files), len(records))
    print(f"Found {len(records)} records and {len(files)} files. Pairing first {count}...")
    
    # 3. Rename files to temp names first to avoid collisions
    temp_files = []
    for i in range(count):
        old_path = files[i]
        record_id = records[i]['id']
        temp_filename = f"temp_{prefix}_{record_id}.jpg"
        temp_path = os.path.join(folder_path, temp_filename)
        os.rename(old_path, temp_path)
        temp_files.append((record_id, temp_path))
        
    # 4. Rename to final names and update DB
    for record_id, temp_path in temp_files:
        final_filename = f"{prefix}_{record_id}.jpg"
        final_path = os.path.join(folder_path, final_filename)
        if os.path.exists(final_path):
            os.remove(final_path)
        os.rename(temp_path, final_path)
        
        cursor.execute(f"UPDATE {table_name} SET image_url = %s, image_filename = %s WHERE id = %s", 
                       (final_filename, final_filename, record_id))
    
    conn.commit()
    cursor.close()
    conn.close()
    print(f"Successfully paired {count} {table_name} records.")

if __name__ == "__main__":
    pair_chronologically("movies", "Backend/uploads/movies", "movie")
    pair_chronologically("music", "Backend/uploads/music", "music")
    print("Done!")
