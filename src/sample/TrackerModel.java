package sample;

import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALTracker;

import java.util.ArrayList;

public class TrackerModel {
    public static void main(String[] args) {

    }
    private String mode = "Head";
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

    public void trackRedball(Session session, ArrayList redBallinfo)throws Exception{
        alTracker = new ALTracker(session);
        alTracker.registerTarget("RedBall",0.05f);
        alTracker.setMode(mode);
        alTracker.track("RedBall");
        while (alTracker.isActive()){
            if (alTracker.isTargetLost()){
                alTracker.stopTracker();
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
