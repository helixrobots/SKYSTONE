package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import java.util.List;

import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_BOX;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_BRIDGE;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_CLOSECLAW;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_MOVE;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_OPENCLAW;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_PAUSE;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_HEAD;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_TURNLEFT;
import static org.firstinspires.ftc.teamcode.ProgramStore.OPCODE_TURNRIGHT;


@Autonomous(name="Ian Autonomous Skynet Red", group="Helix")

public class IanAutonomousSkynetRed extends IanAutonomousBase {

    @Override
    public int getSetting() {
        return 0;
    }

    @Override
    public void execute() {

        ProgramStore.load();

        List<ProgramStore.Instruction> instructions = ProgramStore.getActiveProgram();

        for (ProgramStore.Instruction instruction : instructions) {

            execute(instruction);

        }

    }

}
