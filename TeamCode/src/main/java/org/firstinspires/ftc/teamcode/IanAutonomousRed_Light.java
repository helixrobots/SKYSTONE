package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="Ian Autonomous Red Light", group="Helix")
@Disabled
public class IanAutonomousRed_Light extends IanAutonomousRed {

    @Override
    public int getSetting() {
        return 1;
    }
}
