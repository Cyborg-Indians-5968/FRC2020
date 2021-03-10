 package frc.robot;

 import edu.wpi.first.wpilibj.XboxController;

public class AutonomousMode implements IRobotMode {

    private final XboxController xboxController;
    private final IDrive drive;
    private final ILauncher launcher;
    private final ILimelight limelight;

    public AutonomousMode(final IDrive drive, final ILauncher launcher, final ILimelight limelight) {
        xboxController = new XboxController(PortMap.USB.XBOXCONTROLLER);

        this.drive = drive;
        this.launcher = launcher;
        this.limelight = limelight;
    }

    private AutoMode determineAutoMode() {
        if (xboxController.getPOV() == 0) {
            return AutoMode.AIMING;
        } else if (xboxController.getPOV() == 180) {
            return AutoMode.AUTONAV_BARREL;
        } else if (xboxController.getPOV() == 90) {
            return AutoMode.AUTONAV_BOUNCE;
        } else if (xboxController.getPOV() == 270) {
            return AutoMode.AUTONAV_SLALOM;
        } else {
            return null;
        }
    }

    private IRobotMode getAutoRobotMode() {
        final AutoMode autoMode = determineAutoMode();
        switch (autoMode) {
            case AIMING:
                return new AutoAiming(drive, launcher, limelight);
            case AUTONAV_BARREL:
                return new AutoNavBarrel(drive);
            case AUTONAV_BOUNCE:
                return new AutoNavBounce(drive);
            case AUTONAV_SLALOM:
                return new AutoNavSlalom(drive);
            default: 
                throw new IllegalArgumentException("invalid autonomous mode");
        }
    }

    
    @Override
    public void init() {
        determineAutoMode();
        Debug.log(getAutoRobotMode().getClass().getName());
    }

    @Override
    public void periodic() {
        //Nothing do to 
    }
}
