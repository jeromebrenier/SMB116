package fr.jbrenier.petfoodingcontrol.services.photoservice;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;

public interface PhotoService {
    SingleLiveEvent<Integer> update(Context context, User currentUser,
                                    Map<UserServiceKeysEnum, String> userData);
    void save(Context context, Photo photo, User user);
    void save(Context context, Photo photo, Pet pet);
    void clearDisposables(Context context);
    MutableLiveData<Bitmap> get(Context context, User user);
    MutableLiveData<Bitmap> get(Context context, Pet pet);
}
