package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation;

public class TeleoperatedMode implements IRobotMode {

    private XboxController xboxController;
    private IDrive drive;
    private ILauncher launcher;
    private IColorWheel wheel;
    private IEndgame endgame;
    
    private String data;

    private static final double LEFT_STICK_EXPONENT = 3.0;
    private static final double RIGHT_STICK_EXPONENT = 3.0;
    private static final double ROTATION_SPEED_THRESHOLD = 0.3;

    public TeleoperatedMode(IDrive drive, ILauncher launcher, IColorWheel wheel, IEndgame endgame) {
        xboxController = new XboxController(PortMap.USB.XBOXCONTROLLER);

        this.drive = drive;
        this.launcher = launcher;
        this.wheel = wheel;
        this.endgame = endgame;
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

        if (xboxController.getBumper(Hand.kRight)){
            launcher.shoot();
        }

        if (xboxController.getBumper(Hand.kLeft)) {
            launcher.intake();
        }

        if (xboxController.getAButton()) {
            launcher.reverse();
        }

        if (xboxController.getBButton()) {
            launcher.advance();
        }

        if (xboxController.getYButton()) {
            endgame.raise();
        }

        if (xboxController.getXButton()) {
            endgame.lower();
        } 

        /*
        data = DriverStation.getInstance().getGameSpecificMessage();
        if (xboxController.getBumper(Hand.kRight)) {
            if (data.length() > 0){
                wheel.spinToColor(data.charAt(0));
            } else {
                wheel.spinRevolutions();
            }
        }
        */
    }
}   
