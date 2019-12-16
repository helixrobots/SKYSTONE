package org.firstinspires.ftc.teamcode.pure;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;

import static org.firstinspires.ftc.teamcode.pure.RoboMove.followCurve;


public class Pure_Pursuit  extends OpMode{

    public void init(){

    }

    public void loop(){
        ArrayList<CurvePoint> allPoints = new ArrayList<>();

        allPoints.add(new CurvePoint(0.0,0.0,1.0,1.0,50.0,Math.toRadians(50), 1.0));

        followCurve(allPoints, Math.toRadians(90));

    }


}
