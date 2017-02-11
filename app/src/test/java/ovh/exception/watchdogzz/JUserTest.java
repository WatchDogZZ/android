package ovh.exception.watchdogzz;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import ovh.exception.watchdogzz.data.JUser;

/**
 * Tests for json user class
 */
public class JUserTest {
    JUser user;
    String name;
    Float[] pos = {1f,2f,3f};

    @Before
    public void prepare() {
        name = "Bob";
        user = new JUser();
        user.name = name;
        user.location = pos;
    }

    @Test
    public void user_isCorrect() throws Exception {
        assertEquals(pos[0],user.location[0],0.00000001f);
        assertEquals(pos[1],user.location[1],0.00000001f);
        assertEquals(pos[2],user.location[2],0.00000001f);
        assertEquals(name,user.name);
    }
}