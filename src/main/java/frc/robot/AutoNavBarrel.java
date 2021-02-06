package frc.robot;

public class AutoNav implements IRobotMode {

    private IDrive drive;
    private double rotationSpeed = 0.2;
    private double driveSpeed = 0.4;

    public AutoNav(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void init() { // Directions For BARREL RUN
        // TODO Auto-generated method stub
        driveStraightLong();
        driveStraightLong();
        driveRightLong();
        driveRightLong();
        driveRightLong();
        driveRightLong();
        driveStraightLong();
        driveStraight();
        driveLeftLong();
        driveStraight();
        driveLeftLong();
        driveLeftLong();
        driveStraight();
        driveStraightLong();
        driveLeftLong();
        driveStraightLong();
        driveLeftLong();
        driveStraight();
        driveLeftLong();
        driveStraightLong();
        driveStraightLong();
        driveStraightLong();
        driveStraightLong();
        
    }

    @Override
    public void driveStraight() {
        drive.driveDistance(30.0, driveSpeed, null); // Drivers robot forwards
    }

    @Override
    public void periodic() {
        //Nothing to do 
    }

    @overide
    public void turnRight() {
        drive.rotateDegrees(90.0, rotationSpeed); // Turns robot to the right
    }

    @overide
    public void turnLeft() {
        drive.rotateDegrees(-90.0, rotationSpeed); // Turns robot to the left
    }

    @Override
    public void driveStraightLong() {
        drive.driveDistance(60.0, driveSpeed, null); // Drives robot frowards for longer distance
    }
    
    @Override
    public void driveRightLong() {
        drive.rotateDegrees(90, rotationSpeed); // Turns robot right, then proceeds with driveStraight()
        driveStraight();
    }

    @Override
    public void driveLeftLong() {
        drive.rotateDegrees(-90, rotationSpeed); // Turns robot left, then proceeds with driveStraight()
        driveStraight();
    }


}