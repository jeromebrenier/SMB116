package fr.jbrenier.petfoodingcontrol.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.Fooding;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface FoodingDao {
    @Insert
    Completable insert(Fooding fooding);
    @Insert
    Single<List<Long>> insert(List<Fooding> foodingList);
    @Query("SELECT * FROM Fooding WHERE pet_Id = :petId")
    Flowable<List<Fooding>> getFoodingsForPet (Long petId);
    @Update
    Completable update(Fooding fooding);
    @Delete
    Completable delete(Fooding fooding);
}
