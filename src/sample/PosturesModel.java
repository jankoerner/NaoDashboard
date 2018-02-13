package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class PosturesModel {
    public static void main(String[] args) {

    }
    private ALRobotPosture robotPosture;
    private Image image;
    public void makePosture(Session session, String posture) throws Exception{
        robotPosture = new ALRobotPosture(session);

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

    public List getPostures(Session session)throws Exception{
        robotPosture = new ALRobotPosture(session);
        return robotPosture.getPostureList();

    }

    public void changeImage(String posture, ImageView imageView) {
        try {
            System.out.println(posture);
            switch (posture) {
                case "Crouch":
                    image = new Image("/Nao/Crouch.jpg", true);
                    imageView.imageProperty().set(image);
                    break;
                case "LyingBack":
                    image = new Image("/Nao/LyingBack.jpg", true);
                    imageView.imageProperty().set(image);
                    break;
                case "LyingBelly":
                    image = new Image("/Nao/LyingBelly.jpg", true);
                    imageView.imageProperty().set(image);
                    break;
                case "Sit":
                    image = new Image("/Nao/Sit.jpg", true);
                    imageView.imageProperty().set(image);
                    break;
                case "SitOnChair":
                    image = new Image("/Nao/SitOnChair.jpg", true);
                    imageView.imageProperty().set(image);
                    break;
                case "SitRelax":
                    image = new Image("/Nao/SitRelaxed.jpg", true);
                    imageView.imageProperty().set(image);
                    break;
                case "Stand":
                    image = new Image("/Nao/Stand.jpg", true);
                    imageView.imageProperty().set(image);
                    break;
                case "StandInit":
                    image = new Image("/Nao/StandInit.jpg", true);
                    imageView.imageProperty().set(image);
                    break;
                case "StandZero":
                    image = new Image("/Nao/StandZero.jpg", true);
                    imageView.imageProperty().set(image);
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
