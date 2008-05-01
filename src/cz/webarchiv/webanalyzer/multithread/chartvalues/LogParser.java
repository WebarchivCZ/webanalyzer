/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.webarchiv.webanalyzer.multithread.chartvalues;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author praso
 */
public class LogParser {

    private File log;
    private BufferedReader br;
    private InputStreamReader isr;
    private FileInputStream fis;

    public LogParser(String fileName) throws FileNotFoundException {
        this.log = new File(fileName);
        this.fis = new FileInputStream(log);
        this.isr = new InputStreamReader(fis);
        this.br = new BufferedReader(isr);
    }

    public String readLine() throws IOException {
        return br.readLine();
    }

    public void close() throws IOException {
        br.close();
        isr.close();
        fis.close();
    }
}
