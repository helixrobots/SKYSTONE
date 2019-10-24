package com.helix.appl.simulation.armcontrollers;

import com.helix.appl.simulation.Arm;
import com.helix.lib.LibGamepad;

public interface ArmController {

    public void setTargetArmPosition(Arm arm, double position0, double position1, double position2);
    public void setArmForXY(Arm arm, double timeInSeconds);
}
