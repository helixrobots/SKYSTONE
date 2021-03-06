package org.firstinspires.ftc.teamcode;

import com.helix.common.PID;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;

import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_BOX;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_BRIDGE;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_CLOSECLAW;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_HEAD;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_MOVE;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_OPENCLAW;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_PAUSE;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_TURNLEFT;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_TURNRIGHT;


public abstract class IanAutonomousBase extends LinearOpMode {

    private static final double ANGLE_TOLERANCE = 3.0;
    public static final int STABILITY_THRESHOLD = 500;
    public static final int MEASURE_THRESHOLD = 10;
    private static final double NO_ANGLE=-360;


    // Arm servos
    private Servo armServoBase = null;
    private Servo armServoMiddle = null;
    private Servo gripperServoBase = null;
    private Servo gripper = null;


    // The IMU sensor object
    BNO055IMU imu;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;

    /* Declare OpMode members. */
    HardwarePushbot robot   = new HardwarePushbot();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 288 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 3.54331 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 1;
    static final double     TURN_SPEED              = 1;

    public void setupSensors() {

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        // Set up our telemetry dashboard
        //composeTelemetry();


    }

    public void setupMotors() {

        armServoBase = hardwareMap.get(Servo.class, "s1");
        armServoMiddle = hardwareMap.get(Servo.class, "s2");
        gripperServoBase = hardwareMap.get(Servo.class, "s3");
        gripper = hardwareMap.get(Servo.class, "s6");

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

//        // Send telemetry message to signify robot waiting;
//        telemetry.addData("Status", "Resetting Encoders");    //
//        telemetry.update();

        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

//        // Send telemetry message to indicate successful Encoder reset
//        telemetry.addData("Path0",  "Starting at %7d :%7d",
//                robot.leftDrive.getCurrentPosition(),
//                robot.rightDrive.getCurrentPosition());
//        telemetry.update();
    }


    //----------------------------------------------------------------------------------------------
    // Telemetry Configuration
    //----------------------------------------------------------------------------------------------

    void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
        {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
        }
        });

        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override public String value() {
                        return imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override public String value() {
                        return imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
                .addData("grvty", new Func<String>() {
                    @Override public String value() {
                        return gravity.toString();
                    }
                })
                .addData("mag", new Func<String>() {
                    @Override public String value() {
                        return String.format(Locale.getDefault(), "%.3f",
                                Math.sqrt(gravity.xAccel*gravity.xAccel
                                        + gravity.yAccel*gravity.yAccel
                                        + gravity.zAccel*gravity.zAccel));
                    }
                });
    }


    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    /*
     *  Method to perfmorm a relative move, based on encoder counts.
     *  Encoders are not reset as the move is based on the current position.
     *  Move will stop if any of three conditions occur:
     *  1) Move gets to the desired position
     *  2) Move runs out of time
     *  3) Driver stops the opmode running.
     */

    public void move(double distance){
        encoderDrive(DRIVE_SPEED, DRIVE_SPEED, distance, distance, 0.5+Math.abs(distance)/15);
    }

    public void encoderDrive(double leftSpeed,double rightSpeed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (isSkynetActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.leftDrive.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.rightDrive.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.leftDrive.setTargetPosition(newLeftTarget);
            robot.rightDrive.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.leftDrive.setPower(Math.abs(leftSpeed));
            robot.rightDrive.setPower(Math.abs(rightSpeed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (isSkynetActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftDrive.isBusy() && robot.rightDrive.isBusy())) {

//                // Display it for the driver.
//                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
//                telemetry.addData("Path2",  "Running at %7d :%7d",
//                        robot.leftDrive.getCurrentPosition(),
//                        robot.rightDrive.getCurrentPosition());
//                telemetry.update();
            }

            // Stop all motion;
            robot.leftDrive.setPower(0);
            robot.rightDrive.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //  sleep(250);   // optional pause after each move
        }
    }

    protected boolean isSkynetActive() {
        return opModeIsActive();
    }

    public abstract int getSetting();

    public PID createPID() {
        int setting=getSetting();
        return new PID(1,0,0,0,CalibrationStore.items[setting][0],CalibrationStore.items[setting][1],CalibrationStore.items[setting][2],CalibrationStore.items[setting][3],CalibrationStore.items[setting][4]);
//        With arm
//        return new PID(1,0,0,0,-0.3,-1,0.3,1,30);
//        Without arm
//        return new PID(1,0,0,0,-0.3,-.5,0.3,.5,30);
    }

    public void head(double desiredHeading) {

        if (desiredHeading>180 || desiredHeading<-180) {
            throw new RuntimeException("I can only head from -180 to 180 degrees");
        }

        // Start the logging of measured acceleration
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        // With arm
        PID rotationPID = createPID();

        rotationPID.reset();

        long stableSince = System.currentTimeMillis();

        boolean flipToPositive=false;
        boolean flipToNegative=false;

        // Loop and update the dashboard
        do  {
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
            double adjustedAngle = angles.firstAngle;


            if (!flipToNegative && adjustedAngle>90) {
                flipToPositive = true;
            }

            if (!flipToPositive && adjustedAngle<-90) {
                flipToNegative = true;
            }

            if (adjustedAngle>0 && adjustedAngle<90) {
                flipToPositive = false;
            }

            if (adjustedAngle<0 && adjustedAngle>-90) {
                flipToNegative = false;
            }

            adjustedAngle = getAdjustedAngle(flipToPositive, flipToNegative, adjustedAngle);

            double distance = rotationPID.calculate(desiredHeading,(double)(adjustedAngle));
            telemetry.addData("Initial Heading",  "%f", desiredHeading);
            telemetry.addData("Current Heading",  "%f", angles.firstAngle);
            telemetry.addData("Adjusted Heading",  "%f, %b , %b", adjustedAngle, flipToPositive, flipToNegative);
            telemetry.addData("Distance/Power",  "%f", distance);
            telemetry.update();

            robot.leftDrive.setPower(distance);
            robot.rightDrive.setPower(-distance);


            // If we are farther than 3 degrees, then reset the time
            if ((Math.abs(desiredHeading - adjustedAngle) > ANGLE_TOLERANCE)) {
                stableSince = System.currentTimeMillis();
            } else {
                robot.leftDrive.setPower(0);
                robot.rightDrive.setPower(0);
            }

        } while ((System.currentTimeMillis()-stableSince)< STABILITY_THRESHOLD);
    }

    private double getAdjustedAngle(boolean flipToPositive, boolean flipToNegative, double adjustedAngle) {
        if (flipToPositive) {
            if (adjustedAngle<0) {
                adjustedAngle = 360 + adjustedAngle;
            }
        }

        if (flipToNegative) {
            if (adjustedAngle>0) {
                adjustedAngle = adjustedAngle - 360;
            }
        }
        return adjustedAngle;
    }

    public void openClaw() {
        robot.leftClaw.setPosition(1);
        robot.rightClaw.setPosition(0);
    }

    public void closeClaw() {
        robot.leftClaw.setPosition(0);
        robot.rightClaw.setPosition(1);
        sleep(1000);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        setupSensors();

        setupMotors();

        CalibrationStore.load();

        moveToBoxPosition();

        // Wait until we're told to go
        waitForStart();

//        moveToBridgePosition();

        execute();


    }

    protected void moveToBoxPosition() {
        armServoBase.setPosition(0.9);
        armServoMiddle.setPosition(1.0);
        gripperServoBase.setPosition(0.35);
        gripper.setPosition(1);
    }

    protected void moveToBridgePosition() {
        armServoBase.setPosition(0.8);
        armServoMiddle.setPosition(0.33);
        gripperServoBase.setPosition(0.5);
        gripper.setPosition(1);
    }


    public void turn(double degrees, boolean direction, double wheelRatio) {

        if (degrees>180 || degrees<-180) {
            throw new RuntimeException("I can only head from -180 to 180 degrees");
        }

        // Start the logging of measured acceleration
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

        // With arm
        PID rotationPID = createPID();

        rotationPID.reset();

        long stableSince = System.currentTimeMillis();

        boolean flipToPositive=false;
        boolean flipToNegative=false;

        double initialHeading = NO_ANGLE;
        // Loop and update the dashboard
        do  {
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
            double adjustedAngle = angles.firstAngle;


            if (!flipToNegative && adjustedAngle>90) {
                flipToPositive = true;
            }

            if (!flipToPositive && adjustedAngle<-90) {
                flipToNegative = true;
            }

            if (adjustedAngle>0 && adjustedAngle<90) {
                flipToPositive = false;
            }

            if (adjustedAngle<0 && adjustedAngle>-90) {
                flipToNegative = false;
            }

            adjustedAngle = getAdjustedAngle(flipToPositive, flipToNegative, adjustedAngle);

            if (initialHeading == NO_ANGLE) {
                initialHeading = adjustedAngle;
                if (!direction) {
                    degrees=-degrees;
                }
            }

            double degreesTraveled = adjustedAngle - initialHeading;



            double distance = rotationPID.calculate(degrees,degreesTraveled);
            telemetry.addData("Initial Heading",  "%f", initialHeading);
            telemetry.addData("Adjusted Heading",  "%f", adjustedAngle);
            telemetry.addData("Desired Degrees",  "%f", degrees);
            telemetry.addData("Degrees Traveled",  "%f", degreesTraveled);
            telemetry.addData("Distance/Power",  "%f", distance);
            telemetry.update();

            if (direction) {
                robot.leftDrive.setPower(distance);
                robot.rightDrive.setPower(distance / wheelRatio);
            } else {
                robot.leftDrive.setPower(-distance/wheelRatio);
                robot.rightDrive.setPower(-distance);

            }

            // If we are farther than 3 degrees, then reset the time
            if ((Math.abs(degrees - degreesTraveled) > ANGLE_TOLERANCE)) {
                stableSince = System.currentTimeMillis();
            } else {
                robot.leftDrive.setPower(0);
                robot.rightDrive.setPower(0);
            }

        } while ((System.currentTimeMillis()-stableSince)< STABILITY_THRESHOLD);
    }

    public void turnLeft(double degrees) {
        turn(degrees,true,3);
    }

    public void turnRight(double degrees) {
        turn(degrees,false,3);
    }

    public abstract void execute();

    public void execute(ProgramStore.Instruction instruction) {
        switch (instruction.opCode) {
            case OPCODE_MOVE: {
                move(instruction.parameter);
                break;
            }
            case OPCODE_TURNLEFT: {
                turnLeft(instruction.parameter);
                break;
            }
            case OPCODE_TURNRIGHT: {
                turnRight(instruction.parameter);
                break;
            }
            case OPCODE_HEAD: {
                head(instruction.parameter);
                break;
            }
            case OPCODE_OPENCLAW: {
                openClaw();
                break;
            }
            case OPCODE_CLOSECLAW: {
                closeClaw();
                break;
            }
            case OPCODE_BOX: {
                moveToBoxPosition();
                break;
            }
            case OPCODE_BRIDGE: {
                moveToBridgePosition();
                break;
            }
            case OPCODE_PAUSE: {
                sleep((int)instruction.parameter);
                break;
            }

        }

    }
}
