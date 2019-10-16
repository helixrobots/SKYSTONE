package com.helix.appl.simulation.armcontrollers;

import com.helix.appl.simulation.Arm;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;

public class SampleArmController implements ArmController {

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

        double middleServoAngleInDegrees = 180 - Math.toDegrees(Math.acos((
                BASE_ARM_LENGTH_IN_INCH * BASE_ARM_LENGTH_IN_INCH
                        + END_ARM_LENGTH_IN_INCH * END_ARM_LENGTH_IN_INCH - r_squared)
                / (2 * BASE_ARM_LENGTH_IN_INCH * END_ARM_LENGTH_IN_INCH)));

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
        if (Math.abs(gamepad2_X) <= 0.1) {
            gamepad2_Y = 0;
        }
        if (Math.abs(gamepad2_Y) <= 0.1) {
            gamepad2_Y = 0;
        }

        gripperBaseX = gripperBaseX + gamepad2_X * JOYSTICK_TO_GRIPPER_POSITION_FACTOR * timeInSeconds;
        gripperBaseY = gripperBaseY + gamepad2_Y * JOYSTICK_TO_GRIPPER_POSITION_FACTOR * timeInSeconds;
        return Arrays.asList(gripperBaseX, gripperBaseY);
    }


    @Override
    public void setArmForXY(Arm arm, double controllerX, double controllerY, double timeInSeconds) {
        double angle0 = arm.getServoPositionInDegrees(0);
        double angle1 = arm.getServoPositionInDegrees(1);
        double angle2 = arm.getServoPositionInDegrees(2);
        if (Math.abs(controllerX) <= 0.1) {
            controllerX = 0.0;
        }
        if (Math.abs(controllerY) <= 0.1) {
            controllerY = 0.0;
        }

        List<Double> armAngles2 = armServoPositionsToAnglesInDegree(arm.getServoPosition(0),
                                                                    arm.getServoPosition(1));

        System.out.format("Angles: %s %s\n", angle0, angle1);
        System.out.format("Angles2: %s %s\n", armAngles2.get(0), armAngles2.get(1));
        assert Math.abs(armAngles2.get(0) - (angle0 + 90)) < 1e-9;
        assert Math.abs(armAngles2.get(1) - angle1) < 1e-9;

        System.out.format("Servo Position2: %s %s\n", arm.getServoPosition(0), arm.getServoPosition(1));
        List<Point2D> armPoints = arm.getArmPoints();
        Point2D tipPoint = armPoints.get(2);
        List<Double> tipPoint2 = getGripperBasePositionFromServoAngles(
                armAngles2.get(0), armAngles2.get(1));

        System.out.format("Tip: %s %s\n", tipPoint.getX(), tipPoint.getY());
        System.out.format("Tip2: %s %s\n", tipPoint2.get(0), tipPoint2.get(1));
        assert Math.abs(tipPoint.getX() - tipPoint2.get(0)) < 1e-9;
        assert Math.abs(tipPoint.getY() - tipPoint2.get(1)) < 1e-9;

        List<Double> newGripperBasePosition = getNewGripperBasePosition(controllerX,
                                                                        controllerY,
                                                                        tipPoint.getX(),
                                                                        tipPoint.getY(),
                                                                        timeInSeconds);

        System.out.format("Target Tip2: %s %s\n", newGripperBasePosition.get(0), newGripperBasePosition.get(1));

        List<Double> newServoAngles = getServoAnglesFromGripperBasePosition(
                newGripperBasePosition.get(0), newGripperBasePosition.get(1));

        System.out.format("Target Servo Angles2: %s %s\n", newServoAngles.get(0), newServoAngles.get(1));

        List<Double> newServoPositions = anglesInDegreeToArmServoPositions(
                newServoAngles.get(0), newServoAngles.get(1));

        System.out.format("Target Servo Positions2: %s %s\n", newServoPositions.get(0), newServoPositions.get(1));

        arm.getServo(0).setPosition(newServoPositions.get(0));
        arm.getServo(1).setPosition(newServoPositions.get(1));

        /*
        double angleXY = Math.atan2(tipPoint.getX(), tipPoint.getY());
        double angleXY2 = Math.atan2(tipPoint2.get(0), tipPoint2.get(1));
        assert Math.abs(angleXY - angleXY2) < 1e-9;

        arm.setArmPositionByAnglesInDegrees(angle0 + 0.5 * controllerY, angle1 + 0.5 * controllerX, angle2);
         */


    }
}
