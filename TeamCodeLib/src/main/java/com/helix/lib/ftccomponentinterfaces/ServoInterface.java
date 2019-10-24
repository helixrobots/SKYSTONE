package com.helix.lib.ftccomponentinterfaces;

public interface ServoInterface {

    /**
     * The minimum allowable position to which a servo can be moved
     * @see #setPosition(double)
     */
    double MIN_POSITION = 0.0;
    /**
     * The maximum allowable position to which a servo can be moved
     * @see #setPosition(double)
     */
    double MAX_POSITION = 1.0;


    /**
     * Servos can be configured to internally reverse the values
     * to which their positioning power is set. This makes it easy, e.g.,
     * to have cooperating servos on two sides of a robot arm:
     * one would be set at at forward, the other at reverse, and the
     * difference between the two in that respect could be thereafter ignored.
     *
     * <p>At the start of an OpMode, servos are guaranteed to be in the forward direction.</p>
     *
     * @see #setDirection(Direction)
     */
    enum Direction { FORWARD, REVERSE }

    /**
     * Sets the logical direction in which this servo operates.
     * @param direction the direction to set for this servo
     *
     * @see #getDirection()
     * @see com.qualcomm.robotcore.hardware.Servo.Direction
     */
    void setDirection(Direction direction);

    /**
     * Returns the current logical direction in which this servo is set as operating.
     * @return the current logical direction in which this servo is set as operating.
     * @see #setDirection(Direction)
     */
    Direction getDirection();

    /**
     * Sets the current position of the servo, expressed as a fraction of its available
     * range. If PWM power is enabled for the servo, the servo will attempt to move to
     * the indicated position.
     *
     * @param position the position to which the servo should move, a value in the range [0.0, 1.0]
     * @see ServoController#pwmEnable()
     * @see #getPosition()
     */
    void setPosition(double position);

    /**
     * Returns the position to which the servo was last commanded to move. Note that this method
     * does NOT read a position from the servo through any electrical means, as no such electrical
     * mechanism is, generally, available.
     * @return the position to which the servo was last commanded to move, or Double.NaN
     *         if no such position is known
     * @see #setPosition(double)
     * @see Double#NaN
     * @see Double#isNaN()
     */
    double getPosition();

    /**
     * Scales the available movement range of the servo to be a subset of its maximum range. Subsequent
     * positioning calls will operate within that subset range. This is useful if your servo has
     * only a limited useful range of movement due to the physical hardware that it is manipulating
     * (as is often the case) but you don't want to have to manually scale and adjust the input
     * to {@link #setPosition(double) setPosition()} each time.
     *
     * <p>For example, if scaleRange(0.2, 0.8) is set; then servo positions will be
     * scaled to fit in that range:<br>
     * setPosition(0.0) scales to 0.2<br>
     * setPosition(1.0) scales to 0.8<br>
     * setPosition(0.5) scales to 0.5<br>
     * setPosition(0.25) scales to 0.35<br>
     * setPosition(0.75) scales to 0.65<br>
     * </p>
     *
     * <p>Note the parameters passed here are relative to the underlying full range of motion of
     * the servo, not its currently scaled range, if any. Thus, scaleRange(0.0, 1.0) will reset
     * the servo to its full range of movement.</p>
     *
     * @param min    the lower limit of the servo movement range, a value in the interval [0.0, 1.0]
     * @param max    the upper limit of the servo movement range, a value in the interval [0.0, 1.0]
     *
     * @see #setPosition(double)
     */
    void scaleRange(double min, double max);


}
