package com.helix.appl.simulation;

import com.helix.appl.simulation.armcontrollers.ArmController;
import com.helix.appl.simulation.armcontrollers.SampleArmController;

import java.awt.Canvas;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

public class Simulation extends Canvas implements Runnable, KeyListener, MouseListener, MouseMotionListener {

    JFrame myFrame;
    int myPos;
    Arm myArm;
    List<Point2D> myTrail;
    boolean myCtrlPressed = false;
    boolean myOptionPressed = false;
    boolean myCommandPressed = false;
    boolean myMouseDown = false;
    int myJoystickDiameter = 100;
    int myJoystickCenterX = (int) (1000.0 - myJoystickDiameter * 0.5);
    int myJoystickCenterY = (int) (1025.0 - myJoystickDiameter * 1.5);
    double myJoystickX;
    double myJoystickY;
    ArmController myArmController = new SampleArmController();

    public Simulation() {
        myPos = 100;
        System.out.print("Hello, World.\n");
        myFrame = new JFrame("Testing graphics.");
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setSize(1000, 1025);
        myFrame.setLocationRelativeTo(null);
        myFrame.add(this);
        myFrame.setVisible(true);
        myArm = new Arm();
        myArm.getServo(0).setMaxAngularSpeed(0.2);
        myArm.getServo(1).setMaxAngularSpeed(0.2);
        myArm.getServo(2).setMaxAngularSpeed(0.1);
        myTrail = new ArrayList<Point2D>();
        // myFrame.addKeyListener(this);
        addKeyListener(this);
        // myFrame.addMouseListener(this);
        // myFrame.addMouseMotionListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        myFrame.setFocusable(true);
        run();
    }


    public void clearTrail() {
        myTrail = new ArrayList<Point2D>();
    }

    private Point2D convertToScreenCoordinate(Point2D point) {
        Rectangle bounds = myFrame.getBounds();

        double maxArmLength = myArm.getArmLength(0) + myArm.getArmLength(1) + myArm.getArmLength(2);
        double factor = (Math.min(bounds.width, bounds.height) * 0.98) / (2.0 * maxArmLength);
        Point2D result = new Point((int) (Math.round(point.getX() * factor + bounds.width / 2.0)),
                                   (int) ((Math.round(bounds.height / 2.0 - point.getY() * factor))));
        // System.out.println(result);
        return result;
    }

    public void paint(Graphics g){
        List<Point2D> armPoints = myArm.getArmPoints();

        // Joystick circle
        Rectangle bounds = myFrame.getBounds();
        g.drawOval(bounds.width - myJoystickDiameter, bounds.height - 2 * myJoystickDiameter, myJoystickDiameter, myJoystickDiameter);

        // Trail of previous arms endpoints
        int diameter = 6;
        for (Point2D point : myTrail) {
            g.drawOval((int) (Math.round(point.getX() - ((double) diameter / 2.0))),
                       (int) (Math.round(point.getY() - ((double) diameter / 2.0))),
                       diameter,
                       diameter);
        }

        // Arm
        for (int i = 0; i < armPoints.size() - 1; i++) {
            // System.out.format("%f %f\n", armPoints.get(i+1).getX(), armPoints.get(i+1).getY());
            Point2D point0 = convertToScreenCoordinate(armPoints.get(i));
            Point2D point1 = convertToScreenCoordinate(armPoints.get(i+1));
            g.drawLine((int) point0.getX(), (int) point0.getY(), (int) point1.getX(), (int) point1.getY());
            g.drawOval((int) (Math.round(point0.getX() - ((double) diameter / 2.0))),
                       (int) (Math.round(point0.getY() - ((double) diameter / 2.0))),
                       diameter,
                       diameter);
            g.drawOval((int) (Math.round(point1.getX() - ((double) diameter / 2.0))),
                       (int) (Math.round(point1.getY() - ((double) diameter / 2.0))),
                       diameter,
                       diameter);
            // Save this point to the list of arm endpoints
            myTrail.add(point1);
        }

        g.drawLine(myPos, myPos, myPos + 100, myPos + 100);
    }


    private void adjustArm(double timeInSeconds) {
        // Take snapshot of joystick settings
        double joystickX = myJoystickX;
        double joystickY = myJoystickY;
        myArmController.setArmForXY(myArm, joystickX, joystickY, timeInSeconds);
    }

    private void update(double timeInSeconds) {
        if (myJoystickX != 0.0 || myJoystickY != 0.0) {
            adjustArm(timeInSeconds);
        }
        myArm.update(timeInSeconds);
    }

    public void run() {

        int offset = 0;
        int incr = 1;
        for (int i = 0; i < 100000; i++) {
            try {
                double timeIncrementInSeconds = 1.0 / 60.0;
                myPos = offset;
                // myArm.setArmPositionByServoAngles(((double) offset) / 0.1, ((double) offset) / 0.1, 0.0);
                // myArm.setArmPositionByServoAngles(((double) i), ((double) i), 0.0);
                // myArm.setArmPositionByServoAngles(10.0, 10.0, 0.0);
                offset += incr;
                if (offset > 135 || offset < -135) {
                    incr *= -1;
                }
                update(timeIncrementInSeconds);
                repaint();
                Thread.sleep((long) (timeIncrementInSeconds * 1000));
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    //
    // Key Listener methods
    //

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        char c = keyEvent.getKeyChar();
        int code = keyEvent.getKeyCode();
        System.out.println("KeyTyped = " + Character.toString(c) + " " + Integer.toString(code));
        System.out.println("Got " + Character.toString(c));
        if (!myCtrlPressed && !myOptionPressed && !myCommandPressed) {
            switch (c) {
                case 'a': myArm.setArmPosition(0.1, 0.1, 0.5);
                          break;
                case 'b': myArm.setArmPosition(0.5, 0.5, 0.5);
                          break;
                case 'c': clearTrail();
                          break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        char c = keyEvent.getKeyChar();
        int code = keyEvent.getKeyCode();
        System.out.println("KeyPressed = " + Character.toString(c) + " " + Integer.toString(code));
        System.out.println(keyEvent.isAltDown());
        System.out.println(keyEvent.isControlDown());
        System.out.println(keyEvent.isMetaDown());
        if (code == 17) {
            myCtrlPressed = true;
        }
        else if (code == 18) {
            myOptionPressed = true;
        }
        else if (code == 157) {
            myCommandPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        char c = keyEvent.getKeyChar();
        int code = keyEvent.getKeyCode();
        System.out.println("KeyReleased = " + Character.toString(c) + " " + Integer.toString(code));
        if (code == 17) {
            myCtrlPressed = false;
        }
        else if (code == 18) {
            myOptionPressed = false;
        }
        else if (code == 157) {
            myCommandPressed = false;
        }
    }

    //
    // Mouse Listener methods
    //

    private Point2D.Double getJoystickXY(MouseEvent mouseEvent) {
        double joystickX = ((double) mouseEvent.getX() - myJoystickCenterX) / (myJoystickDiameter / 2.0);
        double joystickY = -((double) mouseEvent.getY() - myJoystickCenterY) / (myJoystickDiameter / 2.0);
        if (((joystickX * joystickX) + (joystickY * joystickY)) > 1.0) {
            joystickX = 0.0;
            joystickY = 0.0;
        }
        System.out.format("Joystick: %s %s %s %s\n", joystickX, joystickY, myJoystickCenterX, myJoystickCenterY);
        return new Point2D.Double(joystickX, joystickY);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        System.out.format("Mouse clicked: %s %s\n", mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        myMouseDown = true;
        System.out.format("Mouse pressed: %s %s\n", mouseEvent.getX(), mouseEvent.getY());
        Point2D.Double point = getJoystickXY(mouseEvent);
        myJoystickX = point.getX();
        myJoystickY = point.getY();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        myMouseDown = false;
        System.out.format("Mouse released: %s %s\n", mouseEvent.getX(), mouseEvent.getY());
        myJoystickX = 0.0;
        myJoystickY = 0.0;
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        System.out.format("Mouse entered: %s %s\n", mouseEvent.getX(), mouseEvent.getY());
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        System.out.format("Mouse exited: %s %s\n", mouseEvent.getX(), mouseEvent.getY());
        myJoystickX = 0.0;
        myJoystickY = 0.0;
    }

    //
    // Mouse Listener methods
    //

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        assert myMouseDown;
        System.out.format("Mouse dragged: %s %s\n", mouseEvent.getX(), mouseEvent.getY());
        Point2D.Double point = getJoystickXY(mouseEvent);
        myJoystickX = point.getX();
        myJoystickY = point.getY();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        // System.out.format("Mouse moved: %s %s\n", mouseEvent.getX(), mouseEvent.getY());
    }

    public static void main(String[] args) {
        Simulation armSimulator = new Simulation();
        System.out.print("Hmm\n");

        System.out.print("Bye");
    }

}
