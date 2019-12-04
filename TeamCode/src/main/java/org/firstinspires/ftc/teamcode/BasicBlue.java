package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Basic: Iterative Blue", group = "Iterative Opmode")
public class BasicBlue extends AshrayBasicOpMode_Iterative {

    protected double invert(float right_stick_x) {
        return -right_stick_x;
    }

}
