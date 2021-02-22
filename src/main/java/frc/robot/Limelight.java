package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight implements ILimelight {

    NetworkTable table;
    NetworkTableEntry tv;
    NetworkTableEntry tx;
    NetworkTableEntry ty;
    NetworkTableEntry ta;

    public Limelight() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tv = table.getEntry("tv");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        ta = table.getEntry("ta");
    }

    public boolean hasValidTarget() {
        return tv.getDouble(0.0) == 1;
    }

    // returns an angle between -29.8 to 29.8 degrees
    public double getHorizontalOffset() {
        return tx.getDouble(0.0);
    }

    // returns an angle between -24.85 to 24.85 degrees
    public double getVerticalOffset() {
        return ty.getDouble(0.0);
    }

    // returns a percentage
    public double getTargetArea() {
        return ta.getDouble(0.0);
    }

}