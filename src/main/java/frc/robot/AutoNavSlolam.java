package frc.robot;

public class AutoNavSlolam implements IRobotMode {

    private IDrive Drive;
    private double rotationSpeed = 0.2;
    private double driveSpeed = 0.4;

    public AutoNav(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void Init() { // Slolam Auto Code
        
    }
    
    @Override
    public void periodic() {
        //Nothing to do 
    }

    @Override
    public void driveStraight() {
        drive.distance(30, driveSpeed, null);
    }

    @Override
    public void driveStraightLong() {
        drive.driveDistance(60, driveSpeed, null);
    }

    @Override
    public void turnRight() {
        drive.rotationSpeed(90, rotationSpeed);
    }

    @Override
    public void turnLeft() {
        drive.rotationSpeed(-90, rotationSpeed);
    }

    @Override
    public void turnRightLong() {
        drive.rotationSpeed(90, rotationSpeed);
        driveStraight();
    }

    @Override
    public void turnLeftLong() {
        drive.rotationSpeed(-90, rotationSpeed);
        driveStraight();
    }
}