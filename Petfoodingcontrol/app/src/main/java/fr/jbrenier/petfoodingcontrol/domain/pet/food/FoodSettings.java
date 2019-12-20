package fr.jbrenier.petfoodingcontrol.domain.pet.food;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * The food settings of a Pet.
 * @author Jérôme Brenier
 */
public class FoodSettings implements Parcelable {
    private int dailyQuantity;
    private List<Integer> preSetPortionList;

    public FoodSettings(int dailyQuantity, List<Integer> preSetPortionList) {
        this.dailyQuantity = dailyQuantity;
        this.preSetPortionList = new ArrayList<>(preSetPortionList);
    }

    protected FoodSettings(Parcel in) {
        dailyQuantity = in.readInt();
        preSetPortionList = in.readArrayList(null);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dailyQuantity);
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
}
