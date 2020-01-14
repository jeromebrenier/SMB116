package fr.jbrenier.petfoodingcontrol.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;

@Dao
public interface PetDao {
    @Insert
    void insert(Pet pet);
    @Insert
    void insert(List<Pet> petList);
    @Query("SELECT * FROM Pet WHERE id = :id")
    Pet getPetbyId (int id);
    @Query("SELECT * FROM Pet WHERE userId = :userId")
    Pet getPetOwnedbyUserId (int userId);
    @Update
    void updatePet (Pet pet);
    @Delete
    void deletePet (Pet pet);
}
