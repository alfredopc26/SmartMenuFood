package database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.facilRecetas.data.models.Recette;

@Database(entities = {Recette.class}, version = 1)
public abstract class RecetteDatabase extends RoomDatabase {
    public abstract recettes getRecettes();
}
