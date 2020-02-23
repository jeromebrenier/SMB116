package fr.jbrenier.petfoodingcontrol.domain.entities.pet.food;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.OffsetDateTime;

import fr.jbrenier.petfoodingcontrol.db.converters.DataTypeConverter;
import fr.jbrenier.petfoodingcontrol.domain.entities.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;


/**
 * A fooding action.
 * @author Jérôme Brenier
 */
@Entity(indices = {
        @Index(value = {"user_Id"}),
        @Index(value = {"pet_Id"})
        },
        foreignKeys = {
        @ForeignKey(entity = User.class,
                parentColumns = "user_Id",
                childColumns = "user_Id",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Pet.class,
                parentColumns = "pet_Id",
                childColumns = "pet_Id",
                onDelete = ForeignKey.CASCADE)})
public class Fooding implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Long foodingId;
    @NonNull
    @ColumnInfo(name = "user_Id")
    private Long userId;
    @NonNull
    @ColumnInfo(name = "pet_Id")
    private Long petId;
    @TypeConverters(DataTypeConverter.class)
    @ColumnInfo(name = "fooding_date")
    private OffsetDateTime foodingDate;
    private Integer quantity;

    public Fooding(@NonNull Long userId,@NonNull Long petId, OffsetDateTime foodingDate,
                   Integer quantity) {
        this.userId = userId;
        this.petId = petId;
        this.foodingDate = foodingDate;
        this.quantity = (quantity == null ? 0 : quantity);
    }

    @Ignore
    public Fooding(@NonNull Long foodingId, @NonNull Long userId, @NonNull Long petId, OffsetDateTime foodingDate, Integer quantity) {
        this.foodingId = foodingId;
        this.userId = userId;
        this.petId = petId;
        this.foodingDate = foodingDate;
        this.quantity = quantity;
    }

    protected Fooding(Parcel in) {
        userId = in.readLong();
        petId = in.readLong();
        foodingDate = (java.time.OffsetDateTime) in.readSerializable();
        quantity = in.readInt();
    }

    public static final Creator<Fooding> CREATOR = new Creator<Fooding>() {
        @Override
        public Fooding createFromParcel(Parcel in) {
            return new Fooding(in);
        }

        @Override
        public Fooding[] newArray(int size) {
            return new Fooding[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(userId);
        dest.writeLong(petId);
        dest.writeSerializable(foodingDate);
        dest.writeInt(quantity);
    }

    @NonNull
    public Long getFoodingId() {
        return foodingId;
    }

    public void setFoodingId(@NonNull Long foodingId) {
        this.foodingId = foodingId;
    }

    @NonNull
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @NonNull
    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public OffsetDateTime getFoodingDate() {
        return foodingDate;
    }

    public void setFoodingDate(OffsetDateTime foodingDate) {
        this.foodingDate = foodingDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
