package frc.robot;

public class AutoNavBarrel implements IRobotMode {

    private IDrive drive;
    private double speed = 0.8;

    public AutoNavBarrel(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void init() { 
        driveStraight();
    }

    @Override
    public void driveStraightLong() { // Step 1
        drive.driveDistance(120, speed, 0 () -> turnRight());
    }

    @Override
    public void turnRight1() { // Step 2
        drive.rotateDegrees(90, () -> driveStraightShort());
    }

    @Override
    public void driveStraightShort() { // Step 3
        drive.driveDistance(30, speed, () -> turnRight2());
    }

    @Override
    public void turnRight2() {
        drive.rotateAbsolute(90, () -> driveStraightShort1());
    }

    @Override
    public void driveStraightShort1() {
        drive.driveDistance(30, speed, 0, () -> turnRight3());
    }

    @Override
    private void turnRight3() {
        drive.rotateAbsolute(90, () -> driveStraightShort2());
    }

    @Override
    private void driveStraightShort2() {
        drive.driveDistance(30, speed, 0, () -> turnRight4());
    }

    @Override
    private void turnRight4() {
        drive.rotateAbsolute(90, () -> driveStraightLong1());
    }

    @Override void driveStraightLong1() {
        drive.driveDistance(124, speed, 0, () -> turnLeft());
    }

    @Override
    private void turnLeft() {
        drive.rotateAbsolute(-90, () -> driveStraightShort3());
    }

    @Override
    private void driveStraightShort3() {
        drive.driveDistance(60, speed, 0, () -> turnLeft2());
    }

    @Override
    private void turnLeft2() {
        drive.rotateAbsolute(-90, () -> driveStraightShort4());
    }

    @Override
    private void driveStraightShort4() {
        drive.driveDistance(30, speed, 0, () -> turnLeft3());
    }

    @Override
    private void turnLeft3() {
        drive.rotateAbsolute(-90, () -> driveStraightLong2());
    }

    @Override
    private void driveStraightLong2() {
        drive.driveDistance(90, speed, 0, () -> turnLeft4());
    }

    @Override
    private void turnLeft4() {
        drive.rotateAbsolute(-90, () -> driveStraightLong3());
    }

    @Override
    private void driveStraightLong3() {
        drive.driveDistance(90, speed, 0, () -> turnLeft5());
    }

    @Override
    private void turnLeft5() {
        drive.rotateAbsolute(-90, () -> driveStraightShort5());
    }

    @Override
    private void driveStraightShort5() {
        drive.driveDistance(30, speed, 0, () -> turnLeft6());
    }

    @Override
    private void turnLeft6() {
        drive.rotateAbsolute(-90, () -> driveStraightLong4());
    }

    @Override
    private void driveStraightLong4() {
        drive.driveDistance(315, speed, 0, null);
    }
   
    @Override
    public void periodic() {
        //Nothing to do 
    }


}