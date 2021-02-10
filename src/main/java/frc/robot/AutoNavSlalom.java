package frc.robot;

public class AutoNavSlalom implements IRobotMode {

    private IDrive Drive;
    private double forwardSpeed = 0.4;
    private double strafeSpeed = 0.1;
    private double angularSpeed = 0.1;

    public AutoNavSlalom(IDrive drive) {
        this.drive = drive;
    }

    @Override
    public void init() { // Slalom Auto Code
        -45AngleMovement();
        driveStraightLong();
        45AngleMovement();
        looptyLoop();
        driveStraight();
        45AngleMovement();
        driveStraightShort();




    }

    @Override
    public void driveStraightShort() { // Moves robot forwards 60 inches
        drive.driveDistance(60, forwardSpeed, null);

    }

    @Override
    public void -45AngleMovement() { // -45 Degrees to the left, moves 60 inches
        drive.lookAt(-45, angularSpeed, null);
        drive.driveDistance(60, forwardSpeed, null);
        drive.lookAt(45, angularSpeed, null);
    }

    @Override
    public void driveStraightLong() { // Drives the robot for a longer distance
        drive.driveDistance(150, forwardSpeed, null);
    }

    @Override
    public void 45AngleMovement() { // 45 Degrees to the right, 60 inch movements
        drive.lookAt(45, angularSpeed, null);
        drive.driveDistance(60, forwardSpeed, null);
        drive.lookAt(-45, angularSpeed, null);
    }

    @Override
    public void looptyLoop() { // Shortest loop around the  end dot
        drive.driveDistance(30, forwardSpeed, null);
        drive.lookAt(-90, angularSpeed, null);
        drive.driveDistance(30, forwardSpeed, null);
        drive.lookAt(-90, angularSpeed, null);
        drive.lookAt(-45, angularSpeed, null);
        drive.driveDistance(60, forwardSpeed, null);
        drive.lookAt(45, angularSpeed, null);
    }

    @Override
    public void periodic() {
        //Nothing to do 
    }
}