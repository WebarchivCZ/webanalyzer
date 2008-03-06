/*
 * PlainText.java
 *
 * Created on �tvrtok, 2007, apr�l 12, 0:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.archive.analyzer;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

/**
 *
 * @author Ivan Vlcek
 */
public class PlainText {
    
    private String plainContent;
    
    /** Creates a new instance of PlainText */
    public PlainText(URL url)   {
        if (url == null) 
            throw new NullPointerException("url = null");
        
        EditorKit kit = new HTMLEditorKit();
        Document doc = kit.createDefaultDocument();
        
        // The Document class does not yet handle charset's properly.
        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        
        try {
            //open connection to URL
            URLConnection conn = url.openConnection();
            Reader reader;            
            reader = new InputStreamReader(conn.getInputStream(), "windows-1250");            
            
            //Parse the HTML
            kit.read(reader,doc, 0);
            //The HTML text is now stored in the document           
            //Text ulozeny v retazci plainContent uz ocisteny od White Spaces
            plainContent = getNoWhiteSpacesText(
                    doc.getText(0, doc.getLength()));
            
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println(" Vynikma pri I/O operacii, metoda getPlainText." +
                    " HTTP ERROR 400 alebo 500 pri konstruktore PlainText");
            //zakomentoval som vypis zasobniku a len som napisal chybove hlasenie
            //v dosledku coho sa mi vyhodila vynimka pri analyzovani stranky, 
            //ak sa tu vyhodi vynimka, stranky by som nemal analyzovat.
            plainContent = null;
//            ex.printStackTrace();
        }
    }
    
    /** Vrati text (retazec) bez HTML tagov, aj bez bielych miest az na medzery
     *  medzi slovami.
     */
    public String getPlainText() {
        return plainContent;
    }
    
    /** Vrati z predaneho textu s bielymi miestami White spaces, retazec
     *  bez bielych miest.
     */
    public String getNoWhiteSpacesText(String text) {
        if (text == null) 
            throw new NullPointerException("text = null");
        //Split search string into individual terms.
        Pattern p = Pattern.compile("[\\s]+");
        String[] terms = p.split(text);
        //Check to see if each term matches.
        StringBuffer pageBuffer = new StringBuffer();
        for (int i = 0; i < terms.length; i++) {
            pageBuffer.append(terms[i] + " ");
        }
        return pageBuffer.toString();
    }    
}
