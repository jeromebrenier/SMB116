package fr.jbrenier.petfoodingcontrol.entities.photo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * A photo that can be used to illustrate a user or a pet in the application
 * Pet Fooding Control.
 * @author Jérôme Brenier
 */
@Entity
public class Photo implements Parcelable {
    @NonNull
    @ColumnInfo(name = "photo_Id")
    @PrimaryKey(autoGenerate = true)
    private Long photoId;
    private String image;

    public Photo(String image) {
        this.image = image;
    }

    @Ignore
    public Photo(Long photoId, String image) {
        this.photoId = photoId;
        this.image = image;
    }

    protected Photo(Parcel in) {
        photoId = in.readLong();
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
        dest.writeLong(photoId == null ? 0 : photoId);
        dest.writeString(image);
    }

    @NonNull
    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(@NonNull Long photoIdd) {
        this.photoId = photoIdd;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

