package org.firstinspires.ftc.teamcode.pure;


import android.graphics.Point;

import java.util.ArrayList;

import static java.lang.Math.*;


public class MathFunctions {
    public static double AngleWrap(double angle){

        while(angle < -Math.PI){
            angle += 2 * Math.PI;
        }
        while(angle > Math.PI){
            angle -= 2 * Math.PI;
        }
        return angle;
    }

    public static ArrayList<Point> lineCircleIntersection(Point circleCenter, double radius,
                                                          Point linePoint1, Point linePoint2){

        double linePoint1y = linePoint1.y;
        double linePoint2y = linePoint2.y;
        double linePoint1x = linePoint1.x;
        double linePoint2x = linePoint2.x;

        if(Math.abs(linePoint1y - linePoint2y) < 0.003){
            linePoint1y = linePoint2y + 0.003;
        }
        if(Math.abs(linePoint1x - linePoint2x) < 0.003){
            linePoint1x = linePoint2x + 0.003;
        }

        double m1 = (linePoint2y - linePoint1y)/(linePoint2x - linePoint1x);

        double quadraticA = 1.0 + Math.pow(m1, 2);

        double x1 = linePoint1x - circleCenter.x;
        double y1 = linePoint1y - circleCenter.x;

        double quadraticB = (2.0 * m1 * y1) - (2.0 * Math.pow(m1, 2) * x1);

        double quadraticC = (Math.pow(m1, 2) *  Math.pow(x1, 2)) - (2.0 * y1 * m1 * x1) + Math.pow(y1, 2) - Math.pow(radius, 2);

        ArrayList<Point> allPoints = new ArrayList<>();




        try{
            double xRoot1 = ((-quadraticB) + sqrt(pow(quadraticB, 2) - (4.0 * quadraticA * quadraticC)))/(2.0 * quadraticA);

            double yRoot1 = m1 * (xRoot1 - x1) + y1;

            xRoot1 += circleCenter.x;
            yRoot1 += circleCenter.y;

            double minX = linePoint1x < linePoint2x ? linePoint1x : linePoint2x;
            double maxX = linePoint1x > linePoint2x ? linePoint1x : linePoint2x;

            if(xRoot1 > minX && xRoot1 < maxX){
                allPoints.add(new Point ((int)xRoot1, (int)yRoot1));
            }

            double xRoot2 = ((-quadraticB) - sqrt(pow(quadraticB, 2) - (4.0 * quadraticA * quadraticC)))/(2.0 * quadraticA);
            double yRoot2 = m1 * (xRoot2 - x1) + y1;

            xRoot2 += circleCenter.x;
            yRoot2 += circleCenter.y;

            if(xRoot2 > minX && xRoot2 < maxX){
                allPoints.add(new Point ((int)xRoot2, (int)yRoot2));
            }


        } catch (Exception e){

        }
        return allPoints;

    }
}
