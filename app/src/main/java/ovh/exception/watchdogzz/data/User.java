package ovh.exception.watchdogzz.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by begarco on 19/11/2016.
 */

public class User implements Parcelable {
    private String id;

    private String name;
    private String email;
    private String idToken;
    private Uri photoUrl;
    private boolean isMe;

    private float[] location;

    private GPSPosition position;

    public void constructAfterSerialized() {
        if(location.length >= 3)
            position = new GPSPosition(location[0],location[1],location[2]);
    }

    public User(String id, String name, String email, String idToken, Uri photoUrl, boolean me, GPSPosition pos) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
        this.setIdToken(idToken);
        this.setPhotoUrl(photoUrl);
        this.setMe(me);
        this.setPosition(pos);
    }

    public User(Parcel p) {
        this.setId(p.readString());
        this.setName(p.readString());
        this.setEmail(p.readString());
        this.setIdToken(p.readString());
        this.setPhotoUrl(Uri.parse(p.readString()));
        this.setMe(Boolean.valueOf(p.readString()));
        this.setPosition(new GPSPosition(p.readFloat(),p.readFloat(),p.readFloat()));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public GPSPosition getPosition() {
        return position;
    }

    public void setPosition(GPSPosition position) {
        this.position = position;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getId());
        parcel.writeString(this.getName());
        parcel.writeString(this.getEmail());
        parcel.writeString(this.getIdToken());
        parcel.writeString(this.getPhotoUrl().toString());
        parcel.writeString(String.valueOf(this.isMe()));
        parcel.writeFloat(this.getPosition().getLatitude());
        parcel.writeFloat(this.getPosition().getLongitude());
        parcel.writeFloat(this.getPosition().getAltitude());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>()
    {
        @Override
        public User createFromParcel(Parcel source)
        {
            return new User(source);
        }

        @Override
        public User[] newArray(int size)
        {
            return new User[size];
        }
    };
}
