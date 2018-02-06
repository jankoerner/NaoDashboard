package sample;

import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import com.aldebaran.qi.helper.proxies.ALTracker;

import java.util.ArrayList;

public class TrackerModel {
    public static void main(String[] args) {

    }
    private ALRobotPosture posture;
    public void trackLandmark(Session session, ArrayList landMarkInfos, ALTracker tracker)throws Exception{
        posture = new ALRobotPosture(session);
        posture.goToPosture("Stand",1f);
        tracker = new ALTracker(session);
        tracker.registerTarget("LandMark","85");
        tracker.setMode("Head");
        tracker.track("LandMark");
        if (tracker.isTargetLost()){
            tracker.stopTracker();
        }
    }
}
