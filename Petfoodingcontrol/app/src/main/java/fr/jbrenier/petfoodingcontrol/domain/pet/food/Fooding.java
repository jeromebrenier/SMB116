package fr.jbrenier.petfoodingcontrol.domain.pet.food;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.TypeConverters;

import java.time.OffsetDateTime;
import java.util.Date;

import fr.jbrenier.petfoodingcontrol.db.converters.DataTypeConverter;
import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.user.User;

/**
 * A fooding action.
 * @author Jérôme Brenier
 */
@Entity(primaryKeys = { "user_Id", "pet_Id" },
        indices = {
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
    @NonNull
    @ColumnInfo(name = "user_Id")
    private Long userId;
    @NonNull
    @ColumnInfo(name = "pet_Id")
    private Long petId;
    @TypeConverters(DataTypeConverter.class)
    private OffsetDateTime date;
    private Integer quantity;

    public Fooding(Long userId, Long petId, OffsetDateTime date, Integer quantity) {
        this.userId = userId;
        this.petId = petId;
        this.date = date;
        this.quantity = (quantity == null ? 0 : quantity);
    }

    protected Fooding(Parcel in) {
        userId = in.readLong();
        petId = in.readLong();
        date = (java.time.OffsetDateTime) in.readSerializable();
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
        dest.writeSerializable(date);
        dest.writeInt(quantity);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
