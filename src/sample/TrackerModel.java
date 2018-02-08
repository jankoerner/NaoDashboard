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
        System.out.println(alTracker.getMode());
    }

    public void trackLandmark(Session session, ArrayList landMarkInfos, ALTracker tracker)throws Exception{
        alTracker = tracker;
        alTracker.registerTarget("LandMark",landMarkInfos);
        alTracker.setMode(mode);
        alTracker.track("LandMark");
    }

    public void searchLandmark(Session session)throws Exception{
        if (alTracker == null){
            alTracker = new ALTracker(session);
        }
        alTracker.toggleSearch(true);
        if (alTracker.isNewTargetDetected()){
            alTracker.toggleSearch(false);
        }
    }

    public void stopTraker()throws Exception{
        if (alTracker == null){
            alTracker = new ALTracker(Controller.getSession());
        }
        alTracker.stopTracker();
    }


}
