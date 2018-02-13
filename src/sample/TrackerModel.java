package sample;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import com.aldebaran.qi.helper.proxies.ALTracker;

import java.util.ArrayList;

public class TrackerModel {
    public static void main(String[] args) {

    }
    private String mode = "";
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
        alTracker.setMode(mode);
    }

    public void trackRedball(Session session, ArrayList redBallinfo, ALTracker tracker)throws Exception{
        alTracker = tracker;
        tracker.registerTarget("RedBall",0.1f);
        tracker.setMode(mode);
        tracker.track("Redball");
    }

    public void stopTraker()throws Exception{
        if (alTracker == null){
            alTracker = new ALTracker(Controller.getSession());
        }
        alTracker.toggleSearch(false);
        alTracker.stopTracker();
    }


}
