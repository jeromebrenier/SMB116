package fr.jbrenier.petfoodingcontrol.services.photoservice;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.User;

public interface PhotoService {
    void save(Photo photo, User user);
    MutableLiveData<Bitmap> get(User user);
}
