package gui;

public class StateRepo {
    // 0=Battery, 1=AC.
    static int EnergyMode = 0;
    static int sleep_timer_seconds = 0;
    // GUI object to is static to be accessed by any context in the app.
    static Home home;
    // Mouse Watcher toggler.
    static WatcherMouseAction watcherMouseAction = new WatcherMouseAction();
    // Stores the last energy level manually set by the user. Default 22.
    static int lastManualEnergyOptionSelected = 22;
}
