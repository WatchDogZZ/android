package ovh.exception.watchdogzz.data;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by begarco on 19/11/2016.
 */

public class UserManager extends Observable {
    private ArrayList<User> users;
    private User me;

    public UserManager() {
        me = null;
        users = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public User getUser(int index) {
        return users.get(index);
    }

    public void addUser(User user) {
        this.users.add(user);
        this.setChanged();
        this.notifyObservers(user);
    }

    public void removeUser(int id) {
        int index = searchById(id);
        this.users.remove(index);
        this.setChanged();
        this.notifyObservers(new Integer(id));
    }

    public void updateUser(int id, User user) {
        int index = searchById(id);
        this.users.get(index).setPosition(user.getPosition());
        this.setChanged();
        this.notifyObservers(this.users.get(index));
    }

    private int searchById(int id) {
        int index = -1;
        int i = 0, j = 0;
        while(i < users.size() && index==-1) {
            j = users.get(i).getId();
            index = j==id?j:-1;
            i++;
        }
        return index;
    }

    public User getMe() {
        return me;
    }

    public void setMe(User me) {
        this.me = me;
        this.addUser(me);
    }
}
