package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Roller implements IMotorPeripheral {
    private TalonSRX rollerMotor;

    private double motorSpeed;
    private static final double HIGH = 0.9;
    private static final double LOW = 0.0;

    public Roller() {
        rollerMotor = new TalonSRX(PortMap.CAN.ROLLER_MOTOR_CONTROLLER);
        // rollerMotor.setInverted(true);

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
        rollerMotor.set(ControlMode.PercentOutput, motorSpeed);
    }
}
