package ovh.exception.watchdogzz.data;

/**
 * Created by begarco on 19/11/2016.
 */

public class GPSPosition {
    private float latitude;
    private float longitude;
    private float altitude;

    public GPSPosition(float latitude, float longitude, float altitude) {
        this.setAltitude(altitude);
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }

    public float[] getForMap(float[] origin, float scale, float angle) {
        float[] res = { (getLatitude() -origin[0])*scale, (getLongitude() -origin[1])*scale, (getAltitude() -origin[2])*scale };

        // TODO manque la rotation
        float[] resTemp = {getLatitude(), getLongitude(), getAltitude()};

        return resTemp;
    }

    @Override
    public String toString() {
        return getLatitude() + " " + getLongitude() + " " + getAltitude();
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getAltitude() {
        return altitude;
    }

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }
}
