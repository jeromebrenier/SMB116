package fr.jbrenier.petfoodingcontrol.domain.user;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;

/**
 * A Pet Fooding Control user.
 * @author Jérôme Brenier
 */
@Entity(foreignKeys = {
                @ForeignKey(entity = Photo.class,
                        parentColumns = "id",
                        childColumns = "photoId"),
                @ForeignKey(entity = Pet.class,
                        parentColumns = "id",
                        childColumns = "petId")
})
public class User implements Parcelable {
    @NonNull
    @PrimaryKey (autoGenerate = true)
    private Long id;
    private String displayedName;
    private String email;
    private String password;
    private Long photoId;

    public User(Long id, String displayedName, String email, String password, Long photoId) {
        this.id = id;
        this.displayedName = displayedName;
        this.email = email;
        this.password = password;
        this.photoId = photoId;
    }

    protected User(Parcel in) {
        id = in.readLong();
        displayedName = in.readString();
        email = in.readString();
        password = in.readString();
        photoId = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong((id == null) ? 0 : id);
        dest.writeString(displayedName);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeLong(photoId);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayedName() {
        return displayedName;
    }

    public void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }
}
