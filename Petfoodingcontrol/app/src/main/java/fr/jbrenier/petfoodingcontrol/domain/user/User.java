package fr.jbrenier.petfoodingcontrol.domain.user;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
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

    public User(String id, String displayedName, String email, String password, Photo photo,
                List<Pet> petOwned, List<Pet> petAuthorizedToFed) {
        this.id = id;
        this.displayedName = displayedName;
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.petOwned = new ArrayList<>();
        if (petOwned != null) {
            this.petOwned.addAll(petOwned);
        }
        this.petAuthorizedToFed = new ArrayList<>();
        if (petAuthorizedToFed != null) {
            this.petAuthorizedToFed.addAll(petAuthorizedToFed);
        }
    }

    protected User(Parcel in) {
        id = in.readString();
        displayedName = in.readString();
        email = in.readString();
        password = in.readString();
        photo = in.readParcelable(Photo.class.getClassLoader());
        petOwned = in.readArrayList(null);
        petAuthorizedToFed = in.readArrayList(null);
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
        dest.writeString(id);
        dest.writeString(displayedName);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeParcelable(photo, flags);
        dest.writeList(petOwned);
        dest.writeList(petAuthorizedToFed);
    }

    public String getDisplayedName() {
        return displayedName;
    }

    public String getEmail() {
        return email;
    }

    public Photo getPhoto() {
        return photo;
    }
}
