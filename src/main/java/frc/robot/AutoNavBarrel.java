package frc.robot;

public class AutoNavBarrel implements IRobotMode {

    private IDrive drive;
    private double speed = 0.8;

    public AutoNavBarrel(IDrive drive) {
        this.drive = drive;
    }

    public void init() { 
        driveStraightLong();
    }

    public void driveStraightLong() { // Step 1
        drive.driveDistance(120, speed, 0, () -> turnRight1());
    }

    public void turnRight1() { // Step 2
        drive.rotateAbsolute(90, () -> driveStraightShort());
    }

    public void driveStraightShort() { // Step 3
        drive.driveDistance(30, speed, 0, () -> turnRight2());
    }

    public void turnRight2() {
        drive.rotateAbsolute(90, () -> driveStraightShort1());
    }

    public void driveStraightShort1() {
        drive.driveDistance(30, speed, 0, () -> turnRight3());
    }

    private void turnRight3() {
        drive.rotateAbsolute(90, () -> driveStraightShort2());
    }

    private void driveStraightShort2() {
        drive.driveDistance(30, speed, 0, () -> turnRight4());
    }

    private void turnRight4() {
        drive.rotateAbsolute(90, () -> driveStraightLong1());
    }

    private void driveStraightLong1() {
        drive.driveDistance(124, speed, 0, () -> turnLeft());
    }

    private void turnLeft() {
        drive.rotateAbsolute(-90, () -> driveStraightShort3());
    }

    private void driveStraightShort3() {
        drive.driveDistance(60, speed, 0, () -> turnLeft2());
    }

    private void turnLeft2() {
        drive.rotateAbsolute(-90, () -> driveStraightShort4());
    }

    private void driveStraightShort4() {
        drive.driveDistance(30, speed, 0, () -> turnLeft3());
    }

    private void turnLeft3() {
        drive.rotateAbsolute(-90, () -> driveStraightLong2());
    }

    private void driveStraightLong2() {
        drive.driveDistance(90, speed, 0, () -> turnLeft4());
    }

    private void turnLeft4() {
        drive.rotateAbsolute(-90, () -> driveStraightLong3());
    }

    private void driveStraightLong3() {
        drive.driveDistance(90, speed, 0, () -> turnLeft5());
    }

    private void turnLeft5() {
        drive.rotateAbsolute(-90, () -> driveStraightShort5());
    }

    private void driveStraightShort5() {
        drive.driveDistance(30, speed, 0, () -> turnLeft6());
    }

    private void turnLeft6() {
        drive.rotateAbsolute(-90, () -> driveStraightLong4());
    }

    private void driveStraightLong4() {
        drive.driveDistance(315, speed, 0, null);
    }
   
    @Override
    public void periodic() {
        //Nothing to do 
    }


}