package fr.jbrenier.petfoodingcontrol.services.photoservice;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;

public interface PhotoService {
    SingleLiveEvent<Integer> update(Object object, User currentUser,
                                    Map<UserServiceKeysEnum, String> userData);
    void save(Context context, Photo photo, User user);
    SingleLiveEvent<Boolean> save(Object object, Photo photo, Pet pet);
    SingleLiveEvent<Boolean> update(Object object, Photo photo);
    void clearDisposables(Object object);
    MutableLiveData<Bitmap> get(Object object, User user);
    MutableLiveData<Bitmap> getPetBitmap(Object object, Pet pet);
    SingleLiveEvent<Photo> getPetPhoto(Object object, Pet pet);
}
