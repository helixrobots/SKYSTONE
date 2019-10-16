package com.helix.appl.simulation.armcontrollers;

import com.helix.appl.simulation.Arm;

public interface ArmController {

    static final double BASE_ARM_LENGTH_IN_INCH = 10;
    static final double END_ARM_LENGTH_IN_INCH = 12;
    static final double JOYSTICK_TO_GRIPPER_POSITION_FACTOR = 2.5;


    public void setArmForXY(Arm arm, double controllerX, double controllerY, double timeInSeconds);
}
