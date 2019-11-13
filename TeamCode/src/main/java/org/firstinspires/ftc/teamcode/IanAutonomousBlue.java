package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Ian Autonomous Blue", group="Helix")
public class IanAutonomousBlue extends IanAutonomousRed {

    @Override
    public int getSetting() {
        return 0;
    }

    @Override
    public void head(double desiredHeading) {
        // Just head in the opposite direction
        super.head(-desiredHeading);
    }

}
