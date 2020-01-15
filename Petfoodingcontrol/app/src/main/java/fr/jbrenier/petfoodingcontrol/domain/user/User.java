package fr.jbrenier.petfoodingcontrol.domain.user;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;

/**
 * A Pet Fooding Control user.
 * @author Jérôme Brenier
 */
@Entity(foreignKeys = @ForeignKey(entity = Photo.class,
                        parentColumns = "photo_Id",
                        childColumns = "photo_Id"),
        indices = {@Index("photo_Id")}
)
public class User implements Parcelable {
    @NonNull
    @ColumnInfo(name = "user_Id")
    @PrimaryKey (autoGenerate = true)
    private Long userId;
    private String displayedName;
    private String email;
    private String password;
    @ColumnInfo(name = "photo_Id")
    private Long photoId;

    public User(Long userId, String displayedName, String email, String password, Long photoId) {
        this.userId = userId;
        this.displayedName = displayedName;
        this.email = email;
        this.password = password;
        this.photoId = photoId;
    }

    protected User(Parcel in) {
        userId = in.readLong();
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
        dest.writeLong((userId == null) ? 0 : userId);
        dest.writeString(displayedName);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeLong(photoId);
    }

    @NonNull
    public Long getUserId() {
        return userId;
    }

    public void setUserId(@NonNull Long userId) {
        this.userId = userId;
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
