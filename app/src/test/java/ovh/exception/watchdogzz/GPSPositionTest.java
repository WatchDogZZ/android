package ovh.exception.watchdogzz;

import org.junit.Before;
import org.junit.Test;

import ovh.exception.watchdogzz.data.GPSPosition;

import static org.junit.Assert.assertEquals;

/**
 * Tests for gps class
 */
public class GPSPositionTest {

    private GPSPosition gps;
    private final int x=1, y=2, z=3;

    @Before
    public void prepare() {
        gps = new GPSPosition(0,0,0);
        gps.setLatitude(x);
        gps.setLongitude(y);
        gps.setAltitude(z);
    }

    @Test
    public void check_formats_are_equals() throws Exception {
        float [] origin = {0,0,0};
        float [] coor = gps.getForMap(origin,1);
        assertEquals(gps.getLatitude(), coor[0], 0.00000001);
        assertEquals(gps.getLongitude(), coor[1], 0.00000001);
        assertEquals(gps.getAltitude(), coor[2], 0.00000001);
    }

    @Test
    public void check_transformations_are_correct() throws Exception {
        float [] origin = {2,4,8};
        float [] coor = gps.getForMap(origin,3);
        assertEquals(3*(gps.getLatitude()-origin[1]), coor[0], 0.00000001);
        assertEquals(3*(gps.getLongitude()-origin[0]), coor[1], 0.00000001);
        assertEquals(3*(gps.getAltitude()-origin[2]), coor[2], 0.00000001);
    }
}