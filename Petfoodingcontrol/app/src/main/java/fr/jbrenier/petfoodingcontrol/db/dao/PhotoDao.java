package fr.jbrenier.petfoodingcontrol.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;

@Dao
public interface PhotoDao {
    @Insert
    void insert(Photo photo);
    @Insert
    void insert(List<Photo> photos);
    @Query("SELECT * FROM Photo WHERE id = :photoId")
    Photo getPhotoById (Long photoId);
    @Update
    void update(Photo photo);
    @Delete
    void delete(Photo photo);
}
