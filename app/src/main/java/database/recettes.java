package database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.facilRecetas.data.models.Recette;

import java.util.List;

@Dao
public interface recettes {

    @Query("SELECT * FROM RECETTES")
    List<Recette> getRecettes();

    @Query("SELECT * FROM RECETTES WHERE id LIKE :uuid")
    Recette getNota(String uuid);

}
