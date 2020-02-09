package fr.jbrenier.petfoodingcontrol.ui.activities.petmanagement;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.PetFeeders;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.FoodSettings;
import fr.jbrenier.petfoodingcontrol.domain.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.model.Feeder;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;
import fr.jbrenier.petfoodingcontrol.services.userservice.UserService;

public class PetManagementViewModel extends ViewModel {

    /* LOGGING */
    private static final String TAG = "PetManagementViewModel";

    @Inject
    UserService userService;

    @Inject
    PhotoService photoService;

    @Inject
    PetService petService;

    private Pet petToAdd;
    private Photo petPhoto;
    private LiveData<List<Feeder>> petFeeders;
    private SingleLiveEvent<Boolean> petSavingStatus = new SingleLiveEvent<>();
    /* Needed for the feeders list fragment */
    private List<Feeder> petFeedersArrayList = new ArrayList<>();
    private FoodSettings foodSettings;

    /* REFERENCES FOR CLEARING */
    private Map<SingleLiveEvent<Pet>, Observer<Pet>> saveNewPetMap = new HashMap<>();
    private Map<SingleLiveEvent<Boolean>, Observer<Boolean>> saveNewPetPhotoMap = new HashMap<>();
    private Map<SingleLiveEvent<Feeder>, Observer<Feeder>> checkFeederExistanceMap =
            new HashMap<>();
    private Map<SingleLiveEvent<Integer>, Observer<Integer>> savePetFeedersMap = new HashMap<>();

    /**
     * Save in the DB a new pet present in the viewModel.
     */
    private SingleLiveEvent<Pet> saveNewPet() {
        if (petToAdd != null) {
            addFoodSettingsToPet();
            return petService.save(this, petToAdd);
        }
        return null;
    }

    /**
     * Add the food settings to the Pet present in the viewModel
     */
    private void addFoodSettingsToPet() {
        if (foodSettings != null) {
            petToAdd.setFoodSettings(foodSettings);
        }
    }

    /**
     * Save the pet data in the DB.
     */
    SingleLiveEvent<Boolean> savePetData() {
        SingleLiveEvent<Pet> saveNewPet = saveNewPet();
        Observer<Pet> saveNewPetObserver = pet -> {
            if (pet != null) {
                Log.i(TAG, "Pet saved.");
                SingleLiveEvent<Boolean> saveNewPetPhoto = savePetPhoto(pet);
                Observer<Boolean> saveNewPetPhotoObserver = result -> {
                    if (result) {
                        Log.i(TAG, "Pet photo saved.");
                    } else {
                        Log.i(TAG, "Pet photo updated.");
                    }
                };
                saveNewPetPhoto.observeForever(saveNewPetPhotoObserver);
                saveNewPetPhotoMap.put(saveNewPetPhoto, saveNewPetPhotoObserver);
                savePetFeeders(pet);
                petSavingStatus.setValue(true);
            } else {
                petSavingStatus.setValue(false);
            }
        };
        saveNewPet.observeForever(saveNewPetObserver);
        saveNewPetMap.put(saveNewPet, saveNewPetObserver);
        return petSavingStatus;
    }

    /**
     * Save in the DB a new pet present in the viewModel.
     * @param pet Pet the photo belongs to
     */
    private SingleLiveEvent<Boolean> savePetPhoto(Pet pet) {
        if (petPhoto != null) {
            if (petPhoto.getPhotoId() == null) {
                return photoService.save(this, petPhoto, pet);
            } else {
                return photoService.update(this, petPhoto);
            }
        }
        return null;
    }

    private SingleLiveEvent<Boolean> savePetFeeders(Pet pet) {
        SingleLiveEvent<Boolean> savePetFeedersResult = new SingleLiveEvent<>();
        Function<Feeder, PetFeeders> mapper =
                feeder -> new PetFeeders(pet.getPetId(), feeder.getUserId());
        List<PetFeeders>  listPetFeeders =
                petFeedersArrayList.stream().map(mapper).collect(Collectors.toList());
        Observer<Integer> observer = result -> savePetFeedersResult.setValue(result == 0);
        SingleLiveEvent<Integer> observable = petService.savePetFeeders(this, listPetFeeders);
        observable.observeForever(observer);
        savePetFeedersMap.put(observable, observer);
        return savePetFeedersResult;
    }

    /**
     * Add the feeder whose email has been given to the ArrayList used for the feeder list
     * production if :
     * <ul>
     *     <li>The email is not empty</li>
     *     <li>The email does not belong to an existing feeder for the pet</li>
     *     <li>The email given in parameter is not the user logged's one</li>
     *     <li>the email given matches one user in the db</li>
     * </ul>
     * Returns :
     * <ul>
     *     <li>0 if successful</li>
     *     <li>1 if the email given is the user's one</li>
     *     <li>2 if the email given does not exist in the db</li>
     *     <li>3 if the email belongs to an existing feeder for the pet</li>
     * </ul>
     * @param feederEmail the email of the feeder to add
     * @return yhe result of the process
     */
    SingleLiveEvent<Integer> newFeederAddInarraylist(String feederEmail) {
        SingleLiveEvent<Integer> result = new SingleLiveEvent<>();
        if (petFeedersArrayList.stream().anyMatch(
                feeder -> feeder.getEmail().equals(feederEmail))) {
            result.setValue(3);
            return result;
        }
        String userLoggedEmail = null;
        PetFoodingControl pfc = userService.getPetFoodingControl();
        if (pfc.getUserLogged() != null && pfc.getUserLogged().getValue() != null
                && pfc.getUserLogged().getValue().getEmail() != null) {
            userLoggedEmail = pfc.getUserLogged().getValue().getEmail();
        }
        if (userLoggedEmail != null && userLoggedEmail.equals(feederEmail)) {
            result.setValue(1);
            //showToast(R.string.error_user_mail_entered);
        } else {
            checkFeederExistance(feederEmail, result);
        }
        return result;
    }

    private void checkFeederExistance(String feederMail, SingleLiveEvent<Integer> result) {
        Observer<Feeder> observer = feeder -> {
            if (feeder != null) {
                petFeedersArrayList.add(feeder);
                result.setValue(0);
/*                showToast(R.string.new_feeder_success);
                newFeederDialog.dismiss();*/
            } else {
                result.setValue(2);
                /*showToast(R.string.error_feeder_mail);*/
            }
        };
        SingleLiveEvent<Feeder> check = petService.checkFeederExistance(this, feederMail);
        check.observeForever(observer);
        checkFeederExistanceMap.put(check, observer);
    }

    /**
     * Remove a feeder from the petFeedersArrayList.
     * @param feeder the feeder to remove
     */
    void removeFeeder(Feeder feeder) {
        petFeedersArrayList.remove(feeder);
    }

    @Override
    public void onCleared() {
        saveNewPetMap.entrySet().forEach(getConsumer());
        saveNewPetPhotoMap.entrySet().forEach(getConsumer());
        checkFeederExistanceMap.entrySet().forEach(getConsumer());
        savePetFeedersMap.entrySet().forEach(getConsumer());
    }

    private <T> Consumer<Map.Entry<SingleLiveEvent<T>, Observer<T>>> getConsumer() {
        return entrySet -> entrySet.getKey().removeObserver(entrySet.getValue());
    }

    public Pet getPetToAdd() {
        return petToAdd;
    }

    public void setPetToAdd(Pet petToAdd) {
        this.petToAdd = petToAdd;
    }

    public Photo getPetPhoto() {
        return petPhoto;
    }

    public void setPetPhoto(Photo petPhoto) {
        this.petPhoto = petPhoto;
    }

    public LiveData<List<Feeder>> getPetFeeders() {
        return petFeeders;
    }

    public List<Feeder> getPetFeedersArrayList() {
        return petFeedersArrayList;
    }

    public FoodSettings getFoodSettings() {
        return foodSettings;
    }

    public void setFoodSettings(FoodSettings foodSettings) {
        this.foodSettings = foodSettings;
    }
}
