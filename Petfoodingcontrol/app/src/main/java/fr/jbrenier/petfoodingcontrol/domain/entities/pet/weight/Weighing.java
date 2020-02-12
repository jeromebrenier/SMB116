package fr.jbrenier.petfoodingcontrol.domain.entities.pet.weight;

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

/**
 * A weighing action.
 * @author Jérôme Brenier
 */
@Entity(indices = {@Index(value = {"pet_Id"})},
        foreignKeys = {
                @ForeignKey(entity = Pet.class,
                        parentColumns = "pet_Id",
                        childColumns = "pet_Id",
                        onDelete = ForeignKey.CASCADE)})
public class Weighing implements Parcelable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long weighingId;
    @NonNull
    @ColumnInfo(name = "pet_Id")
    private Long petId;
    @TypeConverters(DataTypeConverter.class)
    @ColumnInfo(name = "weighing_date")
    private OffsetDateTime weighingDate;
    private Integer weightInGrams;

    public Weighing(@NonNull Long petId, OffsetDateTime weighingDate, Integer weightInGrams) {
        this.petId = petId;
        this.weighingDate = weighingDate;
        this.weightInGrams = weightInGrams;
    }

    @Ignore
    public Weighing(@NonNull Long weighingId, @NonNull Long petId, OffsetDateTime weighingDate, Integer weightInGrams) {
        this.weighingId = weighingId;
        this.petId = petId;
        this.weighingDate = weighingDate;
        this.weightInGrams = weightInGrams;
    }

    protected Weighing(Parcel in) {
        if (in.readByte() == 0) {
            weighingId = null;
        } else {
            weighingId = in.readLong();
        }
        if (in.readByte() == 0) {
            petId = null;
        } else {
            petId = in.readLong();
        }
        if (in.readSerializable() == null) {
            weighingDate = null;
        } else {
            weighingDate = (java.time.OffsetDateTime) in.readSerializable();
        }
        if (in.readByte() == 0) {
            weightInGrams = null;
        } else {
            weightInGrams = in.readInt();
        }
    }

    public static final Creator<Weighing> CREATOR = new Creator<Weighing>() {
        @Override
        public Weighing createFromParcel(Parcel in) {
            return new Weighing(in);
        }

        @Override
        public Weighing[] newArray(int size) {
            return new Weighing[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (weighingId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(weighingId);
        }
        if (petId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(petId);
        }
        if (weighingDate != null) {
            dest.writeSerializable(weighingDate);
        }
        if (weightInGrams == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(weightInGrams);
        }
    }

    @NonNull
    public Long getWeighingId() {
        return weighingId;
    }

    public void setWeighingId(@NonNull Long weighingId) {
        this.weighingId = weighingId;
    }

    @NonNull
    public Long getPetId() {
        return petId;
    }

    public void setPetId(@NonNull Long petId) {
        this.petId = petId;
    }

    public OffsetDateTime getWeighingDate() {
        return weighingDate;
    }

    public void setWeighingDate(OffsetDateTime weighingDate) {
        this.weighingDate = weighingDate;
    }

    public Integer getWeightInGrams() {
        return weightInGrams;
    }

    public void setWeightInGrams(Integer weightInGrams) {
        this.weightInGrams = weightInGrams;
    }
}
