package ovh.exception.watchdogzz;

import android.net.Uri;

import org.junit.Before;
import org.junit.Test;

import ovh.exception.watchdogzz.data.GPSPosition;
import ovh.exception.watchdogzz.data.User;

import static org.junit.Assert.assertEquals;

/**
 * Tests for user class
 */
public class UserTest {

    User user;
    String id;
    String name;
    String email;
    String idToken;
    String photoUri;
    Boolean me;
    GPSPosition pos;

    @Before
    public void prepare() {
        id = "XXX";
        name = "Bob";
        email = "bob@mail.com";
        idToken = "0def13ezr21er2";
        photoUri = null;
        me = false;
        pos = new GPSPosition(1,2,3);
        user = new User(id,name,email,idToken,
                        photoUri,me,pos);
    }

    @Test
    public void user_isCorrect() throws Exception {
        assertEquals(id,user.getId());
        assertEquals(name,user.getName());
        assertEquals(email,user.getEmail());
        assertEquals(idToken,user.getIdToken());
        assertEquals(photoUri,null);
        assertEquals(me,user.isMe());
        assertEquals(pos.getAltitude(),user.getPosition().getAltitude(),0.00000001);
        assertEquals(pos.getLatitude(),user.getPosition().getLatitude(),0.00000001);
        assertEquals(pos.getLongitude(),user.getPosition().getLongitude(),0.00000001);
    }
}