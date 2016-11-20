package ovh.exception.watchdogzz.data;

/**
 * Created by begarco on 19/11/2016.
 */

public class GPSPosition {
    private float latitude;
    private float longitude;
    private float altitude;

    public GPSPosition(float latitude, float longitude, float altitude) {
        this.altitude = altitude;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float[] getForMap(float[] origin, float scale, float angle) {
        float[] res = { (latitude-origin[0])*scale, (longitude-origin[1])*scale, (altitude-origin[2])*scale };

        // TODO manque la rotation
        float[] resTemp = { latitude, longitude, altitude };

        return resTemp;
    }

    @Override
    public String toString() {
        return latitude + " " + longitude + " " + altitude;
    }
}
