package com.helix.appl.simulation.armcontrollers;

import com.helix.appl.simulation.Arm;
import com.helix.appl.simulation.Gamepad;
import com.helix.lib.LibGamepad;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

public class SampleArmController implements ArmController {

    private final double BASE_ARM_LENGTH_IN_INCH;
    private final double END_ARM_LENGTH_IN_INCH;
    private double JOYSTICK_TO_GRIPPER_POSITION_FACTOR;
    private LibGamepad myGamepad1;
    private LibGamepad myGamepad2;


    public SampleArmController(Arm arm,  LibGamepad gamepad1, LibGamepad gamepad2) {
        BASE_ARM_LENGTH_IN_INCH = arm.getArmLength(0);
        END_ARM_LENGTH_IN_INCH = arm.getArmLength(1);
        JOYSTICK_TO_GRIPPER_POSITION_FACTOR = 2.5;
        myGamepad1 = gamepad1;
        myGamepad2 = gamepad2;
    }

    private List<Double> armServoPositionsToAnglesInDegree(double baseServoPosition,
                                                           double middleServoPosition) {
        double baseServoAngleInDegrees = baseServoPosition * 270 - 45;
        double middleServoAngleInDegrees = middleServoPosition * 270 - 135;

        return Arrays.asList(baseServoAngleInDegrees, middleServoAngleInDegrees);
    }

    public List<Double> anglesInDegreeToArmServoPositions(double baseServoAngleInDegrees,
                                                          double middleServoAngleInDegrees) {
        double baseServoPosition = (baseServoAngleInDegrees + 45) / 270;
        double middleServoPosition = (middleServoAngleInDegrees + 135) / 270;

        return Arrays.asList(baseServoPosition, middleServoPosition);
    }

    public List<Double> getGripperBasePositionFromServoAngles(double baseServoAngleInDegrees,
                                                              double middleServoAngleInDegrees) {
        double beta_relative_to_horizontal = baseServoAngleInDegrees + middleServoAngleInDegrees;

        double x = BASE_ARM_LENGTH_IN_INCH * Math.cos(Math.toRadians(baseServoAngleInDegrees))
                + END_ARM_LENGTH_IN_INCH * Math.cos(Math.toRadians(beta_relative_to_horizontal));

        double y = BASE_ARM_LENGTH_IN_INCH * Math.sin(Math.toRadians(baseServoAngleInDegrees))
                + END_ARM_LENGTH_IN_INCH * Math.sin(Math.toRadians(beta_relative_to_horizontal));

        return Arrays.asList(x, y);
    }

    public List<Double> getServoAnglesFromGripperBasePosition(double gripperBaseX,
                                                              double gripperBaseY) {
        double r_squared = gripperBaseX * gripperBaseX + gripperBaseY * gripperBaseY;

        double middleServoAngleInDegrees = -(180 - Math.toDegrees(Math.acos((
                BASE_ARM_LENGTH_IN_INCH * BASE_ARM_LENGTH_IN_INCH
                        + END_ARM_LENGTH_IN_INCH * END_ARM_LENGTH_IN_INCH - r_squared)
                / (2 * BASE_ARM_LENGTH_IN_INCH * END_ARM_LENGTH_IN_INCH))));

        double angleXY = Math.toDegrees(Math.atan2(gripperBaseY, gripperBaseX));

        double delta = Math.toDegrees(Math.acos((r_squared
                + BASE_ARM_LENGTH_IN_INCH * BASE_ARM_LENGTH_IN_INCH
                - END_ARM_LENGTH_IN_INCH * END_ARM_LENGTH_IN_INCH)
                / (2 * Math.sqrt(r_squared) * BASE_ARM_LENGTH_IN_INCH)));

        double baseServoAngleInDegrees = angleXY + delta;
        return Arrays.asList(baseServoAngleInDegrees, middleServoAngleInDegrees);
    }

    public List<Double> getNewGripperBasePosition(double gamepad2_X,
                                                  double gamepad2_Y,
                                                  double gripperBaseX,
                                                  double gripperBaseY,
                                                  double timeInSeconds) {
        System.out.format("JOYSTICK_TO_GRIPPER_POSITION_FACTOR: %s\n", JOYSTICK_TO_GRIPPER_POSITION_FACTOR);
        gripperBaseX = gripperBaseX + gamepad2_X * JOYSTICK_TO_GRIPPER_POSITION_FACTOR * timeInSeconds;
        gripperBaseY = gripperBaseY + gamepad2_Y * JOYSTICK_TO_GRIPPER_POSITION_FACTOR * timeInSeconds;
        return Arrays.asList(gripperBaseX, gripperBaseY);
    }


    @Override
    public void setTargetArmPosition(Arm arm, double position0, double position1, double position2) {
        arm.setTargetArmPosition(position0, position1, position2);
    }

    @Override
    public void setArmForXY(Arm arm, double timeInSeconds) {
        if (myGamepad2.a) {
            arm.setTargetArmPosition(0.25, 0.1, 0.25);
        }
        else  if (myGamepad2.b) {
            arm.setTargetArmPosition(0.5, 0.5, 0.5);
        }

        double controllerX = myGamepad2.right_stick_x;
        double controllerY = myGamepad2.right_stick_y;
        double angle0 = arm.getServoTargetPositionInDegrees(0);
        double angle1 = arm.getServoTargetPositionInDegrees(1);
        double angle2 = arm.getServoTargetPositionInDegrees(2);
        if (Math.abs(controllerX) <= 0.2) {
            controllerX = 0.0;
        }
        if (Math.abs(controllerY) <= 0.2) {
            controllerY = 0.0;
        }
        System.out.format("Joystick in setArmForXY: %s %s\n", controllerX, controllerY);

        List<Double> armAngles2 = armServoPositionsToAnglesInDegree(arm.getServoTargetPosition(0),
                                                                    arm.getServoTargetPosition(1));

        System.out.format("Angles: %s %s\n", angle0, angle1);
        System.out.format("Angles2: %s %s\n", armAngles2.get(0), armAngles2.get(1));
        assert Math.abs(armAngles2.get(0) - (angle0 + 90)) < 1e-9;
        assert Math.abs(armAngles2.get(1) - angle1) < 1e-9;

        System.out.format("Servo Target Position2: %s %s\n", arm.getServoTargetPosition(0), arm.getServoTargetPosition(1));
        List<Point2D> armPoints = arm.getTargetArmPoints();
        Point2D tipPoint = armPoints.get(2);
        List<Double> tipPoint2 = getGripperBasePositionFromServoAngles(
                armAngles2.get(0), armAngles2.get(1));

        System.out.format("Tip: %s %s\n", tipPoint.getX(), tipPoint.getY());
        System.out.format("Tip2: %s %s\n", tipPoint2.get(0), tipPoint2.get(1));
        assert Math.abs(tipPoint.getX() - tipPoint2.get(0)) < 1e-9;
        assert Math.abs(tipPoint.getY() - tipPoint2.get(1)) < 1e-9;

        List<Double> newGripperBasePosition = getNewGripperBasePosition(controllerX,
                                                                        controllerY,
                                                                        tipPoint2.get(0),
                                                                        tipPoint2.get(1),
                                                                        timeInSeconds);

        System.out.format("Target Tip2: %s %s\n", newGripperBasePosition.get(0), newGripperBasePosition.get(1));

        List<Double> newServoAngles = getServoAnglesFromGripperBasePosition(
                newGripperBasePosition.get(0), newGripperBasePosition.get(1));

        System.out.format("Target Servo Angles2: %s %s\n", newServoAngles.get(0), newServoAngles.get(1));

        System.out.format("Arm lengths: %s %s\n", arm.getArmLength(0), arm.getArmLength(1));
        double angleInRadians0 = Math.toRadians(newServoAngles.get(0));
        double angleInRadians1 = Math.toRadians(newServoAngles.get(1));
        double computedTargetX = Math.cos(angleInRadians0) * arm.getArmLength(0) +
                Math.cos(angleInRadians0 + angleInRadians1) * arm.getArmLength(1);
        double computedTargetY = Math.sin(angleInRadians0) * arm.getArmLength(0) +
                Math.sin(angleInRadians0 + angleInRadians1) * arm.getArmLength(1);

        System.out.format("Target XY From Target Servo Angles2: %s %s\n", computedTargetX, computedTargetY);

        List<Double> newServoPositions = anglesInDegreeToArmServoPositions(
                newServoAngles.get(0), newServoAngles.get(1));

        System.out.format("Target Servo Positions2: %s %s\n", newServoPositions.get(0), newServoPositions.get(1));

        if ((!Double.isNaN(newServoPositions.get(0))) &&
            (!Double.isNaN(newServoPositions.get(1))) &&
                (newServoPositions.get(0) >= 0.0) &&
                (newServoPositions.get(1) <= 1.0) &&
                (newServoPositions.get(1) >= 0.0) &&
                (newServoPositions.get(1) <= 1.0)) {
            arm.getServo(0).setPosition(Math.max(Math.min(newServoPositions.get(0), 1.0), 0.0));
            arm.getServo(1).setPosition(Math.max(Math.min(newServoPositions.get(1), 1.0), 0.0));
        }

        /*
        double angleXY = Math.atan2(tipPoint.getX(), tipPoint.getY());
        double angleXY2 = Math.atan2(tipPoint2.get(0), tipPoint2.get(1));
        assert Math.abs(angleXY - angleXY2) < 1e-9;

        arm.setArmPositionByAnglesInDegrees(angle0 + 0.5 * controllerY, angle1 + 0.5 * controllerX, angle2);
         */


    }
}
