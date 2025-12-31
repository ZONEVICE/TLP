package gui;

public class Home extends javax.swing.JFrame {
    public Home() {
        initComponents();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
    
    public String ExecuteTLPVBF(int bf_value) {
        String msg;
        try {
            ProcessBuilder pb = new ProcessBuilder("/usr/local/bin/tlpv", "bf", String.valueOf(bf_value));
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
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
                        .addComponent(jButton_BF42)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
        jTextArea_StateMSG.setText(ExecuteTLPVBF(4));
    }//GEN-LAST:event_jButton_BF4ActionPerformed

    private void jButton_BF8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BF8ActionPerformed
        jTextArea_StateMSG.setText(ExecuteTLPVBF(8));
    }//GEN-LAST:event_jButton_BF8ActionPerformed

    private void jButton_BF16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BF16ActionPerformed
        jTextArea_StateMSG.setText(ExecuteTLPVBF(16));
    }//GEN-LAST:event_jButton_BF16ActionPerformed

    private void jButton_BF22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BF22ActionPerformed
        jTextArea_StateMSG.setText(ExecuteTLPVBF(22));
    }//GEN-LAST:event_jButton_BF22ActionPerformed

    private void jButton_BF42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_BF42ActionPerformed
        jTextArea_StateMSG.setText(ExecuteTLPVBF(42));
    }//GEN-LAST:event_jButton_BF42ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_BF16;
    private javax.swing.JButton jButton_BF22;
    private javax.swing.JButton jButton_BF4;
    private javax.swing.JButton jButton_BF42;
    private javax.swing.JButton jButton_BF8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_StateMSG;
    // End of variables declaration//GEN-END:variables
}
