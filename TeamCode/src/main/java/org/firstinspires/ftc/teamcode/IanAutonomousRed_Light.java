package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Ian Autonomous Red Light", group="Helix")
public class IanAutonomousRed_Light extends IanAutonomousRed {

    @Override
    public int getSetting() {
        return 1;
    }
}
