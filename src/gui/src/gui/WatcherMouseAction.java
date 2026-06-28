package gui;

import java.awt.MouseInfo;
import java.awt.Point;
import java.util.Calendar;
import java.util.Date;

public class WatcherMouseAction extends Thread {

    private int current_mouse_position_x,
            current_mouse_position_y,
            fixed_mouse_position_x,
            fixed_mouse_position_y;

    public volatile boolean run_thread = false;

    private Date lastMouseMovementDate;

    private int secondsRequiredToExecuteTLP = 0;
    
    private int energyOption = 22;

    private final int checkDelay = 2000;
    
    private boolean TLPChangeDone = false;
    
    private final ExecuteTLPBFAC executeTLPBFAC = new ExecuteTLPBFAC();

    public void setSettings(int secondsRequiredToExecuteTLP, int energyOption) {
        this.secondsRequiredToExecuteTLP = secondsRequiredToExecuteTLP;
        this.energyOption = energyOption;
    }

    private boolean hasMouseBeenMoved() {
        return ((this.current_mouse_position_x != this.fixed_mouse_position_x) || (this.current_mouse_position_y != this.fixed_mouse_position_y));
    }

    private void saveCurrentMousePosition() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        this.current_mouse_position_x = p.x;
        this.current_mouse_position_y = p.y;
    }

    private void saveFixedMousePosition() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        this.fixed_mouse_position_x = p.x;
        this.fixed_mouse_position_y = p.y;
    }

    private boolean hasMouseNotBeenMovedForTheMarkedTimer() {
        // Last time mouse movement + timer value.
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.lastMouseMovementDate);
        calendar.add(Calendar.SECOND, this.secondsRequiredToExecuteTLP);
        Date lastMouseMovementDatePlusTimer = calendar.getTime();

        // Current date.
        Date currentDate = new Date();

        // -1: now is before. 0: equal dates. 1: now is after.
        return currentDate.compareTo(lastMouseMovementDatePlusTimer) > -1;
    }

    @Override
    public void run() {
        try {

            // Set current mouse position.
            saveCurrentMousePosition();
            saveFixedMousePosition();
            
            // Las detected mouse movement (virtual in the context of this thread).
            this.lastMouseMovementDate = new Date();

            while (this.run_thread) {

                // Kill switch.
                if (!this.run_thread) {
                    break;
                }

                // Wait 2 seconds before next comprobation
                Thread.sleep(checkDelay);

                // Kill switch.
                if (!this.run_thread) {
                    break;
                }

                saveCurrentMousePosition();

                if (hasMouseBeenMoved()) {
                    // Save new mouse position.
                    saveCurrentMousePosition();
                    saveFixedMousePosition();
                    // Save last time the mouse was detected in a new position.
                    this.lastMouseMovementDate = new Date();
                    // Checks if TLP configuration has been done.
                    if (this.TLPChangeDone) {
                        // Reverse the TLP option to the last known manual one.
                        String stageMSG = executeTLPBFAC.exec(StateRepo.lastManualEnergyOptionSelected);
                        StateRepo.home.SetStateMSGText(stageMSG);
                        this.TLPChangeDone = false;
                    }
                } else {
                    // Checks if the mouse position has been kept enough time to
                    //  set the new TLP configuration. Applies only if not applied already.
                    if (hasMouseNotBeenMovedForTheMarkedTimer() && !this.TLPChangeDone) {
                        // Execute configured TLP option.
                        String stageMSG = executeTLPBFAC.exec(this.energyOption);
                        StateRepo.home.SetStateMSGText(stageMSG);
                        this.TLPChangeDone = true;
                    } else {
                        continue;
                    }
                }

            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }
}
