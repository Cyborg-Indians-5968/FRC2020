package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class Drive implements IDrive {

    private MecanumDrive driveBase;

    private DriveMode driveMode;
    private Runnable currentCompletionRoutine;
    
    private WPI_TalonSRX frontLeftMotor;
    private WPI_TalonSRX frontRightMotor;
    private WPI_TalonSRX rearLeftMotor;
    private WPI_TalonSRX rearRightMotor;

    public Drive() {
        
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

        frontLeftMotor.setInverted(true);
        frontRightMotor.setInverted(false);
        rearLeftMotor.setInverted(true);
        rearRightMotor.setInverted(false);

        driveBase = new MecanumDrive(frontLeftMotor, rearLeftMotor, frontRightMotor, rearRightMotor);
    }

    @Override
    public DriveMode getCurrentDriveMode(){
        return driveMode;
    }

    @Override
    public void driveDistance(double distanceInches, double xDirectionSpeed, double yDirectionSpeed) {

    }

    @Override
    public void rotateDegrees(double angle, double angularSpeed) {

    }

    @Override
    public void driveDistance(double distanceInches, double xDirectionSpeed, double yDirectionSpeed, Runnable completionRoutine) {
        setCompletionRoutine(completionRoutine);
       
    }

    @Override
    public void rotateDegrees(double relativeAngle, double angularSpeed, Runnable completionRoutine) {
        setCompletionRoutine(null);
        driveMode = DriveMode.DRIVERCONTROL;
        driveBase.driveCartesian(0, 0, angularSpeed);
    }

    @Override
    public void driveManual(double xDirectionSpeed, double yDirectionSpeed) {
        setCompletionRoutine(null);
        driveMode = DriveMode.DRIVERCONTROL;
        driveBase.driveCartesian(yDirectionSpeed, xDirectionSpeed, 0);
    }

    public void stop() {
        driveManual(0.0, 0.0);
        lookAt(0.0, 0.0);
    }

    @Override
    public void lookAt(double angle, double speed) {
    }

    @Override
    public void maintainHeading() {
        /*
        driveMode = DriveMode.DRIVERCONTROL;
        setCompletionRoutine(null);
        desiredAngle = gyroscope.getYaw();
        rotationSpeed = MAINTAINING_HEADING_SPEED;
        */
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

    }
}
