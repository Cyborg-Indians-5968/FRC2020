package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;

public class Launcher implements ILauncher {

    private VictorSPX intakeMotor;

    private TalonSRX storageMotor;
    private IEncoder storageEncoder;
    private DigitalInput storageSwitch;
    
    private CANSparkMax shooterMotor;
    private CANPIDController shooterPIDController;

    private enum LauncherMode {
        IDLE,
        FEED_TO_SWITCH,
        STORAGE_INTAKE,
        ADVANCE,
        REVERSE,
        SHOOT,
        DONT_SHOOT,
    }
    private LauncherMode launcherMode = LauncherMode.IDLE;

    private double intakeSpeed = 0.0;
    private double storageSpeed = 0.0;

    private static final double INTAKE_HIGH = 0.65;
    private static final double STORAGE_HIGH = 0.5;

    private static final double ADVANCE_ONE_BALL_ROTATIONS = 16000.0;
    private static final double INITIAL_INTAKE_ROTATIONS = 14000.0;

    // TODO: Tune these constants for each RPM we need
    private double kP = 0.00125;
    private double kI = 0.0;
    private double kD = 0.0;
    private double kF = 0.000215;
    private double maxOutput = 1.0;
    private double minOutput = 0.0;
    private double shooterSetPoint = 0.0;
    private double maxRPM = 2000.0; //2800.0;

    public Launcher() {
        intakeMotor = new VictorSPX(PortMap.CAN.INTAKE_MOTOR_CONTROLLER);

        storageMotor = new TalonSRX(PortMap.CAN.STORAGE_MOTOR_CONTROLLER);
        storageEncoder = new TalonEncoder(storageMotor);
        storageEncoder.setInverted(true);
        storageSwitch = new DigitalInput(PortMap.DIO.BOTTOM_STORAGE);

        shooterMotor = new CANSparkMax(PortMap.CAN.SHOOTER_MOTOR_CONTROLLER, MotorType.kBrushless);
        shooterMotor.setInverted(true);
        shooterPIDController = shooterMotor.getPIDController();
        shooterPIDController.setP(kP);
        shooterPIDController.setI(kI);
        shooterPIDController.setD(kD);
        shooterPIDController.setFF(kF);
        shooterPIDController.setOutputRange(minOutput, maxOutput);
    }

    public void init() {
        stop();
        storageEncoder.reset();
        launcherMode = LauncherMode.IDLE;
    }

    public void stop() {
        intakeSpeed = 0.0;
        storageSpeed = 0.0;
        shooterSetPoint = 0.0;
    }

    public void stopShooting() {
        if(launcherMode == LauncherMode.SHOOT) {
            launcherMode = LauncherMode.DONT_SHOOT;
        }
    }

    public void intake() {
        launcherMode = LauncherMode.FEED_TO_SWITCH;
    }

    public void advance() {
        launcherMode = LauncherMode.ADVANCE;
    }

    public void reverse() {
        launcherMode = LauncherMode.REVERSE;
    }

    public void shoot() {
        launcherMode = LauncherMode.SHOOT;
    }

    public void periodic() {
        stop();

        if(launcherMode == LauncherMode.FEED_TO_SWITCH) {
            if(!storageSwitch.get()) {
                launcherMode = LauncherMode.STORAGE_INTAKE;
                storageEncoder.reset();
            } else {
                intakeSpeed = INTAKE_HIGH;
            }
        } else if(launcherMode == LauncherMode.STORAGE_INTAKE) {
            if(storageEncoder.getPosition() > INITIAL_INTAKE_ROTATIONS) {
                launcherMode = LauncherMode.IDLE;
            } else {
                intakeSpeed = INTAKE_HIGH;
                storageSpeed = STORAGE_HIGH;
            }
        } else if(launcherMode == LauncherMode.ADVANCE) {
            if(storageEncoder.getPosition() > ADVANCE_ONE_BALL_ROTATIONS) {
                launcherMode = LauncherMode.IDLE;
                storageEncoder.reset();
            } else {
                storageSpeed = STORAGE_HIGH;
            }
        } else if(launcherMode == LauncherMode.REVERSE) {
            if(Math.abs(storageEncoder.getPosition()) > ADVANCE_ONE_BALL_ROTATIONS) {
                launcherMode = LauncherMode.IDLE;
                storageEncoder.reset();
            } else {
                storageSpeed = -STORAGE_HIGH;
            }
        } else if(launcherMode == LauncherMode.SHOOT) {
            shooterSetPoint = maxRPM;
        } else if(launcherMode == LauncherMode.DONT_SHOOT) {
            shooterSetPoint = 0.0;
        }

        intakeMotor.set(ControlMode.PercentOutput, intakeSpeed);
        storageMotor.set(ControlMode.PercentOutput, storageSpeed);
        shooterPIDController.setReference(shooterSetPoint, ControlType.kVelocity);
    }
}
