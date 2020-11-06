package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Endgame implements IEndgame {

    private CANSparkMax rightMotor;
    private CANSparkMax leftMotor;

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
        rightMotor = new CANSparkMax(PortMap.CAN.ENDGAME_RIGHT, MotorType.kBrushless);
        leftMotor = new CANSparkMax(PortMap.CAN.ENDGAME_LEFT, MotorType.kBrushless);
        leftMotor.setInverted(true);
    }

    public void raise() {
        climbMode = EndgameClimbingMode.UP;
    }

    public void lower() {
        climbMode = EndgameClimbingMode.DOWN;
    }

    public void resetEncoders() {
        leftMotor.getEncoder().setPosition(0);
        rightMotor.getEncoder().setPosition(0);
    }

    public void init() {
        climbMode = EndgameClimbingMode.NONE;
        resetEncoders();
    }

	public void periodic() {
        
        double leftPosition = leftMotor.getEncoder().getPosition();
        double rightPosition = rightMotor.getEncoder().getPosition();
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

        leftMotor.set(disableMovement ? 0.0 : leftSpeed);
        rightMotor.set(disableMovement ? 0.0 : rightSpeed);

        climbMode = EndgameClimbingMode.NONE;
	}
}