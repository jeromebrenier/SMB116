package fr.jbrenier.petfoodingcontrol.services.photoservice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableManager;
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableOwner;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserServiceKeysEnum;
import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;

/**
 * The Photo service implementation.
 * @author Jérôme Brenier
 */
public class PhotoServiceImpl implements PhotoService {
    /** LOGGING */
    private static final String TAG = "PhotoService";

    private PetFoodingControlRepository pfcRepository;
    private UserService userService;
    private PetService petService;

    @Inject
    DisposableManager disposableManager;

    @Inject
    public PhotoServiceImpl(PetFoodingControl petFoodingControl,
                            PetFoodingControlRepository pfcRepository,
                            UserService userService,
                            PetService petService) {
        petFoodingControl.getAppComponent().inject(this);
        this.pfcRepository = pfcRepository;
        this.userService = userService;
        this.petService = petService;
    }

    /**
     * Update the user's photo.
     * @param disposableOwner the calling object
     * @param userData the user's data
     * @return a SingleLiveEvent<Integer>
     */
    @Override
    public SingleLiveEvent<Integer> update(DisposableOwner disposableOwner, User currentUser,
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
                        disposableManager.addDisposable(disposableOwner, disposableUpdate);
                    } else {
                        updateUserResult.setValue(0);
                        Log.i(TAG, "User's photo update not necessary.");
                    }
                }, throwable ->
                        Log.e(TAG, "User's photo not loaded."));
        disposableManager.addDisposable(disposableOwner, disposableGet);
        return updateUserResult;
    }

    private Completable updateUserPhoto(User currentUser, String newPhotoBase64) {
        Photo newPhoto = new Photo(currentUser.getPhotoId(), newPhotoBase64);
        return pfcRepository.updatePhoto(newPhoto);
    }

    /**
     * Save the user's photo, and if successful update the user with the photo id.
     * @param disposableOwner the calling object
     * @param photo the photo to save
     * @param user the user to update with the photo id
     */
    @Override
    public void save(DisposableOwner disposableOwner, Photo photo, User user) {
        Disposable disposable = pfcRepository.savePhoto(photo).subscribe(
                (photoId) -> {
                    Log.i(TAG,"User's photo saved with id " + photoId);
                    user.setPhotoId(photoId);
                    userService.update(disposableOwner, user);
                },
                throwable -> {
                    Log.e(TAG, "User's photo saving failure.", throwable);
                });
        disposableManager.addDisposable(disposableOwner, disposable);
    }

    /**
     * Save the pet's photo, and if successful update the pet with the photo id.
     * @param disposableOwner the calling object
     * @param photo the photo to save
     * @param pet the pet to update with the photo id
     */
    @Override
    public SingleLiveEvent<Boolean> save(DisposableOwner disposableOwner, Photo photo, Pet pet) {
        SingleLiveEvent<Boolean> result = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.savePhoto(photo).subscribe(
                (photoId) -> {
                    Log.i(TAG,"Pet's photo saved with id " + photoId);
                    pet.setPhotoId(photoId);
                    Log.i(TAG,"Pet's photo id attribute " + pet.getPhotoId());
                    petService.update(disposableOwner, pet);
                    result.setValue(true);
                },
                throwable -> {
                    result.setValue(false);
                    Log.e(TAG, "Pet's photo saving failure.", throwable);
                });
        disposableManager.addDisposable(disposableOwner, disposable);
        return result;
    }

    @Override
    public SingleLiveEvent<Boolean> update(DisposableOwner disposableOwner, Photo photo) {
        SingleLiveEvent<Boolean> result = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.updatePhoto(photo).subscribe(
                () -> {
                    result.setValue(true);
                    Log.i(TAG, "Photo " + photo.getPhotoId() + " updated.");
                },
                throwable -> {
                    result.setValue(false);
                    Log.e(TAG, "Photo " + photo.getPhotoId() + " update failure.");
                });
        disposableManager.addDisposable(disposableOwner, disposable);
        return result;
    }

    /**
     * Get the user's photo, returns it as a bitmap.
     * @param disposableOwner the calling object
     * @param user the user whose photo is to be retrieved
     * @return the bitmap corresponding to the user's photo
     */
    @Override
    public SingleLiveEvent<Bitmap> get(DisposableOwner disposableOwner, User user) {
        SingleLiveEvent<Bitmap> bitmapRetrieved = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.getUserPhoto(user).subscribe(
                photo -> {
                    Log.i(TAG, "User's photo loaded.");
                    byte[] decodedString = Base64.decode(photo.getImage(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
                            decodedString.length);
                    bitmapRetrieved.setValue(decodedByte);
                }, throwable -> {
                        Log.e(TAG, "User's photo not loaded.");
                        bitmapRetrieved.setValue(null);
                });
        disposableManager.addDisposable(disposableOwner, disposable);
        return bitmapRetrieved;
    }

    /**
     * Get the pet's photo, returns it as a bitmap.
     * @param disposableOwner the calling object
     * @param pet the pet whose photo is to be retrieved
     * @return the bitmap corresponding to the pet's photo
     */
    @Override
    public MutableLiveData<Bitmap> getPetBitmap(DisposableOwner disposableOwner, Pet pet) {
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
        disposableManager.addDisposable(disposableOwner, disposable);
        return bitmapRetrieved;
    }

    /**
     * Get the pet's photo.
     * @param disposableOwner the calling object
     * @param pet the pet whose photo is to be retrieved
     * @return the pet's photo
     */
    @Override
    public SingleLiveEvent<Photo> getPetPhoto(DisposableOwner disposableOwner, Pet pet) {
        SingleLiveEvent<Photo> photoRetrieved = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.getPetPhoto(pet).subscribe(
                photo -> {
                    Log.i(TAG, "Pet's photo retrieved.");
                    photoRetrieved.setValue(photo);
                }, throwable -> Log.e(TAG, "Pet's photo not retrieved."));
        disposableManager.addDisposable(disposableOwner, disposable);
        return photoRetrieved;
    }
}
