package fr.jbrenier.petfoodingcontrol.domain.model;

import androidx.room.ColumnInfo;

/**
 * A Pet Fooding Control feeder
 * @author Jérôme Brenier
 */
public class Feeder {
    @ColumnInfo(name = "user_Id")
    private Long userId;
    private String displayedName;
    private String email;

    public Feeder(Long userId, String displayedName, String email) {
        this.userId = userId;
        this.displayedName = displayedName;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public String getDisplayedName() {
        return displayedName;
    }

    public String getEmail() {
        return email;
    }
}
