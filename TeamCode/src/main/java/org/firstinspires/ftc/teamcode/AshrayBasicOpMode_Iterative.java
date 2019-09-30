/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
//        serv1.setDirection(Servo.Direction.FORWARD);
//        serv2.setDirection(Servo.Direction.REVERSE);


    }


    @Override
    public void init_loop() {
    }


    @Override
    public void start() {
        runtime.reset();
    }


    @Override
    public void loop() {


        //conversion rate = 4

        double drive       =  -gamepad1.left_stick_y;
        double turn        =  -gamepad1.right_stick_x/1.5;
        double armPower    =  -gamepad2.right_stick_y/1.75;

        boolean claw;
        double total_turn = 0;
        total_turn        +=  armPower;

        if(gamepad2.a) {
            if (claw = false) {
                claw = gamepad2.a;
            } else if (claw = true) {
                claw = !gamepad2.a;
            }
        }

        if(claw = true){
            serv1.setPosition(1);
        }
        if(claw = false){
            serv1.setPosition(0);
        }


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
