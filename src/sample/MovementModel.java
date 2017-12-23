package sample;
import com.aldebaran.qi.Application;
import com.aldebaran.qi.helper.proxies.*;

public class MovementModel {
    public static void main(String[] args) {

    }

    public void move(Application app, String direction) throws Exception{
        switch (direction){
            case "forward":
                ALMotion alMotion = new ALMotion(app.session());
                alMotion.move(1f,0f,0f);
                System.out.println("test");
                break;
           
        }

    }
}
