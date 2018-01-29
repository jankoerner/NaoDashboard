package sample;

import com.aldebaran.qi.Application;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALPhotoCapture;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class CameraModel {
    private Image image;
    private ALPhotoCapture alPhotoCapture;
    private static int pictureNumber = 0;

    public static void main(String[] args) {

    }
    public void takePhoto(ImageView photoView, Session session)throws Exception{
        if (alPhotoCapture == null){
            alPhotoCapture = new ALPhotoCapture(session);
        }
        System.out.println("test");
        System.out.println(alPhotoCapture.getCameraID());
        alPhotoCapture.setResolution(2);
        alPhotoCapture.setPictureFormat("jpeg");
        alPhotoCapture.takePicture("./","file:xyz",true);
        //showPhoto(photoView);
        //pictureNumber++;
    }
    public void showPhoto(ImageView photoview){
        //image = new Image("Nao/"+String.valueOf(pictureNumber));
        //photoview.imageProperty().set(image);
    }

}
