package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.teamcode.CalibrationStore.MAX_SETTINGS;

public class ProgramStore {
    public static final int MAX_PROGRAMS = 10;
    private static final String PROGRAMS_FILE = "programs.txt";
    public static List<Instruction>[] _programs = new List[MAX_PROGRAMS];
    public static final String[] OPERATIONS = {"MOVE","TURN","OPENCLAW","CLOSECLAW","BOX","BRIDGE","PAUSE"};
    public static final int OPCODE_MOVE=0;
    public static final int OPCODE_TURN=1;
    public static final int OPCODE_OPENCLAW=2;
    public static final int OPCODE_CLOSECLAW=3;
    public static final int OPCODE_BOX=4;
    public static final int OPCODE_BRIDGE=5;
    public static final int OPCODE_PAUSE=6;

    public static final double[] STEP = {0.5,5,0,0,0,0,500};
    private static int _active = 0;


    public static void load() {
        for (int i=0;i<MAX_PROGRAMS;i++) {
            _programs[i] = new ArrayList<Instruction>();
        }
        BufferedReader bin = null;
        try {

            bin = new BufferedReader(new FileReader(AppUtil.getDefContext().getFilesDir().getPath().toString()+ File.pathSeparator+PROGRAMS_FILE));

            _active = -1;
            int index = 0;
            do {
                String input = bin.readLine();
                if (_active==-1) {
                    _active=Integer.parseInt(input);
                } else {
                    if (input.contains(",")) {
                        String parts[] = input.split(",");
                        Instruction instruction = new Instruction();
                        instruction.opCode = Integer.parseInt(parts[0]);
                        instruction.parameter = Double.parseDouble(parts[1]);
                        _programs[index].add(instruction);

                    } else {
                        index++;
                    }
                }
            } while (index<MAX_PROGRAMS);
        } catch (Exception e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
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
            bout = new BufferedWriter(new FileWriter(AppUtil.getDefContext().getFilesDir().getPath().toString()+ File.pathSeparator+PROGRAMS_FILE));
            bout.write(String.format("%d",_active));
            bout.write("\n");
            for (int i=0;i<MAX_PROGRAMS;i++) {
                for (int t=0;t<_programs[i].size();t++) {
                    Instruction instruction = _programs[i].get(t);
                    bout.write(String.format("%d",instruction.opCode));
                    bout.write(",");
                    bout.write(String.format("%.2f",instruction.parameter));
                    bout.write("\n");

                }
                // Program separator
                bout.write("---\n");
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

    public static int getOpCode(int programIndex, int pc) {
        List<Instruction> program = _programs[programIndex];
        if (pc<program.size()) {
            Instruction instruction = program.get(pc);
            return instruction.opCode;
        } else {
            return -1;
        }
    }

    public static double getParameter(int programIndex, int pc) {
        List<Instruction> program = _programs[programIndex];
        if (pc<program.size()) {
            Instruction instruction = program.get(pc);
            return instruction.parameter;
        } else {
            return 0;
        } }

    public static void setOpCode(int programIndex, int pc, int opCode) {
        List<Instruction> program = _programs[programIndex];
        Instruction instruction = null;
        if (pc<=program.size()) {
            if (pc==program.size()) {
                instruction = new Instruction();
                program.add(instruction);
            }
            instruction = program.get(pc);
            instruction.opCode=opCode;
        }
    }

    public static void setParameter(int programIndex, int pc, double parameter) {
        List<Instruction> program = _programs[programIndex];
        Instruction instruction = null;
        if (pc<program.size()) {
            instruction = program.get(pc);
            instruction.parameter=parameter;
        }
    }

    public static int getActive() {
        return _active;
    }

    public static void setActive(int activeProgram) {
        if (activeProgram>=0 && activeProgram<MAX_PROGRAMS) {
            _active = activeProgram;
        }
    }

    public static List<Instruction> getActiveProgram() {
        return _programs[_active];
    }

    public static void insertAt(int programIndex,int pc) {
        List<Instruction> program = _programs[programIndex];
        if (pc>=0 && pc<program.size()) {
            Instruction instruction = new Instruction();
            instruction.opCode=0;
            instruction.parameter=0;
            program.add(pc,instruction);
        }
    }

    public static void delete(int programIndex, int pc) {
        List<Instruction> program = _programs[programIndex];
        if (pc>=0 && pc<program.size()) {
            program.remove(pc);
        }
    }

    static class Instruction {
        public int opCode;
        public double parameter;
    }
}
