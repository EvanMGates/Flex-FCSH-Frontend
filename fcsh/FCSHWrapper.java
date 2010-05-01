package fcsh;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;

/**
 *
 * @author Evan Gates
 */
public class FCSHWrapper extends SwingWorker<String, String> {

    private Process fcshProcess;
    private BufferedWriter inputToFcsh;
    private List<String> fcshExecutablePath;
    private String compilationParameters;
    private Thread outputThread;
    private Thread errorThread;
    
    private StringBuffer incomingText = new StringBuffer();

    public String getText()
    {
        String text = incomingText.toString();
        incomingText.delete(0, text.length());
        return text;
    }

    public FCSHWrapper(List<String> command,  String compilationParameters) {
        this.compilationParameters = compilationParameters;
        this.fcshExecutablePath = command;
    }

    public void recompile()
    {
        try {
            inputToFcsh.write("compile 1\r\n");
            inputToFcsh.flush();
        } catch (IOException ex) {
            Logger.getLogger(FCSHWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    private Thread createDumpThread(InputStream withStream)
    {
        BufferedReader ouputFromFcsh = new BufferedReader(new InputStreamReader(withStream));
        FCSHOutputDump dumper = new FCSHOutputDump(ouputFromFcsh, incomingText);
        return new Thread(dumper);
    }

    private void cleanup()
    {
        outputThread.interrupt();
        try
        {
            inputToFcsh.close();
            fcshProcess.destroy();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground()
        throws Exception
    {
        try
        {
            ProcessBuilder pBuilder = new ProcessBuilder(fcshExecutablePath);
            fcshProcess = pBuilder.start();
     
            outputThread = createDumpThread(fcshProcess.getInputStream());
            outputThread.start();
            
            errorThread = createDumpThread(fcshProcess.getErrorStream());
            errorThread.start();
            
            OutputStream inputToFcshStream = fcshProcess.getOutputStream();
            inputToFcsh = new BufferedWriter(new OutputStreamWriter(inputToFcshStream));
          
            inputToFcsh.write("mxmlc " + compilationParameters + "\r\n");
            inputToFcsh.flush();
            
            while(!isCancelled())
            {
                synchronized(incomingText)
                {
                    incomingText.wait();
                    firePropertyChange("text", 0, 1);
                }
            }
            
        } catch (Exception ex) {
            cleanup();
        }
        return "done";
    }

}