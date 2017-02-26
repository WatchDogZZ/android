package ovh.exception.watchdogzz;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import ovh.exception.watchdogzz.data.GPSPosition;
import ovh.exception.watchdogzz.data.User;
import ovh.exception.watchdogzz.network.IWSConsumer;
import ovh.exception.watchdogzz.network.WebServiceTask;
import static org.junit.Assert.*;

/**
 * Tests for web service
 */
@RunWith(AndroidJUnit4.class)
public class WebServiceTaskTest {
    private User user;
    private String name, email, photo, token;
    private Float[] pos = {1f,2f,3f};
    private Context context;
    private CountDownLatch signal = new CountDownLatch(1);

    @Before
    public void prepare() {
        name = "Bob";
        email = "Bob";
        photo = "http://www.superaktif.net/wp-content/upLoads/2011/07/Han.Solo_.jpg";
        token = "Bob";
        user = new User(name,name,email,token,photo,false,new GPSPosition(pos[0],pos[1],pos[2]));
        context = InstrumentationRegistry.getTargetContext();
    }

    private class TestWSConsumer implements IWSConsumer {
        public String result = "";
        CountDownLatch sig = null;
        @Override
        public void consume(JSONObject json) {
            result = null==json?null:json.toString();
            sig.countDown();
        }
    }

    @Test
    public void test_status_ok() throws Exception {
        TestWSConsumer consumer = new TestWSConsumer();
        consumer.sig = signal;
        WebServiceTask ws = new WebServiceTask(context,consumer);
        ws.post("https://watchdogzz.ddns.net/login",user);
        signal.await();
        assertEquals("{\"status\":\"ok\"}",consumer.result);
    }
}