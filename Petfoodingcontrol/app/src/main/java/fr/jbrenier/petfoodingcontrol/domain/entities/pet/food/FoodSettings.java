package fr.jbrenier.petfoodingcontrol.domain.entities.pet.food;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;

import fr.jbrenier.petfoodingcontrol.db.converters.DataTypeConverter;

/**
 * The food settings of a Pet.
 * @author Jérôme Brenier
 */
public class FoodSettings implements Parcelable {
    private Integer dailyQuantity;
    @TypeConverters(DataTypeConverter.class)
    private List<Integer> preSetPortionList;

    public FoodSettings(Integer dailyQuantity, List<Integer> preSetPortionList) {
        this.dailyQuantity = (dailyQuantity == null ? 0 : dailyQuantity);
        this.preSetPortionList = new ArrayList<>(preSetPortionList);
    }

    protected FoodSettings(Parcel in) {
        dailyQuantity = in.readInt();
        preSetPortionList = new ArrayList<>();
        in.readList(preSetPortionList, Integer.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dailyQuantity == null ? 0 : dailyQuantity);
        dest.writeList(preSetPortionList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FoodSettings> CREATOR = new Creator<FoodSettings>() {
        @Override
        public FoodSettings createFromParcel(Parcel in) {
            return new FoodSettings(in);
        }

        @Override
        public FoodSettings[] newArray(int size) {
            return new FoodSettings[size];
        }
    };

    public Integer getDailyQuantity() {
        return dailyQuantity;
    }

    public void setDailyQuantity(Integer dailyQuantity) {
        this.dailyQuantity = dailyQuantity;
    }

    public List<Integer> getPreSetPortionList() {
        return preSetPortionList;
    }

    public void setPreSetPortionList(List<Integer> preSetPortionList) {
        this.preSetPortionList = preSetPortionList;
    }
}
