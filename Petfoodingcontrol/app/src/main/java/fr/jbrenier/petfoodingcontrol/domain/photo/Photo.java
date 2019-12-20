package fr.jbrenier.petfoodingcontrol.domain.photo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * A photo that can be used to illustrate a user or a pet in the application
 * Pet Fooding Control.
 * @author Jérôme Brenier
 */
public class Photo implements Parcelable {
    private String id;
    private String image;

    public Photo(String id, String image) {
        this.id = id;
        this.image = image;
    }

    protected Photo(Parcel in) {
        id = in.readString();
        image = in.readString();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(image);
    }

    public String getImage() {
        return image;
    }
}
