package frc.robot;

public class LineAuto implements IRobotMode {

    private IDrive drive;

    private static final double DRIVE_DISTANCE = 24.0; // inches
    private static final double DRIVE_SPEED = 0.5;

    public LineAuto(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void init() {
        drive.driveDistance(DRIVE_DISTANCE, 0, DRIVE_SPEED);
    }

    @Override
    public void periodic() {
    // Nothing to do
    }
}
