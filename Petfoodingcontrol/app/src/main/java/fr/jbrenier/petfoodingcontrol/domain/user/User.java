package fr.jbrenier.petfoodingcontrol.domain.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import fr.jbrenier.petfoodingcontrol.domain.pet.Pet;
import fr.jbrenier.petfoodingcontrol.domain.photo.Photo;

/**
 * A Pet Fooding Control user.
 * @author Jérôme Brenier
 */
public class User implements Parcelable {
    private String id;
    private String displayedName;
    private String email;
    private String password;
    private Photo photo;
    private List<Pet> petOwned;
    private List<Pet> petAuthorizedToFed;

    public User(String id, String displayedName, String email, String password) {
        this.id = id;
        this.displayedName = displayedName;
        this.email = email;
        this.password = password;
    }

    protected User(Parcel in) {
        id = in.readString();
        displayedName = in.readString();
        email = in.readString();
        password = in.readString();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public List<Pet> getPetOwned() {
        return petOwned;
    }

    public void setPetOwned(List<Pet> petOwned) {
        this.petOwned = petOwned;
    }

    public List<Pet> getPetAuthorizedToFed() {
        return petAuthorizedToFed;
    }

    public void setPetAuthorizedToFed(List<Pet> petAuthorizedToFed) {
        this.petAuthorizedToFed = petAuthorizedToFed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(displayedName);
        dest.writeString(email);
        dest.writeString(password);
    }
}
