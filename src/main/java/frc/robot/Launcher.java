package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Launcher implements ILauncher
{
    private TalonSRX launcherMotor;

    private double motorSpeed;
    private static final double HIGH = 0.9;
    private static final double LOW = 0.0;

    public Launcher() {
        launcherMotor = new TalonSRX(PortMap.CAN.LAUNCHER_MOTOR_CONTROLLER);
        //launcherMotor.setInverted(true);

        stop();
    }

    @Override
    public void stop() {
        motorSpeed = LOW;
    }

    @Override
    public void start() {
        motorSpeed = HIGH;
        
    }

    @Override
    public void init() {
        stop();
    }

    @Override
    public void periodic() {
        launcherMotor.set(ControlMode.PercentOutput,motorSpeed);
    }
}
