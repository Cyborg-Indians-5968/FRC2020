package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class Drive implements IDrive {

    private MecanumDrive driveBase;

    private DriveMode driveMode;
    private IGyroscopeSensor gyroscope;
    private Runnable currentCompletionRoutine;
    private IEncoder leftEncoder;
    private IEncoder rightEncoder;
    
    private WPI_TalonSRX frontLeftMotor;
    private WPI_TalonSRX frontRightMotor;
    private WPI_TalonSRX rearLeftMotor;
    private WPI_TalonSRX rearRightMotor;

    private double xDirectionSpeed;
    private double yDirectionSpeed;
    private double angularSpeed;
    private double desiredAngle;
    private double desiredDistance;

    private static final double WHEEL_DIAMETER = 8.0; // inches
    private static final double ENCODER_RESOLUTION = 2048.0;

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
    }

    @Override
    public DriveMode getCurrentDriveMode(){
        return driveMode;
    }

    @Override
    public void driveDistance(double distanceInches, double xDirectionSpeed, double yDirectionSpeed) {
        driveDistance(distanceInches, xDirectionSpeed, yDirectionSpeed, null);
    }

    @Override
    public void rotateDegrees(double angle, double angularSpeed) {
        rotateDegrees(angle, angularSpeed, null);
    }

    @Override
    public void driveDistance(double distanceInches, double xDirectionSpeed, double yDirectionSpeed, Runnable completionRoutine) {
        driveMode = DriveMode.AUTODRIVING;
        
        setCompletionRoutine(completionRoutine);
        this.xDirectionSpeed = xDirectionSpeed;
        this.yDirectionSpeed = yDirectionSpeed;
        desiredDistance = distanceInches;

        leftEncoder.reset();
        rightEncoder.reset();
    }

    @Override
    public void rotateDegrees(double angle, double angularSpeed, Runnable completionRoutine) {
        driveMode = DriveMode.AUTODRIVING;
        
        setCompletionRoutine(completionRoutine);
        this.desiredAngle = gyroscope.getYaw() + angle;
        this.angularSpeed = angularSpeed; 
    }

    @Override
    public void driveManual(double xDirectionSpeed, double yDirectionSpeed) {
        driveMode = DriveMode.DRIVERCONTROL;

        double absoluteY = yDirectionSpeed * Math.cos(gyroscope.getYaw()) + xDirectionSpeed * Math.sin(gyroscope.getYaw());
        double absoluteX = -yDirectionSpeed * Math.sin(gyroscope.getYaw()) + xDirectionSpeed * Math.cos(gyroscope.getYaw()); 

        this.xDirectionSpeed = absoluteX;
        this.yDirectionSpeed = absoluteY;
        setCompletionRoutine(null);
    }

    public void stop() {
        driveManual(0.0, 0.0);
        lookAt(0.0, 0.0);
    }

    @Override
    public void lookAt(double angle) {
        this.desiredAngle = angle;
    }

    @Override
    public void lookAt(double angle, double angularSpeed) {
        this.angularSpeed = angularSpeed;
        this.desiredAngle = angle;
    }

    private void setCompletionRoutine(Runnable completionRountime) {
        if (currentCompletionRoutine != null) {
            throw new IllegalStateException("Tried to perform an autonomous action while one was already in progress!");
        }

        currentCompletionRoutine = completionRountime;
    }

    private void handleActionEnd() {
        // Saves currentCompletionRoutine before calling stop() so nothing is cleared
        Runnable oldCompletionRoutine = currentCompletionRoutine;

        // Stops robot from moving
        stop();

        // Dispatch the completion routin if there is one configured
        if (oldCompletionRoutine != null) {
            currentCompletionRoutine = null;
            oldCompletionRoutine.run();
        }

    }

    private void manualControlPeriodic() {
        double deltaAngle = (desiredAngle - gyroscope.getYaw() + (Math.PI * 3)) % (Math.PI * 2) - Math.PI;
        double actualSpeed = angularSpeed * (-deltaAngle / Math.PI);
        
        driveBase.driveCartesian(xDirectionSpeed, yDirectionSpeed, gyroscope.getYaw() != desiredAngle ? actualSpeed : 0);
    }

    @Override
    public void init() {
        currentCompletionRoutine = null;
        stop();
        if(DriverStation.getInstance().isAutonomous()) {
            
        }
    }

    @Override
    public void periodic() {
        if (driveMode == DriveMode.DRIVERCONTROL) {
            manualControlPeriodic();
        } else if (driveMode == DriveMode.AUTODRIVING) {
            double deltaAngle = (desiredAngle + (Math.PI * 3)) % (Math.PI * 2) - Math.PI;
            double actualSpeed = angularSpeed * (-deltaAngle / Math.PI);
            
            driveBase.driveCartesian(yDirectionSpeed, xDirectionSpeed, gyroscope.getYaw() != desiredAngle ? actualSpeed : 0);

            // Check if we've completed our travel
            double averageDistanceTraveled = Math.abs((leftEncoder.getDistance() + rightEncoder.getDistance()) / 2);
            if (averageDistanceTraveled > desiredDistance) {
                handleActionEnd();
            } 
        } else {
            throw new IllegalArgumentException("The drive base controller is in an invalid drive mode.");
        }
    }
}
