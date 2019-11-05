
package org.firstinspires.ftc.teamcode;

import android.net.MacAddress;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.Arrays;
import java.util.List;
import java.lang.*;


@TeleOp(name = "Basic: Iterative OpMode", group = "Iterative Opmode")

public class AshrayBasicOpMode_Iterative extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor arm_up_down = null;
    private Servo armServoBase = null;
    private Servo armServoMiddle = null;
    private Servo gripperServoBase = null;
    private Servo gripper = null;
    double leftPower;
    double rightPower;
    final String ARM_CODE_PICK_UP = "pick up";
    final String ARM_CODE_HOLDING = "hold";
    final String ARM_CODE_REST = "rest";
    // These two states may be redundant (they may be the same as the PICK_UP and HOLDING settings
    // above, but not sure, so we don't interfere with those.  We can reconcile later.
    final String ARM_CODE_READY_FOR_PICK_UP = "ready_pickup";
    final String ARM_CODE_PREP_FOR_TRANSPORT = "prep_transport";
    final String ARM_CODE_TRANSPORT = "transport";
    static final double BASE_ARM_LENGTH_IN_INCH = 9.875;
    static final double END_ARM_LENGTH_IN_INCH = 9.75;
    static final double JOYSTICK_TO_GRIPPER_POSITION_FACTOR = 2.5;
    double presetSpeed = 0.0025;
    private int numIterations = 0;
    private Double targetServoBasePosition = null;
    private Double targetServoMiddlePosition = null;
    private Double targetServoGripperBasePosition = null;
    boolean gripperHold = false;
    private int pickupCounter = 0;

    private enum ArmLoopState {
        NORMAL,
        RUNNING_PRESET,
        PICKING_UP,
        PREP_FOR_TRANSPORT,
    }

    private ArmLoopState currentArmLoopState = ArmLoopState.NORMAL;

    private void setArmPosition(double base, double middle, double gripperBase) {
        /*
        armServoBase.setPosition(base);armServoMiddle.setPosition(middle);
        double newGripperServoBase = gripperBase;
        double newArmBasePosition = base;
        double newArmMiddlePosition = middle;

        double currentBase = armServoBase.getPosition();
        double currentMiddle = armServoMiddle.getPosition();
        double currentGripper = gripperServoBase.getPosition();

        double gripperDiff = Math.abs(newGripperServoBase - currentGripper);
        double baseDiff = Math.abs(newArmBasePosition - currentBase);
        double middleDiff = Math.abs(newArmMiddlePosition - currentMiddle);

        double v = 0.05;
        int sleepMillis = 200;

        double diff = Math.max(baseDiff, middleDiff);
        double x = diff / v;

        for (int f = 1; f <= x; f++) {
            armServoBase.setPosition(f * baseDiff / x + currentBase);    // armBaseNewPosition is used to set the servo position in the arm loop
            armServoMiddle.setPosition(f * middleDiff / x + currentMiddle); // armMiddleNewPosition is used to set the servo position in the arm loop
           gripperServoBase.setPosition(f * gripperDiff / x + currentGripper);
            Thread.sleep(sleepMillis);
       }
        gripperServoBase.setPosition(gripperBase);
        armBaseNewPosition = base;
        armMiddleNewPosition = middle;
        */
        targetServoBasePosition = base;
        targetServoMiddlePosition = middle;
        targetServoGripperBasePosition = gripperBase;
    }

    private void setArmPosition(double base, double middle) {
        /*
        Sets the servos for the arm base and the arm middle servos to the positions specified in
        the arguments.  Sets the servo for the gripper to point the gripper down.
         */
        List<Double> targetServoAngles =
                armServoPositionsToAnglesInDegree(base, middle);
        double gripperAngle = gripperAngleCalculator(targetServoAngles.get(0), targetServoAngles.get(1));
        double gripperPosition = gripperAngleToPosition(gripperAngle);
        setArmPosition(base, middle, gripperPosition);
    }

    private void setArmXYPosition(double x, double y) {
        /*
        Sets the servo positions given a target (x, y) position.  x must be greater than 0, for now.
         */
        assert(x >= 0.0);
        List<Double> currentServoAngles =
                armServoPositionsToAnglesInDegree(
                        armServoBase.getPosition(), armServoMiddle.getPosition());
        List<Double> targetServoAngles = getServoAnglesFromGripperBasePosition(
                x, y, currentServoAngles.get(0), currentServoAngles.get(1));
        double gripperAngle = gripperAngleCalculator(targetServoAngles.get(0), targetServoAngles.get(1));
        List<Double> targetServoPositions = anglesInDegreeToArmServoPositions(
                targetServoAngles.get(0), targetServoAngles.get(1), gripperAngle);
        setArmPosition(targetServoPositions.get(0), targetServoPositions.get(1), targetServoPositions.get(2));
    }

    private void moveTowardsTarget() {
        double currentBase = armServoBase.getPosition();
        double currentMiddle = armServoMiddle.getPosition();
        double currentGripper = gripperServoBase.getPosition();
        Double newArmBasePosition = targetServoBasePosition;
        Double newArmMiddlePosition = targetServoMiddlePosition;
        Double newGripperServoBasePosition = targetServoGripperBasePosition;

        double maxDelta = presetSpeed;

        double baseDiff;
        if (newArmBasePosition != null) {
            baseDiff = newArmBasePosition - currentBase;
            if (Math.abs(baseDiff) > maxDelta) {
                baseDiff = Math.copySign(Math.min(maxDelta, Math.abs(baseDiff)), baseDiff);
            }
            else {
                targetServoBasePosition = null;
            }

        }
        else {
            baseDiff = 0.0;

        }

        double middleDiff;
        if (newArmMiddlePosition != null) {
            middleDiff = newArmMiddlePosition - currentMiddle;
            if (Math.abs(middleDiff) > maxDelta) {
                middleDiff = Math.copySign(Math.min(maxDelta, Math.abs(middleDiff)), middleDiff);
            }
            else {
                targetServoMiddlePosition = null;
            }
        }
        else {
            middleDiff = 0.0;
        }

        double gripperDiff;
        if (newGripperServoBasePosition != null) {
            gripperDiff = newGripperServoBasePosition - currentGripper;
            if (Math.abs(gripperDiff) > maxDelta) {
                gripperDiff = Math.copySign(Math.min(maxDelta, Math.abs(gripperDiff)), gripperDiff);
            }
            else {
                targetServoGripperBasePosition = null;
            }
        }
        else {
            gripperDiff = 0.0;
        }

        gripperServoBase.setPosition(currentGripper + gripperDiff);
        armBaseNewPosition = currentBase + baseDiff;
        armMiddleNewPosition = currentMiddle + middleDiff;
    }

    private void armPositions(String armPos) throws InterruptedException {
        if (armPos.equals(ARM_CODE_REST)) {
//            setArmPosition();
        } else if (armPos.equals(ARM_CODE_PICK_UP)) {
            setArmPosition(0.25, 0.1, 0.5);
        } else if (armPos.equals(ARM_CODE_HOLDING)) {
            setArmPosition(0.5, 0.5, 0.5);
        } else if (armPos.equals(ARM_CODE_READY_FOR_PICK_UP)) {
            setArmXYPosition(9.0, -5.0);
        } else if (armPos.equals(ARM_CODE_PREP_FOR_TRANSPORT)) {
            setArmXYPosition(11.0, 0.0);
        } else if (armPos.equals(ARM_CODE_TRANSPORT)) {
            setArmXYPosition(BASE_ARM_LENGTH_IN_INCH + END_ARM_LENGTH_IN_INCH - 0.1, 0.0);
        }
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

        // cos can tak negative radians
        double x = BASE_ARM_LENGTH_IN_INCH * Math.cos(Math.toRadians(baseServoAngleInDegrees))
                + END_ARM_LENGTH_IN_INCH * Math.cos(Math.toRadians(beta_relative_to_horizontal));

        // sin can tak negative radians
        double y = BASE_ARM_LENGTH_IN_INCH * Math.sin(Math.toRadians(baseServoAngleInDegrees))
                + END_ARM_LENGTH_IN_INCH * Math.sin(Math.toRadians(beta_relative_to_horizontal));

        if(Double.isNaN(x)){
            telemetry.addData("x is bad: " , x);
        }
        if(Double.isNaN(y)){
            telemetry.addData("y is bad: " , y);
        }

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

        if (Double.isNaN(middleServoAngleInDegrees)) {
            telemetry.addData("middleServoAngleInDegrees is bad: ", middleServoAngleInDegrees);

            return Arrays.asList(oldBaseServoAngleInDegrees,  oldMiddleServoAngleInDegrees);
        }

        double gamma = Math.copySign(Math.toDegrees(Math.acos(gripperBaseX / Math.sqrt(r_squared))), gripperBaseY);

        if (Double.isNaN(gamma)) {
            telemetry.addData("gamma is bad: ", gamma);

            return Arrays.asList(oldBaseServoAngleInDegrees,  oldMiddleServoAngleInDegrees);
        }

        double delta = Math.toDegrees(Math.acos((r_squared
                + BASE_ARM_LENGTH_IN_INCH * BASE_ARM_LENGTH_IN_INCH
                - END_ARM_LENGTH_IN_INCH * END_ARM_LENGTH_IN_INCH)
                / (2 * Math.sqrt(r_squared) * BASE_ARM_LENGTH_IN_INCH)));

        telemetry.addData("Gamma: ", gamma);
        telemetry.addData("Delta: ", delta);
        telemetry.addData("middleServoAngleInDegrees: ", middleServoAngleInDegrees);


        double baseServoAngleInDegrees = gamma + delta;
        return Arrays.asList(baseServoAngleInDegrees, middleServoAngleInDegrees);
    }


    private List<Double> anglesInDegreeToArmServoPositions(double baseServoAngleInDegrees,
                                                           double middleServoAngleInDegrees,
                                                           double gripperServoAngleInDegrees) {
        double baseServoPosition = (baseServoAngleInDegrees + 45) / 270;
        double middleServoPosition = (135 - middleServoAngleInDegrees) / 270;
        double gripperServoPosition = gripperAngleToPosition(gripperServoAngleInDegrees);

        return Arrays.asList(baseServoPosition, middleServoPosition, gripperServoPosition);
    }

    private List<Double> getNewGripperBasePosition(double gamepad2_X,
                                                   double gamepad2_Y,
                                                   double gripperBaseX,
                                                   double gripperBaseY) {
//        if (Math.abs(gamepad2_X) > Math.abs(gamepad2_Y)) {
//            gamepad2_Y = 0;
//        } else {
//            gamepad2_X = 0;
//        }

        double newGripperBaseX = gripperBaseX + gamepad2_X * JOYSTICK_TO_GRIPPER_POSITION_FACTOR;
        double newGripperBaseY = gripperBaseY + gamepad2_Y * JOYSTICK_TO_GRIPPER_POSITION_FACTOR;

//        if(gripperBaseY > 16.5){
//            gripperBaseY = 16.5;
//        }
//        if(gripperBaseY < -13){
//            gripperBaseY = -13;
//        }
        if(Math.sqrt(newGripperBaseX * newGripperBaseX + newGripperBaseY * newGripperBaseY) >= (BASE_ARM_LENGTH_IN_INCH + END_ARM_LENGTH_IN_INCH)){
            return Arrays.asList(gripperBaseX, gripperBaseY);
        }


        return Arrays.asList(newGripperBaseX, newGripperBaseY);
    }


    @Override
    public void init() {

        // name all components
        leftDrive = hardwareMap.get(DcMotor.class, "m1");
        rightDrive = hardwareMap.get(DcMotor.class, "m2");
        arm_up_down = hardwareMap.get(DcMotor.class, "m3");

        armServoBase = hardwareMap.get(Servo.class, "s1");
        armServoMiddle = hardwareMap.get(Servo.class, "s2");
        gripperServoBase = hardwareMap.get(Servo.class, "s3");
        gripper = hardwareMap.get(Servo.class, "s6");

        // initialize motion motors
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        arm_up_down.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        armServoMiddle.setDirection(Servo.Direction.REVERSE);
        currentArmLoopState = ArmLoopState.NORMAL;

        // initialize arm position
        try {
            armPositions(ARM_CODE_HOLDING);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gripper.setPosition(0);
    }


    @Override
    public void init_loop() {
    }


    @Override
    public void start() {
        runtime.reset();
    }

    private void motorLoop() {
        double drive = -gamepad1.left_stick_y;
        double turn = -gamepad1.right_stick_x / 1.5;
        leftPower = Range.clip(drive + turn, -1, 1);
        rightPower = Range.clip(drive - turn, -1, 1);

        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
    }

    double armBaseNewPosition = 0.5;
    double armMiddleNewPosition = 0.5;


    private double gripperAngleCalculator(double baseServoAngleInDegrees, double middleServoAngleInDegrees) {
        return Math.min(Math.max(90 + baseServoAngleInDegrees - middleServoAngleInDegrees + 45.0, 0.0), 135.0);
    }

    private double gripperPositionCalculator(double baseServoAngleInDegrees, double middleServoAngleInDegrees){
        double x = 0.3 ;
        double gripperAngle = gripperAngleCalculator(
                baseServoAngleInDegrees, middleServoAngleInDegrees);
        return gripperAngleToPosition(gripperAngle);
    }

    private double gripperAngleToPosition(double angle) {
        return Math.max(Math.min((135.0 - angle) / 270.0, 0.5), 0.0);
    }

    private boolean targetReached() {
        return (targetServoBasePosition == null &&
                targetServoMiddlePosition == null &&
                targetServoGripperBasePosition == null);
    }

    private void armLoop() throws InterruptedException {

        numIterations++;
        telemetry.addData("Num Iterations:", numIterations);

        if ((numIterations % 4000) == 0) {
            // armPositions(ARM_CODE_PICK_UP);
        }
        else if ((numIterations % 4000) == 2000) {
            // armPositions(ARM_CODE_HOLDING);

        }

        telemetry.addData("State: ", currentArmLoopState);
        if (currentArmLoopState == ArmLoopState.RUNNING_PRESET) {
            telemetry.addData("Target Base:", targetServoBasePosition);
            telemetry.addData("Target Middle:", targetServoMiddlePosition);
            telemetry.addData("Target Gripper:", targetServoGripperBasePosition);
            moveTowardsTarget();
            if (targetReached()) {
                currentArmLoopState = ArmLoopState.NORMAL;
            }
            return;
        } else if (currentArmLoopState == ArmLoopState.PREP_FOR_TRANSPORT) {
            telemetry.addData("Target Base:", targetServoBasePosition);
            telemetry.addData("Target Middle:", targetServoMiddlePosition);
            telemetry.addData("Target Gripper:", targetServoGripperBasePosition);
            moveTowardsTarget();
            if (targetReached()) {
                armPositions(ARM_CODE_TRANSPORT);
                currentArmLoopState = ArmLoopState.RUNNING_PRESET;
            }
        } else if (currentArmLoopState == ArmLoopState.PICKING_UP) {
            assert pickupCounter > 0;
            pickupCounter -= 1;
            if (pickupCounter == 0) {
                armPositions(ARM_CODE_PREP_FOR_TRANSPORT);
                currentArmLoopState = ArmLoopState.PREP_FOR_TRANSPORT;
            }
            return;
        }
        assert currentArmLoopState == ArmLoopState.NORMAL;

        if (gamepad2.a) {
            // button A
            // armPositions(ARM_CODE_PICK_UP);
            armPositions(ARM_CODE_READY_FOR_PICK_UP);
            currentArmLoopState = ArmLoopState.RUNNING_PRESET;
            return;
        } else if (gamepad2.b) {
            // button B
            armPositions(ARM_CODE_PREP_FOR_TRANSPORT);
            currentArmLoopState = ArmLoopState.PREP_FOR_TRANSPORT;
            return;
        } else if (gamepad2.y) {
            armPositions(ARM_CODE_HOLDING);
            currentArmLoopState = ArmLoopState.RUNNING_PRESET;
        }

        if (gamepad2.dpad_up) {
           gripperHold = true;
        } else if(gamepad2.dpad_down){
            gripperHold = false;
        }
        if (gripperHold == false) {
            gripper.setPosition(0);
        } else {
            gripper.setPosition(1);
        }


        // logic for gripper base movement - x and y

        double gamepad2_X = gamepad2.right_stick_x / 50;
        double gamepad2_Y = -gamepad2.right_stick_y / 50;

        telemetry.addData("Gamepad2_X", gamepad2_X);
        telemetry.addData("Gamepad2_Y", gamepad2_Y);

        double baseServoPosition = armBaseNewPosition;     //0.4
        double middleServoPosition = armMiddleNewPosition; //0.5
        List<Double> anglesInDegrees = armServoPositionsToAnglesInDegree(baseServoPosition, middleServoPosition);

        double baseServoAngleInDegrees = anglesInDegrees.get(0);    //63
        double middleServoAngleInDegrees = anglesInDegrees.get(1);  //8.1

        double gripperServoTargetAngle = gripperAngleCalculator(baseServoAngleInDegrees, middleServoAngleInDegrees);

        List<Double> gripperBasePosition = getGripperBasePositionFromServoAngles(baseServoAngleInDegrees, middleServoAngleInDegrees);

        double gripperBaseX = gripperBasePosition.get(0); //
        double gripperBaseY = gripperBasePosition.get(1); //

        List<Double> newGripperBasePosition = getNewGripperBasePosition(gamepad2_X, gamepad2_Y, gripperBaseX, gripperBaseY);

        double newGripperBaseX = newGripperBasePosition.get(0);
        double newGripperBaseY = newGripperBasePosition.get(1);

        List<Double> newServoAngles = getServoAnglesFromGripperBasePosition(newGripperBaseX, newGripperBaseY, baseServoAngleInDegrees, middleServoAngleInDegrees);

        double newBaseServoAngle = newServoAngles.get(0);
        double newMiddleServoAngle = newServoAngles.get(1);

        List<Double> newServoPositions = anglesInDegreeToArmServoPositions(newBaseServoAngle, newMiddleServoAngle, gripperServoTargetAngle);
        telemetry.addData("Target base angle:  ", newBaseServoAngle);
        telemetry.addData("Target middle angle:  ", newMiddleServoAngle);
        telemetry.addData("Target gripper angle:  ", gripperServoTargetAngle);
        telemetry.addData("Target gripper position:  ", newServoPositions.get(2));

        armBaseNewPosition = newServoPositions.get(0);
        armMiddleNewPosition = newServoPositions.get(1);
        gripperServoBase.setPosition(newServoPositions.get(2));

//        telemetry.addData("baseServoPosition", baseServoPosition);
//        telemetry.addData("middleServoPosition", middleServoPosition);
//        telemetry.addData("baseServoAngleInDegrees", baseServoAngleInDegrees);
//        telemetry.addData("middleServoAngleInDegrees", middleServoAngleInDegrees);
//        telemetry.addData("newBaseServoAngle", newBaseServoAngle);
//        telemetry.addData("newMiddleServoAngle", newMiddleServoAngle);
//        telemetry.addData("gripperBaseX", gripperBaseX);
//        telemetry.addData("gripperBaseY", gripperBaseY);
//        telemetry.addData("newGripperBaseX", newGripperBaseX);
//        telemetry.addData("newGripperBaseY", newGripperBaseY);
//        telemetry.addData("armBaseNewPosition", armBaseNewPosition);
//        telemetry.addData("armMiddleNewPosition", armMiddleNewPosition);
        telemetry.addData("GripperBoolean", gripperHold);
        telemetry.addData("Position", "(" + newGripperBaseX + ", " + newGripperBaseY + ")");
        telemetry.update();

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

        if (armBaseNewPosition > 1) {
            armBaseNewPosition = 1;
        }

//        if (armBaseNewPosition < 0) {
//            armBaseNewPosition = 0;
//        }
        if (armMiddleNewPosition > 1) {
            armMiddleNewPosition = 1;
        }

        if (armMiddleNewPosition < 0) {
            armMiddleNewPosition = 0;
        }

        armServoBase.setPosition(armBaseNewPosition);
        armServoMiddle.setPosition(armMiddleNewPosition);
    }

    @Override
    public void loop() {


        motorLoop(); // drive train
        try {
            armLoop(); // arm control
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {

    }

}
