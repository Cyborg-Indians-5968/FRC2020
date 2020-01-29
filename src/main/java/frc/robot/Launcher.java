package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Launcher implements IMotorPeripheral {

    private TalonSRX launcherMotor;

    private double motorSpeed;
    private static final double HIGH = 0.9;
    private static final double LOW = 0.0;

    public Launcher() {
        launcherMotor = new TalonSRX(PortMap.CAN.LAUNCHER_MOTOR_CONTROLLER);
        // launcherMotor.setInverted(true);

        stop();
    }

    // sets the motor speed to low
    @Override
    public void stop() {
        motorSpeed = LOW;
    }

    // sets the motor speed to high
    @Override
    public void start() {
        motorSpeed = HIGH;
    }

    // stops the launcher
    @Override
    public void init() {
        stop();
    }

    // sets the laucher motor to motor spped
    @Override
    public void periodic() {
        launcherMotor.set(ControlMode.PercentOutput, motorSpeed);
    }

}
