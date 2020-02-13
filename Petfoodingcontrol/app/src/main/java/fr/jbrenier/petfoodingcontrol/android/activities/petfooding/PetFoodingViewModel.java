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

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.Fooding;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.weight.Weighing;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.services.petservice.PetService;
import fr.jbrenier.petfoodingcontrol.services.photoservice.PhotoService;

public class PetFoodingViewModel extends ViewModel {

    @Inject
    PetService petService;

    @Inject
    PhotoService photoService;

    /** Logging */
    private static final String TAG = "PetFoodingViewModel";

    private final MutableLiveData<Pet> pet = new MutableLiveData<>();
    private MutableLiveData<Bitmap> petPhoto;
    private final MutableLiveData<Integer> petDailyFooding = new MutableLiveData<>();
    private final MutableLiveData<String> strPetDailyFooding = new MutableLiveData<>("No data");
    private final MutableLiveData<List<Weighing>> petWeighings =
            new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> weightTrend = new MutableLiveData<>(1);
    private Map<LiveData<?>, Observer> mapObservableObserver = new HashMap<>();

    public PetFoodingViewModel() {
        updateObsDataOnPetChange();
    }

    private void updateObsDataOnPetChange() {
        Log.d(TAG, "updateObsDataOnPetChange");
        Observer<Pet> observer = PetFoodingViewModel.this::updateObsData;
        pet.observeForever(observer);
        mapObservableObserver.put(pet, observer);
    }

    private void updateObsData(Pet pet) {
        if (pet == null) { return; }
        Log.d(TAG, "updateObsData pet : " + pet.getPetId());
        updatePhoto(pet);
        updateFooding(pet);
        updateWeighing(pet);
    }

    private void updatePhoto(Pet pet) {
        petPhoto = photoService.get(PetFoodingViewModel.this, pet);
    }

    private void updateFooding(Pet pet) {
        LiveData<List<Fooding>> petFoodings = petService.getDailyPetFoodings(pet);
        Observer<List<Fooding>> obsPetFoodings =
                list -> list.stream()
                        .map(Fooding::getQuantity)
                        .reduce(Math::addExact)
                        .ifPresent(PetFoodingViewModel.this.petDailyFooding::setValue);
        petFoodings.observeForever(obsPetFoodings);
        mapObservableObserver.put(petFoodings, obsPetFoodings);
        Observer<Integer> petDailyObs = PetFoodingViewModel.this::updateStrPetDailyFooding;
        PetFoodingViewModel.this.petDailyFooding.observeForever(petDailyObs);
        mapObservableObserver.put(PetFoodingViewModel.this.petDailyFooding, petDailyObs);
    }

    private void updateStrPetDailyFooding(Integer integer) {
        String daily = pet.getValue() == null ? "" :
                pet.getValue().getFoodSettings().getDailyQuantity().toString();
        strPetDailyFooding.setValue(integer + " g out of " + daily + " g");
    }

    private void updateWeighing(Pet pet) {
        LiveData<List<Weighing>> petWeighings = petService.getWeighingsForPet(pet);
        Observer<List<Weighing>> obsPetWeighing = list -> {
            this.petWeighings.setValue(list);
            updateWeightTrend(list);
        };
        petWeighings.observeForever(obsPetWeighing);
        mapObservableObserver.put(petWeighings, obsPetWeighing);
    }

    private void updateWeightTrend(List<Weighing> list) {
        list.stream().peek(w -> Log.d(TAG, w.getWeightInGrams() + " " + w.getWeighingDate())).close();
    }

    void saveFooding(Integer value, User userLogged) {
        if (pet.getValue() == null) { return; }
        Fooding fooding = new Fooding(
                userLogged.getUserId(),
                pet.getValue().getPetId(),
                OffsetDateTime.now(), value);
        petService.savePetFooding(this, fooding);
    }

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

    public MutableLiveData<String> getStrPetDailyFooding() {
        return strPetDailyFooding;
    }

    public MutableLiveData<List<Weighing>> getPetWeighings() {
        return petWeighings;
    }

    public MutableLiveData<Integer> getWeightTrend() {
        return weightTrend;
    }
}
