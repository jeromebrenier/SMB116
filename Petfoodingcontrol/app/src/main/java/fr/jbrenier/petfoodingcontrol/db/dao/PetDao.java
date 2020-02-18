package fr.jbrenier.petfoodingcontrol.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.PetFeeder;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.Fooding;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.weight.Weighing;
import fr.jbrenier.petfoodingcontrol.domain.model.Feeder;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface PetDao {
    /* PET */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertPet(Pet pet);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<List<Long>> insertPet(List<Pet> petList);
    @Query("SELECT * FROM Pet WHERE pet_Id = :petId")
    Single<Pet> getPetbyId (Long petId);
    @Query("SELECT * FROM Pet WHERE user_Id = :userId")
    Flowable<List<Pet>> getPetOwnedbyUserId (Long userId);
    @Query("SELECT Pet.* FROM Pet WHERE user_Id = :userId " +
            "UNION " +
            "SELECT Pet.* FROM PetFeeder INNER JOIN Pet ON PetFeeder.pet_Id = Pet.pet_id " +
            "WHERE PetFeeder.user_Id = :userId")
    Flowable<List<Pet>> getAllUserPetsByUserId(Long userId);
    @Update
    Completable updatePet(Pet pet);
    @Delete
    Completable deletePet(Pet pet);

    /* PET FEEDERS */
    @Insert
    Single<Long> insertPetFeeder(PetFeeder petFeeder);
    @Insert
    Completable insertPetFeeders(List<PetFeeder> petFeederList);
    @Query("SELECT * FROM PetFeeder INNER JOIN Pet ON PetFeeder.pet_Id = Pet.pet_id " +
            "WHERE PetFeeder.user_Id = :userId")
    Flowable<List<Pet>> getPetsforFeeder(Long userId);
    @Query("SELECT * FROM PetFeeder INNER JOIN User ON PetFeeder.user_Id = User.user_Id " +
            "WHERE pet_Id = :petId")
    Single<List<Feeder>> getFeedersForPet (Long petId);
    @Query("SELECT User.user_Id,User.displayedName,User.email FROM User WHERE email = :feederEmail")
    Single<Feeder> getFeederByEmail(String feederEmail);
    @Update
    Completable updatePetFeeder(PetFeeder petFeeder);
    @Delete
    Completable deletePetFeeder(PetFeeder petFeeder);

    /* FOODING */
    @Insert
    Completable insertFooding(Fooding fooding);
    @Query("SELECT * FROM Fooding WHERE pet_Id = :petId")
    Flowable<List<Fooding>> getFoodingsForPet(Long petId);
    @Query("SELECT * FROM Fooding WHERE pet_Id = :petId AND date(fooding_date) = date('now')")
    Flowable<List<Fooding>> getDailyFoodingsForPet(Long petId);
    @Query("SELECT dailyQuantity - (SELECT SUM(quantity) FROM Fooding WHERE pet_Id = :petId " +
            "AND date(fooding_date) = date('now')) FROM Pet WHERE pet_Id = :petId")
    Flowable<Integer> getPetFoodingStatus(Long petId);
    @Update
    Completable updateFooding(Fooding fooding);
    @Delete
    Completable deleteFooding(Fooding fooding);

    /* WEIGHING */
    @Insert
    Completable insertWeighing(Weighing weighing);
    @Insert
    Single<List<Long>> insertWeighing(List<Weighing> weighingList);
    @Query("SELECT * FROM Weighing WHERE pet_Id = :petId")
    Flowable<List<Weighing>> getWeighingsForPet(Long petId);
    @Update
    Completable updateWeighing(Weighing weighing);
    @Delete
    Completable deleteWeighing(Weighing weighing);
}
