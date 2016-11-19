package ovh.exception.watchdogzz.data;

/**
 * Created by begarco on 19/11/2016.
 */

public class User {
    private int id;
    private String name;
    private String email;
    private boolean isMe;
    private GPSPosition position;


    public User(int id, String name, String email, boolean me, GPSPosition pos) {
        this.setId(id);
        this.setName(name);
        this.setEmail(email);
        this.setMe(me);
        this.setPosition(pos);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
}
