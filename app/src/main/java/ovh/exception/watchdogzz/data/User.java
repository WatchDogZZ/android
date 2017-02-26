package ovh.exception.watchdogzz.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * User of the application
 */
public class User implements Parcelable {
    private String id;

    private String name;
    private String email;
    private String idToken;
    private String photoUrl;
    private boolean isMe;

    private GPSPosition position;

    public User(String id, String name, String email, String idToken, String photoUrl, boolean me, GPSPosition pos) {
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
        this.setPhotoUrl(p.readString());
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
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
        parcel.writeString(this.getPhotoUrl());
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
