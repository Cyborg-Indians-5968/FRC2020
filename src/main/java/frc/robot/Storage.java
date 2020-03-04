package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Storage implements IMotorPeripheral{

    private TalonSRX storageMotor;
    private IEncoder encoder;
    private DigitalInput topSwitch;
    private DigitalInput bottomSwitch;
    public StorageMode storageMode;

    private double motorSpeed;
    private int balls = 0;
    private boolean isAdvancing;

    private static final double HIGH = 0.9;
    private static final double REVERSE = -0.9;
    private static final double LOW = 0.0;
    private static final double ROTATIONS_PER_BALL = 1.0;
    private static final int MAXBALLS = 5;

    public Storage(){
        storageMotor = new TalonSRX(PortMap.CAN.STORAGE_MOTOR_CONTROLLER);
        storageMotor.setInverted(true);

        bottomSwitch = new DigitalInput(PortMap.DIO.BOTTOM_STORAGE);
        topSwitch = new DigitalInput(PortMap.DIO.TOP_STORAGE);

        encoder = new TalonEncoder(storageMotor);
        //encoder.setInverted(true);
        encoder.reset();

        storageMode = StorageMode.COLLECTING;

        stop();
    }

    @Override
    public void start(){
        motorSpeed = HIGH;
    }

    public void backtrack(){
        motorSpeed = REVERSE;
    }

    @Override
    public void stop(){
        motorSpeed = LOW;
    }

    @Override
    public void init(){
        storageMode = StorageMode.COLLECTING;
        stop();
        isAdvancing = false;
        encoder.reset();
    }

    public void advance(){
        while(encoder.getPosition() != ROTATIONS_PER_BALL) {
            isAdvancing = true;
            start();
        }
        stop();
        isAdvancing = false;
        encoder.reset();
    }

    public void reverse(){
        while(encoder.getPosition() != -ROTATIONS_PER_BALL) {
            isAdvancing = true;
            backtrack();
        }
        stop();
        isAdvancing = false;
        encoder.reset();
    }

    public void prime(){
        if(storageMode != StorageMode.FIRING){
            for(int i = 0; i < MAXBALLS - balls; i++) {
                advance();
            }
        }
        storageMode = StorageMode.FIRING;
    }

    public void unprime(){
        if(storageMode != StorageMode.COLLECTING && balls > 0) {
            for(int i = 0; i < MAXBALLS - balls; i++) {
                reverse();
            }
        }
        storageMode = StorageMode.COLLECTING;
    }

    @Override
    public void periodic(){
        if(!bottomSwitch.get() && !isAdvancing){
            advance();
            balls++;
        } else if(!topSwitch.get()) {
            balls--;
        }
        if(balls == 0) {
            storageMode = StorageMode.COLLECTING;
        } else if(balls == 5) {
            storageMode = StorageMode.FIRING;
        }

        storageMotor.set(ControlMode.PercentOutput, motorSpeed);
    }

}
