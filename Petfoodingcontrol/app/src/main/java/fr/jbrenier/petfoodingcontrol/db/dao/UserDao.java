package fr.jbrenier.petfoodingcontrol.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.user.User;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface UserDao {
    @Insert
    Completable insert(User user);
    @Insert
    Completable insert(List<User> userList);
    @Query("SELECT * FROM User WHERE user_Id = :userId")
    Single<User> getUserbyId (Long userId);
    @Query("SELECT * FROM User WHERE email = :userEmail")
    Single<User> getUserbyEmail (String userEmail);
    @Query("SELECT * FROM User WHERE email = :userEmail AND password = :userPassword")
    Single<User> getUserbyCredentials (String userEmail, String userPassword);
    @Update
    Completable updateUser (User user);
    @Delete
    Completable deleteUser (User user);
}
