package gui;

public class ExecuteTLPBFAC implements DelayedAction{
    
    @Override
    public String exec(int bf_value) {
        
        String energy_argument = null;
        
        if (StateRepo.EnergyMode == 0) {
            energy_argument = "bf";
        } else {
            energy_argument = "af";
        }
        
        String msg;
        try {
            ProcessBuilder pb = new ProcessBuilder("/usr/local/bin/tlpv", energy_argument, String.valueOf(bf_value));
            System.out.println("Command to execute: " + pb.toString());
            pb.redirectErrorStream(true);
            Process process = pb.start();

            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()));
            java.util.List<String> outputLines = new java.util.ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                outputLines.add(line);
            }
            process.waitFor();
            String[] result = outputLines.toArray(new String[0]);
            msg = String.join("\n", result);
            return msg;
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
