package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Roller implements IMotorPeripheral {
    private VictorSPX rollerMotor;

    private double motorSpeed;
    private static final double HIGH = 0.3;
    private static final double LOW = 0.0;

    public Roller() {
        rollerMotor = new VictorSPX(PortMap.CAN.ROLLER_MOTOR_CONTROLLER);
        rollerMotor.setInverted(true);

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
