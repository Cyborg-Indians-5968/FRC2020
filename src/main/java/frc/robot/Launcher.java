package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import java.util.Map;

public class Launcher implements ILauncher {

    private VictorSPX intakeMotor;

    private TalonSRX storageMotor;
    private IEncoder storageEncoder;
    private DigitalInput storageSwitch;
    
    private CANSparkMax shooterMotor;
    private CANPIDController shooterPIDController;

    private enum StorageMode {
        IDLE,
        FEED_TO_SWITCH,
        STORAGE_INTAKE,
        ADVANCE,
        REVERSE,
    }
    private StorageMode storageMode = StorageMode.IDLE;
    private enum ShooterMode {
        IDLE,
        SHOOT,
    }
    private ShooterMode shooterMode = ShooterMode.IDLE;

    private double intakeSpeed = 0.0;
    private double storageSpeed = 0.0;

    private static final double INTAKE_HIGH = 0.50;
    private static final double STORAGE_HIGH = 0.5;

    private static final double ADVANCE_ONE_BALL_ROTATIONS = 16000.0;
    private static final double INITIAL_INTAKE_ROTATIONS = 18000.0;

    // TODO: Tune these constants for each RPM we need
    // This map stores arrays of gains for each distance we need to shoot from.
    // Order: kP, kI, kD, kF
    private Map<Double, double[]> gainsMap = Map.of(
        60.0,  new double[]{0.0, 0.0, 0.0, 0.0},
        120.0, new double[]{0.0, 0.0, 0.0, 0.0},
        168.0, new double[]{0.00125, 0.0, 0.0, 0.000215},
        240.0, new double[]{0.0, 0.0, 0.0, 0.0}
    );
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
        shooterPIDController.setOutputRange(minOutput, maxOutput);
        setGains(168.0); // This is temporary so code does what it did before
    }

    @Override
    public void init() {
        stop();
        storageEncoder.reset();
        storageMode = StorageMode.IDLE;
        shooterMode = ShooterMode.IDLE;
    }

    @Override
    public void stop() {
        intakeSpeed = 0.0;
        storageSpeed = 0.0;
        shooterSetPoint = 0.0;
    }

    @Override
    public void intake() {
        storageMode = StorageMode.FEED_TO_SWITCH;
    }

    @Override
    public void advance() {
        storageMode = StorageMode.ADVANCE;
    }

    @Override
    public void reverse() {
        storageMode = StorageMode.REVERSE;
    }

    @Override
    public void shoot() {
        shooterMode = ShooterMode.SHOOT;
    }

    public void setGains(double distance) {
        // We may need to add some approximation (e.g. if(distance > 65 && distance < 75) { gainsMap.get(60); })
        // if driving isn't accurate enough
        double[] gains = gainsMap.get(distance);

        shooterPIDController.setP(gains[0]);
        shooterPIDController.setI(gains[1]);
        shooterPIDController.setD(gains[2]);
        shooterPIDController.setFF(gains[3]);
    }

    @Override
    public void periodic() {
        stop();

        if(storageMode == StorageMode.FEED_TO_SWITCH) {

            if(!storageSwitch.get()) {
                storageMode = StorageMode.STORAGE_INTAKE;
                storageEncoder.reset();
            } else {
                intakeSpeed = INTAKE_HIGH;
            }

        } else if(storageMode == StorageMode.STORAGE_INTAKE) {

            if(storageEncoder.getPosition() > INITIAL_INTAKE_ROTATIONS) {
                storageMode = StorageMode.IDLE;
            } else {
                intakeSpeed = INTAKE_HIGH;
                storageSpeed = STORAGE_HIGH;
            }

        } else if(storageMode == StorageMode.ADVANCE) {

            if(storageEncoder.getPosition() > ADVANCE_ONE_BALL_ROTATIONS) {
                storageMode = StorageMode.IDLE;
                storageEncoder.reset();
            } else {
                storageSpeed = STORAGE_HIGH;
            }

        } else if(storageMode == StorageMode.REVERSE) {

            if(Math.abs(storageEncoder.getPosition()) > ADVANCE_ONE_BALL_ROTATIONS) {
                storageMode = StorageMode.IDLE;
                storageEncoder.reset();
            } else {
                storageSpeed = -STORAGE_HIGH;
            }

        }
        
        if(shooterMode == ShooterMode.SHOOT) {

            shooterSetPoint = maxRPM;
            shooterMode = ShooterMode.IDLE;

        }

        intakeMotor.set(ControlMode.PercentOutput, intakeSpeed);
        storageMotor.set(ControlMode.PercentOutput, storageSpeed);
        shooterPIDController.setReference(shooterSetPoint, ControlType.kVelocity);
    }
}
