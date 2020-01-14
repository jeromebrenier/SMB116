package fr.jbrenier.petfoodingcontrol.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.food.Fooding;

@Dao
public interface FoodingDao {
    @Insert
    void insert(Fooding fooding);
    @Insert
    void insert(List<Fooding> foodingList);
    @Query("SELECT * FROM Fooding WHERE petId = :petId")
    List<Fooding> getFoodingsForPet (Long petId);
    @Update
    void update(Fooding fooding);
    @Delete
    void delete(Fooding fooding);
}
