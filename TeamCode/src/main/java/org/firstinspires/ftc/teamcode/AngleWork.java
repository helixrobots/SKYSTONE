package org.firstinspires.ftc.teamcode;


import static java.lang.Math.*;

public class AngleWork {

    public static Force forceCalc(double angleInDegree) {

        angleInDegree = abs(angleInDegree);

        double x = 1;
        double y;

        angleInDegree = 90 - angleInDegree;

        y = sin(angleInDegree) / (sin(90 - angleInDegree) / x);

//        x = x / ((x+y) / 2);
//        y = y / ((x+y) / 2);

        return new Force(x, y);
    }
}
