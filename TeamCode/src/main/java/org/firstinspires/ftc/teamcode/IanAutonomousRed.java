package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Ian Autonomous Red", group="Helix")
public class IanAutonomousRed extends IanAutonomousBase {

    @Override
    public int getSetting() {
        return 0;
    }

    @Override
    public void execute() {
        openClaw();
        move(-30);
        closeClaw();
        move(21.5);
        openClaw();
        move(4);
        head(-90);
        move(30);
        head(-180);
        move(35);
        head(90);
        move(45);
        head(0);
        move(35);
        move(-5);
        // Not quite 90 degrees so we get closer to the center of the bridge when we move
        head(-80);
        move(40);

    }
}
