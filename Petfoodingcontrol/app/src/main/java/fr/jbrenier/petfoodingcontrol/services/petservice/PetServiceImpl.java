package fr.jbrenier.petfoodingcontrol.services.petservice;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;

import java.util.List;

import javax.inject.Inject;

import fr.jbrenier.petfoodingcontrol.androidextras.SingleLiveEvent;
import fr.jbrenier.petfoodingcontrol.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.entities.user.User;
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
    public SingleLiveEvent<Pet> save(Context context, Pet pet) {
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
        addToCompositeDisposable(context, disposable);
        return savePetResult;
    }

    @Override
    public SingleLiveEvent<Integer> update(Context context, Pet pet) {
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
        addToCompositeDisposable(context, disposable);
        return updatePetResult;
    }

    @Override
    public LiveData<List<Pet>> getUserPets(User user) {
        return LiveDataReactiveStreams.fromPublisher(
                pfcRepository.getAllUserPetsByUserId(user.getUserId()));
    }

    @Override
    public PetFoodingControlRepository getPfcRepository() {
        return pfcRepository;
    }
}
