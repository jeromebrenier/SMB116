package fr.jbrenier.petfoodingcontrol.repository;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.User;
import io.reactivex.Completable;
import io.reactivex.Single;

public interface PetFoodingControlRepository {
    public Single<User> getUserByEmail(String email);
    public Single<User> getUserById(Long userId);
    public Single<Photo> getUserPhoto(User user);
    public void setUserLogged(User user);
    public MutableLiveData<User> getUserLogged();
    public Single<Long> save(User user);
    public Completable update(User user);

    public Single<Long> save(Photo photo);

    public Single<Pet> getPetById(Long petId);
    public void setUserPets(User user);
    public Single<Photo> getPetPhoto(Pet pet);
    public MutableLiveData<List<Pet>> getUserPets();
}
