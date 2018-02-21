package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALVideoDevice;

import javafx.scene.image.ImageView;

/**
 * a class to get the camera of the nao
 */
public class VideoController
{

    @FXML ImageView iv;
    // a timer for getting the video stream
    private ScheduledExecutorService timer;
    // a flag to change the button behavior
    private boolean cameraActive;
    // the logo to be loaded
    private final String NAO_CAMERA_NAME = "Nao Image";
    private LogModel log = new LogModel();

    /**
     * initializes this class to use items at the FXML
     */
    public void initialize(Session session, ImageView iv)
    {
        this.cameraActive = false;
        this.iv = iv;
        try{
            initNAO(session, iv);
        }
        catch(Exception e) {
            System.out.println(e.getStackTrace());
        }

    }

    /**
     * initialise NAO camera
     * @throws Exception
     */
    protected void initNAO(Session session, ImageView iv) throws Exception {

        int topCamera = 0;
        int resolution = 1;
        int colorspace = 13; // BGR
        int frameRate = 30; // FPS
        ALVideoDevice video;
        String moduleName;

        try {
            video = new ALVideoDevice(session);
            moduleName = video.subscribeCamera(NAO_CAMERA_NAME, topCamera, resolution, colorspace, frameRate);
            getNaoFrames(video, moduleName);
        } catch(Exception e) {
            // if a Nao has received 8 subscriptions to his camera he returns void
            log.write("Too many subscriptions. Cant get an image. WARN");
        }
    }

    /**
     * gets Nao camera frames
     * @param alVideoDevice
     * @param subscribeCamera
     * @throws InterruptedException
     * @throws CallError
     */

    private void getNaoFrames(ALVideoDevice alVideoDevice, String subscribeCamera) throws InterruptedException, CallError {
        Runnable frameGrabber = new Runnable() {
            @Override
            public void run() {
                List<Object> imageRemote = null;
                try {
                    imageRemote = (List<Object>) alVideoDevice.getImageRemote(subscribeCamera);
                } catch (CallError callError) {
                    callError.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try{
                    BufferedImage img;
                    ByteBuffer b = (ByteBuffer) imageRemote.get(6); // index 6 of the list contains the infos needed to build an image
                    img = new BufferedImage(320, 240, BufferedImage.TYPE_3BYTE_BGR); // creates a buffered img
                    img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(b.array(), b.array().length), new Point() ) ); // fills the buffered img with data
                    Image image = SwingFXUtils.toFXImage(img, null); // converts buffered img to fx img and doesnt copy it (null)
                    updateImageView(iv, image); //updates the img view in a platform run later
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
    }

    /**
     * calls a platform run later which changes the image property to the current camera image
     * @param view
     * @param image
     */

    private void updateImageView(ImageView view, Image image)
    {
        Utils.onFXThread(view.imageProperty(), image);
    }
}

