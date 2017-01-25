package ovh.exception.watchdogzz.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by begarco on 25/01/2017.
 */

public class JUser implements Serializable {


    @SerializedName("name")
    public String name;

    @SerializedName("location")
    public Float[] location;
}
