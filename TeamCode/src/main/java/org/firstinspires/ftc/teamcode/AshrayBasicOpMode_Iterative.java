
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.util.Arrays;
import java.util.List;


@TeleOp(name = "Basic: Iterative OpMode", group = "Iterative Opmode")

public class AshrayBasicOpMode_Iterative extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor arm_up_down = null;
    private Servo armServoBase = null;
    private Servo armServoMiddle = null;
    private Servo gripperServoBase = null;
    double leftPower;
    double rightPower;
    final String ARM_CODE_PICK_UP = "pick up";
    final String ARM_CODE_HOLDING = "hold";
    final String ARM_CODE_REST = "rest";
    final int BASE_ARM_LENGTH_IN_INCH = 10;
    final int END_ARM_LENGTH_IN_INCH = 12;
    final double JOYSTICK_TO_GRIPPER_POSITION_FACTOR = 2.5;


    private void setArmPosition(double base, double middle, double gripperBase) {
        armServoBase.setPosition(base);
        armServoMiddle.setPosition(middle);
        gripperServoBase.setPosition(gripperBase);
    }

    private List<Double> armServoPositionsToAnglesInDegree(double baseServoPosition,
                                                           double middleServoPosition) {
        double baseServoAngleInDegrees = baseServoPosition * 270 - 45;
        double middleServoAngleInDegrees = -(135 - 270 * middleServoPosition);

        return Arrays.asList(baseServoAngleInDegrees, middleServoAngleInDegrees);
    }

    private List<Double> getGripperBasePositionFromServoAngles(double baseServoAngleInDegrees,
                                                               double middleServoAngleInDegrees) {
        double beta_relative_to_horizontal = baseServoAngleInDegrees + middleServoAngleInDegrees;

        double x = BASE_ARM_LENGTH_IN_INCH * Math.cos(Math.toRadians(baseServoAngleInDegrees))
                + END_ARM_LENGTH_IN_INCH * Math.cos(Math.toRadians(beta_relative_to_horizontal));

        double y = BASE_ARM_LENGTH_IN_INCH * Math.sin(Math.toRadians(baseServoAngleInDegrees))
                + END_ARM_LENGTH_IN_INCH * Math.sin(Math.toRadians(beta_relative_to_horizontal));

        return Arrays.asList(x, y);
    }

    private List<Double> getServoAnglesFromGripperBasePosition(double gripperBaseX,
                                                               double gripperBaseY) {
        double r_squared = gripperBaseX * gripperBaseX + gripperBaseY * gripperBaseY;
        double middleServoAngleInDegrees = Math.toDegrees(-Math.acos((r_squared
                - BASE_ARM_LENGTH_IN_INCH * BASE_ARM_LENGTH_IN_INCH
                - END_ARM_LENGTH_IN_INCH * END_ARM_LENGTH_IN_INCH)
                / (2 * BASE_ARM_LENGTH_IN_INCH * END_ARM_LENGTH_IN_INCH)));
        double gamma = Math.atan(gripperBaseY / gripperBaseX);
        double delta = Math.toDegrees(-Math.acos((r_squared
                + BASE_ARM_LENGTH_IN_INCH * BASE_ARM_LENGTH_IN_INCH
                - END_ARM_LENGTH_IN_INCH * END_ARM_LENGTH_IN_INCH)
                / (2 * Math.sqrt(r_squared) * BASE_ARM_LENGTH_IN_INCH)));
        double baseServoAngleInDegrees = gamma - delta;
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
        return Arrays.asList(gripperBaseX, gripperBaseY);
    }

    private void armPositions(String armPos) {
        if (armPos.equals(ARM_CODE_REST)) {
//            setArmPosition();

        } else if (armPos.equals(ARM_CODE_PICK_UP)) {
            setArmPosition(0.1, 0.9, 0.5);

        } else if (armPos.equals(ARM_CODE_HOLDING)) {
            setArmPosition(0.5, 0.5, 0.5);
        }
    }


    @Override
    public void init() {

        // name all compoents
        leftDrive = hardwareMap.get(DcMotor.class, "m1");
        rightDrive = hardwareMap.get(DcMotor.class, "m2");
        arm_up_down = hardwareMap.get(DcMotor.class, "m3");

        armServoBase = hardwareMap.get(Servo.class, "s1");
        armServoMiddle = hardwareMap.get(Servo.class, "s2");
        gripperServoBase = hardwareMap.get(Servo.class, "s3");

        // initialize motion motors
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        arm_up_down.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

        // initialize arm position
//        armPositions(ARM_CODE_HOLDING);
    }


    @Override
    public void init_loop() {
    }


    @Override
    public void start() {
        runtime.reset();
        // todo: ashray - verify start is not resetting things from init()
    }

    private void motorLoop() {
        double drive = -gamepad1.left_stick_y;
        double turn = -gamepad1.right_stick_x / 1.5;
        leftPower = Range.clip(drive + turn, -1, 1);
        rightPower = Range.clip(drive - turn, -1, 1);

        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
    }

    private void armLoop() {

        if (gamepad2.a) {

            // button A
            armPositions(ARM_CODE_PICK_UP);
            return;
        } else if (gamepad2.b) {

            // button B
            armPositions(ARM_CODE_HOLDING);
            return;
        }

        // logic for gripper base movement - x and y

        double gamepad2_X = gamepad2.right_stick_x;
        double gamepad2_Y = gamepad2.right_stick_y;

        double baseServoPosition = armServoBase.getPosition();
        double middleServoPosition = armServoMiddle.getPosition();
        List<Double> anglesInDegrees = armServoPositionsToAnglesInDegree(baseServoPosition, middleServoPosition);

        double baseServoAngleInDegrees = anglesInDegrees.get(0);
        double middleServoAngleInDegrees = anglesInDegrees.get(1);

        List<Double> gripperBasePosition = getGripperBasePositionFromServoAngles(baseServoAngleInDegrees, middleServoAngleInDegrees);

        double gripperBaseX = gripperBasePosition.get(0);
        double gripperBaseY = gripperBasePosition.get(1);

        List<Double> newGripperBasePosition = getNewGripperBasePosition(gamepad2_X, gamepad2_Y, gripperBaseX,gripperBaseY);

        double newGripperBaseX = newGripperBasePosition.get(0);
        double newGripperBaseY = newGripperBasePosition.get(1);

        List<Double> newServoAngles = getServoAnglesFromGripperBasePosition(newGripperBaseX, newGripperBaseY);

        double newBaseServoAngle = newServoAngles.get(0);
        double newMiddleServoAngle = newServoAngles.get(1);

        List<Double> newServoPositions = anglesInDegreeToArmServoPositions(newBaseServoAngle, newMiddleServoAngle);

        double armBaseNewPosition = newServoPositions.get(0);
        double armMiddleNewPosition = newServoPositions.get(1);

        if (armBaseNewPosition > 0.5) {
            armBaseNewPosition = 0.5;
        }

        if (armMiddleNewPosition < 0.5) {
            armMiddleNewPosition = 0.5;
        }

        if (armMiddleNewPosition > 0.833) {
            armMiddleNewPosition = 0.833;
        }

        armServoBase.setPosition(armBaseNewPosition);
        armServoMiddle.setPosition(armMiddleNewPosition);
    }

    @Override
    public void loop() {

        telemetry.addData("Servo1 Position", armServoBase.getPosition());
        telemetry.addData("Servo2 Position", armServoMiddle.getPosition());
        telemetry.update();

        motorLoop();
        armLoop();
    }

    @Override
    public void stop() {

    }

}
