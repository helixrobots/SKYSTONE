package org.firstinspires.ftc.helixdesktop;

import com.helix.common.PID;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class PIDTunner extends JPanel implements Runnable {


    private static final int POINT_RADIUS = 5;
    private Object _syncObject = new Object();
    private double _currentPower = 0;

    private class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x=x;
            this.y=y;
        }
    }

    private static final double TOLERANCE = 3.0;
    public static final int STABILITY_THRESHOLD = 5000;

    private long _startTime;

    private double _desired = 100;
    private double _current = 0;
    private double _output = 0;

    private List<Point> _points = new ArrayList();
    private List<Point> _outputs = new ArrayList();
    private List<Point> _powers = new ArrayList();

    public static void main(String[] args) {
        JFrame f=new JFrame();

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PIDTunner pidTunner = new PIDTunner();
        f.setContentPane(pidTunner);
        f.setSize(640,500);
        f.setVisible(true);
        f.repaint();

        pidTunner._startTime = System.currentTimeMillis();
        Thread t = new Thread(pidTunner);
        t.start();
    }

    @Override
    public void run() {
        PID pid = new PID(1,0,0,0,-0.1,-1,0.1,1,100);
        pid.reset();

        double desired = 100;
        double current = 0;
        long stableSince = System.currentTimeMillis();

        do {
            double output = pid.calculate(100,_current);
            current += applyPower(output);
            System.out.println(String.format("PID output=%.2f \toutput=%.2f \tcurrent=%.2f\tcurrentPower=%.2f",pid.previousOutput,output,current, _currentPower));
            // If we are farther than 3 degrees, then reset the time


            synchronized (_syncObject) {
                this._output = output;
                this._desired = desired;
                this._current = current;

            }
            repaint();

            if ((Math.abs(desired - current) > TOLERANCE)) {
                stableSince = System.currentTimeMillis();
            } else {
                output = 0;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while ((System.currentTimeMillis()-stableSince)< STABILITY_THRESHOLD);


    }

    private double applyPower(double output) {

        if (_currentPower < output) {
            _currentPower +=0.025;
        } else if (_currentPower > output) {
            _currentPower -=0.025;
        }
        // Assume immediate response
//        _currentPower = output;

        return _currentPower;

    }

    @Override
    protected void paintComponent(Graphics graphics) {
        synchronized (_syncObject) {
            long elapsed = System.currentTimeMillis() - _startTime;

            elapsed = elapsed / 1000;

            // Adjust it a bit to show better
            elapsed *= POINT_RADIUS;

            graphics.setColor(Color.BLUE);
            graphics.drawLine(0, 200 - (int) _desired, 640, 200 - (int) _desired);
            graphics.setColor(Color.GRAY);
            graphics.drawLine(0, 300, 640, 300);
            graphics.setColor(Color.GRAY);
            graphics.drawLine(0, 400, 640, 400);
            graphics.setColor(Color.BLACK);
            Point np = new Point((int) elapsed, 300 - (int) (_currentPower * 70.0));
            _powers.add(np);
            for (Point p : _powers) {
                graphics.fillOval(p.x, p.y, POINT_RADIUS, 2);
            }
            np = new Point((int) elapsed, 400 - (int) (_output * 70.0));
            _outputs.add(np);
            for (Point p : _outputs) {
                graphics.fillOval(p.x, p.y, POINT_RADIUS, 2);
            }
            graphics.setColor(Color.red);
            np = new Point((int) elapsed, 200 - (int) _current);
            _points.add(np);
            for (Point p : _points) {
                graphics.fillOval(p.x, p.y, POINT_RADIUS, 2);
            }
        }
        graphics.dispose();
    }
}
