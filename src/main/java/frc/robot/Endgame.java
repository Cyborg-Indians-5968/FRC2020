package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class Endgame implements IEndgame {

    private TalonSRX rightMotor;
    private TalonSRX leftMotor;
    private IEncoder rightEncoder;
    private IEncoder leftEncoder;

    private double rightSpeed;
    private double leftSpeed;

    private EndgameClimbingMode climbMode;
    private static final double HIGH = 1.0;
    private static final double LOW = 0.7;
    private static final double MAX_HEIGHT = 17700.0;
    private static final double MAX_ERROR = 25.0;

    private enum EndgameClimbingMode {
        UP,
        DOWN,
        NONE,
    }

    public Endgame(){
        rightMotor = new TalonSRX(PortMap.CAN.ENDGAME_RIGHT);
        leftMotor = new TalonSRX(PortMap.CAN.ENDGAME_LEFT);
        leftMotor.setInverted(true);
        
        rightEncoder = new TalonEncoder(rightMotor);
        leftEncoder = new TalonEncoder(leftMotor);
    }

    public void raise() {
        climbMode = EndgameClimbingMode.UP;
    }

    public void lower() {
        climbMode = EndgameClimbingMode.DOWN;
    }

    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void init() {
        climbMode = EndgameClimbingMode.NONE;
        resetEncoders();
    }

	public void periodic() {
        
        double leftPosition = leftEncoder.getPosition();
        double rightPosition = rightEncoder.getPosition();
        boolean disableMovement = false;

        if(climbMode == EndgameClimbingMode.NONE) {
            disableMovement = true;
        } else if(climbMode == EndgameClimbingMode.UP) {
            if(leftPosition > MAX_HEIGHT || rightPosition > MAX_HEIGHT) {
                disableMovement = true;
            }
        } else {
            if(leftPosition < 0.0 || rightPosition < 0.0) {
                disableMovement = true;
            }
        }

        double encoderDelta = leftPosition - rightPosition;
        if(climbMode == EndgameClimbingMode.DOWN) {
            encoderDelta = -encoderDelta;
        }

        if(Math.abs(encoderDelta) > MAX_ERROR) {
            if(encoderDelta > 0) {
                leftSpeed = LOW;
                rightSpeed = HIGH;
            } else {
                leftSpeed = HIGH;
                rightSpeed = LOW;
            }
        }
        if(climbMode == EndgameClimbingMode.DOWN) {
            leftSpeed = -leftSpeed;
            rightSpeed = -rightSpeed;
        }

        leftMotor.set(ControlMode.PercentOutput, disableMovement ? 0.0 : leftSpeed);
        rightMotor.set(ControlMode.PercentOutput, disableMovement ? 0.0 : rightSpeed);

        climbMode = EndgameClimbingMode.NONE;
	}
}