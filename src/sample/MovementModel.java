package sample;
import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.*;

public class MovementModel {
    public static void main(String[] args) {

    }

    public void move(Application app, String direction) throws Exception{
        ALMotion alMotion = new ALMotion(app.session());
        switch (direction){
            case "forward":
                alMotion.moveTo(0.1f, 0f, 0f);
                break;
            case "backward":
                alMotion.moveTo(-0.1f,0f,0f);
                break;
            case "left":
                alMotion.moveTo(0f,0.1f,0f);
                break;
            case "right":
                alMotion.moveTo(0f,-0.1f,0f);
                break;
        }
    }
}
