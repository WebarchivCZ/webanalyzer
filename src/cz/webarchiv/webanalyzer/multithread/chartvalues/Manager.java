/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.webarchiv.webanalyzer.multithread.chartvalues;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author praso
 */
public class Manager {
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        List<String> geoIpSearhcer_a = new ArrayList<String>();
        List<String> geoIpSearhcer_v = new ArrayList<String>();
        List<String> dictSearhcer_a = new ArrayList<String>();
        List<String> dictSearhcer_v = new ArrayList<String>();
        List<String> htmlSearhcer_a = new ArrayList<String>();
        List<String> htmlSearhcer_v = new ArrayList<String>();
        List<String> emailSearhcer_a = new ArrayList<String>();
        List<String> emailSearhcer_v = new ArrayList<String>();
        
        String logParserFile = "log/log4j_stat.log";
        LogParser logParser = new LogParser(logParserFile);
        String line;
        while ((line = logParser.readLine()) != null) {
            if (line.contains("GeoIpSearcher")) {
                geoIpSearhcer_a.add(allElementValue(line));
                geoIpSearhcer_v.add(validElementValue(line));
            }
            if (line.contains("DictionarySearcher")) {
                dictSearhcer_a.add(allElementValue(line));
                dictSearhcer_v.add(validElementValue(line));
            }
            if (line.contains("EmailSearcher")) {
                htmlSearhcer_a.add(allElementValue(line));
                htmlSearhcer_v.add(validElementValue(line));
            }
            if (line.contains("HtmlLangSearcher")) {
                emailSearhcer_a.add(allElementValue(line));
                emailSearhcer_v.add(validElementValue(line));
            }
        }
        logParser.close();
        
        // write collections to files
        File f = new File("log/chartsValues.txt");
        FileWriter fw = new FileWriter(f);
        
        write(fw, geoIpSearhcer_a);
        fw.write("========================================================\n");
        write(fw, geoIpSearhcer_v);
        fw.write("===========================geoip====================\n\n\n");
        write(fw, dictSearhcer_a);
        fw.write("=========================================================\n");
        write(fw, dictSearhcer_v);
        fw.write("===========================dict=====================\n\n\n");
        write(fw, htmlSearhcer_a);
        fw.write("========================================================\n");
        write(fw, htmlSearhcer_v);
        fw.write("===========================html=======================\n\n\n");
        write(fw, emailSearhcer_a);
        fw.write("=======================================================\n");
        write(fw, emailSearhcer_v);
        fw.write("===========================email=====================\n\n\n");
        
        fw.close();
    }
    
    private static String validElementValue(String line) {
        int i = line.indexOf("validElements=");
        int j = line.indexOf(",", i+14);
        return line.substring(i+14, j);
    }
    
    private static String allElementValue(String line) {
        int i = line.indexOf("allElements=");
        int j = line.indexOf(",", i+12);
        return line.substring(i+12, j);
    }
    
    private static void write(FileWriter fw, List<String> list) throws IOException {
        for (String s : list) {
            fw.write(s);
            fw.write("\n");
        }
    }

}
