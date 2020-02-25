package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.DigitalInput;

public class Endgame implements IMotorPeripheral {

    private DigitalInput EndgameSwitch;
    private double motorSpeed;
    private TalonSRX rightMotor;
    private TalonSRX leftMotor;

    private static final double HIGH = 0.9;
    private static final double LOW = 0.0;
    private static final double REVERSE = -0.9;

    public Endgame(){
        EndgameSwitch = new DigitalInput(0); //TODO: Set actual port
        rightMotor = new TalonSRX(PortMap.CAN.LAUNCHER_MOTOR_CONTROLLER);
        leftMotor = new TalonSRX(PortMap.CAN.LAUNCHER_MOTOR_CONTROLLER);
        leftMotor.setInverted(true);
    }

    @Override
    public void stop() {
        motorSpeed = LOW;
    }

    @Override
    public void start() {
        motorSpeed = HIGH;
    }

    public void reverse() {
        motorSpeed = REVERSE;
    }

    @Override
    public void init() {
        stop();
    }

	@Override
	public void periodic() {
		if(EndgameSwitch.get()) {
            motorSpeed = LOW;
        }
        rightMotor.set(ControlMode.PercentOutput, motorSpeed);
        leftMotor.set(ControlMode.PercentOutput, motorSpeed);
	}


}