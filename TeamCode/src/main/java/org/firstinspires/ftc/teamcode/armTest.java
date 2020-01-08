package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;


import java.lang.*;


@TeleOp(name = "armTest", group = "Iterative Opmode")

public class armTest extends OpMode {
    private Servo armServoBase = null;
    private Servo armServoMiddle = null;
    private Servo gripper = null;

    @Override
    public void init() {
        armServoBase = hardwareMap.get(Servo.class, "s1");
        armServoMiddle = hardwareMap.get(Servo.class, "s2");
        gripper = hardwareMap.get(Servo.class, "s6");

        armServoBase.setPosition(0.5);
        armServoMiddle.setPosition(0.5);

        gripper.setPosition(0);
    }

    @Override
    public void init_loop() {
        super.init_loop();
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
    }
    //0.49500 - up bottom
    //0.50333 - up middle
    //0.84166 - 90 middle
    //
    @Override
    public void loop() {

        boolean gripperHold = false;
        if (gamepad2.x) {
            if (gripperHold == true) {
                gripperHold = false;
            } else {
                gripperHold = true;
            }
        }
        if (gripperHold == false) {
            gripper.setPosition(0);
        } else {
            gripper.setPosition(1);
        }


        double x = gamepad2.right_stick_y/10000;
        double y = gamepad2.left_stick_y/10000;

        armServoBase.setPosition(armServoBase.getPosition() + x);
        armServoMiddle.setPosition(armServoMiddle.getPosition() + y);

        telemetry.addData("ArmBasePos: ", armServoBase.getPosition());
        telemetry.addData("MiddleBasePos: ", armServoMiddle.getPosition());
//
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }
}
