package fr.jbrenier.petfoodingcontrol.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface UserDao {
    @Insert
    Single<Long> insert(User user);
    @Query("SELECT * FROM User WHERE user_Id = :userId")
    Single<User> getUserById(Long userId);
    @Query("SELECT * FROM User WHERE email = :userEmail")
    Single<User> getUserByEmail(String userEmail);
    @Query("SELECT User.user_Id,User.displayedName,User.email FROM User WHERE email = :feederEmail")
    Single<User> getFeederByEmail(String feederEmail);
    @Update
    Completable update(User user);
    @Delete
    Completable delete(User user);
}
