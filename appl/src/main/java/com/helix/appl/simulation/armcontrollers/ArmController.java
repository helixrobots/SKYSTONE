package com.helix.appl.simulation.armcontrollers;

import com.helix.appl.simulation.Arm;

public interface ArmController {

    public void setArmForXY(Arm arm, double controllerX, double controllerY, double timeInSeconds);
}
