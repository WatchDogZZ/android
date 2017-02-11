package ovh.exception.watchdogzz;

import android.net.Uri;

import org.junit.Before;
import org.junit.Test;

import ovh.exception.watchdogzz.data.GPSPosition;
import ovh.exception.watchdogzz.data.User;
import ovh.exception.watchdogzz.data.UserManager;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the user manager
 */
public class UserManagerTest {

    UserManager um;
    User user, user2, user3;
    String id;
    String name;
    String email;
    String idToken;
    Uri photoUri;
    Boolean me;
    GPSPosition pos;

    @Before
    public void prepare() {
        um = new UserManager();
        id = "XXX";
        name = "Bob";
        email = "bob@mail.com";
        idToken = "0def13ezr21er2";
        photoUri = null;
        me = false;
        pos = new GPSPosition(1,2,3);
        user = new User(id,name,email,idToken,
                photoUri,me,pos);
        user2 = new User("YYY","Alice",email,idToken,
                photoUri,me,pos);
        user3 = new User("ZZZ","Toto",email,idToken,
                photoUri,me,pos);
    }

    @Test
    public void default_case() throws Exception {
        assertNotNull(um);
        assertNotNull(um.getUsers());
        assertNull(um.getMe());
        assertEquals(0, um.getUsers().size());
        assertFalse(um.contains(user));
    }

    @Test
    public void add_user() throws Exception {
        um.addUser(user);
        um.addUser(user2);
        um.addUser(user3);
        assertEquals(3, um.getUsers().size());
    }

    @Test
    public void remove_user() throws Exception {
        um.addUser(user);
        um.addUser(user2);
        um.addUser(user3);
        assertEquals(3, um.getUsers().size());
        um.removeUser(user.getId());
        um.removeUser(user2.getId());
        um.removeUser(user3.getId());
        assertEquals(0, um.getUsers().size());
    }

    @Test
    public void update_user() throws Exception {
        um.addUser(user);
        um.addUser(user2);
        um.addUser(user3);

        User alou = new User("YYY","Alou",email,idToken,
                photoUri,me,pos);

        um.updateUser(alou.getId(),alou);

        assertEquals("Alou", um.getUser(alou.getId()).getName());
    }

    @Test
    public void check_me() throws Exception {
        um.setMe(user);
        assertEquals(user,um.getMe());
    }
}