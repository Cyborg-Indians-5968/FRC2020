package frc.robot;

public interface ILauncher {

    public void init();

    public void stop();

    public void stopShooting();

    public void intake();

    public void advance();

    public void reverse();

    public void shoot();

    public void periodic();

}