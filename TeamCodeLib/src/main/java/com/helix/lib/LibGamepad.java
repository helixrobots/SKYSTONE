package com.helix.lib;

import com.helix.lib.ftccomponentinterfaces.GamepadInterface;

public class LibGamepad implements GamepadInterface {
    public float left_stick_x = 0.0f;
    public float left_stick_y = 0.0f;
    public float right_stick_x = 0.0f;
    public float right_stick_y = 0.0f;
    public boolean a = false;
    public boolean b = false;

    @Override
    public float getLeftStickX() {
        return left_stick_x;
    }

    @Override
    public float getLeftStickY() {
        return left_stick_y;
    }

    @Override
    public float getRightStickX() {
        return right_stick_x;
    }

    @Override
    public float getRightStickY() {
        return right_stick_y;
    }
}
