package gui;

import java.awt.Color;

public class Home extends javax.swing.JFrame {

    private ExecuteTLPBFAC executeTLPBFAC = new ExecuteTLPBFAC();

    public Home() {
        initComponents();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        jSpinner_delayMinutes.setValue(1); // Set 1 minute.
        jComboBox_delayChangeTo.setSelectedIndex(1); // Set BF 8.
        jSpinner_watchSeconds.setValue(5); // Set 5 seconds.
        jComboBox_watchChangeTo.setSelectedIndex(1); // Set BF 8.
    }

    public DelayedActionExec delayedActionExec; // Thread for set BF after x minutes.

    public void SetStateMSGText(String msg) {
        this.jTextArea_StateMSG.setText(msg);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_StateMSG = new javax.swing.JTextArea();
        jButton_BF4 = new javax.swing.JButton();
        jButton_BF8 = new javax.swing.JButton();
        jButton_BF16 = new javax.swing.JButton();
        jButton_BF22 = new javax.swing.JButton();
        jButton_BF42 = new javax.swing.JButton();
        jButton_ToggleACBATTMode = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBox_delayChangeTo = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jSpinner_delayMinutes = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jButton_ChangeBFWithDelay = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jComboBox_watchChangeTo = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jSpinner_watchSeconds = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        jButton_watchOnOff = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea_StateMSG.setColumns(20);
        jTextArea_StateMSG.setLineWrap(true);
        jTextArea_StateMSG.setRows(5);
        jScrollPane1.setViewportView(jTextArea_StateMSG);

        jButton_BF4.setFont(new java.awt.Font("Liberation Sans", 0, 24)); // NOI18N
        jButton_BF4.setText("BF 4");
        jButton_BF4.setToolTipText("");
        jButton_BF4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_BF4ActionPerformed(evt);
            }
        });

        jButton_BF8.setFont(new java.awt.Font("Liberation Sans", 0, 24)); // NOI18N
        jButton_BF8.setText("BF 8");
        jButton_BF8.setToolTipText("");
        jButton_BF8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_BF8ActionPerformed(evt);
            }
        });

        jButton_BF16.setFont(new java.awt.Font("Liberation Sans", 0, 24)); // NOI18N
        jButton_BF16.setText("BF 16");
        jButton_BF16.setToolTipText("");
        jButton_BF16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_BF16ActionPerformed(evt);
            }
        });

        jButton_BF22.setFont(new java.awt.Font("Liberation Sans", 0, 24)); // NOI18N
        jButton_BF22.setText("BF 22");
        jButton_BF22.setToolTipText("");
        jButton_BF22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_BF22ActionPerformed(evt);
            }
        });

        jButton_BF42.setFont(new java.awt.Font("Liberation Sans", 0, 24)); // NOI18N
        jButton_BF42.setText("BF 42");
        jButton_BF42.setToolTipText("");
        jButton_BF42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_BF42ActionPerformed(evt);
            }
        });

        jButton_ToggleACBATTMode.setText("Toggle AC/BATTERY mode");
        jButton_ToggleACBATTMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ToggleACBATTModeActionPerformed(evt);
            }
        });

        jLabel1.setText("Change to");

        jComboBox_delayChangeTo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BF 4", "BF 8", "BF 16", "BF 22", "BF 42" }));

        jLabel2.setText("after");

        jLabel3.setText("minutes >");

        jButton_ChangeBFWithDelay.setText("OK");
        jButton_ChangeBFWithDelay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ChangeBFWithDelayActionPerformed(evt);
            }
        });

        jLabel4.setText("Change to");

        jComboBox_watchChangeTo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BF 4", "BF 8", "BF 16", "BF 22", "BF 42" }));

        jLabel5.setText("after mouse idle for");

        jLabel6.setText("seconds >");

        jButton_watchOnOff.setText("OK");
        jButton_watchOnOff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_watchOnOffActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_delayChangeTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSpinner_delayMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_ChangeBFWithDelay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton_BF4, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_BF8, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_BF16, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_BF22, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_BF42, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton_ToggleACBATTMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_watchChangeTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_watchSeconds, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_watchOnOff, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox_watchChangeTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jSpinner_watchSeconds, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jButton_watchOnOff))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox_delayChangeTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jSpinner_delayMinutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jButton_ChangeBFWithDelay))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_ToggleACBATTMode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_BF4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_BF8, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_BF16, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_BF22, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_BF42, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_BF4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BF4ActionPerformed
        this.setState(javax.swing.JFrame.ICONIFIED);
        jTextArea_StateMSG.setText(executeTLPBFAC.exec(4));
        StateRepo.lastManualEnergyOptionSelected = 4;
    }//GEN-LAST:event_jButton_BF4ActionPerformed

    private void jButton_BF8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BF8ActionPerformed
        this.setState(javax.swing.JFrame.ICONIFIED);
        jTextArea_StateMSG.setText(executeTLPBFAC.exec(8));
        StateRepo.lastManualEnergyOptionSelected = 8;
    }//GEN-LAST:event_jButton_BF8ActionPerformed

    private void jButton_BF16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BF16ActionPerformed
        this.setState(javax.swing.JFrame.ICONIFIED);
        jTextArea_StateMSG.setText(executeTLPBFAC.exec(16));
        StateRepo.lastManualEnergyOptionSelected = 16;
    }//GEN-LAST:event_jButton_BF16ActionPerformed

    private void jButton_BF22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BF22ActionPerformed
        this.setState(javax.swing.JFrame.ICONIFIED);
        jTextArea_StateMSG.setText(executeTLPBFAC.exec(22));
        StateRepo.lastManualEnergyOptionSelected = 22;
    }//GEN-LAST:event_jButton_BF22ActionPerformed

    private void jButton_BF42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BF42ActionPerformed
        this.setState(javax.swing.JFrame.ICONIFIED);
        jTextArea_StateMSG.setText(executeTLPBFAC.exec(42));
        StateRepo.lastManualEnergyOptionSelected = 42;
    }//GEN-LAST:event_jButton_BF42ActionPerformed

    private void jButton_ToggleACBATTModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ToggleACBATTModeActionPerformed
        // Determine mode to toggle.
        if (StateRepo.EnergyMode == 0) {
            StateRepo.EnergyMode = 1;
        } else {
            StateRepo.EnergyMode = 0;
        }

        // Change buttons text.
        if (StateRepo.EnergyMode == 1) {
            jButton_BF4.setText("AC 4");
            jButton_BF8.setText("AC 8");
            jButton_BF16.setText("AC 16");
            jButton_BF22.setText("AC 22");
            jButton_BF42.setText("AC 42");
        } else {
            jButton_BF4.setText("BF 4");
            jButton_BF8.setText("BF 8");
            jButton_BF16.setText("BF 16");
            jButton_BF22.setText("BF 22");
            jButton_BF42.setText("BF 42");
        }

    }//GEN-LAST:event_jButton_ToggleACBATTModeActionPerformed

    private void jButton_ChangeBFWithDelayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ChangeBFWithDelayActionPerformed
        // Validate minutes.
        int minutes = (int) jSpinner_delayMinutes.getValue();
        System.out.println(minutes);
        if (minutes <= 0) {
            minutes = 1;
            jSpinner_delayMinutes.setValue(1);
        }

        // Get minutes.
        StateRepo.sleep_timer_seconds = minutes * 60 * 1000; // Production line.
        //StateRepo.sleep_timer_seconds = minutes * 1000;// Testing line.

        // Get BF option.
        String bf = this.jComboBox_delayChangeTo.getItemAt(this.jComboBox_delayChangeTo.getSelectedIndex());
        bf = bf.substring(3);

        this.delayedActionExec = new DelayedActionExec(this.executeTLPBFAC, Integer.parseInt(bf));

        // Minimize the window.
        this.setState(javax.swing.JFrame.ICONIFIED);
    }//GEN-LAST:event_jButton_ChangeBFWithDelayActionPerformed

    private void jButton_watchOnOffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_watchOnOffActionPerformed
        // Validate spinner/seconds value.
        int seconds = (int) jSpinner_watchSeconds.getValue();
        if (seconds <= 0) {
            seconds = 5;
            jSpinner_watchSeconds.setValue(5);
        }
        
        // Retrieve energy option.
        String bf = this.jComboBox_watchChangeTo.getItemAt(this.jComboBox_watchChangeTo.getSelectedIndex());
        bf = bf.substring(3);

        // GUI and state update.
        if (StateRepo.watcherMouseAction.run_thread) {
            StateRepo.watcherMouseAction.run_thread = false;
            StateRepo.watcherMouseAction = new WatcherMouseAction();
            jButton_watchOnOff.setBackground(null);
        } else {
            StateRepo.watcherMouseAction.run_thread = true;
            StateRepo.watcherMouseAction.setSettings(seconds, Integer.parseInt(bf));
            StateRepo.watcherMouseAction.start();
            jButton_watchOnOff.setBackground(Color.green);
            this.setState(javax.swing.JFrame.ICONIFIED);
        }
    }//GEN-LAST:event_jButton_watchOnOffActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_BF16;
    private javax.swing.JButton jButton_BF22;
    private javax.swing.JButton jButton_BF4;
    private javax.swing.JButton jButton_BF42;
    private javax.swing.JButton jButton_BF8;
    private javax.swing.JButton jButton_ChangeBFWithDelay;
    private javax.swing.JButton jButton_ToggleACBATTMode;
    private javax.swing.JButton jButton_watchOnOff;
    private javax.swing.JComboBox<String> jComboBox_delayChangeTo;
    private javax.swing.JComboBox<String> jComboBox_watchChangeTo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner_delayMinutes;
    private javax.swing.JSpinner jSpinner_watchSeconds;
    private javax.swing.JTextArea jTextArea_StateMSG;
    // End of variables declaration//GEN-END:variables
}
