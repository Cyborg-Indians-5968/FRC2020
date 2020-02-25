package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Launcher implements IMotorPeripheral {

    private CANSparkMax launcherMotor;

    private double motorSpeed;
    private static final double HIGH = 0.5;
    private static final double LOW = 0.0;

    public Launcher() {
        launcherMotor = new CANSparkMax(PortMap.CAN.LAUNCHER_MOTOR_CONTROLLER, MotorType.kBrushless);
        launcherMotor.setInverted(true);

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
        launcherMotor.set(motorSpeed);
    }
}
