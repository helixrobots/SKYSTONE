package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Ian Autonomous Skynet Blue", group="Helix")
public class IanAutonomousSkynetBlue extends IanAutonomousSkynet {

    @Override
    public void turn(double desiredHeading) {
        // Just turn in the opposite direction
        super.turn(-desiredHeading);
    }
}
