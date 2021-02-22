package frc.robot;

public class AutoNavSlalom implements IRobotMode {

    private IDrive drive;
    private double speed = 0.4;

    public AutoNavSlalom(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void init() {
        driveStraightShort();
    }

    private void driveStraightShort() {
        drive.driveDistance(60, speed, 45, () -> driveStraightLong());
    }

    private void driveStraightLong() {
        drive.driveDistance(120, speed, 90, () -> driveStraightShort1());
    }

    private void driveStraightShort1() {
        drive.driveDistance(60, speed, 135, () -> driveStraightShort2());
    }

    private void driveStraightShort2() {
        drive.driveDistance(30, speed, 90, () -> driveStraight3());
    }

    private void driveStraight3() {
        drive.driveDistance(30, speed, 0, () -> driveStraightShort4());
    }

    private void driveStraightShort4() {
        drive.driveDistance(30, speed, -90, () -> driveStraightShort5());
    }

    private void driveStraightShort5() {
        drive.driveDistance(60, speed, -135, () -> driveStraightLong1());
    }

    private void driveStraightLong1() {
        drive.driveDistance(120, speed, -90, () -> driveStraightShort6());
    }

    private void driveStraightShort6() {
        drive.driveDistance(60, speed, -45, () -> driveStraightShort7());
    }

    private void driveStraightShort7() {
        drive.driveDistance(60, speed, -90, null);
    }
    
    @Override
    public void periodic() {
        //Nothing to do 
    }
}