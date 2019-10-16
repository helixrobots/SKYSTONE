package org.firstinspires.ftc.appl;

import org.firstinspires.ftc.teamcode.AshrayBasicOpMode_Iterative;

import java.util.Arrays;
import java.util.List;


public class TestFormulas {

    public static void main(String[] args) {
        AshrayBasicOpMode_Iterative opMode = new AshrayBasicOpMode_Iterative();
        List<Double> angles = opMode.armServoPositionsToAnglesInDegree(0.4, 0.5 / 3.0);
        System.out.println(angles);

        List<Double> basePosition = opMode.getGripperBasePositionFromServoAngles(angles.get(0), angles.get(1));
        System.out.println(basePosition);

        List<Double> servoAngles = opMode.getServoAnglesFromGripperBasePosition(basePosition.get(0), basePosition.get(1));
        System.out.println(servoAngles);

        List<Double> servoPositions = opMode.anglesInDegreeToArmServoPositions(45, 15);
        System.out.println(servoPositions);

        List<Double> newBasePosition = opMode.getNewGripperBasePosition(0.0, 1.0, 45, 15);
        System.out.println(newBasePosition);

        basePosition = opMode.getGripperBasePositionFromServoAngles(45, 0);
        System.out.println(basePosition);

        servoAngles = opMode.getServoAnglesFromGripperBasePosition(basePosition.get(0), basePosition.get(1));
        System.out.println(servoAngles);




    }
}
