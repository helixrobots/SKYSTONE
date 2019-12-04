package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Ian Autonomous Skynet Blue", group="Helix")
public class IanAutonomousSkynetBlue extends IanAutonomousSkynetRed {

    @Override
    public void head(double desiredHeading) {
        // Just head in the opposite direction
        super.head(-desiredHeading);
    }
}
