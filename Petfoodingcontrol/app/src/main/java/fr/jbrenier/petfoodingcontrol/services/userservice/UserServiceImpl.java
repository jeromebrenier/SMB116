package fr.jbrenier.petfoodingcontrol.services.userservice;

import android.content.SharedPreferences;
import android.util.Log;

import java.time.OffsetDateTime;
import java.util.Map;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.AutoLogin;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableManager;
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableOwner;
import fr.jbrenier.petfoodingcontrol.utils.AutoLoginUtils;
import fr.jbrenier.petfoodingcontrol.utils.CryptographyUtils;
import io.reactivex.disposables.Disposable;

/**
 * The User service implementation.
 * @author Jérôme Brenier
 */
public class UserServiceImpl implements UserService {
    /** Logging */
    private static final String TAG = "UserService";

    /** Preferences AUTO LOGIN KEY */
    private static final String AUTO_LOGIN_TOKEN = "autoLoginToken";

    private PetFoodingControl petFoodingControl;
    private PetFoodingControlRepository pfcRepository;
    private SharedPreferences sharedPreferences;

    @Inject
    DisposableManager disposableManager;

    @Inject
    public UserServiceImpl(PetFoodingControl petFoodingControl,
                           PetFoodingControlRepository pfcRepository,
                           SharedPreferences sharedPreferences) {
        this.petFoodingControl = petFoodingControl;
        petFoodingControl.getAppComponent().inject(this);
        this.pfcRepository = pfcRepository;
        this.sharedPreferences = sharedPreferences;
    }

    /**
     * Init the login process.
     * @param disposableOwner the calling object
     */
    @Override
    public void initLogin(DisposableOwner disposableOwner) {
        String autoLogin = sharedPreferences.getString(AUTO_LOGIN_TOKEN,"");
        if (!autoLogin.isEmpty()) {
            Log.i(TAG, "Auto login value stored in preferences : " + autoLogin);
            tryToAutologin(disposableOwner, autoLogin);
        } else {
            petFoodingControl.getUserLogged().setValue(null);
        }
    }

    /**
     * Try to log with auto login with the local token given as a parameter.
     * @param disposableOwner the calling object
     * @param autoLoginLocalToken the local auto login token
     */
    private void tryToAutologin(DisposableOwner disposableOwner, String autoLoginLocalToken) {
        Disposable disposable = pfcRepository.getUserByAutoLogin(autoLoginLocalToken).subscribe(
                user -> {
                    Log.i(TAG, "User from AutoLogin successfully retrieved");
                    petFoodingControl.getUserLogged().setValue(user);
                }, throwable -> {
                    Log.e(TAG, "AutoLogin failed", throwable);
                    petFoodingControl.getUserLogged().setValue(null);
                });
        disposableManager.addDisposable(disposableOwner, disposable);
    }

    /**
     * Try to log with the data provided, initiate the auto login feature saving and storing
     * in preferences if necessary.
     * Return a SingleLiveEvent<Integer> taking the value 0 in case of success, 1 in case
     * of wrong password and 2 In case of user retrieval problem.
     * @param disposableOwner the calling object
     * @return logInResult SingleLiveEvent<Integer> result of the try
     */
    @Override
    public SingleLiveEvent<Integer> tryToLog(DisposableOwner disposableOwner,
                                             String email,
                                             String password,
                                             boolean isKeepLogged) {
        SingleLiveEvent<Integer> logInResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.getUserByEmail(email).subscribe(
                user -> CryptographyUtils.checkPassword(password, user.getPassword()).subscribe(
                        () -> {
                            petFoodingControl.getUserLogged().setValue(user);
                            if (isKeepLogged) {
                                setAutoLogin(disposableOwner);
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
        disposableManager.addDisposable(disposableOwner, disposable);
        return logInResult;
    }

    /**
     * Save in db and if successful, store in preferences the AutoLogin given in parameter.
     * @param disposableOwner the calling object
     */
    private void setAutoLogin(DisposableOwner disposableOwner) {
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
        disposableManager.addDisposable(disposableOwner, disposable);
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
     * @param disposableOwner the calling object
     * @param user User to save
     * @return logInResult SingleLiveEvent<Integer> result of the operation
     */
    @Override
    public SingleLiveEvent<User> save(DisposableOwner disposableOwner, User user) {
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
        disposableManager.addDisposable(disposableOwner, disposable);
        return saveUserResult;
    }

    /**
     * Update the user in the data source.
     * Return a SingleLiveEvent<Integer> taking the value 0 in case of success and 1 in case
     * of failure.
     * @param disposableOwner the calling object
     * @param user User to save
     * @return updateUserResult SingleLiveEvent<Integer> result of the operation
     */
    @Override
    public SingleLiveEvent<Integer> update(DisposableOwner disposableOwner, User user) {
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
        disposableManager.addDisposable(disposableOwner, disposable);
        return updateUserResult;
    }

    /**
     * Update the user in the data source.
     * Return a SingleLiveEvent<Integer> taking the value 0 in case of success and 1 in case
     * of failure.
     * @param disposableOwner the caller
     * @param userData the updated user data
     * @return updateUserResult SingleLiveEvent<Integer> result of the operation
     */
    @Override
    public SingleLiveEvent<Integer> update(DisposableOwner disposableOwner,
                                           Map<UserServiceKeysEnum, String> userData) {
        SingleLiveEvent<Integer> updateUserResult = new SingleLiveEvent<>();
        managePasswordData(userData);
        if (!(compareDataToCurrentUserLogged(userData))) {
            User userLogged = petFoodingControl.getUserLogged().getValue();
            if (userLogged != null) {
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
                            Log.i(TAG, "User updated");
                        },
                        throwable -> {
                            updateUserResult.setValue(1);
                            Log.e(TAG, "User update failure", throwable);
                        });
                disposableManager.addDisposable(disposableOwner, disposable);
            }
        } else {
            updateUserResult.setValue(0);
            Log.i(TAG,"No user update needed");
        }
        return updateUserResult;
    }

    /**
     * Replace an empty userData password (no update required) provided with the current
     * user password. Else encrypt the password entered.
     * @param userData the user data provided
     */
    private void managePasswordData(Map<UserServiceKeysEnum, String> userData) {
        User userLogged = petFoodingControl.getUserLogged().getValue();
        String passwordProvided = userData.get(UserServiceKeysEnum.PASSWORD_KEY);
        if (passwordProvided != null && userLogged != null && passwordProvided.equals("")) {
            // No update we put the current user password in the map
            userData.replace(UserServiceKeysEnum.PASSWORD_KEY, passwordProvided,
                    userLogged.getPassword());
        } else {
            // Update required : we encrypt the password value
            userData.replace(UserServiceKeysEnum.PASSWORD_KEY, passwordProvided,
                    CryptographyUtils.hashPassword(passwordProvided));
        }
    }

    /**
     * Compare the data provided with the data from current user logged.
     * Return true if no updated information, false otherwise.
     * @param userData the user's new data to compare
     * @return the result of the comparison
     */
    private boolean compareDataToCurrentUserLogged(Map<UserServiceKeysEnum, String> userData) {
        User userLogged = petFoodingControl.getUserLogged().getValue();
        return (userLogged !=null &&
                userLogged.getEmail().equals(userData.get(UserServiceKeysEnum.EMAIL_KEY)) &&
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
    public PetFoodingControlRepository getPfcRepository() {
        return pfcRepository;
    }

    @Override
    public PetFoodingControl getPetFoodingControl() {
        return petFoodingControl;
    }
}
