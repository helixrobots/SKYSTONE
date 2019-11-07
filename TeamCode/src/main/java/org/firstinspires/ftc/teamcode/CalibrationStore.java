package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CalibrationStore {

    private static final String SETTINGS_FILE="settings.txt";

    final static int MAX_SETTINGS=2;
    final static int MAX_ITEMS=5;
    static double items[][] = new double[MAX_SETTINGS][MAX_ITEMS];

    public static void load() {
        BufferedReader bin = null;
        try {

            bin = new BufferedReader(new FileReader(AppUtil.getDefContext().getFilesDir().getPath().toString()+ File.pathSeparator+SETTINGS_FILE));

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

    public static boolean save() {
        BufferedWriter bout = null;
        try {
            bout = new BufferedWriter(new FileWriter(AppUtil.getDefContext().getFilesDir().getPath().toString()+ File.pathSeparator+SETTINGS_FILE));
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
}
