package fr.jbrenier.petfoodingcontrol.services.photoservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.PetFoodingControlService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Service to manage Photos.
 * @author Jérôme Brenier
 */
public class PhotoServiceImpl extends PetFoodingControlService implements PhotoService {
    /** LOGGING */
    private static final String TAG = "PhotoService";

    private PetFoodingControlRepository pfcRepository;
    private SharedPreferences sharedPreferences;
    private UserService userService;

    @Inject
    public PhotoServiceImpl(PetFoodingControlRepository pfcRepository, SharedPreferences
            sharedPreferences, UserService userService) {
        this.pfcRepository = pfcRepository;
        this.sharedPreferences = sharedPreferences;
        this.userService = userService;
    }

    /**
     * Update the user's photo.
     * @param context
     * @param userData
     * @return
     */
    @Override
    public SingleLiveEvent<Integer> update(Context context, Map<UserServiceKeysEnum,
            String> userData) {
        SingleLiveEvent<Integer> updateUserResult = new SingleLiveEvent<>();
        Disposable disposableGet = pfcRepository.getUserPhoto(
                pfcRepository.getUserLogged().getValue()).subscribe(
                photo -> {
                    Log.i(TAG, "User's photo loaded.");
                    if (photo.getImage().equals(userData.get(UserServiceKeysEnum.PHOTO_KEY))) {
                        Disposable disposableUpdate = updateUserPhoto(userData.get(UserServiceKeysEnum.PHOTO_KEY)).subscribe(
                                () -> {
                                    updateUserResult.setValue(0);
                                    Log.i(TAG, "User's photo updated.");
                                },
                                throwable -> {
                                    updateUserResult.setValue(1);
                                    Log.e(TAG, "User's photo update error.", throwable);
                                }
                        );
                        addToCompositeDisposable(context, disposableUpdate);
                    } else {
                        updateUserResult.setValue(0);
                        Log.i(TAG, "User's photo update not necessary.");
                    }
                }, throwable ->
                        Log.e(TAG, "User's photo not loaded.", throwable));
        addToCompositeDisposable(context, disposableGet);
        return updateUserResult;
    }

    private Completable updateUserPhoto(String newPhotoBase64) {
        Photo newPhoto = new Photo(
                pfcRepository.getUserLogged().getValue().getPhotoId(),
                newPhotoBase64);
        return pfcRepository.update(newPhoto);
    }

    /**
     * Save the user's photo, and if successful update the user with the photo id.
     * @param photo the photo to save
     * @param user the user to update with the photo id
     */
    public void save(Context context, Photo photo, User user) {
        Disposable disposable = pfcRepository.save(photo).subscribe(
                (photoId) -> {
                    Log.i(TAG,"User's photo saved with id " + photoId);
                    user.setPhotoId(photoId);
                    userService.update(context, user);
                },
                throwable -> {
                    Log.e(TAG, "User's photo saving failure.", throwable);
                });
        addToCompositeDisposable(context, disposable);
    }

    @Override
    public void clearDisposables(Context context) {
        compositeDisposableClear(context);
    }

    /**
     * Get the user's photo, returns it as a bitmap.
     * @param user the user whose photo is to be retrieved
     * @return the bitmap corresponding to the user's photo
     */
    @Override
    public MutableLiveData<Bitmap> get(Context context, User user) {
        MutableLiveData<Bitmap> bitmapRetrieved = new MutableLiveData<>(null);
        Disposable disposable = pfcRepository.getUserPhoto(user).subscribe(
                photo -> {
                    Log.i(TAG, "User's photo loaded.");
                    byte[] decodedString = Base64.decode(photo.getImage(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
                            decodedString.length);
                    bitmapRetrieved.setValue(decodedByte);
                }, throwable ->
                        Log.e(TAG, "User's photo not loaded.", throwable));
        addToCompositeDisposable(context, disposable);
        return bitmapRetrieved;
    }
}
