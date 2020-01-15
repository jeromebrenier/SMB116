package fr.jbrenier.petfoodingcontrol.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.user.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);
    @Insert
    void insert(List<User> userList);
    @Query("SELECT * FROM User WHERE user_Id = :userId")
    User getUserbyId (int userId);
    @Update
    void updateUser (User user);
    @Delete
    void deleteUser (User user);
}
