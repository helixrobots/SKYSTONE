package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Mecanum Drive", group = "drive")


public class MecanumDrive extends OpMode {


    private DcMotor TopLeft = null;
    private DcMotor TopRight = null;
    private DcMotor BottomLeft = null;
    private DcMotor BottomRight = null;
    private Servo leftClaw = null;
    private Servo rightClaw = null;
    public double x;
    public double y;
    public double r;

    @Override
    public void init() {

        TopLeft = hardwareMap.get(DcMotor.class, "TL");
        TopRight = hardwareMap.get(DcMotor.class, "TR");
        BottomLeft = hardwareMap.get(DcMotor.class, "BL");
        BottomRight = hardwareMap.get(DcMotor.class, "BR");
        leftClaw = hardwareMap.get(Servo.class, "s4");
        rightClaw = hardwareMap.get(Servo.class, "s5");

    }

    private void openClaw() {
        leftClaw.setPosition(1);
        rightClaw.setPosition(0);
    }

    private void closeClaw() {
        leftClaw.setPosition(0);
        rightClaw.setPosition(1);
    }



    @Override
    public void loop() {

        x = gamepad1.left_stick_x;
        y = gamepad1.left_stick_y;
        r = gamepad1.right_stick_x;


        TopLeft.setPower(r - y - x);
        BottomLeft.setPower(r - y + x);
        BottomRight.setPower(r + y + x);
        TopRight.setPower(r + y - x);


        if (gamepad1.right_bumper) {
            closeClaw();
        }
        if (gamepad1.left_bumper) {
            openClaw();
        }

        

    }
}
