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

    public User getUser(String id) {
        int index = searchById(id);
        return index==-1? null : users.get(index);
    }

    public void addUser(User user) {
        this.users.add(user);
        this.setChanged();
        this.notifyObservers(user);
    }

    public void removeUser(String id) {
        int index = searchById(id);
        this.users.remove(index);
        this.setChanged();
        this.notifyObservers(id);
    }

    public void updateUser(String id, User user) {
        int index = searchById(id);
        this.users.get(index).setPosition(user.getPosition());
        this.users.get(index).setName(user.getName());
        this.users.get(index).setIdToken(user.getIdToken());
        this.users.get(index).setEmail(user.getEmail());
        this.users.get(index).setPhotoUrl(user.getPhotoUrl());
        this.setChanged();
        this.notifyObservers(this.users.get(index));
    }

    public boolean contains(User user) {
        boolean res = false;
        for (User u : this.getUsers()) {
            res = (res||(u.getName()==user.getName()));
        }
        return res;
    }

    private int searchById(String id) {
        int index = -1;
        int i = 0;
        String j = "";
        while(i < users.size() && index==-1) {
            j = users.get(i).getId();
            index = j.equals(id)?i:-1;
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

    public void updateUserPosition(String id, User user) {
        int index = searchById(id);
        this.users.get(index).setPosition(user.getPosition());
        this.setChanged();
        this.notifyObservers(this.users.get(index));
    }
}
