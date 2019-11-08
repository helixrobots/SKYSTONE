package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Ian Autonomous Blue", group="Helix")
public class IanAutonomousBlue extends IanAutonomousBase {

    @Override
    public int getSetting() {
        return 0;
    }

    @Override
    public void execute() {
        openClaw();
        move(-28.5);
        closeClaw();
        move(20);
        openClaw();
        move(4);
        turn(-90);
        move(30);
        turn(-170);
        move(50);
        turn(-90);
        move(-55);
        turn(0);
        move(33);
        move(-5);
        // Not quite 90 degrees so we get closer to the center of the bridge when we move
        turn(-80);
        move(40);

    }
}
