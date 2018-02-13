package sample;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALVideoDevice;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VideoController {
    //private VideoCapture capture;
    // a flag to change the button behavior
    private boolean cameraActive;
    private ScheduledExecutorService timer;
    private ALVideoDevice alVideoDevice;
    private final static Integer RESOLUTION =2;   //640x480
    private final static Integer COLORSPACE = 11; //RGB
    private final static Integer FRAMERATE = 30;
    private final static String NAO_CAMERA_NAME= "Nao Image";
    private final static Integer TOPCAM = 0;
    public void startup(Session session, ImageView iv_camera){
        this.cameraActive = false;
        try {
            alVideoDevice = new ALVideoDevice(session);
            String module = alVideoDevice.subscribeCamera(NAO_CAMERA_NAME, TOPCAM,  RESOLUTION, COLORSPACE, FRAMERATE);
            getNaoFrames(alVideoDevice, module, iv_camera);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

@SuppressWarnings("unchecked")
    private void getNaoFrames(ALVideoDevice alVideoDevice, String subscribeCamera, ImageView iv_camera) {


    Runnable frameGrabber = new Runnable() {

        @Override
        public void run() {
            Mat blue = new Mat();
            List<Object> imageRemote = null;
            try {
                imageRemote = (List<Object>) alVideoDevice.getImageRemote(subscribeCamera);
            } catch (CallError callError) {
                callError.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ByteBuffer b = (ByteBuffer) imageRemote.get(6);
            Mat image = new Mat((int) imageRemote.get(1), (int) imageRemote.get(0), CvType.CV_8UC3);
            image.put(0, 0, b.array());


            // convert and show the frame
            Image imageToShow = mat2Image(image);
            updateFrame(iv_camera, imageToShow);

        }
    };
}


    private static Image mat2Image(Mat frame)
    {
        try
        {
            return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
        }
        catch (Exception e)
        {
            System.err.println("Cannot convert the Mat obejct: " + e);
            return null;
        }
    }

    private void updateFrame(ImageView iv_camera, Image image)
    {
        onFXThread(iv_camera.imageProperty(), image);
    }

    private static BufferedImage matToBufferedImage(Mat original)
    {
        BufferedImage image = null;
        int width = original.width(), height = original.height(), channels = original.channels();
        byte[] sourcePixels = new byte[width * height * channels];
        original.get(0, 0, sourcePixels);

        if (original.channels() > 1)
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        }
        else
        {
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        }
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }
    private static <T> void onFXThread(final ObjectProperty<T> property, final T value)
    {
        Platform.runLater(() -> {
            property.set(value);
        });
    }
}
