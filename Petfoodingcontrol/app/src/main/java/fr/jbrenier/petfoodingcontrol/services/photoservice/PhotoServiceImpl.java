package fr.jbrenier.petfoodingcontrol.services.photoservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.entities.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.PetFoodingControlService;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;
import io.reactivex.Completable;
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
    private PetService petService;

    @Inject
    public PhotoServiceImpl(PetFoodingControlRepository pfcRepository, SharedPreferences
            sharedPreferences, UserService userService, PetService petService) {
        this.pfcRepository = pfcRepository;
        this.sharedPreferences = sharedPreferences;
        this.userService = userService;
        this.petService = petService;
    }

    /**
     * Update the user's photo.
     * @param object the calling object
     * @param userData the user's data
     * @return a SingleLiveEvent<Integer>
     */
    @Override
    public SingleLiveEvent<Integer> update(Object object, User currentUser,
                                           Map<UserServiceKeysEnum, String> userData) {
        SingleLiveEvent<Integer> updateUserResult = new SingleLiveEvent<>();
        Disposable disposableGet = pfcRepository.getUserPhoto(currentUser).subscribe(
                photo -> {
                    Log.i(TAG, "User's photo loaded.");
                    if (!photo.getImage().equals(userData.get(UserServiceKeysEnum.PHOTO_KEY))) {
                        Disposable disposableUpdate = updateUserPhoto(currentUser,
                                userData.get(UserServiceKeysEnum.PHOTO_KEY)).subscribe(
                                () -> {
                                    updateUserResult.setValue(0);
                                    Log.i(TAG, "User (id " + currentUser.getUserId()
                                            + ") 's photo updated.");
                                },
                                throwable -> {
                                    updateUserResult.setValue(1);
                                    Log.e(TAG, "User(id " + currentUser.getUserId()
                                            + ") 's photo update error.", throwable);
                                }
                        );
                        addToCompositeDisposable(object, disposableUpdate);
                    } else {
                        updateUserResult.setValue(0);
                        Log.i(TAG, "User's photo update not necessary.");
                    }
                }, throwable ->
                        Log.e(TAG, "User's photo not loaded."));
        addToCompositeDisposable(object, disposableGet);
        return updateUserResult;
    }

    private Completable updateUserPhoto(User currentUser, String newPhotoBase64) {
        Photo newPhoto = new Photo(currentUser.getPhotoId(), newPhotoBase64);
        return pfcRepository.update(newPhoto);
    }

    /**
     * Save the user's photo, and if successful update the user with the photo id.
     * @param photo the photo to save
     * @param user the user to update with the photo id
     */
    @Override
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

    /**
     * Save the pet's photo, and if successful update the pet with the photo id.
     * @param photo the photo to save
     * @param pet the pet to update with the photo id
     */
    @Override
    public void save(Context context, Photo photo, Pet pet) {
        Disposable disposable = pfcRepository.save(photo).subscribe(
                (photoId) -> {
                    Log.i(TAG,"Pet's photo saved with id " + photoId);
                    pet.setPhotoId(photoId);
                    Log.i(TAG,"Pet's photo id attribute " + pet.getPhotoId());
                    petService.update(context, pet);
                },
                throwable -> {
                    Log.e(TAG, "Pet's photo saving failure.", throwable);
                });
        addToCompositeDisposable(context, disposable);
    }

    @Override
    public void update(Context context, Photo photo) {
        Disposable disposable = pfcRepository.update(photo).subscribe(
                () -> Log.i(TAG, "Photo " + photo.getPhotoId() + " updated."),
                throwable -> Log.e(TAG, "Photo " + photo.getPhotoId() + " update failure."));
        addToCompositeDisposable(context, disposable);
    }

    @Override
    public void clearDisposables(Object object) {
        compositeDisposableClear(object);
    }

    /**
     * Get the user's photo, returns it as a bitmap.
     * @param object the calling object
     * @param user the user whose photo is to be retrieved
     * @return the bitmap corresponding to the user's photo
     */
    @Override
    public MutableLiveData<Bitmap> get(Object object, User user) {
        MutableLiveData<Bitmap> bitmapRetrieved = new MutableLiveData<>(null);
        Disposable disposable = pfcRepository.getUserPhoto(user).subscribe(
                photo -> {
                    Log.i(TAG, "User's photo loaded.");
                    byte[] decodedString = Base64.decode(photo.getImage(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
                            decodedString.length);
                    bitmapRetrieved.setValue(decodedByte);
                }, throwable ->
                        Log.e(TAG, "User's photo not loaded."));
        addToCompositeDisposable(object, disposable);
        return bitmapRetrieved;
    }

    /**
     * Get the pet's photo, returns it as a bitmap.
     * @param pet the pet whose photo is to be retrieved
     * @return the bitmap corresponding to the pet's photo
     */
    @Override
    public MutableLiveData<Bitmap> get(Object object, Pet pet) {
        MutableLiveData<Bitmap> bitmapRetrieved = new MutableLiveData<>(null);
        Disposable disposable = pfcRepository.getPetPhoto(pet).subscribe(
                photo -> {
                    Log.i(TAG, "Pet's photo loaded.");
                    byte[] decodedString = Base64.decode(photo.getImage(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
                            decodedString.length);
                    bitmapRetrieved.setValue(decodedByte);
                }, throwable ->
                        Log.e(TAG, "Pet's photo not loaded."));
        addToCompositeDisposable(object, disposable);
        return bitmapRetrieved;
    }
}
