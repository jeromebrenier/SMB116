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

@Dao
public interface PetFeedersDao {
    @Insert
    void insert(PetFeeders petFeeders);
    @Insert
    void insert(List<PetFeeders> petFeedersList);
    @Query("SELECT * FROM PetFeeders INNER JOIN Pet ON PetFeeders.pet_Id = Pet.pet_id " +
            "WHERE feeder_Id = :userId")
    List<Pet> getPetsforFeeder(Long userId);
    @Query("SELECT * FROM PetFeeders INNER JOIN User ON PetFeeders.feeder_Id = User.user_Id " +
            "WHERE pet_Id = :petId")
    List<User> getUsersForPet (Long petId);
    @Update
    void update(PetFeeders petFeeders);
    @Delete
    void delete(PetFeeders petFeeders);
}
