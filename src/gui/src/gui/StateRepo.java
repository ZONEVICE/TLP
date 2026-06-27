package gui;

public class StateRepo {
    // 0=Battery, 1=AC.
    public static int EnergyMode = 0;
    public static int sleep_timer_seconds = 0;
    // GUI object to is static to be accessed by any context in the app.
    static Home home;
}
