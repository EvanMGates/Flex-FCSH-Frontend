package views;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

public class ConsoleHighlighter
{
    private JTextArea console;
    
    private final HighlightPainter greenPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(188, 255,
        188));
    private final HighlightPainter yellowPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 255,
        188));
    private final HighlightPainter redPainter = new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 188,
        188));
    
    private Map<String, HighlightPainter> wordToLinePainter = new HashMap<String, HighlightPainter>();
    
    public ConsoleHighlighter(JTextArea console)
    {
        this.console = console;
        buildWordHighlighters();
    }
    
    public void processNewLine(String line)
    {
        addHighlightsForLine(line);
    }
    
    private void buildWordHighlighters()
    {
        wordToLinePainter.put(".swf", greenPainter);
        wordToLinePainter.put("Warning: ", yellowPainter);
        wordToLinePainter.put("Error: ", redPainter);
    }
    
    private void addHighlightsForLine(String line)
    {
        Highlighter h = console.getHighlighter();
        String text = console.getText();
        
        for(Map.Entry<String, HighlightPainter> wordEntry : wordToLinePainter.entrySet())
        {
            if(line.indexOf(wordEntry.getKey()) == -1) continue;
            
            int targetIndex = text.lastIndexOf(wordEntry.getKey());
            if (targetIndex != -1)
            {
                try
                {
                    int beginningIndex = text.lastIndexOf("\n", targetIndex);
                    int endIndex = text.length() - 1;
                    h.addHighlight(beginningIndex, endIndex, wordEntry.getValue());
                }
                catch (BadLocationException ble)
                {
                    ble.printStackTrace();
                }
            }
        }
    }    
}
