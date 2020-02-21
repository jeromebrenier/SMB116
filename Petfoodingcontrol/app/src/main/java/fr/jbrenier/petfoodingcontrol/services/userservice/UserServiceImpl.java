package fr.jbrenier.petfoodingcontrol.services.userservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.AutoLogin;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.PetFoodingControlService;
import fr.jbrenier.petfoodingcontrol.utils.AutoLoginUtils;
import fr.jbrenier.petfoodingcontrol.utils.CryptographyUtils;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Service to manage Users.
 * @author Jérôme Brenier
 */
public class UserServiceImpl extends PetFoodingControlService implements UserService {
    /** LOGGING */
    private static final String TAG = "UserService";

    /** PREFERENCES AUTO LOGIN KEY */
    private static final String AUTO_LOGIN_TOKEN = "autoLoginToken";

    /** Disposable management */
    private Map<Context, CompositeDisposable> compositeDisposableMap = new HashMap<>();

    private PetFoodingControl petFoodingControl;
    private PetFoodingControlRepository pfcRepository;
    private SharedPreferences sharedPreferences;

    @Inject
    public UserServiceImpl(PetFoodingControl petFoodingControl,
                           PetFoodingControlRepository pfcRepository,
                           SharedPreferences sharedPreferences) {
        this.petFoodingControl = petFoodingControl;
        this.pfcRepository = pfcRepository;
        this.sharedPreferences = sharedPreferences;
    }

    /**
     * Init the login process.
     * @param context the Context of the caller
     */
    @Override
    public void initLogin(Context context) {
        String autoLogin = sharedPreferences.getString(AUTO_LOGIN_TOKEN,"");
        if (!autoLogin.isEmpty()) {
            Log.i(TAG, "Auto login value stored in preferences : " + autoLogin);
            tryToAutologin(context, autoLogin);
        } else {
            petFoodingControl.getUserLogged().setValue(null);
        }
    }

    /**
     * Try to log with auto login with the local token given as a parameter.
     * @param autoLoginLocalToken the local auto login token
     */
    private void tryToAutologin(Context context, String autoLoginLocalToken) {
        Disposable disposable = pfcRepository.getUserByAutoLogin(autoLoginLocalToken).subscribe(
                user -> {
                    Log.i(TAG, "User from AutoLogin successfully retrieved");
                    petFoodingControl.getUserLogged().setValue(user);
                }, throwable -> {
                    Log.e(TAG, "AutoLogin failed", throwable);
                    petFoodingControl.getUserLogged().setValue(null);
                });
        addToCompositeDisposable(context, disposable);
    }

    /**
     * Try to log with the data provided, initiate the auto login feature saving and storing
     * in preferences if necessary.
     * Return a SingleLiveEvent<Integer> taking the value 0 in case of success, 1 in case
     * of wrong password and 2 In case of user retrieval problem.
     * @param context the Context of the caller
     * @return logInResult SingleLiveEvent<Integer> result of the try
     */
    @Override
    public SingleLiveEvent<Integer> tryToLog(Context context, String email, String password,
                                             boolean isKeepLogged) {
        SingleLiveEvent<Integer> logInResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.getUserByEmail(email).subscribe(
                user -> CryptographyUtils.checkPassword(password, user.getPassword()).subscribe(
                        () -> {
                            petFoodingControl.getUserLogged().setValue(user);
                            if (isKeepLogged) {
                                setAutoLogin(context);
                            }
                            logInResult.setValue(0);
                            Log.i(TAG, "Log in success.");
                        }, throwable -> {
                            logInResult.setValue(1);
                            Log.i(TAG, "Log in failure, wrong password.");
                        }),
                throwable -> {
                    logInResult.setValue(2);
                    Log.e(TAG, "Log in failure ", throwable);
                });
        addToCompositeDisposable(context, disposable);
        return logInResult;
    }

    /**
     * Save in db and if successful, store in preferences the AutoLogin given in parameter.
     * @param context the Context of the caller
     */
    private void setAutoLogin(Context context) {
        AutoLogin autoLogin = new AutoLogin(
                AutoLoginUtils.getInstance().getUuid().toString(),
                OffsetDateTime.now().plusWeeks(1L),
                petFoodingControl.getUserLogged().getValue().getUserId()
        );
        Disposable disposable = pfcRepository.insertAutoLogin(autoLogin).subscribe(
                () -> {
                    Log.i(TAG, "AutoLogin for user " + autoLogin.getUserId() +
                            " successfully inserted");
                    storeAutoLoginInPreferences(autoLogin);
                },
                throwable -> Log.e(TAG, "AutoLogin failed to be inserted for user " +
                        autoLogin.getUserId()));
        addToCompositeDisposable(context, disposable);
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
     * Save the user in the data source.
     * Return a SingleLiveEvent<Integer> taking the value 0 in case of success and 1 in case
     * of failure.
     * @param context the Context of the caller
     * @param user User to save
     * @return logInResult SingleLiveEvent<Integer> result of the operation
     */
    @Override
    public SingleLiveEvent<User> save(Context context, User user) {
        SingleLiveEvent<User> saveUserResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.saveUser(user).subscribe(
                (userId) -> {
                    user.setUserId(userId);
                    saveUserResult.setValue(user);
                    Log.i(TAG, "User saved with id : " + userId);
                },
                throwable -> {
                    saveUserResult.setValue(null);
                    Log.e(TAG, "User saving failure", throwable);
                });
        addToCompositeDisposable(context, disposable);
        return saveUserResult;
    }

    /**
     * Update the user in the data source.
     * Return a SingleLiveEvent<Integer> taking the value 0 in case of success and 1 in case
     * of failure.
     * @param context the Context of the caller
     * @param user User to save
     * @return updateUserResult SingleLiveEvent<Integer> result of the operation
     */
    @Override
    public SingleLiveEvent<Integer> update(Context context, User user) {
        SingleLiveEvent<Integer> updateUserResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.updateUser(user).subscribe(
                () -> {
                    updateUserResult.setValue(0);
                    Log.i(TAG,"User updated");
                },
                throwable -> {
                    updateUserResult.setValue(1);
                    Log.e(TAG, "User " + user.getUserId() + " update failure : ", throwable);
                });
        addToCompositeDisposable(context, disposable);
        return updateUserResult;
    }

    /**
     * Update the user in the data source.
     * Return a SingleLiveEvent<Integer> taking the value 0 in case of success and 1 in case
     * of failure.
     * @param object the caller
     * @param userData the updated user data
     * @return updateUserResult SingleLiveEvent<Integer> result of the operation
     */
    @Override
    public SingleLiveEvent<Integer> update(Object object,
                                           Map<UserServiceKeysEnum, String> userData) {
        SingleLiveEvent<Integer> updateUserResult = new SingleLiveEvent<>();
        userData = managePasswordData(userData);
        userData = setOrigPwdIfNoPwdUpdate(userData);
        if (!(compareDataToCurrentUserLogged(userData))) {
            User userLogged = petFoodingControl.getUserLogged().getValue();
            User userUpdated = new User(
                    userLogged.getUserId(),
                    userData.get(UserServiceKeysEnum.USERNAME_KEY),
                    userData.get(UserServiceKeysEnum.EMAIL_KEY),
                    userData.get(UserServiceKeysEnum.PASSWORD_KEY),
                    userLogged.getPhotoId()
            );
            Disposable disposable = pfcRepository.updateUser(userUpdated).subscribe(
                    () -> {
                        updateUserResult.setValue(0);
                        petFoodingControl.getUserLogged().setValue(userUpdated);
                        Log.i(TAG,"User updated");
                    },
                    throwable -> {
                        updateUserResult.setValue(1);
                        Log.e(TAG, "User update failure", throwable);
                    });
            addToCompositeDisposable(object, disposable);
        } else {
            updateUserResult.setValue(1);
        }
        return updateUserResult;
    }

    private Map<UserServiceKeysEnum, String> managePasswordData(
            Map<UserServiceKeysEnum, String> userData) {
        User userLogged = petFoodingControl.getUserLogged().getValue();
        String passwordProvided = userData.get(UserServiceKeysEnum.PASSWORD_KEY);
        if (passwordProvided.equals("")) {
            // No update we put the current user password in the map
            userData.replace(UserServiceKeysEnum.PASSWORD_KEY, passwordProvided,
                    userLogged.getPassword());
        } else {
            // Update required : we encrypt the password value
            userData.replace(UserServiceKeysEnum.PASSWORD_KEY, passwordProvided,
                    CryptographyUtils.hashPassword(passwordProvided));
        }
        return userData;
    }

    /**
     * Replace an empty userData password (no update required) provided with the current
     * user password.
     * @param userData the user data provided
     * @return the user data with password value modified if needed
     */
    private Map<UserServiceKeysEnum, String> setOrigPwdIfNoPwdUpdate(
            Map<UserServiceKeysEnum, String> userData) {
        User userLogged = petFoodingControl.getUserLogged().getValue();
        if (userData.get(UserServiceKeysEnum.PASSWORD_KEY).equals("")) {
            userData.replace(UserServiceKeysEnum.PASSWORD_KEY, "",
                    userLogged.getPassword());
        }
        return userData;
    }

    /**
     * Compare the data provided with the data from current user logged.
     * Return true if no updated information, false otherwise.
     * @param userData the user's new data to compare
     * @return the result of the comparison
     */
    private boolean compareDataToCurrentUserLogged(Map<UserServiceKeysEnum, String> userData) {
        User userLogged = petFoodingControl.getUserLogged().getValue();
        return (userLogged.getEmail().equals(userData.get(UserServiceKeysEnum.EMAIL_KEY)) &&
                userLogged.getPassword().equals(userData.get(UserServiceKeysEnum.PASSWORD_KEY)) &&
                userLogged.getDisplayedName()
                        .equals(userData.get(UserServiceKeysEnum.USERNAME_KEY)));
    }

    /**
     * Clear the keep me logged data in the preferences.
     */
    @Override
    public void clearKeepMeLogged() {
        if (sharedPreferences.contains(AUTO_LOGIN_TOKEN)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(AUTO_LOGIN_TOKEN);
            editor.apply();
        }
    }

    @Override
    public void leave() {
        petFoodingControl.getUserLogged().setValue(null);
    }

    @Override
    public void clearDisposables(Object object) {
        compositeDisposableClear(object);
    }

    @Override
    public PetFoodingControlRepository getPfcRepository() {
        return pfcRepository;
    }

    @Override
    public PetFoodingControl getPetFoodingControl() {
        return petFoodingControl;
    }
}
