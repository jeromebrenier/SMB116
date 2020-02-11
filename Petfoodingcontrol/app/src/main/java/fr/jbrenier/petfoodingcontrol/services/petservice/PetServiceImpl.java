package fr.jbrenier.petfoodingcontrol.services.petservice;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import java.util.List;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.PetFeeders;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.Fooding;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.domain.model.Feeder;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.PetFoodingControlService;
import io.reactivex.disposables.Disposable;

public class PetServiceImpl extends PetFoodingControlService implements PetService {
    /** LOGGING */
    private static final String TAG = "PetService";

    private PetFoodingControlRepository pfcRepository;

    @Inject
    public PetServiceImpl(PetFoodingControlRepository pfcRepository) {
        this.pfcRepository = pfcRepository;
    }

    @Override
    public SingleLiveEvent<Pet> save(Object object, Pet pet) {
        SingleLiveEvent<Pet> savePetResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.save(pet).subscribe(
                (petId) -> {
                    pet.setPetId(petId);
                    savePetResult.setValue(pet);
                    Log.i(TAG, "Pet saved with id : " + petId);
                },
                throwable -> {
                    savePetResult.setValue(null);
                    Log.e(TAG, "Pet saving failure", throwable);
                });
        addToCompositeDisposable(object, disposable);
        return savePetResult;
    }

    @Override
    public SingleLiveEvent<Integer> update(Object object, Pet pet) {
        SingleLiveEvent<Integer> updatePetResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.update(pet).subscribe(
                () -> {
                    updatePetResult.setValue(0);
                    Log.i(TAG,"Pet updated");
                },
                throwable -> {
                    updatePetResult.setValue(1);
                    Log.e(TAG, "Pet " + pet.getPetId() + " update failure : ", throwable);
                });
        addToCompositeDisposable(object, disposable);
        return updatePetResult;
    }

    @Override
    public LiveData<List<Pet>> getUserPets(User user) {
        return LiveDataReactiveStreams.fromPublisher(
                pfcRepository.getAllUserPetsByUserId(user.getUserId()));
    }

    @Override
    public SingleLiveEvent<Feeder> checkFeederExistance(Object object, String email) {
        SingleLiveEvent<Feeder> checkFeederExistanceResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.getFeederByEmail(email).subscribe(
                feeder -> {
                    Log.i(TAG,"Feeder found.");
                    checkFeederExistanceResult.setValue(feeder);
                },
                throwable -> {
                    Log.e(TAG, "Feeder not found.");
                    checkFeederExistanceResult.setValue(null);
                }
        );
        addToCompositeDisposable(object, disposable);
        return checkFeederExistanceResult;
    }

    @Override
    public SingleLiveEvent<Integer> savePetFeeders(Object object, List<PetFeeders> petFeedersList) {
        SingleLiveEvent<Integer> savePetFeedersResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.insert(petFeedersList).subscribe(
                () -> {
                    savePetFeedersResult.setValue(0);
                    Log.i(TAG,"PetFeeders saved successfully");
                },
                throwable -> {
                    savePetFeedersResult.setValue(1);
                    Log.e(TAG, "PetFeeders saving failure : ", throwable);
                });
        addToCompositeDisposable(object, disposable);
        return savePetFeedersResult;
    }

    @Override
    public PetFoodingControlRepository getPfcRepository() {
        return pfcRepository;
    }

    @Override
    public LiveData<List<Fooding>> getPetFoodings(Pet pet) {
        return LiveDataReactiveStreams.fromPublisher(
                pfcRepository.getFoodingsForPet(pet.getPetId()));
    }

    @Override
    public SingleLiveEvent<Boolean> savePetFooding(Object object, Fooding fooding) {
        SingleLiveEvent<Boolean> savePetFoodingResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.insert(fooding).subscribe(
                () -> {
                    savePetFoodingResult.setValue(true);
                    Log.d(TAG,"Pet Fooding saved successfully");
                },
                throwable -> {
                    savePetFoodingResult.setValue(false);
                    Log.e(TAG, "Pet Fooding saving failure : ", throwable);
                });
        addToCompositeDisposable(object, disposable);
        return savePetFoodingResult;
    }
}
