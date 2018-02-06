package sample;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALVideoDevice;

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
    private final static Integer resolution =2;   //640x480
    private final static Integer colorspace = 11; //RGB
    private final static String NAO_CAMERA_NAME= "Nao Image";

    public void startup(Session session){
        //this.capture = new VideoCapture();
        this.cameraActive = false;
        try {
            alVideoDevice = new ALVideoDevice(session);
            String module = alVideoDevice.subscribe(NAO_CAMERA_NAME, resolution, colorspace, 30);
            getNaoFrames(alVideoDevice, module);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getNaoFrames(ALVideoDevice alVideoDevice, String subscribeCamera){
        Runnable frameGrabber = new Runnable() {
            @Override
            public void run() {
                List<Object> imageRemote = null;
                try {
                    imageRemote=(List<Object>) alVideoDevice.getImageRemote(subscribeCamera);
                } catch (Exception e){
                    e.printStackTrace();
                }
                ByteBuffer b = (ByteBuffer) imageRemote.get(6);
            }
        };
        this.timer = Executors.newSingleThreadScheduledExecutor();
        this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
    }
}
