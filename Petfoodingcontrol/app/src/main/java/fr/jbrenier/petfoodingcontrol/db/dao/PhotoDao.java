package fr.jbrenier.petfoodingcontrol.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import fr.jbrenier.petfoodingcontrol.domain.entities.photo.Photo;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface PhotoDao {
    @Insert
    Single<Long> insert(Photo photo);
    @Query("SELECT * FROM Photo WHERE photo_Id = :photoId")
    Single<Photo> getPhotoById (Long photoId);
    @Update
    Completable update(Photo photo);
    @Delete
    Completable delete(Photo photo);
}
