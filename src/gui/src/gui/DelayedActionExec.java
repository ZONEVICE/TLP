package gui;

public class DelayedActionExec extends Thread {
    
    private DelayedAction delayedAction = null;
    private int delayedAction_intOption = -1;
    
    public DelayedActionExec(DelayedAction delayedAction, int delayedAction_intOption) {
        this.delayedAction = delayedAction;
        this.delayedAction_intOption = delayedAction_intOption;
        this.start();
    }
    
    @Override
    public void run() {
        try {
            Thread.sleep(StateRepo.sleep_timer_seconds);
            System.out.println("Done waiting for " + StateRepo.sleep_timer_seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        String stageMSG = this.delayedAction.exec(this.delayedAction_intOption);
        StateRepo.home.SetStateMSGText(stageMSG);
    }
}
