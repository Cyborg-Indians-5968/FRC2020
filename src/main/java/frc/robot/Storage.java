package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class Storage implements IMotorPeripheral{

    private TalonSRX storageMotor;
    private DigitalInput topSwitch;
    private DigitalInput bottomSwitch;
    private IEncoder encoder;
    public StorageMode storageMode;

    private double motorSpeed;
    private int balls = 0;
    private boolean isAdvancing;

    private static final double HIGH = 0.9;
    private static final double REVERSE = -0.9;
    private static final double LOW = 0.0;
    private static final double DELAY = 2.0;
    private static final int MAXBALLS = 5;

    public Storage(){
        storageMotor = new TalonSRX(PortMap.CAN.STORAGE_MOTOR_CONTROLLER);
        storageMotor.setInverted(true);

        bottomSwitch = new DigitalInput(0); // TODO: Set actual port
        topSwitch = new DigitalInput(1); // TODO: Set actual port

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

    public void reverse(){
        isAdvancing = true;
        backtrack();
        Timer.delay(DELAY);
        stop();
        isAdvancing = false;
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
    public void stop(){
        motorSpeed = LOW;
    }

    public void advance(){
        isAdvancing = true;
        start();
        Timer.delay(DELAY);
        stop();
        isAdvancing = false;
    }

    @Override
    public void init(){
        storageMode = StorageMode.COLLECTING;
        stop();
        isAdvancing = false;
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
