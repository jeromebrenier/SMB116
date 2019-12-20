package fr.jbrenier.petfoodingcontrol.domain.pet;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import fr.jbrenier.petfoodingcontrol.domain.pet.food.FoodSettings;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;

/**
 * A Pet that can be fed and weight through the application
 * Pet Fooding Control.
 * @author Jérôme Brenier
 */
public class Pet implements Parcelable {
    private String id;
    private String name;
    private Photo photo;
    private Date birthDate;
    private FoodSettings foodSettings;

    public Pet(String id, String name, Photo photo, Date birthDate, FoodSettings foodSettings) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.birthDate = birthDate;
        this.foodSettings = foodSettings;
    }


    protected Pet(Parcel in) {
        id = in.readString();
        name = in.readString();
        photo = in.readParcelable(Photo.class.getClassLoader());
        birthDate = (java.util.Date) in.readSerializable();
        foodSettings = in.readParcelable(FoodSettings.class.getClassLoader());
    }

    public static final Creator<Pet> CREATOR = new Creator<Pet>() {
        @Override
        public Pet createFromParcel(Parcel in) {
            return new Pet(in);
        }

        @Override
        public Pet[] newArray(int size) {
            return new Pet[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeParcelable(photo, flags);
        dest.writeSerializable(birthDate);
        dest.writeParcelable(foodSettings, flags);
    }
}
