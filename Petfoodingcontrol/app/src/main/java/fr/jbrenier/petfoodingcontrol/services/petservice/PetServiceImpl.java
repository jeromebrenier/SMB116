package fr.jbrenier.petfoodingcontrol.services.petservice;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.PetFeeder;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.Fooding;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.weight.Weighing;
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
        Disposable disposable = pfcRepository.savePet(pet).subscribe(
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
    public SingleLiveEvent<Boolean> delete(Object object, Pet pet) {
        SingleLiveEvent<Boolean> deletePetResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.deletePet(pet).subscribe(
                () -> {
                    deletePetResult.setValue(true);
                    Log.i(TAG, "Pet with id : " + pet.getPetId() + " deleted");
                },
                throwable -> {
                    deletePetResult.setValue(false);
                    Log.e(TAG, "Pet saving failure", throwable);
                });
        addToCompositeDisposable(object, disposable);
        return deletePetResult;
    }

    @Override
    public SingleLiveEvent<Integer> update(Object object, Pet pet) {
        SingleLiveEvent<Integer> updatePetResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.updatePet(pet).subscribe(
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
    public SingleLiveEvent<Pet> getPetById(Object object, Long petId) {
        SingleLiveEvent<Pet> resultGetPetById = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.getPetById(petId).subscribe(
                pet -> {
                    resultGetPetById.setValue(pet);
                    Log.i(TAG,"Pet retrieved by id : " + petId);
                },
                throwable -> {
                    resultGetPetById.setValue(null);
                    Log.e(TAG, "Pet with id " + petId + " retrieval failure : ");
                });
        addToCompositeDisposable(object, disposable);
        return resultGetPetById;
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
    public SingleLiveEvent<List<Feeder>> getFeeders(Object object, Pet pet) {
        SingleLiveEvent<List<Feeder>> getFeeders = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.getFeedersForPet(pet.getPetId()).subscribe(
                getFeeders::setValue, throwable -> getFeeders.setValue(null));
        addToCompositeDisposable(object, disposable);
        return getFeeders;
    }

    @Override
    public SingleLiveEvent<Integer> savePetFeeders(Object object, List<PetFeeder> petFeederList) {
        SingleLiveEvent<Integer> savePetFeedersResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.insertPetFeeders(petFeederList).subscribe(
                () -> {
                    savePetFeedersResult.setValue(0);
                    Log.i(TAG,"PetFeeder saved successfully");
                },
                throwable -> {
                    savePetFeedersResult.setValue(1);
                    Log.e(TAG, "PetFeeder saving failure : ", throwable);
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
    public LiveData<Integer> getPetStatus(Pet pet) {
        return LiveDataReactiveStreams.fromPublisher(
                pfcRepository.getPetFoodingStatus(pet.getPetId()));
    }

    @Override
    public LiveData<List<Fooding>> getDailyPetFoodings(Pet pet) {
        return LiveDataReactiveStreams.fromPublisher(
                pfcRepository.getDailyFoodingsForPet(pet.getPetId()));
    }

    @Override
    public SingleLiveEvent<Boolean> savePetFooding(Object object, Fooding fooding) {
        SingleLiveEvent<Boolean> savePetFoodingResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.insertFooding(fooding).subscribe(
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

    @Override
    public LiveData<List<Weighing>> getWeighingsForPet(Pet pet) {
        return LiveDataReactiveStreams.fromPublisher(
                pfcRepository.getWeighingsForPet(pet.getPetId()));
    }

    @Override
    public SingleLiveEvent<Boolean> saveNewWeighing(Object object, Weighing weighing) {
        SingleLiveEvent<Boolean> saveNewWeighingResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.insertWeighing(weighing).subscribe(
                () -> {
                    saveNewWeighingResult.setValue(true);
                    Log.d(TAG,"Pet Weighing saved successfully");
                },
                throwable -> {
                    saveNewWeighingResult.setValue(false);
                    Log.e(TAG, "Pet Weighing saving failure : ", throwable);
                });
        addToCompositeDisposable(object, disposable);
        return saveNewWeighingResult;
    }
}
