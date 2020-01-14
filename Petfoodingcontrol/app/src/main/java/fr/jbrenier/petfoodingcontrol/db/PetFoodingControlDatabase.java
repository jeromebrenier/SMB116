package fr.jbrenier.petfoodingcontrol.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import fr.jbrenier.petfoodingcontrol.db.converters.DataTypeConverter;
import fr.jbrenier.petfoodingcontrol.db.dao.FoodingDao;
import fr.jbrenier.petfoodingcontrol.db.dao.PetDao;
import fr.jbrenier.petfoodingcontrol.db.dao.PetFeedersDao;
import fr.jbrenier.petfoodingcontrol.db.dao.PhotoDao;
import fr.jbrenier.petfoodingcontrol.db.dao.UserDao;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.pet.PetFeeders;
import fr.jbrenier.petfoodingcontrol.domain.pet.food.Fooding;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.User;

/**
 * The Pet Fooding Control Local Sqlite Database.
 * @author Jérôme Brenier
 */
@Database(entities = {
        User.class,
        Pet.class,
        Photo.class,
        Fooding.class,
        PetFeeders.class},
        version = 1
)
public abstract class PetFoodingControlDatabase extends RoomDatabase {
    public abstract UserDao getUserDao();
    public abstract PetDao getPetDao();
    public abstract PhotoDao getPhotoDao();
    public abstract FoodingDao getFoodingDao();
    public abstract PetFeedersDao getPetFeedersDao();
}
