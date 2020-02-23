package fr.jbrenier.petfoodingcontrol.domain.entities.pet;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import fr.jbrenier.petfoodingcontrol.domain.entities.user.User;

/**
 * Pet feeders.
 * @author Jérôme Brenier
 */
@Entity(primaryKeys = { "pet_Id", "user_Id" },
        indices = {
        @Index(value = {"user_Id"})
        },
        foreignKeys = {
                @ForeignKey(entity = Pet.class,
                        parentColumns = "pet_Id",
                        childColumns = "pet_Id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                        parentColumns = "user_Id",
                        childColumns = "user_Id",
                        onDelete = ForeignKey.CASCADE)},
        inheritSuperIndices = true
)
public class PetFeeder implements Parcelable {
    @NonNull
    @ColumnInfo(name = "pet_Id")
    private Long petId;
    @NonNull
    @ColumnInfo(name = "user_Id")
    private Long userId; // The feeder

    public PetFeeder(@NonNull Long petId, @NonNull Long userId) {
        this.petId = petId;
        this.userId = userId;
    }

    protected PetFeeder(Parcel in) {
        petId = in.readLong();
        userId = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(petId);
        dest.writeLong(userId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PetFeeder> CREATOR = new Creator<PetFeeder>() {
        @Override
        public PetFeeder createFromParcel(Parcel in) {
            return new PetFeeder(in);
        }

        @Override
        public PetFeeder[] newArray(int size) {
            return new PetFeeder[size];
        }
    };

    @NonNull
    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    @NonNull
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
