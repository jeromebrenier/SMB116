package fr.jbrenier.petfoodingcontrol.ui.activities.petfooding;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.Fooding;
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

    private MutableLiveData<Pet> pet = new MutableLiveData<>();
    private MutableLiveData<Bitmap> petPhoto;
    private MutableLiveData<Integer> petDailyFooding = new MutableLiveData<>();
    private MutableLiveData<String> strPetDailyFooding = new MutableLiveData<>("No data");

    private Map<LiveData<?>, Observer> mapObservableObserver = new HashMap<>();

    public PetFoodingViewModel() {
        updatePhotoAndFoodingOnPetChange();
    }

    private void updatePhotoAndFoodingOnPetChange() {
        Log.d(TAG, "updatePhotoAndFoodingOnPetChange");
        Observer<Pet> observer = PetFoodingViewModel.this::updatePhotoAndFooding;
        pet.observeForever(observer);
        mapObservableObserver.put(pet, observer);
    }

    private void updatePhotoAndFooding(Pet pet) {
        if (pet == null) { return; }
        Log.d(TAG, "updatePhotoAndFooding pet : " + pet.getPetId());
        petPhoto = photoService.get(PetFoodingViewModel.this, pet);
        LiveData<List<Fooding>> petFoodings = petService.getPetFoodings(pet);
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

    public void saveFooding(Integer value, User userLogged) {
        if (pet.getValue() == null) { return; }
        Fooding fooding = new Fooding(
                userLogged.getUserId(),
                pet.getValue().getPetId(),
                OffsetDateTime.now(), value);
        petService.savePetFooding(this, fooding);
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
}
