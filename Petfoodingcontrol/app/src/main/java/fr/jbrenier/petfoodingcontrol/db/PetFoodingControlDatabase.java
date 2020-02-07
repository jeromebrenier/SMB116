package fr.jbrenier.petfoodingcontrol.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import fr.jbrenier.petfoodingcontrol.db.dao.AutoLoginDao;
import fr.jbrenier.petfoodingcontrol.db.dao.FoodingDao;
import fr.jbrenier.petfoodingcontrol.db.dao.PetDao;
import fr.jbrenier.petfoodingcontrol.db.dao.PetFeedersDao;
import fr.jbrenier.petfoodingcontrol.db.dao.PhotoDao;
import fr.jbrenier.petfoodingcontrol.db.dao.UserDao;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.PetFeeders;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.food.Fooding;
import fr.jbrenier.petfoodingcontrol.domain.entities.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.AutoLogin;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;

/**
 * The Pet Fooding Control Local Sqlite Database.
 * @author Jérôme Brenier
 */
@Database(entities = {
        User.class,
        AutoLogin.class,
        Pet.class,
        Photo.class,
        Fooding.class,
        PetFeeders.class},
        version = 1,
        exportSchema = false
)
public abstract class PetFoodingControlDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
    public abstract AutoLoginDao getAutoLoginDao();
    public abstract PetDao getPetDao();
    public abstract PhotoDao getPhotoDao();
    public abstract FoodingDao getFoodingDao();
    public abstract PetFeedersDao getPetFeedersDao();
}
