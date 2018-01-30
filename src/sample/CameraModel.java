package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALPhotoCapture;
import com.aldebaran.qi.helper.proxies.ALVideoDevice;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class CameraModel {
    private Image image;
    private ALVideoDevice alVideoDevice;
    private Controller controller;
    private ALPhotoCapture alPhotoCapture;
    private static int pictureNumber = 0;

    public static void main(String[] args) {

    }

    public ALVideoDevice getAlVideoDevice() {
        if(alVideoDevice==null) {
            try {
                alVideoDevice=new ALVideoDevice(controller.getSession());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void takePhoto(ImageView photoView, Session session)throws Exception{
        if (alPhotoCapture == null){
            alPhotoCapture = new ALPhotoCapture(session);
        }
        System.out.println("test");
        System.out.println(alPhotoCapture.getCameraID());
        alPhotoCapture.setResolution(2);
        alPhotoCapture.setPictureFormat("jpeg");
        File picture = (File)alPhotoCapture.takePicture("/home/student/IdeaProjects/NaoDashboardRichtig/out/production/NaoDashboardRichtig/sample","xyz",true);
        picture.mkdir();
        //showPhoto(photoView);
        //pictureNumber++;
    }
    public void showPhoto(ImageView photoview){
        //image = new Image("Nao/"+String.valueOf(pictureNumber));
        //photoview.imageProperty().set(image);
    }

}
