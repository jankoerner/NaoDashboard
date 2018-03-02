package sample;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.EventCallback;
import com.aldebaran.qi.helper.proxies.*;

import java.util.ArrayList;

public class TrackerModel {

    private ALRedBallDetection redBallDetection;
    private ALFaceDetection alFaceDetection;
    private ALMemory memory;
    private boolean tracked = false;
    private String mode;
    private String target;
    private ALTracker alTracker;
    private long redBallid;
    private long faceid;
    public static void main(String[] args) {

    }
    public void setMode(String mode) throws Exception {
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
        if (alTracker != null && alTracker.isActive()){
            stopTraker();
            startTracking(Controller.getSession(),this.target,this.mode);
        }
    }


    public void startTracking(Session session, String target, String mode)throws Exception{
        memory = new ALMemory(session);
        Controller.log.write("You have to end the tracker by yourself if you selected \"Move\" as tracking mode. WARN");
        Controller.log.write("Press \"Stop Tracker\" to stop tracking. INFO");
        System.out.println(memory.getEventList());
        setMode(mode);
        this.target = target;
        switch (this.target){
            case "Face":
                alFaceDetection = new ALFaceDetection(session);
                alFaceDetection.subscribe("FaceDetected");
                faceid=memory.subscribeToEvent("FaceDetected", new EventCallback<ArrayList>() {
                    @Override
                    public void onEvent(ArrayList info ) throws InterruptedException, CallError {
                        try {
                            while (tracked==false){
                                tracked=true;
                                track();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                break;
            case "RedBall":
                if (redBallDetection == null){
                    redBallDetection = new ALRedBallDetection(session);
                }
                redBallDetection.subscribe("redBallDetected");

                redBallid = memory.subscribeToEvent("redBallDetected", new EventCallback<ArrayList>() {
                @Override
                public void onEvent(ArrayList info) throws InterruptedException, CallError {
                    try {
                        while (tracked==false){
                            tracked=true;
                            track();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
                break;
        }
    }

    public void track()throws Exception{
        if (alTracker==null){
            alTracker = new ALTracker(Controller.getSession());
        }
        alTracker.setMode(mode);
        alTracker.setMaximumDistanceDetection(2f);
        alTracker.registerTarget(target, 0.1);
        alTracker.track(target);
        if (mode.equals("Move")){
            alTracker.setEffector("None");
        }else{
            alTracker.setEffector("Arms");
        }

    }

    public void stopTraker()throws Exception{
        if (alTracker == null){
            alTracker = new ALTracker(Controller.getSession());
        }
        if (target.equals("Face")){
            memory.unsubscribeToEvent(faceid);
            alFaceDetection.unsubscribe("FaceDetected");
        }else if(redBallDetection!=null){
            memory.unsubscribeToEvent(redBallid);
            redBallDetection.unsubscribe("redBallDetected");
        }
        alTracker.stopTracker();
        tracked = false;
            PosturesModel posturesModel = new PosturesModel();
            posturesModel.makePosture(Controller.getSession(),"Stand");

    }


}
