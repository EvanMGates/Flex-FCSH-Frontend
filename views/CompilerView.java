package views;

import fcsh.FCSHWrapper;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * CompilerView.java
 *
 * Created on Mar 4, 2010, 10:33:35 PM
 */

/**
 *
 * @author Evan Gates
 */
public class CompilerView extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	
	/** Creates new form CompilerView */
    public CompilerView(String sdkPath, String compilationParameters) {
        try {
            initComponents();
            spawnFCSHProcess(sdkPath, compilationParameters);
        } catch (IOException ex) {
            Logger.getLogger(CompilerView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private FCSHWrapper wrapper;
    private ConsoleHighlighter highlighter;

    private void spawnFCSHProcess(String sdkPath, String compilationParameters) throws IOException {
        List<String> fcshExecutable = new ArrayList<String>();
        fcshExecutable.add(sdkPath + "/bin/fcsh.exe");
        wrapper = new FCSHWrapper(fcshExecutable, compilationParameters.replace("\n", " ").replace("\\", "/"));
        wrapper.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                String newBlockOfText = wrapper.getText();
                String[] newLines = newBlockOfText.split("\n");
                for(String line : newLines)
                {
                    if(line.length() == 0) continue;
                    line += "\n";
                    consoleOutput.append(line);
                    moveOutputAreaDown();
                    highlighter.processNewLine(line);
                }
            }});
        wrapper.execute();
    }

    private void initComponents() {
        recompile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        consoleOutput = new javax.swing.JTextArea();
        
        highlighter = new ConsoleHighlighter(consoleOutput);

        addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                killFcshProcess(evt);
            }
        });

        recompile.setText("Recompile");
        recompile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recompileActionPerformed(evt);
            }
        });

        consoleOutput.setColumns(20);
        consoleOutput.setEditable(false);
        consoleOutput.setRows(5);
        consoleOutput.setLineWrap(true);
        consoleOutput.setWrapStyleWord(true);
        
        jScrollPane1.setViewportView(consoleOutput);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE)
                    .addComponent(recompile, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 807, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(recompile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addContainerGap())
        );
    }
    
    private void moveOutputAreaDown()
    {
        Rectangle visible = consoleOutput.getVisibleRect();
        visible.y = consoleOutput.getHeight() - visible.height + 10;
        consoleOutput.scrollRectToVisible(visible);
    }

    private void recompileActionPerformed(java.awt.event.ActionEvent evt) {
        wrapper.recompile();
    }

    private void killFcshProcess(java.awt.event.ContainerEvent evt) {
        wrapper.cancel(true);
    }
    
    private javax.swing.JTextArea consoleOutput;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton recompile;

}
