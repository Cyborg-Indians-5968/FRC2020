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

    private static final double INTAKE_HIGH = 0.5;
    private static final double STORAGE_HIGH = 0.5;

    private static final double ADVANCE_ONE_BALL_ROTATIONS = 20000.0;
    private static final double INITIAL_INTAKE_ROTATIONS = 30000.0;

    // TODO: Tune these constants for each RPM we need
    // This map stores arrays of gains and angular velocities for each distance we need to shoot from.
    private Map<Double, double[]> gainsMap = Map.of(
        //           Order: kP,      kI,  kD,  kF,       maxRPM
        108.0, new double[]{0.001,   0.0, 0.0, 0.000185, 2000.0}, // This reaches setpoint faster but only reaches 1960 RPM
        168.0, new double[]{0.00125, 0.0, 0.0, 0.000215, 2000.0}, // This reaches setpoint slower but actually reaches 2000 RPM
        228.0, new double[]{0.00125, 0.0, 0.0, 0.000215, 2200.0}
    );
    private double maxOutput = 1.0;
    private double minOutput = 0.0;
    private double shooterSetPoint = 0.0;
    private double maxRPM = 0.0;

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
        setDistance(228.0); // This is temporary until we have Limelight code
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

    @Override
    public void setDistance(double distance) {
        if(distance > 96.0 && distance < 120.0) {
            distance = 108.0;
        } else if(distance > 156.0 && distance < 180.0) {
            distance = 168.0;
        } else if(distance > 216.0 && distance < 240.0) {
            distance = 228.0;
        } else {
            distance = 108.0;
            Debug.log("Invalid distance!");
        }
        
        double[] gains = gainsMap.get(distance);

        shooterPIDController.setP(gains[0]);
        shooterPIDController.setI(gains[1]);
        shooterPIDController.setD(gains[2]);
        shooterPIDController.setFF(gains[3]);

        maxRPM = gains[4];
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
