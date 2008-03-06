/*
 * NewClass.java
 *
 * Created on Štvrtok, 2007, jún 14, 23:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.archive.test;

/**
 *
 * @author praso
 */
import java.io.*;
import java.net.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

class GetHTMLText {
    
    private static int urlLenght;
    
    public static void main(String[] args)
    throws Exception {
        EditorKit kit = new HTMLEditorKit();
        Document doc = kit.createDefaultDocument();
        
        // The Document class does not yet handle charset's properly.
        doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        
        // Create a reader on the HTML content.
        String arg = "http://praso.webzdarma.cz/index.html";
        Reader rd = getReader(arg);
        
        /* Get pageContents.
         */
        
            //Open connection to URL for reading.
            BufferedReader reader = new BufferedReader(rd);
            
            // Set reader at the begining.
            // Kontrola markovania.
            if (!(reader.markSupported()))
                System.err.println("Stream reader nieje znackovatelny");   
            System.out.println("Velkost obsahu URL : " + urlLenght);
            reader.mark(urlLenght);
            
            //Read page into buffer.
            String line;
            StringBuffer pageBuffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                
                pageBuffer.append(line);
            }
//            return pageBuffer.toString();
            System.out.println(pageBuffer.toString());
            

            reader.reset();
            
            // Parse the HTML.
            kit.read(reader, doc, 0);
            
            //  The HTML text is now stored in the document
            System.out.println( doc.getText(0, doc.getLength()) );
            
            reader.close();
         
        
        rd.close();
    }
    
    // Returns a reader on the HTML data. If 'uri' begins
    // with "http:", it's treated as a URL; otherwise,
    // it's assumed to be a local filename.
    
    static Reader getReader(String uri)
    throws IOException {
        // Retrieve from Internet.
        if (uri.startsWith("http:")) {
            URLConnection conn = new URL(uri).openConnection();
            // Nastavi dlzku obsahu url.
            urlLenght = conn.getContentLength();
            return new InputStreamReader(conn.getInputStream());
        }
        // Retrieve from file.
        else {
            return new FileReader(uri);
        }
    }
}
