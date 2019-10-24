package com.helix.appl.simulation.armcontrollers;

import com.helix.appl.simulation.Arm;
import com.helix.appl.simulation.Gamepad;
import com.helix.appl.simulation.Servo;
import com.helix.appl.simulation.Telemetry;
import com.helix.lib.AshrayOpModeAlgos;
import com.helix.lib.LibGamepad;

import java.util.Arrays;
import java.util.List;

public class AshrayArmController implements ArmController {

    final String ARM_CODE_PICK_UP = "pick up";
    final String ARM_CODE_HOLDING = "hold";
    final String ARM_CODE_REST = "rest";
    static final double JOYSTICK_TO_GRIPPER_POSITION_FACTOR = 2.5;
    AshrayOpModeAlgos myAlgos;

    public AshrayArmController(Arm arm, LibGamepad gamepad1, LibGamepad gamepad2) {
        myAlgos = new AshrayOpModeAlgos(
                gamepad1,
                gamepad2,
                arm.getServo(0),
                arm.getServo(1),
                arm.getServo(2),
                new Servo(),
                new Telemetry(),
                ARM_CODE_PICK_UP,
                ARM_CODE_HOLDING,
                ARM_CODE_REST,
                arm.getArmLength(0),
                arm.getArmLength(1),
                JOYSTICK_TO_GRIPPER_POSITION_FACTOR);
    }

    @Override
    public void setTargetArmPosition(Arm arm, double position0, double position1, double position2) {
        System.out.println("In setTargetArmPosition");
        myAlgos.runArmLoop();
    }

    public void setArmForXY(Arm arm, double timeInSeconds) {
        System.out.println("In setArmForXY");

        myAlgos.runArmLoop();
        // setArmServosForXY(controllerX, controllerY, arm.getServo(0), arm.getServo(1));
    }
}
