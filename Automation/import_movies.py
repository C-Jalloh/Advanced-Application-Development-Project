import pandas as pd
import mysql.connector
import json
import os
import requests
from datetime import datetime

# Database configuration
db_config = {
    "host": "localhost",
    "user": "root",
    "password": "ebrim@CJ@lloh25",
    "database": "_backend"
}

# Path to CSV
csv_path = "data/tmdb_5000_movies_mergedwith_movie_metadata.csv"
upload_dir = "Backend/uploads/movies"

if not os.path.exists(upload_dir):
    os.makedirs(upload_dir)

def clean_data(df):
    # Fill NaNs
    df['movie_title'] = df['movie_title'].fillna('Unknown Title').str.strip()
    df['genres'] = df['genres'].fillna('')
    df['director_name'] = df['director_name'].fillna('Unknown Director')
    df['overview'] = df['overview'].fillna('')
    df['budget'] = df['budget'].fillna(0)
    df['revenue'] = df['revenue'].fillna(0)
    df['popularity'] = df['popularity'].fillna(0.0)
    df['vote_average'] = df['vote_average'].fillna(0.0)
    df['vote_count'] = df['vote_count'].fillna(0)
    df['duration'] = df['duration'].fillna(0)
    df['title_year'] = df['title_year'].fillna(0)
    return df

def import_data():
    try:
        conn = mysql.connector.connect(**db_config)
        cursor = conn.cursor()

        # Load CSV
        df = pd.read_csv(csv_path)
        df = clean_data(df)

        # Take 200 movies, ensuring all genres are represented
        # First, get unique genres
        all_genres = set()
        for g_str in df['genres']:
            if isinstance(g_str, str):
                all_genres.update(g_str.split('|'))
        
        print(f"Found {len(all_genres)} unique genres.")

        # Select 200 movies
        # To ensure all genres are represented, we can pick one movie for each genre first
        selected_indices = set()
        for genre in all_genres:
            genre_movies = df[df['genres'].str.contains(genre, na=False)]
            if not genre_movies.empty:
                selected_indices.add(genre_movies.index[0])
        
        # Fill the rest to reach 200
        remaining_needed = 200 - len(selected_indices)
        if remaining_needed > 0:
            remaining_df = df.drop(list(selected_indices))
            selected_indices.update(remaining_df.head(remaining_needed).index)
        
        final_df = df.loc[list(selected_indices)].head(200)

        for _, row in final_df.iterrows():
            # 1. Handle Director
            director_name = row['director_name']
            cursor.execute("SELECT id FROM directors WHERE name = %s", (director_name,))
            director_res = cursor.fetchone()
            if director_res:
                director_id = director_res[0]
            else:
                cursor.execute("INSERT INTO directors (name) VALUES (%s)", (director_name,))
                director_id = cursor.lastrowid

            # 2. Insert Movie
            # Check if movie exists
            cursor.execute("SELECT id FROM movies WHERE title = %s", (row['movie_title'],))
            if cursor.fetchone():
                print(f"Skipping existing movie: {row['movie_title']}")
                continue

            sql = """
            INSERT INTO movies (title, genre, director, release_year, duration, description, 
                               budget, revenue, popularity, vote_average, vote_count, created_at, director_id)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
            """
            # We keep the string genre and director for backward compatibility
            main_genre = row['genres'].split('|')[0] if row['genres'] else ""
            
            values = (
                row['movie_title'],
                main_genre,
                director_name,
                int(row['title_year']) if row['title_year'] > 0 else None,
                int(row['duration']),
                row['overview'][:1000], # Truncate description if too long
                int(row['budget']),
                int(row['revenue']),
                float(row['popularity']),
                float(row['vote_average']),
                int(row['vote_count']),
                datetime.now(),
                director_id
            )
            cursor.execute(sql, values)
            movie_id = cursor.lastrowid

            # 3. Handle Genres (ManyToMany)
            genres = row['genres'].split('|')
            for g_name in genres:
                if not g_name: continue
                cursor.execute("SELECT id FROM genres WHERE name = %s", (g_name,))
                g_res = cursor.fetchone()
                if g_res:
                    genre_id = g_res[0]
                else:
                    cursor.execute("INSERT INTO genres (name) VALUES (%s)", (g_name,))
                    genre_id = cursor.lastrowid
                
                # Link to movie
                cursor.execute("INSERT INTO movie_genres (movie_id, genre_id) VALUES (%s, %s)", (movie_id, genre_id))

            # 4. Handle Actors (ManyToMany)
            actors = [row['actor_1_name'], row['actor_2_name'], row['actor_3_name']]
            for a_name in actors:
                if pd.isna(a_name) or not a_name: continue
                cursor.execute("SELECT id FROM actors WHERE name = %s", (a_name,))
                a_res = cursor.fetchone()
                if a_res:
                    actor_id = a_res[0]
                else:
                    cursor.execute("INSERT INTO actors (name) VALUES (%s)", (a_name,))
                    actor_id = cursor.lastrowid
                
                # Link to movie
                cursor.execute("INSERT INTO movie_actors (movie_id, actor_id) VALUES (%s, %s)", (movie_id, actor_id))

            # 5. Handle Image (Download placeholder)
            image_filename = f"movie_{movie_id}.jpg"
            image_path = os.path.join(upload_dir, image_filename)
            
            # Use a placeholder service for the poster
            placeholder_url = f"https://placehold.co/600x900/000000/FFFFFF/png?text={row['movie_title'].replace(' ', '+')}"
            try:
                img_data = requests.get(placeholder_url).content
                with open(image_path, 'wb') as handler:
                    handler.write(img_data)
                
                # Update movie with image info
                cursor.execute("UPDATE movies SET image_url = %s, image_filename = %s WHERE id = %s", 
                             (image_filename, image_filename, movie_id))
            except Exception as e:
                print(f"Failed to download image for {row['movie_title']}: {e}")

            print(f"Imported: {row['movie_title']}")

        conn.commit()
        print("Import completed successfully!")

    except Exception as e:
        print(f"Error: {e}")
    finally:
        if conn.is_connected():
            cursor.close()
            conn.close()

if __name__ == "__main__":
    import_data()
