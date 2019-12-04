package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Autonomous(name="Ian Autonomous Blue Light", group="Helix")
@Disabled
public class IanAutonomousBlue_Light extends IanAutonomousBlue {

    @Override
    public int getSetting() {
        return 1;
    }
}
