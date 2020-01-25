package frc.robot;

public interface IEncoder {

    public void setInverted(boolean inverted);

    public void setDistancePerPulse(double distance);

    public double getDistance();
    
    public void reset();

}
