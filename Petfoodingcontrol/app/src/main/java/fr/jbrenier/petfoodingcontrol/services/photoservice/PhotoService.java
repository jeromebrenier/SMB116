package fr.jbrenier.petfoodingcontrol.services.photoservice;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.entities.user.User;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;

public interface PhotoService {
    SingleLiveEvent<Integer> update(Object object, User currentUser,
                                    Map<UserServiceKeysEnum, String> userData);
    void save(Context context, Photo photo, User user);
    void save(Context context, Photo photo, Pet pet);
    void update(Context context, Photo photo);
    void clearDisposables(Object object);
    MutableLiveData<Bitmap> get(Object object, User user);
    MutableLiveData<Bitmap> get(Object object, Pet pet);
}
