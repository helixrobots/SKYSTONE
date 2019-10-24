package com.helix.lib.ftccomponentinterfaces;

public interface GamepadInterface {
    /**
     * left analog stick horizontal axis
     */
    public float left_stick_x = 0f;

    /**
     * left analog stick vertical axis
     */
    public float left_stick_y = 0f;

    /**
     * right analog stick horizontal axis
     */
    public float right_stick_x = 0f;

    /**
     * right analog stick vertical axis
     */
    public float right_stick_y = 0f;

    /**
     * dpad up
     */
    public boolean dpad_up = false;

    /**
     * dpad down
     */
    public boolean dpad_down = false;

    /**
     * dpad left
     */
    public boolean dpad_left = false;

    /**
     * dpad right
     */
    public boolean dpad_right = false;

    /**
     * button a
     */
    public boolean a = false;

    /**
     * button b
     */
    public boolean b = false;

    /**
     * button x
     */
    public boolean x = false;

    /**
     * button y
     */
    public boolean y = false;


}
