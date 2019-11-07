package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

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

    final int MAX_SETTINGS=2;
    final int MAX_ITEMS=5;
    double items[][] = new double[MAX_SETTINGS][MAX_ITEMS];
    double step[] = {0.1,0.1,0.1,0.1,5};
    int setting=0;
    int item=0;
    boolean saved=true;

    private final String SETTINGS_FILE="settings.txt";

    @Override
    public void init() {
        load();

    }

    public void load() {
        BufferedReader bin = null;
        try {
            bin = new BufferedReader(new FileReader(Environment.getDataDirectory().getPath().toString()+ File.pathSeparator+SETTINGS_FILE));

            for (int i=0;i<MAX_SETTINGS;i++) {
                String input = bin.readLine();
                String parts[] = input.split(",");
                for (int t=0;t<MAX_ITEMS;t++) {
                    items[i][t]=Double.parseDouble(parts[t]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bin!=null)
                    bin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public boolean save() {
        BufferedWriter bout = null;
        try {
            bout = new BufferedWriter(new FileWriter(Environment.getExternalStorageDirectory().getPath().toString()+ File.pathSeparator+SETTINGS_FILE));
            for (int i=0;i<MAX_SETTINGS;i++) {
                for (int t=0;t<MAX_ITEMS;t++) {
                    if (t>0) {
                        bout.write(",");
                    }
                    bout.write(String.format("%.2f", items[i][t]));

                }
                bout.write("\n");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
//            return false;
        } finally {
            try {
                if (bout!=null)
                    bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void loop() {
        if (gamepad1.right_bumper) {
            saved=save();
        }
        if(gamepad1.left_bumper){
            setting++;
            setting = setting % MAX_SETTINGS;
        }

        if(gamepad1.dpad_up){
            items[setting][item]+=step[item];
            saved=false;
        }else if(gamepad1.dpad_down){
            items[setting][item]-=step[item];
            saved=false;
        }
        if(gamepad1.dpad_right){
            item++;
        }else if(gamepad1.dpad_left) {
            item--;
            if (item < 0) {
                item = MAX_ITEMS - 1;
            }
        }
        item=item % MAX_ITEMS;
        telemetry.addData("Setting", setting);
        StringBuffer out = new StringBuffer();
        for (int i=0;i<MAX_ITEMS;i++) {
            if (out.length()>0) {
                out.append("   ,   ");
            }
            if (i==item) {
                    out.append("->");
                }
            out.append(String.format("%.2f",items[setting][i]));
            if (i==item) {
                out.append("<-");
            }
        }
        telemetry.addData("Items", out.toString());
        telemetry.addData("Saved",saved);
        telemetry.update();

        while (gamepad1.dpad_right || gamepad1.dpad_left || gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.left_bumper || gamepad1.right_bumper) {

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}