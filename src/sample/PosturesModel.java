package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import javafx.collections.ObservableList;

import java.util.List;

public class PosturesModel {
    public static void main(String[] args) {

    }
    private ALRobotPosture robotPosture;
    public void makePosture(Application app, String posture) throws Exception{
        if (robotPosture == null){
            robotPosture = new ALRobotPosture(app.session());
        }

        System.out.println(robotPosture.getPostureList());
        switch (posture){
            case"Crouch":
                robotPosture.goToPosture("Crouch", 2f);
                break;
            case"LyingBack":
                robotPosture.goToPosture("LyingBack", 2f);
                break;
            case"LyingBelly":
                robotPosture.goToPosture("LyingBelly", 2f);
                break;
            case"Sit":
                robotPosture.goToPosture("Sit", 2f);
                break;
            case"SitOnChair":
                robotPosture.goToPosture("SitOnChair", 2f);
                break;
            case"SitRelax":
                robotPosture.goToPosture("SitRelax", 2f);
                break;
            case"Stand":
                robotPosture.goToPosture("Stand", 2f);
                break;
            case"StandInit":
                robotPosture.goToPosture("StandInit", 2f);
                break;
            case"StandZero":
                robotPosture.goToPosture("StandZero", 2f);
                break;
        }
    }

    public List getPostures(Application app)throws Exception{
        if (robotPosture == null){
            robotPosture = new ALRobotPosture(app.session());
        }

        return robotPosture.getPostureList();
    }

}
