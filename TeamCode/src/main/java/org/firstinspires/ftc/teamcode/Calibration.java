package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@TeleOp(name = "Calibration", group = "Helix")
public class Calibration  extends OpMode {
    int iVertical=0;
    int iHorizontal=0;


    double step[] = {0.05,0.05,0.05,0.05,5};
    int setting=0;
    int item=0;
    boolean saved=true;


    @Override
    public void init() {
        CalibrationStore.load();

    }

    @Override
    public void loop() {
        if (gamepad1.right_bumper) {
            saved=CalibrationStore.save();
        }
        if(gamepad1.left_bumper){
            setting++;
            setting = setting % CalibrationStore.MAX_SETTINGS;
        }

        if(gamepad1.y){
            CalibrationStore.items[setting][item]+=step[item];
            saved=false;
        }else if(gamepad1.a){
            CalibrationStore.items[setting][item]-=step[item];
            saved=false;
        }
        if(gamepad1.dpad_right){
            item++;
        }else if(gamepad1.dpad_left) {
            item--;
            if (item < 0) {
                item = CalibrationStore.MAX_ITEMS - 1;
            }
        }
        item=item % CalibrationStore.MAX_ITEMS;
        telemetry.addData("Setting", setting);
        StringBuffer out = new StringBuffer();
        for (int i=0;i<CalibrationStore.MAX_ITEMS;i++) {
            if (out.length()>0) {
                out.append("   ,   ");
            }
            if (i==item) {
                    out.append("->");
                }
            out.append(String.format("%.2f",CalibrationStore.items[setting][i]));
            if (i==item) {
                out.append("<-");
            }
        }
        telemetry.addData("Items", out.toString());
        telemetry.addData("Saved",saved);
        telemetry.update();

        while (gamepad1.dpad_right || gamepad1.dpad_left || gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.left_bumper || gamepad1.right_bumper || gamepad1.y || gamepad1.a || gamepad1.x || gamepad1.b || gamepad1.left_stick_button || gamepad1.right_stick_button) {

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}