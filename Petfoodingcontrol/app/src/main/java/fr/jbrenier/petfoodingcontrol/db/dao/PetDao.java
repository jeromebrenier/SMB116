package fr.jbrenier.petfoodingcontrol.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface PetDao {
    @Insert
    Single<Long> insert(Pet pet);
    @Insert
    Single<List<Long>> insert(List<Pet> petList);
    @Query("SELECT * FROM Pet WHERE pet_Id = :petId")
    Single<Pet> getPetbyId (Long petId);
    @Query("SELECT * FROM Pet WHERE user_Id = :userId")
    Flowable<List<Pet>> getPetOwnedbyUserId (Long userId);
    @Update
    Completable updatePet (Pet pet);
    @Delete
    Completable deletePet (Pet pet);
}
