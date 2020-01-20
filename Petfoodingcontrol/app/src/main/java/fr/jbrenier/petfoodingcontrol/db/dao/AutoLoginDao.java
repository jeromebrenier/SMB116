package fr.jbrenier.petfoodingcontrol.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import fr.jbrenier.petfoodingcontrol.domain.user.AutoLogin;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface AutoLoginDao {
    @Insert
    Completable insert(AutoLogin autoLogin);
    @Query("SELECT * FROM User INNER JOIN AutoLogin ON User.user_Id = AutoLogin.user_Id AND " +
            "AutoLogin.token_Id = :autoLoginToken " +
            "AND datetime(Autologin.expiration_date) > datetime(current_timestamp)")
    Single<User> getUserByAutoLogin(String autoLoginToken);
    @Delete
    Completable delete (AutoLogin autoLogin);
}
