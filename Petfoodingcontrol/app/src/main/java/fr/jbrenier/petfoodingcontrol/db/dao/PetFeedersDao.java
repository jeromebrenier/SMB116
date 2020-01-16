package fr.jbrenier.petfoodingcontrol.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.pet.PetFeeders;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface PetFeedersDao {
    @Insert
    Completable insert(PetFeeders petFeeders);
    @Insert
    Completable insert(List<PetFeeders> petFeedersList);
    @Query("SELECT * FROM PetFeeders INNER JOIN Pet ON PetFeeders.pet_Id = Pet.pet_id " +
            "WHERE PetFeeders.user_Id = :userId")
    Flowable<List<Pet>> getPetsforFeeder(Long userId);
    @Query("SELECT * FROM PetFeeders INNER JOIN User ON PetFeeders.user_Id = User.user_Id " +
            "WHERE pet_Id = :petId")
    Flowable<List<User>> getUsersForPet (Long petId);
    @Update
    Completable update(PetFeeders petFeeders);
    @Delete
    Completable delete(PetFeeders petFeeders);
}
