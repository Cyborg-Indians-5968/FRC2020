package frc.robot;

public class AutoNavBounce implements IRobotMode {

    private IDrive Drive;
    private double angularSpeed = 0.2;
    private double forwardSpeed = 0.4;

    public AutoNavBounce(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void init() { // Bounce Auto Code

    }

    @Override
    public void driveStraight() {
        drive.distance(30, forwardSpeed, null);
    }

    @Override
    public void driveStraightLong() {
        drive.driveDistance(60, forwardSpeed, null);
    }

    @Override
    public void turnRight() {
        drive.lookAt(90, angularSpeed);
    }

    @Override
    public void turnLeft() {
        drive.lookAt(-90, angularSpeed);
    }

    @Override
    public void turnRightLong() {
        drive.lookAt(90, angularSpeed);
        driveStraight();
    }

    @Override
    public void turnLeftLong() {
        drive.lookAt(-90, angularSpeed);
        driveStraight();
    }

    @Override
    public void periodic() {
        //Nothing to do 
    }
}