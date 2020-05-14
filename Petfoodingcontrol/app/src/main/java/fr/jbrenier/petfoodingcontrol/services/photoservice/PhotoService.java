package fr.jbrenier.petfoodingcontrol.services.photoservice;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableOwner;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;

/**
 * The Photo service contract.
 * @author Jérôme Brenier
 */
public interface PhotoService {
    SingleLiveEvent<Integer> update(DisposableOwner disposableOwner, User currentUser,
                                    Map<UserServiceKeysEnum, String> userData);
    void save(DisposableOwner disposableOwner, Photo photo, User user);
    SingleLiveEvent<Boolean> save(DisposableOwner disposableOwner, Photo photo, Pet pet);
    SingleLiveEvent<Boolean> update(DisposableOwner disposableOwner, Photo photo);
    SingleLiveEvent<Bitmap> get(DisposableOwner disposableOwner, User user);
    MutableLiveData<Bitmap> getPetBitmap(DisposableOwner disposableOwner, Pet pet);
    SingleLiveEvent<Photo> getPetPhoto(DisposableOwner disposableOwner, Pet pet);
}
