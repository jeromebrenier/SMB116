package fr.jbrenier.petfoodingcontrol.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.pet.food.FoodSettings;

@Dao
public interface FoodSettingsDao {
    @Insert
    void insertOnlySingleFoodSettings (Pet pet);
    @Insert
    void insertMultipleFoodSettings (List<Pet> petList);
    @Query("SELECT * FROM FoodSettings WHERE id = :id")
    FoodSettings fetchOneFoodSettingsbyId (int id);
    @Update
    void updateFoodSettings (FoodSettings foodSettings);
    @Delete
    void deleteFoodSettingss (FoodSettings foodSettings);
}
