package ovh.exception.watchdogzz.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Serializable user for web service
 */

public class JUser implements Serializable {

    @SerializedName("name")
    public String name;

    @SerializedName("location")
    public Float[] location;
}
