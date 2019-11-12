package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.ftccommon.internal.ProgramAndManageActivity;

@TeleOp(name = "Skynet T-1 (Red)", group = "Helix")
public class SkynetT1Program extends Program {

    int heading = 0;

    SkynetT1 t1;

    private static final double TURN_STEP=5;
    private static final double MOVE_STEP=5;


    @Override
    public void init() {
        super.init();
        t1 = new SkynetT1();
        t1.hardwareMap = hardwareMap;
        t1.telemetry = telemetry;
        t1.init();
        t1.setupSensors();
        t1.setupMotors();
        // Fix to the last program to prevent messing up normal programs
        program=ProgramStore.MAX_PROGRAMS-1;
    }

    @Override
    public void loop() {
        if (gamepad1.dpad_up) {
            int opCode = ProgramStore.getOpCode(program,pc);
            double parameter = 0;
            if (opCode == ProgramStore.OPCODE_MOVE) {
                parameter = ProgramStore.getParameter(program,pc);
            } else {
                if (opCode!=ProgramStore.END_OF_LINE) {
                    pc++;
                }
                ProgramStore.setOpCode(program,pc,ProgramStore.OPCODE_MOVE);
            }
            parameter+=MOVE_STEP;
            ProgramStore.setParameter(program,pc,parameter);
            t1.move(MOVE_STEP);
        }
        if (gamepad1.dpad_left) {
            int opCode = ProgramStore.getOpCode(program,pc);
            if (opCode != ProgramStore.OPCODE_TURN) {
                if (opCode!=ProgramStore.END_OF_LINE) {
                    pc++;
                }
                ProgramStore.setOpCode(program,pc,ProgramStore.OPCODE_TURN);
            }
            heading+=TURN_STEP;
            ProgramStore.setParameter(program,pc,heading);
            t1.turn(heading);
        }
        if (gamepad1.dpad_down) {
            int opCode = ProgramStore.getOpCode(program,pc);
            double parameter = 0;
            if (opCode == ProgramStore.OPCODE_MOVE) {
                parameter = ProgramStore.getParameter(program,pc);
            } else {
                if (opCode!=ProgramStore.END_OF_LINE) {
                    pc++;
                }
                ProgramStore.setOpCode(program,pc,ProgramStore.OPCODE_MOVE);
            }
            parameter-=MOVE_STEP;
            ProgramStore.setParameter(program,pc,parameter);
            t1.move(-MOVE_STEP);
        }
        if (gamepad1.dpad_right) {
            int opCode = ProgramStore.getOpCode(program,pc);
            if (opCode != ProgramStore.OPCODE_TURN) {
                if (opCode!=ProgramStore.END_OF_LINE) {
                    pc++;
                }
                ProgramStore.setOpCode(program,pc,ProgramStore.OPCODE_TURN);
            }
            heading-=TURN_STEP;
            ProgramStore.setParameter(program,pc,heading);
            t1.turn(heading);
        }
        if (gamepad1.y) {
            int opCode = ProgramStore.getOpCode(program,pc);
            if (opCode!=ProgramStore.END_OF_LINE && opCode!=ProgramStore.OPCODE_OPENCLAW) {
                pc++;
            }
            ProgramStore.setOpCode(program,pc,ProgramStore.OPCODE_OPENCLAW);
            ProgramStore.setParameter(program,pc,0);
            t1.openClaw();
        }
        if (gamepad1.a) {
            int opCode = ProgramStore.getOpCode(program,pc);
            if (opCode!=ProgramStore.END_OF_LINE && opCode!=ProgramStore.OPCODE_CLOSECLAW) {
                pc++;
            }
            ProgramStore.setOpCode(program,pc,ProgramStore.OPCODE_CLOSECLAW);
            ProgramStore.setParameter(program,pc,0);
            t1.closeClaw();
        }
        super.loop();
    }


    @Override
    protected boolean dpadLeftBumper() {
        return false;
    }

    @Override
    protected boolean dpadA() {
        return false;
    }

    @Override
    protected boolean dpadY() {
        return false;
    }

    @Override
    protected boolean dpadRight() {
        return false;
    }

    @Override
    protected boolean dpadLeft() {
        return false;
    }

    @Override
    protected boolean dpadDown() {
        return false;
    }

    @Override
    protected boolean dpadUp() {
        return false;
    }
}
