package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;

public class Launcher implements ILauncher {

    private VictorSPX intakeMotor;

    private TalonSRX storageMotor;
    private IEncoder storageEncoder;
    private DigitalInput storageSwitch;
    
    private CANSparkMax shooterMotor;

    private enum LauncherMode {
        IDLE,
        FEED_TO_SWITCH,
        STORAGE_INTAKE,
        ADVANCE,
        REVERSE,
        SHOOT,
    }
    private LauncherMode launcherMode = LauncherMode.IDLE;

    private double intakeSpeed = 0.0;
    private double storageSpeed = 0.0;

    private static final double INTAKE_HIGH = 0.5;
    private static final double STORAGE_HIGH = 0.5;

    private static final double ADVANCE_ONE_BALL_ROTATIONS = 16000.0;
    private static final double INITIAL_INTAKE_ROTATIONS = 14000.0;

    private static final double SHOOTER_ENABLED_RPM = 2400.0; //2800.0;
    private double shooterGoalRpm = 0.0;
    private double shooterPower = 0.0;
    private double shooterPowerVelocity = 0.0;
    private static final double SHOOTER_POWER_ACCELERATION = 0.0001;
    private static final double MAX_RPM_ERROR = 300.0;

    public Launcher() {
        intakeMotor = new VictorSPX(PortMap.CAN.INTAKE_MOTOR_CONTROLLER);
        intakeMotor.setInverted(true);

        storageMotor = new TalonSRX(PortMap.CAN.STORAGE_MOTOR_CONTROLLER);
        storageEncoder = new TalonEncoder(storageMotor);
        storageEncoder.setInverted(true);
        storageSwitch = new DigitalInput(PortMap.DIO.BOTTOM_STORAGE);

        shooterMotor = new CANSparkMax(PortMap.CAN.SHOOTER_MOTOR_CONTROLLER, MotorType.kBrushless);
        shooterMotor.setInverted(true);
    }

    public void init() {
        stop();
        storageEncoder.reset();
        launcherMode = LauncherMode.IDLE;
    }

    public void stop() {
        intakeSpeed = 0.0;
        storageSpeed = 0.0;
        shooterGoalRpm = 0.0;
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
            double shooterRpm = shooterMotor.getEncoder().getVelocity();
            shooterGoalRpm = SHOOTER_ENABLED_RPM;

            if(Math.abs(shooterGoalRpm - shooterRpm) > MAX_RPM_ERROR) {
                shooterPowerVelocity += SHOOTER_POWER_ACCELERATION;
            } else {
                shooterPowerVelocity = SHOOTER_POWER_ACCELERATION;
            }

            if(shooterRpm < shooterGoalRpm) {
                shooterPower += shooterPowerVelocity;
            } else if(shooterRpm > shooterGoalRpm) {
                shooterPower -= shooterPowerVelocity;
            }

            if (shooterPower < 0.1 && shooterGoalRpm != 0.0) {
                shooterPower = 0.1;
            }
        }

        intakeMotor.set(ControlMode.PercentOutput, intakeSpeed);
        storageMotor.set(ControlMode.PercentOutput, storageSpeed);
        shooterMotor.set(shooterPower);
    }
}
