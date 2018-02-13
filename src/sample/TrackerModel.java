package sample;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import com.aldebaran.qi.helper.proxies.ALTracker;

import java.util.ArrayList;

public class TrackerModel {
    public static void main(String[] args) {

    }
    private String mode;
    private ALTracker alTracker;
    public void setMode(String mode) throws Exception {
        if (alTracker == null){
            alTracker = new ALTracker(Controller.getSession());
        }
        switch (mode){
            case"Head only":
                this.mode="Head";
                break;
            case "Whole Body":
                this.mode="WholeBody";
                break;
            case "Move":
                this.mode="Move";
                break;
        }
    }

    public void trackLandmark(Session session, ArrayList landMarkInfos, ALTracker tracker)throws Exception{
        if (!alTracker.equals(tracker)){
            alTracker = tracker;
        }
        Object[] strings = {"0.1", 68 };
        alTracker.setMode(mode);
        alTracker.registerTarget("LandMark",strings);
        alTracker.track("LandMark");
        while(alTracker.isActive()){
            if (alTracker.isTargetLost());
            alTracker.stopTracker();
        }
    }

    public void searchLandmark(Session session)throws Exception{
        if (alTracker == null){
            alTracker = new ALTracker(session);
        }
        alTracker.toggleSearch(true);
        while(alTracker.isSearchEnabled()){
            if (alTracker.isNewTargetDetected()){
                alTracker.toggleSearch(false);
            }
        }

    }

    public void stopTraker()throws Exception{
        if (alTracker == null){
            alTracker = new ALTracker(Controller.getSession());
        }
        alTracker.toggleSearch(false);
        alTracker.stopTracker();
    }


}
