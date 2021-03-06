package fr.jbrenier.petfoodingcontrol.android.activities.petfooding;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.Fooding;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.weight.Weighing;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableManager;
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableOwner;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;

/**
 * The pet fooding view model
 * @author Jérôme Brenier
 */
public class PetFoodingViewModel extends ViewModel implements DisposableOwner {

    @Inject
    DisposableManager disposableManager;

    @Inject
    PetService petService;

    @Inject
    PhotoService photoService;

    /** Logging */
    private static final String TAG = "PetFoodingViewModel";

    private final MutableLiveData<Pet> pet = new MutableLiveData<>();
    private final MutableLiveData<Bitmap> petPhoto = new MutableLiveData<>();
    private final MutableLiveData<Integer> petDailyFooding = new MutableLiveData<>();
    private final MutableLiveData<List<Weighing>> petWeighings =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> weightTrend = new MutableLiveData<>(99);
    private final Map<LiveData<?>, Observer> mapObservableObserver = new HashMap<>();

    public PetFoodingViewModel() {
        updateObsDataOnPetChange();
    }

    /**
     * Update the observables when the pet change.
     */
    private void updateObsDataOnPetChange() {
        Log.d(TAG, "updateObsDataOnPetChange");
        Observer<Pet> observer = PetFoodingViewModel.this::updateObsData;
        pet.observeForever(observer);
        mapObservableObserver.put(pet, observer);
    }

    /**
     * Update the observables for the pet given in parameter
     * @param pet the pet new value
     */
    private void updateObsData(Pet pet) {
        if (pet == null) { return; }
        Log.d(TAG, "updateObsData pet : " + pet.getPetId());
        updatePhoto(pet);
        updateFooding(pet);
        updateWeighing(pet);
    }

    /**
     * Refresh the observables data including the pet. This method is used when an attribute of the
     * pet is changed, but not the pet itself.
     */
    void refreshObsData() {
        Observer<Pet> observer = this.pet::setValue;
        if (pet.getValue() == null) {return;}
        SingleLiveEvent<Pet> petRetrieved =
                petService.getPetById(this, pet.getValue().getPetId());
        petRetrieved.observeForever(observer);
        mapObservableObserver.put(petRetrieved, observer);
    }

    /**
     * Update the photo observable for the pet given in parameter.
     * @param pet the involved pet
     */
    private void updatePhoto(Pet pet) {
        Observer<Bitmap> observer = this::setPhoto;
        LiveData<Bitmap> updatePhoto = photoService.getPetBitmap(PetFoodingViewModel.this, pet);
        updatePhoto.observeForever(observer);
        mapObservableObserver.put(updatePhoto, observer);
    }

    /**
     * Set the photo observable value.
     * @param bitmap the bitmap value to set in the photo
     */
    private void setPhoto(Bitmap bitmap) {
        this.petPhoto.setValue(bitmap);
    }

    /**
     * Update the fooding observable for the pet given in parameter.
     * @param pet the pet involved
     */
    private void updateFooding(Pet pet) {
        LiveData<List<Fooding>> petFoodings = petService.getDailyPetFoodings(pet);
        Observer<List<Fooding>> obsPetFoodings =
                list -> list.stream()
                        .map(Fooding::getQuantity)
                        .reduce(Math::addExact)
                        .ifPresent(PetFoodingViewModel.this.petDailyFooding::setValue);
        petFoodings.observeForever(obsPetFoodings);
        mapObservableObserver.put(petFoodings, obsPetFoodings);
    }

    /**
     * Update the weighing observable for the pet given in parameter.
     * @param pet the pet involved
     */
    private void updateWeighing(Pet pet) {
        LiveData<List<Weighing>> petWeighings = petService.getWeighingsForPet(pet);
        Observer<List<Weighing>> obsPetWeighing = list -> {
            if (list != null && !list.isEmpty()) {
                this.petWeighings.setValue(list);
                updateWeightTrend(list);
            }
        };
        petWeighings.observeForever(obsPetWeighing);
        mapObservableObserver.put(petWeighings, obsPetWeighing);
    }

    /**
     * Update the weight trend observable.
     * @param list the list of weighing used for updating
     */
    private void updateWeightTrend(List<Weighing> list) {
        List<Weighing> twoLastWaighings = list.stream()
                .sorted(Comparator.comparing(Weighing::getWeighingDate).reversed())
                .limit(2).collect(Collectors.toList());
        if (twoLastWaighings.size() > 1) {
            int lastWeighing = twoLastWaighings.get(0).getWeightInGrams();
            int beforeLastWeighing = twoLastWaighings.get(1).getWeightInGrams();
            if (lastWeighing > beforeLastWeighing) {
                weightTrend.setValue(2);
            } else if (lastWeighing < beforeLastWeighing) {
                weightTrend.setValue(0);
            } else {
                weightTrend.setValue(1);
            }
        } else {
            weightTrend.setValue(1);
        }
    }

    /**
     * Save the fooding.
     * @param value the value of the fooding
     * @param userLogged the user having given the fooding
     */
    SingleLiveEvent<Boolean> saveFooding(Integer value, User userLogged) {
        if (pet.getValue() == null) {
            SingleLiveEvent<Boolean> returnFalse = new SingleLiveEvent<>();
            returnFalse.setValue(false);
            return returnFalse;
        }
        Fooding fooding = new Fooding(
                userLogged.getUserId(),
                pet.getValue().getPetId(),
                OffsetDateTime.now(), value);
        return petService.savePetFooding(this, fooding);
    }

    /**
     * Save a new weighing with a value given in parameter.
     * @param newValue the new weight
     * @return the result of the process
     */
    SingleLiveEvent<Boolean>  saveNewWeighing(Integer newValue) {
        SingleLiveEvent<Boolean> saveNewWeighingResult = new SingleLiveEvent<>();
        Weighing newWeighing = new Weighing(
                pet.getValue().getPetId(),
                OffsetDateTime.now(),
                newValue
        );
        Observer<Boolean> observer = saveNewWeighingResult::setValue;
        SingleLiveEvent<Boolean> observable = petService.saveNewWeighing(this, newWeighing);
        observable.observeForever(observer);
        mapObservableObserver.put(observable, observer);
        return saveNewWeighingResult;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mapObservableObserver.forEach(LiveData::removeObserver);
    }

    public MutableLiveData<Pet> getPet() {
        return pet;
    }

    public MutableLiveData<Bitmap> getPetPhoto() {
        return petPhoto;
    }

    public MutableLiveData<Integer> getPetDailyFooding() {
        return petDailyFooding;
    }

    public MutableLiveData<List<Weighing>> getPetWeighings() {
        return petWeighings;
    }

    public MutableLiveData<Integer> getWeightTrend() {
        return weightTrend;
    }

    @Override
    public void clearDisposables() {
        disposableManager.clear(this);
    }
}
