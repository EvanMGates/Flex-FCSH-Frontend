package fcsh;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 
 * @author Evan Gates
 */
public class FCSHOutputDump
    implements Runnable
{

    private StringBuffer outputText;

    private BufferedReader ouputFromFcsh;

    public FCSHOutputDump(BufferedReader outputFromFcsh, StringBuffer incomingText)
    {
        this.outputText = incomingText;
        this.ouputFromFcsh = outputFromFcsh;
    }

    public void run()
    {
        try
        {
            String line;
            while ((line = ouputFromFcsh.readLine()) != null)
            {
                synchronized(outputText)
                {
                    outputText.append(line + "\n");
                    outputText.notify();
                }
            }
        }
        catch (Exception ex)
        {
            cleanup();
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void cleanup()
    {
        try
        {
            ouputFromFcsh.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
