package fr.jbrenier.petfoodingcontrol.domain.photo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * A photo that can be used to illustrate a user or a pet in the application
 * Pet Fooding Control.
 * @author Jérôme Brenier
 */
@Entity
public class Photo implements Parcelable {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String image;

    public Photo(Long id, String image) {
        this.id = id;
        this.image = image;
    }

    protected Photo(Parcel in) {
        id = in.readLong();
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
        dest.writeLong(id == null ? 0 : id);
        dest.writeString(image);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

