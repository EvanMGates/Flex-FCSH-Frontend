package views;

import java.io.IOException;

import javax.swing.UIManager;

import config.ConfigFile;

/* 
 * MainView.java 
 *
 * Created on Mar 3, 2010, 10:58:25 PM
 */

/**
 * 
 * @author Evan Gates
 */
public class MainView
    extends javax.swing.JFrame
{
    private static final long serialVersionUID = 1L;
    
    private static ConfigFile configFile = null;
    
    
    public MainView()
        throws IOException
    {
        initComponents();
    }

    private void initComponents()
    {

        consoleContainer = new javax.swing.JTabbedPane();
        sdkPath = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        mxmlcArguments = new javax.swing.JTextArea();
        compileButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        targetName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        setTitle("Fast Flex Compile      (vershun.com)");
        
        mxmlcArguments.setColumns(20);
        mxmlcArguments.setRows(5);
        mxmlcArguments.setLineWrap(true);
        mxmlcArguments.setWrapStyleWord(true);
        jScrollPane1.setViewportView(mxmlcArguments);

        compileButton.setText("Spawn New Compiler");
        compileButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                compileButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Target Name:");

        targetName.setText(configFile.targetName);

        jLabel2.setText("SDK Path (use forward slashes, no trailing slash):");

        jLabel3.setText("MXMLC command-line arguments (the \"mxmlc\" is implied)");
        
        mxmlcArguments.setText(configFile.mxmlcArgs);
        sdkPath.setText(configFile.sdkPath);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup().addContainerGap().addGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(consoleContainer,
                    javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 958,
                    Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 958,
                    Short.MAX_VALUE).addGroup(
                    layout.createSequentialGroup().addGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1)
                            .addComponent(jLabel2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(sdkPath,
                                javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 888,
                                Short.MAX_VALUE).addComponent(targetName, javax.swing.GroupLayout.PREFERRED_SIZE, 175,
                                javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jLabel3).addComponent(
                    compileButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE,
                    958, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
            layout.createSequentialGroup().addGap(12, 12, 12).addGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1)
                    .addComponent(targetName, javax.swing.GroupLayout.PREFERRED_SIZE,
                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(
                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(sdkPath,
                    javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
                    javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel2)).addGap(6, 6, 6)
                .addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 54,
                    javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
                    javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(compileButton)
                .addGap(18, 18, 18).addComponent(consoleContainer, javax.swing.GroupLayout.DEFAULT_SIZE, 523,
                    Short.MAX_VALUE).addContainerGap()));

        pack();
    }

    private void compileButtonActionPerformed(java.awt.event.ActionEvent evt)
    {
        CompilerView newCompiler = new CompilerView(sdkPath.getText(), mxmlcArguments.getText());
        consoleContainer.addTab(targetName.getText(), newCompiler);
        compileButton.setEnabled(false);
        mxmlcArguments.setEnabled(false);
        sdkPath.setEnabled(false);
        targetName.setEnabled(false);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        if(args.length > 0)
        {
            try
            {
            configFile = new ConfigFile(args[0]);
            }
            catch(Throwable t)
            {
                t.printStackTrace();
            }
        }
        else
        {
            configFile = new ConfigFile();
        }
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    new MainView().setVisible(true);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        });
    }

    private javax.swing.JButton compileButton;
    private javax.swing.JTabbedPane consoleContainer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea mxmlcArguments;
    private javax.swing.JTextField sdkPath;
    private javax.swing.JTextField targetName;
}
