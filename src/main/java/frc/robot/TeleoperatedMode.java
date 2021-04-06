package frc.robot;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.DriverStation;

public class TeleoperatedMode implements IRobotMode {

    private XboxController xboxController;
    private IDrive drive;
    private ILauncher launcher;
    private ILimelight limelight;
    private IColorWheel wheel;

    private String data;
    private double distanceFromTarget;

    private static final double LEFT_STICK_EXPONENT = 3.0;
    private static final double RIGHT_STICK_EXPONENT = 3.0;
    private static final double ROTATION_THRESHOLD = 0.3;

    public TeleoperatedMode(IDrive drive, ILauncher launcher, ILimelight limelight, IColorWheel wheel) {
        xboxController = new XboxController(PortMap.USB.XBOXCONTROLLER);

        this.drive = drive;
        this.launcher = launcher;
        this.limelight = limelight;
        this.wheel = wheel;
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

        double angle = Math.atan2(rightX, rightY);
        
        if(Math.sqrt(Math.pow(rightX, 2) + Math.pow(rightY, 2)) > ROTATION_THRESHOLD) {
            drive.rotateAbsolute(angle);
        }

        // Process Limelight data
        // distance = (targetHeight - robotHeight) / tan(angleFromGroundToLimelightCenter + limelightVerticalOffset)
        distanceFromTarget = (98.25 - 35.5) / Math.tan(Math.toRadians(31.0 + limelight.getVerticalOffset()));
        Debug.logPeriodic("Vertical Offset: " + limelight.getVerticalOffset());
        Debug.logPeriodic("Horizontal Offset: " + limelight.getHorizontalOffset());
        Debug.logPeriodic("Distance: " + distanceFromTarget);
        Debug.logPeriodic("---");

        // Process Peripheral control
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

        if (xboxController.getXButton()) {
            launcher.setDistance(108.0);
            Debug.log("Distance set to yellow and blue!");
        }

        if(xboxController.getYButton()) {
            launcher.setDistance(228.0);
            Debug.log("Distance set to red!");
        }

        //color wheel code 
        //never tested so may not work
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
