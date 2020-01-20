package fr.jbrenier.petfoodingcontrol.domain.pet;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.OffsetDateTime;
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
                parentColumns = "user_Id",
                childColumns = "user_Id"),
        @ForeignKey(
                entity = Photo.class,
                parentColumns = "photo_Id",
                childColumns = "photo_Id")},
        indices = {
        @Index(value = {"user_Id"}),
        @Index(value = {"photo_Id"})
        },
        inheritSuperIndices = true
)
public class Pet implements Parcelable {
    @NonNull
    @ColumnInfo(name = "pet_Id")
    @PrimaryKey(autoGenerate = true)
    private Long petId;
    private String name;
    @ColumnInfo(name = "photo_Id")
    private Long photoId;
    @TypeConverters(DataTypeConverter.class)
    private OffsetDateTime birthDate;
    @Embedded
    private FoodSettings foodSettings;
    @ColumnInfo(name = "user_Id")
    private Long userId;

    public Pet(Long petId, String name, Long photoId, OffsetDateTime birthDate,
               FoodSettings foodSettings, Long userId) {
        this.petId = petId;
        this.name = name;
        this.photoId = photoId;
        this.birthDate = birthDate;
        this.foodSettings = foodSettings;
        this.userId = userId;
     }


    protected Pet(Parcel in) {
        petId = in.readLong();
        name = in.readString();
        photoId = in.readLong();
        birthDate = (java.time.OffsetDateTime) in.readSerializable();
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
        dest.writeLong((petId == null) ? 0 : petId);
        dest.writeString(name);
        dest.writeLong(photoId);
        dest.writeSerializable(birthDate);
        dest.writeParcelable(foodSettings, flags);
        dest.writeLong(userId);
    }

    @NonNull
    public Long getPetId() {
        return petId;
    }

    public void setPetId(@NonNull Long petId) {
        this.petId = petId;
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

    public OffsetDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(OffsetDateTime birthDate) {
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
