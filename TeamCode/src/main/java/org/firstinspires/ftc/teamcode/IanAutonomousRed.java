package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Ian Autonomous Red", group="Helix")
public class IanAutonomousRed extends IanAutonomousBlue {

    @Override
    public int getSetting() {
        return 0;
    }

    @Override
    public void turn(double desiredHeading) {
        // Just turn in the opposite direction
        super.turn(-desiredHeading);
    }

}
