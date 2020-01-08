package frc.robot;

public class Debug {

    // Don't allow instantiation
    private Debug() {
        
    }
    
    private static boolean isPeriodicLogTick = true;
    private static final long logPeriod = 500; // ms
    private static long lastLogTime = 0;
    
    // Call this method during the update loop in Robot.startCompetition to 
    // update the periodic log timer
    public static void periodic() {
        long currentTime = System.currentTimeMillis();
        
        // If the current time is before the last log tick time, we set the
        // last log tick time to the current time
        // (This prevents issues with the system clock changing while we run.)
        if (currentTime < lastLogTime) {
            lastLogTime = currentTime;
        }
        
        // Compute the time that has elapsed since the last log tick
        long deltaTime = currentTime - lastLogTime;
        
        // If less than logPeriod ms have passed since the last log tick, this
        // isn't a log tick
        if (deltaTime < logPeriod) {
            isPeriodicLogTick = false;
        } else { 
            // Otherwise, this is a log tick so we record as such
            isPeriodicLogTick = true;
            lastLogTime = currentTime;
        }
    }
    
    public static void log(String message) {
        System.out.println(message);
    }
    public static void logPeriodic(String message) {
        if (isPeriodicLogTick) {
            log(message);
        }
    }
}
