package ovh.exception.watchdogzz.network;

import org.json.JSONObject;

public interface IWSConsumer {
    void consume(JSONObject json);
}
