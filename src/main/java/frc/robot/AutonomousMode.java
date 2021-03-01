 package frc.robot;

 import edu.wpi.first.wpilibj.XboxController;

public class AutonomousMode implements IRobotMode {

    private final XboxController xboxController;
    private final IDrive drive;
    private final ILauncher launcher;

    private IRobotMode autonomousSubMode;

    public AutonomousMode(final IDrive drive, final ILauncher launcher) {
        xboxController = new XboxController(PortMap.USB.XBOXCONTROLLER);

        this.drive = drive;
        this.launcher = launcher;
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
            return AutoMode.AUTONAV_SLALOM;
        }
    }

    private IRobotMode getAutoRobotMode() {
        final AutoMode autoMode = determineAutoMode();
        switch (autoMode) {
            case AUTONAV_BOUNCE:
                return new AutoNavBounce(drive);
            case AUTONAV_BARREL:
                return new AutoNavBarrel(drive);
            case AUTONAV_SLALOM:
                return new AutoNavSlalom(drive);
            case GALACTIC:
                return new AutoGalactic(drive, launcher);
            case AIMING:
                return new AutoAiming(drive, launcher);
            default:
                throw new IllegalArgumentException("invalid autonomous mode");
        }
    }

    
    @Override
    public void init() {
        autonomousSubMode = getAutoRobotMode();
        Debug.log(autonomousSubMode.getClass().getName() + " is now running");
        autonomousSubMode.init();
    }

    @Override
    public void periodic() {
        //Nothing do to 
    }
}
