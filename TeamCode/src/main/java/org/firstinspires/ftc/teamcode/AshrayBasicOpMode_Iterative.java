
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Basic: Iterative OpMode", group="Iterative Opmode")

public class AshrayBasicOpMode_Iterative extends OpMode
{

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor arm_up_down = null;
    private Servo serv1 = null;
    private Servo serv2 = null;
    double leftPower;
    double rightPower;



    @Override
    public void init() {

        leftDrive   = hardwareMap.get(DcMotor.class, "m1");
        rightDrive  = hardwareMap.get(DcMotor.class, "m2");
        arm_up_down = hardwareMap.get(DcMotor.class, "m3");
        serv1       = hardwareMap.get(Servo.class, "s1");
        serv2       = hardwareMap.get(Servo.class, "s2");

        serv1.setPosition(0.0);
        serv2.setPosition(0.0);

        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        arm_up_down.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        serv1.setDirection(Servo.Direction.FORWARD);
        serv2.setDirection(Servo.Direction.REVERSE);


    }


    @Override
    public void init_loop() {
    }


    @Override
    public void start() {
        runtime.reset();
        serv1.setPosition(0.0);
        serv2.setPosition(0.0);
    }


    @Override
    public void loop() {





        telemetry.addData("Servo1 Position", serv1.getPosition());
        telemetry.addData("Servo2 Position", serv2.getPosition());
        telemetry.update();
        double drive       =  -gamepad1.left_stick_y;
        double turn        =  -gamepad1.right_stick_x/1.5;
        double armPower    =  -gamepad2.right_stick_y/1.75;

        boolean claw = false;
//        double total_turn = 0;
//        total_turn        +=  armPower;


//        if (claw = false) {
//            claw = gamepad2.a;
//        } else if (claw = true) {
//            claw = !gamepad2.a;
//        }
//
//
//        if(claw = true){
//            serv1.setPosition(1);
//        }
//        if(claw = false){
//            serv1.setPosition(0);
//        }

        serv1.setPosition(/*serv1.getPosition() + */gamepad2.left_stick_x);
        serv2.setPosition(/*serv2.getPosition() + */gamepad2.left_stick_x);

        leftPower  = Range.clip(drive+turn , -1,1);
        rightPower = Range.clip(drive-turn , -1,1);




        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
        arm_up_down.setPower(armPower);

//        if(leftDrive.getCurrentPosition() <= ){

//        }
    }

    @Override
    public void stop() {

    }

}
