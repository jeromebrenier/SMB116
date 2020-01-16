package fr.jbrenier.petfoodingcontrol.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface PetDao {
    @Insert
    Completable insert(Pet pet);
    @Insert
    Completable insert(List<Pet> petList);
    @Query("SELECT * FROM Pet WHERE pet_Id = :petId")
    Single<Pet> getPetbyId (int petId);
    @Query("SELECT * FROM Pet WHERE user_Id = :userId")
    Single<Pet> getPetOwnedbyUserId (int userId);
    @Update
    Completable updatePet (Pet pet);
    @Delete
    Completable deletePet (Pet pet);
}
