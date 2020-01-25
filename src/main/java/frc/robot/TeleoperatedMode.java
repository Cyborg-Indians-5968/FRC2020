package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;

public class TeleoperatedMode implements IRobotMode {

    private XboxController xboxController;
    private IDrive drive;
    private IMotorPeripheral roller;
    private IMotorPeripheral launcher;
    private IMotorPeripheral storage;

    private static final double LEFT_STICK_EXPONENT = 3.0;
    private static final double RIGHT_STICK_EXPONENT = 3.0;
    private static final double ROTATION_SPEED_THRESHOLD = 0.3;

    public TeleoperatedMode(IDrive drive) {

        xboxController = new XboxController(PortMap.USB.XBOXCONTROLLER);

        this.drive = drive;
        // this.launcher = launcher;
        // this.storage = storage;
    }

    @Override
    public void init() {
        drive.init();
    }

    @Override
    public void periodic() {

        // Process Linear Motion Controls
        double leftX = xboxController.getX(Hand.kLeft);
        double leftY = -xboxController.getY(Hand.kLeft);

        leftX = Math.pow(leftX, LEFT_STICK_EXPONENT);
        leftY = Math.pow(leftY, LEFT_STICK_EXPONENT);

        drive.driveManual(leftY, leftX);

        // Process Rotation control
        double rightX = xboxController.getX(Hand.kRight);
        double rightY = -xboxController.getY(Hand.kRight);

        rightX = Math.pow(rightX, RIGHT_STICK_EXPONENT);
        rightY = Math.pow(rightY, RIGHT_STICK_EXPONENT);

        double angle = rightX >= 0 ? Math.atan2(rightY, rightX) : -Math.atan2(rightY, rightX);
        double rotationSpeed = Math.sqrt(Math.pow(rightX, 2) + Math.pow(rightY, 2));

        if (rotationSpeed > ROTATION_SPEED_THRESHOLD) {
            drive.lookAt(angle, rotationSpeed);
        } else {
            drive.lookAt(angle, 0);
        }

        // if (xboxController.getBumper(Hand.kRight)){
        // roller.start();
        // } else {
        // roller.stop();
        // }

        // if (xboxController.getBumper(Hand.kRight)) {
        // storage.advance();
        // launcher.start();
        // } else {
        // launcher.stop();
        // }

    }
}
