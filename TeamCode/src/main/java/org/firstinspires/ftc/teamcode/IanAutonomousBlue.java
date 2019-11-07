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
        move(23.5);
        openClaw();
        move(4);
        turn(-90);
        move(30);
        turn(-170);
        move(45);
        turn(-90);
        move(-40);
        turn(0);
        move(33);
        move(-5);
        turn(-90);
        move(40);

    }
}
