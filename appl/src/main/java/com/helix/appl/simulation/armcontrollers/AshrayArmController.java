package com.helix.appl.simulation.armcontrollers;

import com.helix.appl.simulation.Arm;
import com.helix.appl.simulation.Servo;

import java.util.Arrays;
import java.util.List;

public class AshrayArmController implements ArmController {

    final String ARM_CODE_PICK_UP = "pick up";
    final String ARM_CODE_HOLDING = "hold";
    final String ARM_CODE_REST = "rest";

    final double BASE_ARM_LENGTH_IN_INCH;
    final double END_ARM_LENGTH_IN_INCH;
    final double JOYSTICK_TO_GRIPPER_POSITION_FACTOR;


    public AshrayArmController(Arm arm) {
        BASE_ARM_LENGTH_IN_INCH = arm.getArmLength(0);
        END_ARM_LENGTH_IN_INCH = arm.getArmLength(1);
        JOYSTICK_TO_GRIPPER_POSITION_FACTOR = 2.5;
    }

    private List<Double> armServoPositionsToAnglesInDegree(double baseServoPosition,
                                                           double middleServoPosition) {
        double baseServoAngleInDegrees = baseServoPosition * 270 - 45;
        double middleServoAngleInDegrees = 135 - 270 * middleServoPosition;

        return Arrays.asList(baseServoAngleInDegrees, middleServoAngleInDegrees);
    }

    private List<Double> getGripperBasePositionFromServoAngles(double baseServoAngleInDegrees,
                                                               double middleServoAngleInDegrees) {
        double beta_relative_to_horizontal = baseServoAngleInDegrees - middleServoAngleInDegrees;

        double x = BASE_ARM_LENGTH_IN_INCH * Math.cos(Math.toRadians(baseServoAngleInDegrees))
                + END_ARM_LENGTH_IN_INCH * Math.cos(Math.toRadians(beta_relative_to_horizontal));

        double y = BASE_ARM_LENGTH_IN_INCH * Math.sin(Math.toRadians(baseServoAngleInDegrees))
                + END_ARM_LENGTH_IN_INCH * Math.sin(Math.toRadians(beta_relative_to_horizontal));

        return Arrays.asList(x, y);
    }

    private List<Double> getServoAnglesFromGripperBasePosition(double gripperBaseX,
                                                               double gripperBaseY,
                                                               double oldBaseServoAngleInDegrees,
                                                               double oldMiddleServoAngleInDegrees) {

        double r_squared = gripperBaseX * gripperBaseX + gripperBaseY * gripperBaseY;

//        if((BASE_ARM_LENGTH_IN_INCH * BASE_ARM_LENGTH_IN_INCH
//                + END_ARM_LENGTH_IN_INCH * END_ARM_LENGTH_IN_INCH - r_squared) < 0){
//
//            return Arrays.asList(oldBaseServoAngleInDegrees, oldMiddleServoAngleInDegrees);
//
//        }


        double middleServoAngleInDegrees = 180 - Math.toDegrees(Math.acos(((
                BASE_ARM_LENGTH_IN_INCH * BASE_ARM_LENGTH_IN_INCH
                        + END_ARM_LENGTH_IN_INCH * END_ARM_LENGTH_IN_INCH - r_squared)
                / (2 * BASE_ARM_LENGTH_IN_INCH * END_ARM_LENGTH_IN_INCH))));

        double gamma = Math.abs(gripperBaseX) < 0.1 ? 90 : Math.toDegrees(Math.atan(gripperBaseY / gripperBaseX));

        double delta = Math.toDegrees(Math.acos((r_squared
                + BASE_ARM_LENGTH_IN_INCH * BASE_ARM_LENGTH_IN_INCH
                - END_ARM_LENGTH_IN_INCH * END_ARM_LENGTH_IN_INCH)
                / (2 * Math.sqrt(r_squared) * BASE_ARM_LENGTH_IN_INCH)));

        double baseServoAngleInDegrees = gamma + delta;
        return Arrays.asList(baseServoAngleInDegrees, middleServoAngleInDegrees);
    }


    private List<Double> anglesInDegreeToArmServoPositions(double baseServoAngleInDegrees,
                                                           double middleServoAngleInDegrees) {
        double baseServoPosition = (baseServoAngleInDegrees + 45) / 270;
        double middleServoPosition = (135 - middleServoAngleInDegrees) / 270;

        return Arrays.asList(baseServoPosition, middleServoPosition);
    }

    private List<Double> getNewGripperBasePosition(double gamepad2_X,
                                                   double gamepad2_Y,
                                                   double gripperBaseX,
                                                   double gripperBaseY) {
        if (Math.abs(gamepad2_X) > Math.abs(gamepad2_Y)) {
            gamepad2_Y = 0;
        } else {
            gamepad2_X = 0;
        }

        gripperBaseX = gripperBaseX + gamepad2_X * JOYSTICK_TO_GRIPPER_POSITION_FACTOR;
        gripperBaseY = gripperBaseY + gamepad2_Y * JOYSTICK_TO_GRIPPER_POSITION_FACTOR;

//        if(gripperBaseY > 16.5){
//            gripperBaseY = 16.5;
//        }
//        if(gripperBaseY < -13){
//            gripperBaseY = -13;
//        }
        if (gripperBaseX < 0) {
            gripperBaseX = 0;
        }


        return Arrays.asList(gripperBaseX, gripperBaseY);
    }

    private void setArmServosForXY(double gamepad2_X, double gamepad2_Y, Servo armServoBase, Servo armServoMiddle) {
        double baseServoPosition = armServoBase.getPosition();
        double middleServoPosition = armServoMiddle.getPosition();
        /*
        if (gamepad2.a) {
            // button A
            armPositions(ARM_CODE_PICK_UP);
            return;
        } else if (gamepad2.b) {
            // button B
            armPositions(ARM_CODE_HOLDING);
            return;
        }
        */

        // logic for gripper base movement - x and y

        gamepad2_X = gamepad2_X / 50;
        gamepad2_Y = -gamepad2_Y / 50;


        //---------------------------------------------------------------------------------------------

        System.out.println("In setArmServosForXY");
        double armBaseNewPosition = baseServoPosition;     //0.4
        double armMiddleNewPosition = middleServoPosition; //0.5
        System.out.format("%s %s %s %s\n", gamepad2_X, gamepad2_Y, armBaseNewPosition, armMiddleNewPosition);
        List<Double> anglesInDegrees = armServoPositionsToAnglesInDegree(baseServoPosition, middleServoPosition);

        double baseServoAngleInDegrees = anglesInDegrees.get(0);    //63
        double middleServoAngleInDegrees = anglesInDegrees.get(1);  //8.1

        List<Double> gripperBasePosition = getGripperBasePositionFromServoAngles(baseServoAngleInDegrees, middleServoAngleInDegrees);

        double gripperBaseX = gripperBasePosition.get(0); //
        double gripperBaseY = gripperBasePosition.get(1); //

        List<Double> newGripperBasePosition = getNewGripperBasePosition(gamepad2_X, gamepad2_Y, gripperBaseX, gripperBaseY);

        double newGripperBaseX = newGripperBasePosition.get(0);
        double newGripperBaseY = newGripperBasePosition.get(1);

        List<Double> newServoAngles = getServoAnglesFromGripperBasePosition(newGripperBaseX, newGripperBaseY, baseServoAngleInDegrees, middleServoAngleInDegrees);

        double newBaseServoAngle = newServoAngles.get(0);
        double newMiddleServoAngle = newServoAngles.get(1);

        List<Double> newServoPositions = anglesInDegreeToArmServoPositions(newBaseServoAngle, newMiddleServoAngle);

        armBaseNewPosition = newServoPositions.get(0);
        armMiddleNewPosition = newServoPositions.get(1);

        /*
        telemetry.addData("baseServoPosition", baseServoPosition);
        telemetry.addData("middleServoPosition", middleServoPosition);
        telemetry.addData("baseServoAngleInDegrees", baseServoAngleInDegrees);
        telemetry.addData("middleServoAngleInDegrees", middleServoAngleInDegrees);
        telemetry.addData("newBaseServoAngle", newBaseServoAngle);
        telemetry.addData("newMiddleServoAngle", newMiddleServoAngle);
        telemetry.addData("gripperBaseX", gripperBaseX);
        telemetry.addData("gripperBaseY", gripperBaseY);
        telemetry.addData("newGripperBaseX", newGripperBaseX);
        telemetry.addData("newGripperBaseY", newGripperBaseY);
        telemetry.addData("armBaseNewPosition", armBaseNewPosition);
        telemetry.addData("armMiddleNewPosition", armMiddleNewPosition);
        telemetry.update();
        */

//        if (armBaseNewPosition > 0.5) {
//            armBaseNewPosition = 0.5;
//        }
//
//        if (armBaseNewPosition < 0.0) {
//            armBaseNewPosition = 0.0;
//        }
//
//        if (armMiddleNewPosition > 0.5) {
//            armMiddleNewPosition = 0.5;
//        }
//
//        if (armMiddleNewPosition < 0.1) {
//            armMiddleNewPosition = 0.1;
//        }
        System.out.format("Pre New position: %s %s\n", armBaseNewPosition, armMiddleNewPosition);

        if (armBaseNewPosition > 1) {
            armBaseNewPosition = 1;
        }
        if (Double.valueOf(armBaseNewPosition).isNaN()) {
            armBaseNewPosition = baseServoPosition;
        }
        if (armBaseNewPosition < 0) {
            armBaseNewPosition = 0;
        }
        if (armMiddleNewPosition > 1) {
            armMiddleNewPosition = 1;
        }
        if (Double.valueOf(armMiddleNewPosition).isNaN()) {
            armMiddleNewPosition = middleServoPosition;
        }
        if (armMiddleNewPosition < 0) {
            armMiddleNewPosition = 0;
        }

        System.out.format("New position: %s %s\n", armBaseNewPosition, armMiddleNewPosition);
        armServoBase.setPosition(armBaseNewPosition);
        armServoMiddle.setPosition(armMiddleNewPosition);
    }

    public void setArmForXY(Arm arm, double controllerX, double controllerY, double timeInSeconds) {
        setArmServosForXY(controllerX, controllerY, arm.getServo(0), arm.getServo(1));
    }
}
