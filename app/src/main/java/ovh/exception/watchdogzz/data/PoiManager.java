package ovh.exception.watchdogzz.data;

import android.util.Log;

/**
 * Manage points of interest
 */
public class PoiManager extends UserManager {

    private boolean isCreatingNewPoi = false;
    private User mNewUser = null;

    public void createNewPoi(String name) {
        mNewUser = new User(name,name,"","","https://i.stack.imgur.com/6cDGi.png",false,null);
        this.isCreatingNewPoi = true;
    }

    public void finalizeNewPoi(GPSPosition position) {
        if(this.isCreatingNewPoi) {
            this.mNewUser.setPosition(position);
            Log.i("POIS", position.toString());
            this.addUser(this.mNewUser);
            this.isCreatingNewPoi = false;
        }
    }

}
