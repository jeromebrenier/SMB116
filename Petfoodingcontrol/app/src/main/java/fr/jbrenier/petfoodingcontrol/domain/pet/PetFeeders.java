package fr.jbrenier.petfoodingcontrol.domain.pet;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import fr.jbrenier.petfoodingcontrol.domain.user.User;

/**
 * Pet feeders.
 * @author Jérôme Brenier
 */
@Entity(primaryKeys = { "pet_Id", "feeder_Id" },
        foreignKeys = {
                @ForeignKey(entity = Pet.class,
                        parentColumns = "pet_Id",
                        childColumns = "pet_Id",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = User.class,
                        parentColumns = "user_Id",
                        childColumns = "feeder_Id",
                        onDelete = ForeignKey.CASCADE)},
        inheritSuperIndices = true
)
public class PetFeeders implements Parcelable {
    @NonNull
    @ColumnInfo(name = "pet_Id")
    private Long petId;
    @NonNull
    @ColumnInfo(name = "feeder_Id")
    private Long feederId;

    public PetFeeders(Long petId, Long feederId) {
        this.petId = petId;
        this.feederId = feederId;
    }

    protected PetFeeders(Parcel in) {
        if (in.readByte() == 0) {
            petId = null;
        } else {
            petId = in.readLong();
        }
        if (in.readByte() == 0) {
            feederId = null;
        } else {
            feederId = in.readLong();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (petId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(petId);
        }
        if (feederId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(feederId);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PetFeeders> CREATOR = new Creator<PetFeeders>() {
        @Override
        public PetFeeders createFromParcel(Parcel in) {
            return new PetFeeders(in);
        }

        @Override
        public PetFeeders[] newArray(int size) {
            return new PetFeeders[size];
        }
    };

    public Long getPetId() {
        return petId;
    }

    public void setPetId(Long petId) {
        this.petId = petId;
    }

    public Long getFeederId() {
        return feederId;
    }

    public void setFeederId(Long feederId) {
        this.feederId = feederId;
    }
}
