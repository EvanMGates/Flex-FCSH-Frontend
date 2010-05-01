package config;

import java.io.BufferedReader;
import java.io.FileReader;

public class ConfigFile
{
    public String targetName = "";
    public String sdkPath = "";
    public String mxmlcArgs = "";
       
    public ConfigFile()
    {
        
    }
    
    public ConfigFile(String configFile)
    {
        BufferedReader inputStream = null;
        try
        {
            inputStream = new BufferedReader(new FileReader(configFile));
            targetName = inputStream.readLine();
            sdkPath = inputStream.readLine();
            mxmlcArgs = inputStream.readLine();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
        finally
        {
            if(inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch(Throwable t)
                {
                    t.printStackTrace();
                }
            }
        }
    }
}
