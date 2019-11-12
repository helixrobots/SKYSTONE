package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.ftccommon.internal.ProgramAndManageActivity;

@TeleOp(name = "Skynet", group = "Helix")
public class Program extends OpMode {

    int pc = 0;
    int ic = 0;
    boolean saved=true;
    int program=0;


    @Override
    public void init() {
        ProgramStore.load();

    }

    @Override
    public void loop() {
        if (gamepad1.right_bumper) {
            saved=ProgramStore.save();
        }
        if(gamepad1.left_bumper){
            program++;
            program = program % ProgramStore.MAX_PROGRAMS;
            ic=0;
            pc=0;
        }

        if(gamepad1.dpad_up){
            int opCode = ProgramStore.getOpCode(program,pc);
            if (ic==0) {
                opCode++;
                opCode = opCode % ProgramStore.OPERATIONS.length;
                ProgramStore.setOpCode(program,pc,opCode);
            } else {
                double parameter = ProgramStore.getParameter(program,pc);
                parameter+=ProgramStore.STEP[opCode];
                ProgramStore.setParameter(program,pc,parameter);

            }

            saved=false;
        }else if(gamepad1.dpad_down){
            int opCode = ProgramStore.getOpCode(program, pc);
            if (ic==0) {
                opCode--;
                if (opCode < 0) {
                    opCode = ProgramStore.OPERATIONS.length - 1;
                }
                ProgramStore.setOpCode(program, pc, opCode);
            } else {
                double parameter = ProgramStore.getParameter(program,pc);
                parameter-=ProgramStore.STEP[opCode];
                ProgramStore.setParameter(program,pc,parameter);
            }
            saved=false;
        }
        if(gamepad1.a){
            int opCode = ProgramStore.getOpCode(program,pc);
            if (opCode!=-1) {
                pc++;
                ic=0;
            }
        }else if(gamepad1.y) {
            pc--;
            if (pc < 0) {
                pc = 0;
            } else {
                ic = 0;
            }
        }

        if (gamepad1.dpad_left) {
            ic=0;
        }
        if (gamepad1.dpad_right) {
            // Only allow this if we already have an instruction
            if (ProgramStore.getOpCode(program,pc)!=-1) {
                ic = 1;
            }
        }

        if (gamepad1.b) {
            int activeProgram = ProgramStore.getActive();
            activeProgram++;
            if (activeProgram>=ProgramStore.MAX_PROGRAMS) {
                activeProgram=0;
            }
            ProgramStore.setActive(activeProgram);
            saved=false;
        }

        if (gamepad1.x) {
            int activeProgram = ProgramStore.getActive();
            activeProgram--;
            if (activeProgram < 0) {
                activeProgram = ProgramStore.MAX_PROGRAMS -1;
            }
            ProgramStore.setActive(activeProgram);
            saved=false;
        }

        if (gamepad1.right_stick_button) {
            ProgramStore.insertAt(program,pc);
            saved=false;
        }
        if (gamepad1.left_stick_button) {
            ProgramStore.delete(program,pc);
            int opCode = ProgramStore.getOpCode(program,pc);
            if (opCode==-1) {
                pc--;
                if (pc<0) {
                    pc=0;
                }
            }
            saved=false;
        }

        telemetry.addData("Active Program",ProgramStore.getActive());
        telemetry.addData("Editing Program", program);
        StringBuffer out = new StringBuffer();
        // Basic style!
        //
        out.append((pc+1)*10);
        out.append(" ");
        if (ic==0) {
            out.append("->");
        }
        int opCode = ProgramStore.getOpCode(program,pc);
        if (opCode==-1) {
            out.append("???");
        } else {
            out.append(ProgramStore.OPERATIONS[opCode]);
        }
        if (ic==0) {
            out.append("<-");
        }
        out.append(" ");
        if (ic==1) {
            out.append("->");
        }
        out.append(ProgramStore.getParameter(program,pc));
        if (ic==1) {
            out.append("<-");
        }

        telemetry.addData("Saved",saved);
        telemetry.addData("I", out.toString());
        telemetry.update();

        while (gamepad1.dpad_right || gamepad1.dpad_left || gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.left_bumper || gamepad1.right_bumper || gamepad1.y || gamepad1.a || gamepad1.x || gamepad1.b || gamepad1.left_stick_button || gamepad1.right_stick_button) {

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

