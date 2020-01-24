package frc.robot;

import edu.wpi.first.wpilibj.util.Color;

public interface IColorWheel {

    public void spinToColor(char color);

    public void spinRevolutions();

    public void periodic();

}
