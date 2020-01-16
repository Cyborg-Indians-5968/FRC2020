package frc.robot;

public interface IGyroscopeSensor {

    public double getPitch();

    public double getRoll();

    public double getYaw();

    public void resetYaw();

}
