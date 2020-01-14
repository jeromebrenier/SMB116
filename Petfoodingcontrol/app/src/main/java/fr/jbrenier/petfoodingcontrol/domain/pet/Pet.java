package fr.jbrenier.petfoodingcontrol.domain.pet;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.jbrenier.petfoodingcontrol.db.converters.DataTypeConverter;
import fr.jbrenier.petfoodingcontrol.domain.pet.food.FoodSettings;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;
import fr.jbrenier.petfoodingcontrol.domain.user.User;

/**
 * A Pet that can be fed and weight through the application
 * Pet Fooding Control.
 * @author Jérôme Brenier
 */
@Entity (foreignKeys = {
        @ForeignKey(
                entity = User.class,
                parentColumns = "id",
                childColumns = "userId"),
        @ForeignKey(
                entity = Photo.class,
                parentColumns = "id",
                childColumns = "photoId")}
)
public class Pet implements Parcelable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private Long photoId;
    @TypeConverters(DataTypeConverter.class)
    private Date birthDate;
    @Embedded
    private FoodSettings foodSettings;
    private Long userId;

    public Pet(Long id, String name, Long photoId, Date birthDate, FoodSettings foodSettings,
               Long userId) {
        this.id = id;
        this.name = name;
        this.photoId = photoId;
        this.birthDate = birthDate;
        this.foodSettings = foodSettings;
        this.userId = userId;
     }


    protected Pet(Parcel in) {
        id = in.readLong();
        name = in.readString();
        photoId = in.readLong();
        birthDate = (java.util.Date) in.readSerializable();
        foodSettings = in.readParcelable(FoodSettings.class.getClassLoader());
        userId = in.readLong();
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
        dest.writeLong((id == null) ? 0 : id);
        dest.writeString(name);
        dest.writeLong(photoId);
        dest.writeSerializable(birthDate);
        dest.writeParcelable(foodSettings, flags);
        dest.writeLong(userId);
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public FoodSettings getFoodSettings() {
        return foodSettings;
    }

    public void setFoodSettings(FoodSettings foodSettings) {
        this.foodSettings = foodSettings;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
