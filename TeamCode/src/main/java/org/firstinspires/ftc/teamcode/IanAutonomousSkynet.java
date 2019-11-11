package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.util.List;

import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_BOX;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_BRIDGE;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_CLOSECLAW;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_MOVE;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_OPENCLAW;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_PAUSE;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_TURN;

@Autonomous(name="Ian Autonomous Skynet Red", group="Helix")

public class IanAutonomousSkynet extends IanAutonomousBase {

    @Override
    public int getSetting() {
        return 0;
    }

    @Override
    public void execute() {

        ProgramStore.load();

        List<ProgramStore.Instruction> instructions = ProgramStore.getActiveProgram();

        for (ProgramStore.Instruction instruction : instructions) {
//            public static final String[] OPERATIONS = {"MOVE","TURN","OPENCLAW","CLOSECLAW","BOX","BRIDGE","PAUSE"};
            switch (instruction.opCode) {
                case OPCODE_MOVE: {
                    move(instruction.parameter);
                    break;
                }
                case OPCODE_TURN: {
                    turn (instruction.parameter);
                    break;
                }
                case OPCODE_OPENCLAW: {
                    openClaw();
                    break;
                }
                case OPCODE_CLOSECLAW: {
                    closeClaw();
                    break;
                }
                case OPCODE_BOX: {
                    moveToBoxPosition();
                    break;
                }
                case OPCODE_BRIDGE: {
                    moveToBridgePosition();
                    break;
                }
                case OPCODE_PAUSE: {
                    sleep((int)instruction.parameter);
                    break;
                }

            }
        }


    }
}
