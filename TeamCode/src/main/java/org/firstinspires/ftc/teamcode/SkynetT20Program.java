package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Skynet T-20 (Red)", group = "Helix")
public class SkynetT20Program extends SkynetT1Program {


    @Override
    protected void processControls() {
        if (mode == MODE_NORMAL) {
            if (gamepad1.dpad_up) {
                int opCode = ProgramStore.getOpCode(program, pc);
                double parameter = 0;
                if (opCode == ProgramStore.OPCODE_MOVE) {
                    parameter = ProgramStore.getParameter(program, pc);
                } else {
                    if (opCode != ProgramStore.END_OF_LINE) {
                        t1.execute(ProgramStore.getInstruction(program, pc));
                        pc++;
                    }
                    ProgramStore.setOpCode(program, pc, ProgramStore.OPCODE_MOVE);
                }
                parameter += MOVE_STEP;
                ProgramStore.setParameter(program, pc, parameter);
            }

            if (gamepad1.dpad_left) {
                int opCode = ProgramStore.getOpCode(program, pc);
                if (opCode != ProgramStore.OPCODE_HEAD) {
                    if (opCode != ProgramStore.END_OF_LINE) {
                        t1.execute(ProgramStore.getInstruction(program, pc));
                        pc++;
                    }
                    ProgramStore.setOpCode(program, pc, ProgramStore.OPCODE_HEAD);
                }
                heading += TURN_STEP;
                if (heading > ProgramStore.MAX_ANGLE) {
                    heading = (int) (ProgramStore.MAX_ANGLE);
                }
                ProgramStore.setParameter(program, pc, heading);
            }
            if (gamepad1.dpad_down) {
                int opCode = ProgramStore.getOpCode(program, pc);
                double parameter = 0;
                if (opCode == ProgramStore.OPCODE_MOVE) {
                    parameter = ProgramStore.getParameter(program, pc);
                } else {
                    if (opCode != ProgramStore.END_OF_LINE) {
                        t1.execute(ProgramStore.getInstruction(program, pc));
                        pc++;
                    }
                    ProgramStore.setOpCode(program, pc, ProgramStore.OPCODE_MOVE);
                }
                parameter -= MOVE_STEP;
                ProgramStore.setParameter(program, pc, parameter);
            }
            if (gamepad1.dpad_right) {
                int opCode = ProgramStore.getOpCode(program, pc);
                if (opCode != ProgramStore.OPCODE_HEAD) {
                    if (opCode != ProgramStore.END_OF_LINE) {
                        t1.execute(ProgramStore.getInstruction(program, pc));
                        pc++;
                    }
                    ProgramStore.setOpCode(program, pc, ProgramStore.OPCODE_HEAD);
                }
                heading -= TURN_STEP;
                if (heading < ProgramStore.MIN_ANGLE) {
                    heading = (int) ProgramStore.MIN_ANGLE;
                }
                ProgramStore.setParameter(program, pc, heading);
            }

        } else if (mode == MODE_TURN) {

            if (gamepad1.dpad_down) {
                int opCode = ProgramStore.getOpCode(program, pc);
                double parameter = 0;
                if (opCode == ProgramStore.OPCODE_TURNRIGHT) {
                    parameter = ProgramStore.getParameter(program, pc);

                } else {
                    if (opCode != ProgramStore.END_OF_LINE) {
                        t1.execute(ProgramStore.getInstruction(program, pc));
                        pc++;
                    }
                    ProgramStore.setOpCode(program, pc, ProgramStore.OPCODE_TURNRIGHT);
                }
                parameter -= MOVE_STEP;
                if (parameter < ProgramStore.MIN_ANGLE) {
                    parameter = (int) ProgramStore.MIN_ANGLE;
                }
                ProgramStore.setParameter(program, pc, parameter);
            }
            if (gamepad1.dpad_left) {
                int opCode = ProgramStore.getOpCode(program, pc);
                double parameter = 0;
                if (opCode == ProgramStore.OPCODE_TURNLEFT) {
                    parameter = ProgramStore.getParameter(program, pc);

                } else {
                    if (opCode != ProgramStore.END_OF_LINE) {
                        t1.execute(ProgramStore.getInstruction(program, pc));
                        pc++;
                    }
                    ProgramStore.setOpCode(program, pc, ProgramStore.OPCODE_TURNLEFT);
                }
                parameter += MOVE_STEP;
                if (parameter > ProgramStore.MAX_ANGLE) {
                    parameter = ProgramStore.MAX_ANGLE;
                }
                ProgramStore.setParameter(program, pc, parameter);

            }
            if (gamepad1.dpad_up) {
                int opCode = ProgramStore.getOpCode(program, pc);
                double parameter = 0;
                if (opCode == ProgramStore.OPCODE_TURNLEFT) {
                    parameter = ProgramStore.getParameter(program, pc);

                } else {
                    if (opCode != ProgramStore.END_OF_LINE) {
                        t1.execute(ProgramStore.getInstruction(program, pc));
                        pc++;
                    }
                    ProgramStore.setOpCode(program, pc, ProgramStore.OPCODE_TURNLEFT);
                }
                parameter -= MOVE_STEP;
                if (parameter < ProgramStore.MIN_ANGLE) {
                    parameter = (int) ProgramStore.MIN_ANGLE;
                }
                ProgramStore.setParameter(program, pc, parameter);
            }
            if (gamepad1.dpad_right) {
                int opCode = ProgramStore.getOpCode(program, pc);
                double parameter = 0;
                if (opCode == ProgramStore.OPCODE_TURNRIGHT) {
                    parameter = ProgramStore.getParameter(program, pc);

                } else {
                    if (opCode != ProgramStore.END_OF_LINE) {
                        t1.execute(ProgramStore.getInstruction(program, pc));
                        pc++;
                    }
                    ProgramStore.setOpCode(program, pc, ProgramStore.OPCODE_TURNRIGHT);
                }
                parameter += MOVE_STEP;
                if (parameter > ProgramStore.MAX_ANGLE) {
                    parameter = ProgramStore.MAX_ANGLE;
                }
                ProgramStore.setParameter(program, pc, parameter);
            }

        }
    }

    @Override
    protected boolean persist() {
        ProgramStore.Instruction instruction = ProgramStore.getInstruction(program,pc);
        if (instruction!=null) {
            t1.execute(instruction);
            pc++;
        }
        return super.persist();
    }
}
