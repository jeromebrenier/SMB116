package fr.jbrenier.petfoodingcontrol.services.photoservice;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Service to manage Photos.
 * @author Jérôme Brenier
 */
public class PhotoServiceImpl implements PhotoService {
    /** LOGGING */
    private static final String TAG = "PhotoService";

    private PetFoodingControlRepository pfcRepository;
    private SharedPreferences sharedPreferences;
    private UserService userService;

    /** Manages disposables */
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public PhotoServiceImpl(PetFoodingControlRepository pfcRepository, SharedPreferences
            sharedPreferences,UserService userService) {
        this.pfcRepository = pfcRepository;
        this.sharedPreferences = sharedPreferences;
        this.userService = userService;
    }

    /**
     * Save the user's photo, and if successful update the user with the photo id.
     * @param photo the photo to save
     * @param user the user to update with the photo id
     */
    public void save(Photo photo, User user) {
        Disposable disposable = pfcRepository.save(photo).subscribe(
                (photoId) -> {
                    Log.i(TAG,"User's photo saved with id " + photoId);
                    user.setPhotoId(photoId);
                    userService.update(user);
                },
                throwable -> {
                    Log.e(TAG, "User's photo saving failure.", throwable);
                });
        compositeDisposable.add(disposable);
    }

    /**
     * Get the user's photo, returns it as a bitmap.
     * @param user the user whose photo is to be retrieved
     * @return the bitmap corresponding to the user's photo
     */
    @Override
    public MutableLiveData<Bitmap> get(User user) {
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
        compositeDisposable.add(disposable);
        return bitmapRetrieved;
    }
}
