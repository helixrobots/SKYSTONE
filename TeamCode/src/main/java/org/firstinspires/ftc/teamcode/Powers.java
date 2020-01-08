package org.firstinspires.ftc.teamcode;

public class Powers {
    private double TL;
    private double BL;
    private double BR;
    private double TR;

    public Powers(double TL, double BL, double BR, double TR){

        this.TL = TL;
        this.BL = BL;
        this.BR = BR;
        this.TR = TR;

    }

    public double getTL() {
        return TL;
    }

    public void setTL(double TL) {
        this.TL = TL;
    }

    public double getBL() {
        return BL;
    }

    public void setBL(double BL) {
        this.BL = BL;
    }

    public double getBR() {
        return BR;
    }

    public void setBR(double BR) {
        this.BR = BR;
    }

    public double getTR() {
        return TR;
    }

    public void setTR(double TR) {
        this.TR = TR;
    }
}
