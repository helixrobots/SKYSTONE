package com.helix.appl.simulation;


public class Servo {
    double myPosition;            // Setting between 0.0 and 1.0
    double myTargetPosition;      // Setting between 0.0 and 1.0
    double myAngularVelocity;
    double myLowerLimit;          // Angle (as a setting between 0.0 and 1.0) for lower limit
    double myUpperLimit;          // Angle (as a setting between 0.0 and 1.00 for upper limit
    double myMaxAngularSpeed;     // Absolute value of maximum angular velocity (as a setting
                                  // between 0.0 and 1.0, in units per second)

    public Servo(double position, double lowerLimit, double upperLimit, double maxAngularSpeed) {
        assert (lowerLimit >= 0.0) && (lowerLimit <= 1.0);
        assert (upperLimit >= 0.0) && (upperLimit <= 1.0);
        assert lowerLimit <= upperLimit;
        assert maxAngularSpeed > 0.0;
        myPosition = position;
        myTargetPosition = position;
        myAngularVelocity = 0.0;
        myLowerLimit = lowerLimit;
        myUpperLimit = upperLimit;
        myMaxAngularSpeed = maxAngularSpeed;
    }

    public Servo(double position, double lowerLimit, double upperLimit) {
        this(position, lowerLimit, upperLimit, 1.0);
    }

    public Servo() {
        this(0.5, 0.0, 1.0);
    }

    public double getPosition() {
        // This attempts to replicate the behavior of the actual physical servo, which returns
        // the last position to which the servo was commanded to move.  The method is unfortunately
        // named, but we keep it this way to be consistent with the physical servo's API.
        return myTargetPosition;
    }

    protected double getCurrentPosition() {
        return myPosition;
    }

    protected double getTargetPosition() {
        return getPosition();
    }

    protected double getCurrentPositionInDegrees() {
        return getCurrentPosition() * 270 - 135;
    }

    protected double getTargetPositionInDegrees() {
        return getTargetPosition() * 270 - 135;
    }

    // Methods to set the position of the servo.  The names of these methods may be slightly
    // misleading because the main one, setPosition, is named to match the API of the physical
    // servo.  These methods actually set a target position, which the servo moves towardd
    // when update is called.

    public void setPosition(double position) {
        assert position >= 0.0 && position <= 1.0;    // Enforce this because we don't really know
                                                      // how the actual servos will react.
        myTargetPosition = Math.min(Math.max(position, myLowerLimit), myUpperLimit);
    }

    private void setCurrentPosition(double position) {
        assert position >= 0.0 && position <= 1.0;
        myPosition = position;
    }

    protected void setTargetPosition(double position) {
        setPosition(position);
    }

    protected void setTargetPositionInDegrees(double positionInDegrees) {
        setPosition((positionInDegrees + 135) / 270.0);
    }

    public void setMaxAngularSpeed(double maxAngularSpeed) {
        assert maxAngularSpeed > 0.0;
        myMaxAngularSpeed = maxAngularSpeed;
    }

    public void update(double timeInSeconds) {
        /*
        System.out.format("Servo data");
        System.out.format("Target and real position: %s %s\n", myTargetPosition, myPosition);
        System.out.format("Max angular speed: %s\n", myMaxAngularSpeed);
        */
        double delta = myTargetPosition - myPosition;
        double myAngularVelocity = Math.copySign(Math.min(Math.abs(delta) / timeInSeconds, myMaxAngularSpeed), delta);
        setCurrentPosition(myPosition + myAngularVelocity * timeInSeconds);
    }
}
