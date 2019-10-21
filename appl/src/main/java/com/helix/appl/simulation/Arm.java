package com.helix.appl.simulation;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.helix.lib.utils.Conversions;


public class Arm {

    Servo myServo0;
    Servo myServo1;
    Servo myServo2;
    double myServoOrientation0;     // Orientation relative to straight up, in degrees.  0.0 means
                                    // position 0.5 on the servo is straight up.  90.0 means
                                    // position 0.5 on the servo points to the left.
    double myServoOrientation1;     // Orientation relative to continuing the previous arm segment.
                                    // straight up, in degrees.  0.0 means position 0.5 on the
                                    // keeps this segment as a straight continuation of the previous
                                    // segment.
    double myServoOrientation2;
    boolean myServoReversed0;       // False if servo0 increases from 0.0 to 1.0 counterclockwise.
    boolean myServoReversed1;       // False if servo1 increases from 0.0 to 1.0 counterclockwise.
    boolean myServoReversed2;       // False if servo1 increases from 0.0 to 1.0 counterclockwise.
    double myArmLength0;
    double myArmLength1;
    double myArmLength2;

    public Arm(Servo servo0, double servoOrientation0, boolean servoReversed0, double armLength0,
               Servo servo1, double servoOrientation1, boolean servoReversed1, double armLength1) {
        myServo0 = servo0;
        myServoOrientation0 = servoOrientation0;
        myServoReversed0 = servoReversed0;
        myArmLength0 = armLength0;
        myServo1 = servo1;
        myServoOrientation1 = servoOrientation1;
        myServoReversed1 = servoReversed1;
        myArmLength1 = armLength1;
        // Arm three is currently a null arm.
        myServo2 = new Servo();
        myServoOrientation2 = 0.0;
        myServoReversed2 = false;
        myArmLength2 = 0.0;
    }

    public Arm(Servo servo0, double armLength0,
               Servo servo1, double armLength1) {
        this(servo0, 0.0, false, armLength0,
                servo1, 0.0, false, armLength1);
    }

    public Arm() {
        this(new Servo(), 0.0, false, 10.0,
                new Servo(), 0.0, false, 12.0);
    }

    public void setTargetArmPosition(double position0, double position1, double position2) {
        myServo0.setPosition(position0);
        myServo1.setPosition(position1);
        myServo2.setPosition(position2);
    }

    public void setTargetArmPositionByAnglesInDegrees(double angle0, double angle1, double angle2) {
        myServo0.setTargetPositionInDegrees(angle0);
        myServo1.setTargetPositionInDegrees(angle1);
        myServo2.setTargetPositionInDegrees(angle2);
    }

    public Servo getServo(int i) {
        if (i == 0) {
            return myServo0;
        }
        else if (i == 1) {
            return myServo1;
        }
        else {
            assert i == 2;
            return myServo2;
        }
    }

    protected double getServoCurrentPosition(int i) {
        return getServo(i).getCurrentPosition();
    }

    public double getServoTargetPosition(int i) {
        return getServo(i).getTargetPosition();
    }

    protected double getServoCurrentPositionInDegrees(int i) {
        return getServo(i).getCurrentPositionInDegrees();
    }

    public double getServoTargetPositionInDegrees(int i) {
        return getServo(i).getTargetPositionInDegrees();
    }

    public double getArmLength(int i) {
        if (i == 0) {
            return myArmLength0;
        }
        else if (i == 1) {
            return myArmLength1;
        }
        else {
            assert i == 2;
            return myArmLength2;
        }
    }

    protected ArrayList<Point2D> getCurrentArmPoints() {
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        Point2D curPoint = new Point2D.Double(0.0, 0.0);
        points.add(curPoint);
        double degreesRelativeToHorizontal0 = myServo0.getCurrentPositionInDegrees() + 90 + myServoOrientation0;
        double radiansRelativeToHorizontal0 = Conversions.degreesToRadians(degreesRelativeToHorizontal0);
        // System.out.format("%f %f %f\n", myServo0.getPosition(), myServo0.getPositionInDegrees(), myServoOrientation0);
        // System.out.println(degreesRelativeToHorizontal0);
        curPoint = new Point2D.Double(
                curPoint.getX() + Math.cos(radiansRelativeToHorizontal0) * myArmLength0,
                curPoint.getY() + Math.sin(radiansRelativeToHorizontal0) * myArmLength0);
        // System.out.println(curPoint);
        points.add(curPoint);
        double degreesRelativeToHorizontal1 = myServo1.getCurrentPositionInDegrees() + degreesRelativeToHorizontal0 + myServoOrientation1;
        double radiansRelativeToHorizontal1 = Conversions.degreesToRadians(degreesRelativeToHorizontal1);
        // System.out.println(degreesRelativeToHorizontal1);
        curPoint = new Point2D.Double(
                curPoint.getX() + Math.cos(radiansRelativeToHorizontal1) * myArmLength1,
                curPoint.getY() + Math.sin(radiansRelativeToHorizontal1) * myArmLength1);
        // System.out.println(curPoint);
        points.add(curPoint);
        double degreesRelativeToHorizontal2 = myServo2.getCurrentPositionInDegrees() + degreesRelativeToHorizontal1 + myServoOrientation2;
        double radiansRelativeToHorizontal2 = Conversions.degreesToRadians(degreesRelativeToHorizontal2);
        // System.out.println(degreesRelativeToHorizontal2);

        curPoint = new Point2D.Double(
                curPoint.getX() + Math.cos(radiansRelativeToHorizontal2) * myArmLength2,
                curPoint.getY() + Math.sin(radiansRelativeToHorizontal2) * myArmLength2);
        // System.out.println(curPoint);
        points.add(curPoint);
        return points;
    }

    public ArrayList<Point2D> getTargetArmPoints() {
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        Point2D curPoint = new Point2D.Double(0.0, 0.0);
        points.add(curPoint);
        double degreesRelativeToHorizontal0 = myServo0.getTargetPositionInDegrees() + 90 + myServoOrientation0;
        double radiansRelativeToHorizontal0 = Conversions.degreesToRadians(degreesRelativeToHorizontal0);
        // System.out.format("%f %f %f\n", myServo0.getPosition(), myServo0.getPositionInDegrees(), myServoOrientation0);
        // System.out.println(degreesRelativeToHorizontal0);
        curPoint = new Point2D.Double(
                curPoint.getX() + Math.cos(radiansRelativeToHorizontal0) * myArmLength0,
                curPoint.getY() + Math.sin(radiansRelativeToHorizontal0) * myArmLength0);
        // System.out.println(curPoint);
        points.add(curPoint);
        double degreesRelativeToHorizontal1 = myServo1.getTargetPositionInDegrees() + degreesRelativeToHorizontal0 + myServoOrientation1;
        double radiansRelativeToHorizontal1 = Conversions.degreesToRadians(degreesRelativeToHorizontal1);
        // System.out.println(degreesRelativeToHorizontal1);
        curPoint = new Point2D.Double(
                curPoint.getX() + Math.cos(radiansRelativeToHorizontal1) * myArmLength1,
                curPoint.getY() + Math.sin(radiansRelativeToHorizontal1) * myArmLength1);
        // System.out.println(curPoint);
        points.add(curPoint);
        double degreesRelativeToHorizontal2 = myServo2.getTargetPositionInDegrees() + degreesRelativeToHorizontal1 + myServoOrientation2;
        double radiansRelativeToHorizontal2 = Conversions.degreesToRadians(degreesRelativeToHorizontal2);
        // System.out.println(degreesRelativeToHorizontal2);

        curPoint = new Point2D.Double(
                curPoint.getX() + Math.cos(radiansRelativeToHorizontal2) * myArmLength2,
                curPoint.getY() + Math.sin(radiansRelativeToHorizontal2) * myArmLength2);
        // System.out.println(curPoint);
        points.add(curPoint);
        return points;
    }


    public void update(double timeInSeconds) {
        myServo0.update(timeInSeconds);
        myServo1.update(timeInSeconds);
        myServo2.update(timeInSeconds);
    }

    public static void main(String[] args) {

    }
}
