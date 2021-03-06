package org.firstinspires.ftc.teamcode;

public class SkynetT1 extends IanAutonomousBase {

    @Override
    public int getSetting() {
        return 0;
    }

    @Override
    public void execute() {

    }

    @Override
    public void runOpMode() throws InterruptedException {
        // Just load the calibration data
        CalibrationStore.load();
    }

    @Override
    protected boolean isSkynetActive() {
        // Pretend we are active so that we can control it interactively
        return true;
    }
}
