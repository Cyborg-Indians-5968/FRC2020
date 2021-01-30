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
            } else if (limelight == B {
                return AutoMode.GALACTIC_B;
            }
            */
            return null;
            //TODO: determine if running galacitc A or B
        } else if (xboxController.getPOV() == 180) {
            return AutoMode.SHOOTING;
        } else {
            return null;
        }
    }

    private IRobotMode getAutoRobotMode() {
        AutoMode autoMode = determineAutoMode();
        switch (autoMode) {
            case AUTONAV:
                return new AutoNav(drive);
            case GALACTIC_A:
                return new AutoGalacticA(drive, launcher);
            case GALACTIC_B:
                return new AutoGalacticB(drive, launcher);
            case SHOOTING:
                return new AutoShooter(drive, launcher);
            default:
                throw new IllegalArgumentException("invalid autonomous mode");
        }
    }

    
    @Override
    public void init() {
        determineAutoMode();
        Debug.log(getAutoRobotMode().toString());
    }

    @Override
    public void periodic() {
        //Nothing do to 
    }
}
