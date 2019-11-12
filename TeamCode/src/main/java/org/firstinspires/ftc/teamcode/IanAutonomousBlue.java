package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Ian Autonomous Blue", group="Helix")
public class IanAutonomousBlue extends IanAutonomousRed {

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
