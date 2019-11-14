package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Skynet T-1 (Red)", group = "Helix")
public class SkynetT1Program extends Program {

    int heading = 0;

    SkynetT1 t1=null;

    private static final double TURN_STEP=5;
    private static final double MOVE_STEP=5 ;


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

        if (mode==MODE_NORMAL) {
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
                int opCode = ProgramStore.getOpCode(program, pc);
                if (opCode != ProgramStore.OPCODE_HEAD) {
                    if (opCode != ProgramStore.END_OF_LINE) {
                        pc++;
                    }
                    ProgramStore.setOpCode(program, pc, ProgramStore.OPCODE_HEAD);
                }
                heading += TURN_STEP;
                if (heading>ProgramStore.MAX_ANGLE) {
                    heading=(int)(ProgramStore.MAX_ANGLE);
                }
                ProgramStore.setParameter(program, pc, heading);
                t1.head(heading);
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
                int opCode = ProgramStore.getOpCode(program, pc);
                if (opCode != ProgramStore.OPCODE_HEAD) {
                    if (opCode != ProgramStore.END_OF_LINE) {
                        pc++;
                    }
                    ProgramStore.setOpCode(program, pc, ProgramStore.OPCODE_HEAD);
                }
                heading -= TURN_STEP;
                if (heading<ProgramStore.MIN_ANGLE) {
                    heading = (int)ProgramStore.MIN_ANGLE;
                }
                ProgramStore.setParameter(program, pc, heading);
                t1.head(heading);
            }

        } else if (mode==MODE_TURN) {

            if (gamepad1.dpad_down) {
                int opCode = ProgramStore.getOpCode(program, pc);
                double parameter = 0;
                if (opCode == ProgramStore.OPCODE_TURNRIGHT) {
                    parameter = ProgramStore.getParameter(program, pc);

                }else {
                    if (opCode != ProgramStore.END_OF_LINE) {
                        pc++;
                    }
                    ProgramStore.setOpCode(program, pc, ProgramStore.OPCODE_TURNRIGHT);
                }
                parameter -= MOVE_STEP;
                if (parameter<ProgramStore.MIN_ANGLE) {
                    parameter = (int)ProgramStore.MIN_ANGLE;
                }
                ProgramStore.setParameter(program, pc, parameter);
                t1.turnRight(-MOVE_STEP);
            }
            if (gamepad1.dpad_left) {
                int opCode = ProgramStore.getOpCode(program, pc);
                double parameter = 0;
                if (opCode == ProgramStore.OPCODE_TURNLEFT) {
                    parameter = ProgramStore.getParameter(program, pc);

                }else {
                    if (opCode != ProgramStore.END_OF_LINE) {
                        pc++;
                    }
                    ProgramStore.setOpCode(program, pc, ProgramStore.OPCODE_TURNLEFT);
                }
                parameter += MOVE_STEP;
                if (parameter>ProgramStore.MAX_ANGLE) {
                    parameter = ProgramStore.MAX_ANGLE;
                }
                ProgramStore.setParameter(program, pc, parameter);
                t1.turnLeft(MOVE_STEP);

            }
            if (gamepad1.dpad_up) {
                int opCode = ProgramStore.getOpCode(program, pc);
                double parameter = 0;
                if (opCode == ProgramStore.OPCODE_TURNLEFT) {
                    parameter = ProgramStore.getParameter(program, pc);

                }else {
                    if (opCode != ProgramStore.END_OF_LINE) {
                        pc++;
                    }
                    ProgramStore.setOpCode(program, pc, ProgramStore.OPCODE_TURNLEFT);
                }
                parameter -= MOVE_STEP;
                if (parameter<ProgramStore.MIN_ANGLE) {
                    parameter = (int)ProgramStore.MIN_ANGLE;
                }
                ProgramStore.setParameter(program, pc, parameter);
                t1.turnLeft(-MOVE_STEP);
            }
            if (gamepad1.dpad_right) {
                int opCode = ProgramStore.getOpCode(program, pc);
                double parameter = 0;
                if (opCode == ProgramStore.OPCODE_TURNRIGHT) {
                    parameter = ProgramStore.getParameter(program, pc);

                }else {
                    if (opCode != ProgramStore.END_OF_LINE) {
                        pc++;
                    }
                    ProgramStore.setOpCode(program, pc, ProgramStore.OPCODE_TURNRIGHT);
                }
                parameter += MOVE_STEP;
                if (parameter>ProgramStore.MAX_ANGLE) {
                    parameter = ProgramStore.MAX_ANGLE;
                }
                ProgramStore.setParameter(program, pc, parameter);
                t1.turnRight(MOVE_STEP);
            }

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

        if (gamepad1.b) {
            mode++;
            if (mode>=MODES.length) {
                mode = MODES.length-1;
            }
        }

        if (gamepad1.x) {
            mode--;
            if (mode <= 0) {
                mode=0;
            }
        }

        super.loop();
    }


    @Override
    protected boolean dpadB() {
        return false;
    }

    @Override
    protected boolean dpadX() {
        return false;
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
