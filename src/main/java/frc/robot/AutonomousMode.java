 package frc.robot;

 import edu.wpi.first.wpilibj.XboxController;

public class AutonomousMode implements IRobotMode {

    private XboxController xboxController;
    private IDrive drive;
    private ILauncher launcher;

    public AutonomousMode(IDrive drive, ILauncher launcher) {
        xboxController = new XboxController(PortMap.USB.XBOXCONTROLLER);

        this.drive = drive;
        this.launcher = launcher;
    }

    private AutoMode determineAutoMode() {
        if (xboxController.getPOV() == 0) {
            return AutoMode.AUTONAV;
        } else if (xboxController.getPOV() == 90) {
            /*
            if (limelight == A) {
                return AutoMode.GALACTIC_A;
            } else {
                return AutoMode.GALACTIC_B;
            }
            */
            return null;
            //TODO: determine if running galacitc red or blue
        } else if (xboxController.getPOV() == 180) {
            return AutoMode.AIMING;
        } else {
            return null;
        }
    }

    private IRobotMode getAutoRobotMode() {
        AutoMode autoMode = determineAutoMode();
        switch (autoMode) {
            case AUTONAV_BOUNCE:
                return new AutoNavBounce(drive);
            case AUTONAV_BARREL:
                return new AutoNavBarrel(drive);
            case AUTONAV_SLALOM:
                return new AutoNavSlalom(drive);
            case GALACTIC_A_RED:
                return new AutoGalacticA(drive, launcher);
            case GALACTIC_B_RED:
                return new AutoGalacticB(drive, launcher);
            case GALACTIC_A_BLUE:
                return new AutoGalacticABlue(drive, launcher);
            case GALACTIC_B_BLUE:
                return new (drive, launcher);
            case AIMING:
                return new AutoAiming(drive, launcher);
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
