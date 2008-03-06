/*
 * FIleDownload.java
 *
 * Created on September 11, 2007, 9:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.archive.test;

import java.io.*;
import java.net.*;


/**
 *
 * @author praso
 */
/*
 * Command line program to download data from URLs and save
 * it to local files. Run like this:
 * java FileDownload http://schmidt.devlib.org/java/file-download.html
 * @author Marco Schmidt
 */
public class FileDownload {
    public static void download(String address, String localFileName) {
        OutputStream out = null;
        URLConnection conn = null;
        InputStream  in = null;
        try {
            URL url = new URL(address);
            out = new BufferedOutputStream(
                    new FileOutputStream(localFileName));
            conn = url.openConnection();
            in = conn.getInputStream();
            byte[] buffer = new byte[4096];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                
                numWritten += numRead;
            }
            System.out.println(localFileName + "\t" + numWritten);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    
    public static void download(String address) {
        int lastSlashIndex = address.lastIndexOf('/');
        if (lastSlashIndex >= 0 &&
                lastSlashIndex < address.length() - 1) {
            download(address, address.substring(lastSlashIndex + 1));
        } else {
            System.err.println("Could not figure out local file name for " +
                    address);
        }
    }
    
    public static void main(String[] args) {
        String[] args2 = new String[1];
        args2[0] = "http://zena-in.cz";
        for (int i = 0; i < args2.length; i++) {
            download(args2[i]);
        }
    }
}