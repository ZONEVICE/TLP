package gui;

public class Home extends javax.swing.JFrame {

    private ExecuteTLPBFAC executeTLPBFAC = new ExecuteTLPBFAC();

    public Home() {
        initComponents();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        jSpinner_minutes.setValue(1); // Set 1 minute.
        jComboBox_changeTo.setSelectedIndex(1); // Set BF 8.
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
        jComboBox_changeTo = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jSpinner_minutes = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jButton_ChangeBFWithDelay = new javax.swing.JButton();

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

        jComboBox_changeTo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BF 4", "BF 8", "BF 16", "BF 22", "BF 42" }));

        jLabel2.setText("after");

        jLabel3.setText("minutes >");

        jButton_ChangeBFWithDelay.setText("OK");
        jButton_ChangeBFWithDelay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ChangeBFWithDelayActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton_BF4, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton_BF8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton_BF16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_BF22)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton_BF42))
                            .addComponent(jButton_ToggleACBATTMode, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox_changeTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinner_minutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_ChangeBFWithDelay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox_changeTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jSpinner_minutes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jButton_ChangeBFWithDelay))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton_ToggleACBATTMode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_BF22, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_BF42, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_BF8, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_BF4, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_BF16, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_BF4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BF4ActionPerformed
        this.setState(javax.swing.JFrame.ICONIFIED);
        jTextArea_StateMSG.setText(executeTLPBFAC.exec(4));
    }//GEN-LAST:event_jButton_BF4ActionPerformed

    private void jButton_BF8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BF8ActionPerformed
        this.setState(javax.swing.JFrame.ICONIFIED);
        jTextArea_StateMSG.setText(executeTLPBFAC.exec(8));
    }//GEN-LAST:event_jButton_BF8ActionPerformed

    private void jButton_BF16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BF16ActionPerformed
        this.setState(javax.swing.JFrame.ICONIFIED);
        jTextArea_StateMSG.setText(executeTLPBFAC.exec(16));
    }//GEN-LAST:event_jButton_BF16ActionPerformed

    private void jButton_BF22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BF22ActionPerformed
        this.setState(javax.swing.JFrame.ICONIFIED);
        jTextArea_StateMSG.setText(executeTLPBFAC.exec(22));
    }//GEN-LAST:event_jButton_BF22ActionPerformed

    private void jButton_BF42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BF42ActionPerformed
        this.setState(javax.swing.JFrame.ICONIFIED);
        jTextArea_StateMSG.setText(executeTLPBFAC.exec(42));
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
        int minutes = (int) jSpinner_minutes.getValue();
        System.out.println(minutes);
        if (minutes <= 0) {
            minutes = 1;
            jSpinner_minutes.setValue(1);
        }

        // Get minutes.
        StateRepo.sleep_timer_seconds = minutes * 60 * 1000; // Production line.
        //StateRepo.sleep_timer_seconds = minutes * 1000;// Testing line.

        // Get BF option.
        String bf = this.jComboBox_changeTo.getItemAt(this.jComboBox_changeTo.getSelectedIndex());
        bf = bf.substring(3);

        this.delayedActionExec = new DelayedActionExec(this.executeTLPBFAC, Integer.parseInt(bf));
        
        this.setState(javax.swing.JFrame.ICONIFIED);
    }//GEN-LAST:event_jButton_ChangeBFWithDelayActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_BF16;
    private javax.swing.JButton jButton_BF22;
    private javax.swing.JButton jButton_BF4;
    private javax.swing.JButton jButton_BF42;
    private javax.swing.JButton jButton_BF8;
    private javax.swing.JButton jButton_ChangeBFWithDelay;
    private javax.swing.JButton jButton_ToggleACBATTMode;
    private javax.swing.JComboBox<String> jComboBox_changeTo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner_minutes;
    private javax.swing.JTextArea jTextArea_StateMSG;
    // End of variables declaration//GEN-END:variables
}
