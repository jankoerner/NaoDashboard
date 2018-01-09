package sample;
import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.*;


public class MovementModel {
    public static void main(String[] args) {

    }

    public void move(Application app, String direction) throws Exception{
        ALMotion alMotion = new ALMotion(app.session());

        switch (direction) {
                case "forward":
                    alMotion.moveTo(0.1f, 0f, 0f);
                    alMotion.waitUntilMoveIsFinished();
                    break;
                case "backward":
                    alMotion.moveTo(-0.1f, 0f, 0f);
                    alMotion.waitUntilMoveIsFinished();
                        break;
                case "left":
                    alMotion.moveTo(0f, 0.1f, 0f);
                    alMotion.waitUntilMoveIsFinished();
                    break;
                case "right":
                    alMotion.moveTo(0f, -0.1f, 0f);
                    alMotion.waitUntilMoveIsFinished();
                    break;
        }
    }
    public void moveKeyboard(ALMotion alMotion, String direction, Float velocity)throws Exception{

        switch (direction){
            case "w":
                alMotion.move(velocity, 0f, 0f);
                break;
            case "s":
                alMotion.move(-velocity, 0f, 0f);
                break;
            case "a":
                alMotion.move(0f, velocity, 0f);
                break;
            case "d":
                alMotion.move(0f, -velocity

                        , 0f);
                break;
            case "stop":
                alMotion.stopWalk();
                break;
        }
    }
}
