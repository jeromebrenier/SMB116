package fr.jbrenier.petfoodingcontrol.services.userservice;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.time.OffsetDateTime;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.domain.user.AutoLogin;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.utils.AutoLoginUtils;
import fr.jbrenier.petfoodingcontrol.utils.CryptographyUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Service to manage Users.
 * @author Jérôme Brenier
 */
public class UserServiceImpl implements UserService {
    /** LOGGING */
    private static final String TAG = "UserService";

    private static final String AUTO_LOGIN_TOKEN = "autoLoginToken";

    /** Manages disposables */
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private PetFoodingControlRepository pfcRepository;
    private SharedPreferences sharedPreferences;

    @Inject
    public UserServiceImpl(PetFoodingControlRepository pfcRepository,
                           SharedPreferences sharedPreferences) {
        this.pfcRepository = pfcRepository;
        this.sharedPreferences = sharedPreferences;
    }

    /**
     * Init the login process.
     */
    @Override
    public void initLogin() {
        String autoLogin = sharedPreferences.getString(AUTO_LOGIN_TOKEN,"");
        if (!autoLogin.isEmpty()) {
            Log.i(TAG, "Auto login value stored in preferences : " + autoLogin);
            tryToAutologin(autoLogin);
        }
    }

    /**
     * Try to log with auto login with the local token given as a parameter.
     * @param autoLoginLocalToken the local auto login token
     */
    private void tryToAutologin(String autoLoginLocalToken) {
        Disposable disposable = pfcRepository.getUserByAutoLogin(autoLoginLocalToken).subscribe(
                user -> {
                    Log.i(TAG, "User from AutoLogin successfully retrieved");
                    pfcRepository.setUserLogged(user);
                }, throwable ->
                        Log.e(TAG, "AutoLogin failed", throwable));
        compositeDisposable.add(disposable);
    }

    /**
     * Save in db and if successful, store in preferences the AutoLogin given in parameter.
     */
    private void setAutoLogin() {
        AutoLogin autoLogin = new AutoLogin(
                AutoLoginUtils.getInstance().getUuid().toString(),
                OffsetDateTime.now().plusWeeks(1L),
                pfcRepository.getUserLogged().getValue().getUserId()
        );
        Disposable disposable = pfcRepository.insert(autoLogin).subscribe(
                () -> {
                    Log.i(TAG, "AutoLogin for user " + autoLogin.getUserId() +
                            " successfully inserted");
                    storeAutoLoginInPreferences(autoLogin);
                },
                throwable -> Log.e(TAG, "AutoLogin failed to be inserted for user " +
                        autoLogin.getUserId()));
        compositeDisposable.add(disposable);
    }

    /**
     * Store the auto logging data into the application preferences.
     * @param autoLogin the AutoLogin to store in preferences
     */
    private void storeAutoLoginInPreferences(AutoLogin autoLogin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTO_LOGIN_TOKEN, autoLogin.getTokenId());
        editor.apply();
        Log.i(TAG,"Auto login successfully stored in the preferences");
    }


    /**
     * Try to log with the data provided, initiate the auto login feature saving and storing
     * in preferences if necessary.
     * Return a MutableLiveData<Integer> taking the value 0 in case of success and 1 in case
     * of failure (init at 2).
     * @return logInResult MutableLiveData<Integer> result of the try
     */
    @Override
    public MutableLiveData<Integer> tryToLog(String email, String password, boolean isKeepLogged) {
        MutableLiveData<Integer> logInResult = new MutableLiveData<>(9);
        Disposable disposable = pfcRepository.getUserByEmail(email).subscribe(
                user -> CryptographyUtils.checkPassword(password, user.getPassword()).subscribe(
                        () -> {
                            pfcRepository.setUserLogged(user);
                            if (isKeepLogged) {
                                setAutoLogin();
                            }
                            logInResult.setValue(0);
                            Log.i(TAG, "Log in success");
                        }),
                throwable -> {
                    logInResult.setValue(1);
                    Log.e(TAG, "Log in failure ", throwable);
                });
        compositeDisposable.add(disposable);
        return logInResult;
    }

    /**
     * Save the user in the data source.
     * Return a MutableLiveData<Integer> taking the value 0 in case of success and 1 in case
     * of failure (init at 2).
     * @param user User to save
     * @return logInResult MutableLiveData<Integer> result of the operation
     */
    @Override
    public MutableLiveData<Integer> save(User user) {
        MutableLiveData<Integer> saveUserResult = new MutableLiveData<>(9);
        Disposable disposable = pfcRepository.save(user).subscribe(
                (userId) -> {
                    saveUserResult.setValue(0);
                    Log.i(TAG,"User saved with id : " + userId);
                },
                throwable -> {
                    saveUserResult.setValue(1);
                    Log.e(TAG, "User "+ user.getUserId() + " saving failure", throwable);
                });
        compositeDisposable.add(disposable);
        return saveUserResult;
    }

    /**
     * Update the user in the data source.
     * Return a MutableLiveData<Integer> taking the value 0 in case of success and 1 in case
     * of failure (init at 2).
     * @param user User to save
     * @return updateUserResult MutableLiveData<Integer> result of the operation
     */
    @Override
    public MutableLiveData<Integer> update(User user) {
        MutableLiveData<Integer> updateUserResult = new MutableLiveData<>(9);
        Disposable disposable = pfcRepository.save(user).subscribe(
                (userId) -> {
                    updateUserResult.setValue(0);
                    Log.i(TAG,"User saved with id : " + userId);
                },
                throwable -> {
                    updateUserResult.setValue(1);
                    Log.e(TAG, "User " + user.getUserId() + " update failure", throwable);
                });
        compositeDisposable.add(disposable);
        return updateUserResult;
    }

    /**
     * Logout.
     */
    @Override
    public void logout() {
        pfcRepository.setUserLogged(null);
        if (sharedPreferences.contains(AUTO_LOGIN_TOKEN)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(AUTO_LOGIN_TOKEN);
            editor.apply();
        }
    }

    @Override
    public void leave() {
        pfcRepository.setUserLogged(null);
    }

    public PetFoodingControlRepository getPfcRepository() {
        return pfcRepository;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
