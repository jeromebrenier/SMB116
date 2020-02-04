package fr.jbrenier.petfoodingcontrol.entities.user;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.OffsetDateTime;

import fr.jbrenier.petfoodingcontrol.db.converters.DataTypeConverter;

/**
 * Autologins storage.
 */
@Entity(foreignKeys =
        @ForeignKey(entity = User.class,
                parentColumns = "user_Id",
                childColumns = "user_Id",
                onDelete = ForeignKey.CASCADE),
        indices = @Index(value = {"user_Id"})
)
public class AutoLogin implements Parcelable {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "token_Id")
    private String tokenId;
    @NonNull
    @TypeConverters(DataTypeConverter.class)
    @ColumnInfo(name = "expiration_date")
    private OffsetDateTime expirationDate;
    @NonNull
    @ColumnInfo(name = "user_Id")
    private Long userId;

    public AutoLogin(@NonNull String tokenId, @NonNull OffsetDateTime expirationDate,
                     @NonNull Long userId) {
        this.tokenId = tokenId;
        this.expirationDate = expirationDate;
        this.userId = userId;
    }

    protected AutoLogin(Parcel in) {
        tokenId = in.readString();
        expirationDate = (java.time.OffsetDateTime) in.readSerializable();
        userId = in.readLong();
    }

    public static final Creator<AutoLogin> CREATOR = new Creator<AutoLogin>() {
        @Override
        public AutoLogin createFromParcel(Parcel in) {
            return new AutoLogin(in);
        }

        @Override
        public AutoLogin[] newArray(int size) {
            return new AutoLogin[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tokenId);
        dest.writeSerializable(expirationDate);
        dest.writeLong(userId);
    }

    @NonNull
    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(@NonNull String tokenId) {
        this.tokenId = tokenId;
    }

    @NonNull
    public OffsetDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(@NonNull OffsetDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    @NonNull
    public Long getUserId() {
        return userId;
    }

    public void setUserId(@NonNull Long userId) {
        this.userId = userId;
    }
}
