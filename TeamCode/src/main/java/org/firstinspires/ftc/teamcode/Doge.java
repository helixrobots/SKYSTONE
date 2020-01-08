package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Doge", group = "Sensor")

public class Doge extends LinearOpMode {
    private DcMotor TopLeft = null;
    private DcMotor TopRight = null;
    private DcMotor BottomLeft = null;
    private DcMotor BottomRight = null;
    public double x;
    public double y;
    public double r;
    public double facingAngle; //0 is forward

    @Override
    public void runOpMode() {

        TopLeft = hardwareMap.get(DcMotor.class, "TL");
        TopRight = hardwareMap.get(DcMotor.class, "TR");
        BottomLeft = hardwareMap.get(DcMotor.class, "BL");
        BottomRight = hardwareMap.get(DcMotor.class, "BR");

        waitForStart();

        double angle = 90;
        double times = 0;

        while (opModeIsActive()) {

            Force direction = AngleWork.forceCalc(angle);

            x = (direction.getX()) / 3.0;
            y = -(direction.getY()) / 6.0;
            r = (0.0) / 3.0;

            double TL = r - y - x;
            double BL = r - y + x;
            double BR = r + y + x;
            double TR = r + y - x;

            Powers anglePow = new Powers(TL, BL, BR, TR);


            TopLeft.setPower(anglePow.getTL());
            BottomLeft.setPower(anglePow.getBL());
            BottomRight.setPower(anglePow.getBR());
            TopRight.setPower(anglePow.getTR());

            telemetry.addData("x: ", x);
            telemetry.addData("y: ", y);
            telemetry.update();

//            times+= 0.0003;

//            angle=Math.sin(times * Math.PI) * 90;

//            angle += 0.02;
//            angle = angle % 90;
        }
    }
}
