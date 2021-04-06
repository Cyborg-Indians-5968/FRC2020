package frc.robot;

public interface ILimelight {

    public boolean hasValidTarget();
    
    public double getHorizontalOffset();

    public double getVerticalOffset();

    public double getTargetArea();

    public void periodic();

}