package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.controller.PIDController;

public class Drive implements IDrive {

    private MecanumDrive driveBase;

    private DriveMode driveMode;
    private IGyroscopeSensor gyroscope;
    private Runnable currentCompletionRoutine;
    private IEncoder leftEncoder;
    private IEncoder rightEncoder;

    // Just proportional gain works for now, add other gains if needed
    private PIDController rotationController;
    private double kP = 0.6;
    private double kI = 0.0;
    private double kD = 0.0;
    
    private WPI_TalonSRX frontLeftMotor;
    private WPI_TalonSRX frontRightMotor;
    private WPI_TalonSRX rearLeftMotor;
    private WPI_TalonSRX rearRightMotor;

    // Teleoperated
    private double forwardSpeed;
    private double strafeSpeed;
    private double angularSpeed;
    private double desiredAngle;

    // Autonomous
    private double autoSpeed;
    private double autoAngleDegrees;
    private double desiredDistance;

    private static final double WHEEL_DIAMETER = 8.0; // inches
    private static final double ENCODER_RESOLUTION = 2048.0;
    private static final double ROTATION_TOLERANCE_DEGREES = 2.0;

    public Drive(IGyroscopeSensor gyroscope) {
        
        this.gyroscope = gyroscope;

        frontLeftMotor = new WPI_TalonSRX(PortMap.CAN.FRONT_LEFT_MOTOR);
        frontRightMotor = new WPI_TalonSRX(PortMap.CAN.FRONT_RIGHT_MOTOR);
        rearLeftMotor = new WPI_TalonSRX(PortMap.CAN.REAR_LEFT_MOTOR);
        rearRightMotor = new WPI_TalonSRX(PortMap.CAN.REAR_RIGHT_MOTOR);

        frontLeftMotor.configFactoryDefault();
        frontRightMotor.configFactoryDefault();
        rearLeftMotor.configFactoryDefault();
        rearRightMotor.configFactoryDefault();

        frontLeftMotor.setNeutralMode(NeutralMode.Brake);
        frontRightMotor.setNeutralMode(NeutralMode.Brake);
        rearLeftMotor.setNeutralMode(NeutralMode.Brake);
        rearRightMotor.setNeutralMode(NeutralMode.Brake);

        frontLeftMotor.setInverted(false);
        frontRightMotor.setInverted(false);
        rearLeftMotor.setInverted(false);
        rearRightMotor.setInverted(false);

        driveBase = new MecanumDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);

        leftEncoder = new TalonEncoder(frontLeftMotor);
        rightEncoder = new TalonEncoder(frontRightMotor);

        double distancePerPulse = (WHEEL_DIAMETER * Math.PI) / ENCODER_RESOLUTION;

        leftEncoder.setDistancePerPulse(distancePerPulse);
        rightEncoder.setDistancePerPulse(distancePerPulse);

        leftEncoder.setInverted(true);

        rotationController = new PIDController(kP, kI, kD);
        rotationController.enableContinuousInput(-Math.PI, Math.PI);
        rotationController.setTolerance(Math.toRadians(ROTATION_TOLERANCE_DEGREES));
    }

    @Override
    public DriveMode getCurrentDriveMode(){
        return driveMode;
    }

    @Override
    public void rotateRelative(double angle) {
        driveMode = DriveMode.IDLEORMANUAL;
        
        desiredAngle = gyroscope.getYaw() + angle; 
    }

    @Override
    public void rotateAbsolute(double angle) {
        driveMode = DriveMode.IDLEORMANUAL;

        desiredAngle = angle;
    }

    @Override
    public void driveDistance(double distanceInches, double speed, double angle) {
        driveDistance(distanceInches, speed, angle, null);
    }

    @Override
    public void driveDistance(double distanceInches, double speed, double angle, Runnable completionRoutine) {
        leftEncoder.reset();
        rightEncoder.reset();
        Debug.log("I should've reset the encoders");
        
        driveMode = DriveMode.AUTODRIVING;
        
        setCompletionRoutine(completionRoutine);
        desiredDistance = distanceInches;
        autoSpeed = speed;
        autoAngleDegrees = -angle;
    }

    @Override
    public void rotateRelative(double angle, Runnable completionRoutine) {
        driveMode = DriveMode.AUTODRIVING;
        
        setCompletionRoutine(completionRoutine);
        desiredAngle = gyroscope.getYaw() + Math.toRadians(angle);
    }

    @Override
    public void rotateAbsolute(double angle, Runnable completionRoutine) {
        driveMode = DriveMode.AUTODRIVING;
        
        setCompletionRoutine(completionRoutine);
        desiredAngle = Math.toRadians(angle);
    }

    public void driveManualImplementation(double forwardSpeed, double strafeSpeed) {
        driveMode = DriveMode.IDLEORMANUAL;

        double absoluteForward = forwardSpeed * Math.cos(gyroscope.getYaw()) + strafeSpeed * Math.sin(gyroscope.getYaw());
        double absoluteStrafe = -forwardSpeed * Math.sin(gyroscope.getYaw()) + strafeSpeed * Math.cos(gyroscope.getYaw()); 

        this.forwardSpeed = absoluteForward;
        this.strafeSpeed = absoluteStrafe;
    }

    @Override
    public void driveManual(double forwardSpeed, double strafeSpeed) {
        setCompletionRoutine(null);
        driveManualImplementation(forwardSpeed, strafeSpeed);
    }

    @Override
    public void stop() {
        driveManualImplementation(0.0, 0.0);
        desiredAngle = gyroscope.getYaw();
        angularSpeed = 0.0;
    }

    @Override
    public void init() {
        currentCompletionRoutine = null;
        stop();
        leftEncoder.reset();
        rightEncoder.reset();
    }

    private void setCompletionRoutine(Runnable completionRountime) {
        if (currentCompletionRoutine != null) {
            throw new IllegalStateException("Tried to perform an autonomous action while one was already in progress!");
        }

        currentCompletionRoutine = completionRountime;
    }

    private void handleActionEnd() {
        stop();
        
        if (currentCompletionRoutine != null) {
            Runnable oldCompletionRoutine = currentCompletionRoutine;
            currentCompletionRoutine = null;
            oldCompletionRoutine.run();
        }
    }

    private void manualControlPeriodic() {
        angularSpeed = rotationController.calculate(gyroscope.getYaw(), desiredAngle);
        
        driveBase.driveCartesian(strafeSpeed, forwardSpeed, angularSpeed);
    }

    @Override
    public void periodic() {
        if (driveMode == DriveMode.IDLEORMANUAL) {
            manualControlPeriodic();
        } else if (driveMode == DriveMode.AUTODRIVING) {
            angularSpeed = rotationController.calculate(gyroscope.getYaw(), desiredAngle);
        
            driveBase.drivePolar(autoSpeed, autoAngleDegrees, angularSpeed);

            // Check if we've completed our travel
            double averageDistanceTraveled = Math.abs((leftEncoder.getDistance() + rightEncoder.getDistance()) / 2);
            Debug.logPeriodic("Distance: " + averageDistanceTraveled);
            if (averageDistanceTraveled > desiredDistance) {
                handleActionEnd();
            } 
        } else {
            throw new IllegalArgumentException("The drive base controller is in an invalid drive mode.");
        }
    }
}
