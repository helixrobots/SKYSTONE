package org.firstinspires.ftc.teamcode.pure;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name="Ashray Pure Pursuit Auto", group="Helix")

public class Robot extends OpMode{
    public static double worldXPosition;
    public static double worldYPosition;
    public static double worldAngle_rad;
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

    @Override
    public void loop() {

        x = -movement_x;
        r = -movement_y;
        y = movement_turn;


        TopLeft.setPower(y + r + x);
        BottomLeft.setPower(y + r - x);
        BottomRight.setPower(y - r - x);
        TopRight.setPower(y - r + x);


    }

    public static double getXPos() {
        return worldXPosition;
    }

    public static double getYPos() {
        return worldYPosition;
    }

    public Robot(){
        worldXPosition = 50;
        worldYPosition = 140;
        worldAngle_rad = Math.toRadians(-180);
    }

    public static double movement_x;
    public static double movement_y;
    public static double movement_turn;




}
