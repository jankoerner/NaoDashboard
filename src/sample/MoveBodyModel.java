package sample;

import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALMotion;

/**
 * this class is responsible for moving naos body and head
 */
public class MoveBodyModel {
    public static void main(String[] args) {

    }
    private Controller controller;
    private ALMotion alMotion;

    /**
     * walks responding to keys w,a,s,d with adjustable velocity and angle
     * @param session
     * @param direction
     * @param velocity
     * @param angle
     * @throws Exception
     */
    public void moveKeyboard(Session session, String direction, Float velocity, Float angle)throws Exception {
        alMotion = new ALMotion(session);
            switch (direction) {
            case "w":
                alMotion.move(velocity, 0f, angle);
                break;
            case "s":
                alMotion.move(-velocity, 0f, angle);
                break;
            case "a":
                alMotion.move(0f, velocity,angle);
                break;
            case "d":
                alMotion.move(0f, -velocity, angle);
                break;
            case "stop":
                alMotion.stopMove();
                break;
        }
    }

    /**
     * moves naos head responding to keys i,j,k,l,m
     * @param session
     * @param direction
     * @throws Exception
     */
    public void moveKeyboard(Session session, String direction)throws Exception{
        alMotion = new ALMotion(session);
        switch (direction) {
            case "i":
               alMotion.changeAngles("HeadPitch", -0.015, 1f);
                break;
            case "k":
                alMotion.changeAngles("HeadPitch", 0.015, 1f);
                break;
            case "l":
                alMotion.changeAngles("HeadYaw", -0.015, 1f);
                break;
            case "j":
                alMotion.changeAngles("HeadYaw", 0.015, 1f);
                break;
            case "m":
                alMotion.setAngles("HeadYaw", 0f, 1f);
                alMotion.setAngles("HeadPitch", 0f, 1f);
                break;
        }
    }

    /**
     * moves head responding to buttons @FXML
     * @param session
     * @param direction
     * @throws Exception
     */

    public void moveHeadButtons(Session session, String direction) throws  Exception{
        alMotion = new ALMotion(session);

        switch (direction) {
            case "i":
                alMotion.changeAngles("HeadPitch", -0.75, 1f);
                break;
            case "k":
                alMotion.changeAngles("HeadPitch", 0.75, 1f);
                break;
            case "l":
                alMotion.changeAngles("HeadYaw", -0.75, 1f);
                break;
            case "j":
                alMotion.changeAngles("HeadYaw", 0.75, 1f);
                break;
            case "m":
                alMotion.setAngles("HeadYaw", 0f, 1f);
                alMotion.setAngles("HeadPitch", 0f, 1f);
                break;
        }

    }

    /**
     * turns nao by an entered value of degrees
     * @param session
     * @param degree
     * @throws Exception
     */

    public void turn(Session session, float degree) throws Exception{
        alMotion = new ALMotion(session);
        alMotion.moveTo(0f,0f,degree);
        alMotion.waitUntilMoveIsFinished();
    }

    /**
     * makes nao either stand up or relax depending on togglebuttons
     * @param session
     * @param mode
     * @throws Exception
     */

    public void mode(Session session, String mode) throws Exception{
            alMotion = new ALMotion(session);
            switch (mode){
            case "Relax":
                alMotion.rest();
                break;
            case "Stand":
                alMotion.wakeUp();
                break;
        }
    }

    /**
     * returns true if stands, else false
     * used to set togglebuttons at connecting
     * @param session
     * @return
     * @throws Exception
     */

    public boolean getMode(Session session) throws Exception{
        alMotion = new ALMotion(session);
        return alMotion.robotIsWakeUp();
    }

    /**
     * front sensor touched action
     * @param session
     * @throws Exception
     */
    public void frontTouched(Session session)throws Exception{
        alMotion = new ALMotion(session);
        dab();
    }

    /**
     * dabs
     * @throws Exception
     */
        private void dab()throws Exception{
            alMotion.setAngles("HeadPitch", 2, 1f);
            alMotion.setAngles("RShoulderPitch", 0, 1f);
            alMotion.setAngles("LShoulderPitch", -0.25, 1f);
            alMotion.setAngles("LShoulderRoll", 1.3, 1f);
            alMotion.setAngles("RElbowRoll", 1.5, 1f);
            alMotion.setAngles("RElbowYaw", 0, 1f);
        }

}
