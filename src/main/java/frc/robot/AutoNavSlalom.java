package frc.robot;

public class AutoNavSlalom implements IRobotMode {

    private IDrive Drive;
    private double speed = 0.4;

    public AutoNavSlalom(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void init() {
        driveStraightStart();
    }

    @Override
    private void driveStart() {
        drive.rotateAbsolute(-45, completionRoutine, () -> driveStraightShort());
    }

    @Override
    private void driveStraightShort() {
        drive.driveDistance(60, speed, 45, completionRoutine, () -> driveStraightLong());
    }

    @Override
    private void driveStraightLong() {
        drive.driveDistance(120, speed, 45, () -> driveStraightShort1());
    }

    @Override
    private void driveStraightShort1() {
        drive.driveDistance(60, speed, -45, () -> driveStraightShort2());
    }

    @Override
    private void driveStraightShort2() {
        drive.driveDistance(30, speed, -90, () -> driveStraightShort3());
    }

    @Override
    private void driveStraight3() {
        drive.driveDistance(30, speed, -90 () -> driveStart1());
    }

    @Override
    private void driveStart1() {
        drive.rotateAbsolute(-45, () -> driveStraightShort4());
    }

    @Override
    private void driveStraightShort4() {
        drive.driveDistance(60, speed, 45, () -> driveStraightLong1());
    }

    @Override
    private void driveStraightLong1() {
        drive.driveDistance(120, speed, 45, () -> driveStraightShort5());
    }

    @Override
    private void driveStraightShort5() {
        drive.driveDistance(60, speed, -45, () -> driveStraightShort6());
    }

    @Override
    private void driveStraightShort6() {
        drive.driveDistance(75, speed, completionRoutine, null)
    }
    
    @Override
    public void periodic() {
        //Nothing to do 
    }
}