package org.firstinspires.ftc.teamcode;

/**
 * This code implements a simple PID algorithm
 */
public class PID {

    private final double minReversePower;
    private final double maxReversePower;
    private final double minForwardPower;
    private final double maxForwardPower;
    /**
     * The Ks for every PID element
     */
    private double Kp=1;
    private double Ki=0;
    private double Kd=0;

    private double bias = 0;

    /**
     * The range of power
     */


    private double error;
    private double integral = 0;
    private double derivative = 0;

    private double previousError=0;

    private long elapsed;

    private long lastTime = -1;

    private double maxDelta = 0;

    /**
     *
     * @param kp The Proportional Constant
     * @param ki The Integral Constant
     * @param kd The Derivative Constant
     * @param bias This can be set to a very low value or even zero? (0.001?)
     * @param minReversePower The Min power that will make the motor turn in reverse
     * @param maxReversePower The Max power that will make the motor turn in reverse the fastest
     * @param minForwardPower Tee Min power that will make the motor turn forward
     * @param maxForwardPower The Max power that will make the robot turn forward the fastest
     * @param maxDelta The max delta between the error and the target (if the difference between the target and error is more than this delta, max power will be used)
     */
    public PID(double kp, double ki, double kd, double bias, double minReversePower, double maxReversePower, double minForwardPower, double maxForwardPower, double maxDelta) {
        Kp = kp;
        Ki = ki;
        Kd = kd;
        this.minReversePower = minReversePower;
        this.maxReversePower = maxReversePower;
        this.minForwardPower = minForwardPower;
        this.maxForwardPower = maxForwardPower;
        this.bias = bias;
        this.maxDelta = maxDelta;
    }

    public void reset() {
        lastTime = -1;
        previousError = 0;
        integral = 0;
    }

    public double calculate(double desired, double current) {

        long now = System.currentTimeMillis();

        if (lastTime == -1) {
            lastTime = now;
        }

        elapsed = now-lastTime;

        error =desired-current;
        integral = integral + (error*elapsed);
        derivative = (error-previousError)/elapsed;

        previousError = error;
        lastTime = now;

        double output = Kp*error + Ki*integral + Kd*derivative + bias;

        double throttle = output;

        if (throttle>maxDelta) {
            throttle = maxDelta;
        } else if (throttle<-maxDelta) {
            throttle = -maxDelta;
        }

        if (throttle>0) {
            throttle = throttle*maxForwardPower/maxDelta;
        } else if (throttle<0) {
            throttle = throttle*maxReversePower/-maxDelta;
        }

        // Min throttle that will actually move the robot
        if (throttle>0 && throttle<minForwardPower) {
            throttle = minForwardPower;
        } else if (throttle<0 && throttle>minReversePower) {
            throttle = minReversePower;
        }

        return throttle;

    }
}
