package fr.jbrenier.petfoodingcontrol.services.petservice;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import java.util.List;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.android.application.PetFoodingControl;
import fr.jbrenier.petfoodingcontrol.android.extras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.PetFeeder;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.Fooding;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.weight.Weighing;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;
import fr.jbrenier.petfoodingcontrol.domain.model.Feeder;
import fr.jbrenier.petfoodingcontrol.repository.PetFoodingControlRepository;
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableManager;
import fr.jbrenier.petfoodingcontrol.services.disposablemanagement.DisposableOwner;
import io.reactivex.disposables.Disposable;

/**
 * The Pet service implementation.
 * @author Jérôme Brenier
 */
public class PetServiceImpl implements PetService {
    /** LOGGING */
    private static final String TAG = "PetService";

    private PetFoodingControlRepository pfcRepository;

    @Inject
    DisposableManager disposableManager;

    @Inject
    public PetServiceImpl(PetFoodingControl petFoodingControl,
                          PetFoodingControlRepository pfcRepository) {
        petFoodingControl.getAppComponent().inject(this);
        this.pfcRepository = pfcRepository;
    }

    @Override
    public SingleLiveEvent<Pet> save(DisposableOwner disposableOwner, Pet pet) {
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
        disposableManager.addDisposable(disposableOwner, disposable);
        return savePetResult;
    }

    @Override
    public SingleLiveEvent<Boolean> delete(DisposableOwner disposableOwner, Pet pet) {
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
        disposableManager.addDisposable(disposableOwner, disposable);
        return deletePetResult;
    }

    @Override
    public SingleLiveEvent<Pet> update(DisposableOwner disposableOwner, Pet pet) {
        SingleLiveEvent<Pet> updatePetResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.updatePet(pet).subscribe(
                () -> {
                    updatePetResult.setValue(pet);
                    Log.i(TAG,"Pet updated");
                },
                throwable -> {
                    updatePetResult.setValue(null);
                    Log.e(TAG, "Pet " + pet.getPetId() + " update failure : ", throwable);
                });
        disposableManager.addDisposable(disposableOwner, disposable);
        return updatePetResult;
    }

    @Override
    public LiveData<List<Pet>> getUserPets(User user) {
        return LiveDataReactiveStreams.fromPublisher(
                pfcRepository.getAllUserPetsByUserId(user.getUserId()));
    }

    @Override
    public SingleLiveEvent<Pet> getPetById(DisposableOwner disposableOwner, Long petId) {
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
        disposableManager.addDisposable(disposableOwner, disposable);
        return resultGetPetById;
    }

    @Override
    public SingleLiveEvent<Feeder> checkFeederExistance(DisposableOwner disposableOwner,
                                                        String email) {
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
        disposableManager.addDisposable(disposableOwner, disposable);
        return checkFeederExistanceResult;
    }

    @Override
    public SingleLiveEvent<List<Feeder>> getFeeders(DisposableOwner disposableOwner, Pet pet) {
        SingleLiveEvent<List<Feeder>> getFeeders = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.getFeedersForPet(pet.getPetId()).subscribe(
                getFeeders::setValue, throwable -> getFeeders.setValue(null));
        disposableManager.addDisposable(disposableOwner, disposable);
        return getFeeders;
    }

    @Override
    public SingleLiveEvent<Boolean> removePetFeeder(DisposableOwner disposableOwner,
                                                    PetFeeder petFeeder) {
        SingleLiveEvent<Boolean> removePetFeederResult = new SingleLiveEvent<>();
        Disposable disposable = pfcRepository.deletePetFeeder(petFeeder).subscribe(
                () -> {
                    removePetFeederResult.setValue(true);
                    Log.i(TAG, "PetFeeder for pet : " + petFeeder.getPetId() + " deleted");
                },
                throwable -> {
                    removePetFeederResult.setValue(false);
                    Log.e(TAG, "PetFeeder delete failure", throwable);
                });
        disposableManager.addDisposable(disposableOwner, disposable);
        return removePetFeederResult;
    }

    @Override
    public SingleLiveEvent<Integer> savePetFeeders(DisposableOwner disposableOwner,
                                                   List<PetFeeder> petFeederList) {
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
        disposableManager.addDisposable(disposableOwner, disposable);
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
    public SingleLiveEvent<Boolean> savePetFooding(DisposableOwner disposableOwner, Fooding fooding) {
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
        disposableManager.addDisposable(disposableOwner, disposable);
        return savePetFoodingResult;
    }

    @Override
    public LiveData<List<Weighing>> getWeighingsForPet(Pet pet) {
        return LiveDataReactiveStreams.fromPublisher(
                pfcRepository.getWeighingsForPet(pet.getPetId()));
    }

    @Override
    public SingleLiveEvent<Boolean> saveNewWeighing(DisposableOwner disposableOwner, Weighing weighing) {
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
        disposableManager.addDisposable(disposableOwner, disposable);
        return saveNewWeighingResult;
    }
}
