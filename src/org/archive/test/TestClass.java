/*
 * TestClass.java
 *
 * Created on Sobota, 2007, jún 23, 23:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.archive.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author praso
 */
public class TestClass {
    
    /** Creates a new instance of TestClass */
    public TestClass() {
    }
    
    /**
     * Metoda, ktora testuje atomicIncrement a decrement.
     */
    public static void testAtomicInrement() {
        AtomicInteger atomicInteger = new AtomicInteger();
        System.out.println(atomicInteger.intValue());
        int i = 10;
        System.out.println(atomicInteger.addAndGet(i));
        i = 10;
        System.out.println(atomicInteger.addAndGet(-i));
        System.out.println(atomicInteger.addAndGet(-i));
    }
    
    
    /**
     * Metoda testuje nastavovanie spravneho kodovania textu z analyzovanej
     * stranky.
     */
    public static void testURLEncoding() throws IOException {
        try {
            URL url = new URL("http://ticketpro.cz");
            URL url2 = url;
            if (url.toString().indexOf("www") == -1) {
                url2 = new URL("http://www." + url.toString().substring(7));
            }
            URLConnection conn = url2.openConnection();
            System.out.println(conn.getContentLength());
            System.out.println(conn.getContentEncoding());
            System.out.println(conn.getContentType());
            System.out.println(conn.getHeaderField("encoding"));
            System.out.println("HOST je : " + conn.getURL().getHost());
            String type = conn.getContentType();
            if (type.indexOf("charset") != -1) {
                int index = type.indexOf('=');
                System.out.println("charset je : " + type.substring(index + 1).toUpperCase());
            }
            System.out.println(url.toString());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Metoda, ktora zisti IP adresu z danej stranky.
     */
    public static void testIPAddress() {
        try {
            String hostToIP = "www.ironmaiden.com";
            java.net.InetAddress inetAdd =
                    java.net.InetAddress.getByName(hostToIP);
            System.out.println("IP Address is : " + inetAdd.getHostAddress());
        } catch(java.net.UnknownHostException uhe) {
            uhe.printStackTrace();
        }
    }
    
    /**
     * Hlupy test.
     */
    public static void testDelenie() {
        int i = 7;
        int j = 3;
        double result = j*100 / j;
        System.out.println("vysledok je :" + result);
    }
    
    /**
     * Metoda, ktora dlhy retazec textu rozdeli na slova. Oddeli ich tak
     * ze odstani nadbytocne medzery, bodky, ciarky, vykricniky, otazniky a
     * nahradi ich jednou medzerou. Tento retazec sa potom preda na prehladava-
     * nie v sloniku.
     */
    public static void testRemoveInterpunctionFromText() {
        String text = "nejaky text   slovo1 slovo2....slovo3! slovo4? slovo5: " +
                "slovo6_slovo7 slovo8&slovo9 slovo10(slovo11)slovo12 " +
                "slovo13$slovo14 slovo15/slovo16 slovo17,slovo18 slovo19; " +
                "slovo20\\slovo21      slovo22\"slovo23       " +
                "slovo24{slovo25}slovo26  slovo27[slovo28]slovo29 slovo30=";
        Pattern pattern = Pattern.compile("[\\s]+|\\.|!|_|:|\\?|&|\\$|\\(|\\)|/|,|;|\\\\|\"|\\{|\\}|\\[|\\]|=");
        //Split search string into individual terms.
        String[] terms = pattern.split(text);
        //Check to see if each term matches.
        StringBuffer pageBuffer = new StringBuffer();
        for (int i = 0; i < terms.length; i++) {
            pageBuffer.append(terms[i] + " ");
        }
        String resultText = pageBuffer.toString();
        System.out.println(resultText);
        
        StringTokenizer stringTokenizer = new StringTokenizer(resultText);
        while (stringTokenizer.hasMoreTokens()) {
            System.out.println(stringTokenizer.nextToken());
        }
        
    }
    
    /**
     * Metoda, ktora prefiltruje dany text. Necha prejst len slova z abecedy,
     * ale take, ktore patria do diakritiky.
     */
    public static void testFilterText() throws UnsupportedEncodingException {
        // Priprav pole bytu ceskej abecedy.
        //A Á B C Č D Ď E É Ě F G H Ch I Í J K L M N Ň O Ó P Q R Ř S Š T Ť U Ú Ů V W X Y Ý Z Ž
        String text = "aábcčdďeéěfghiíjklmnňoópqrřsštťuúůvwxyýzž-";
        List list = new ArrayList();
        for (int i = 0; i < text.getBytes("utf-8").length; i++) {
            list.add(text.getBytes("utf-8")[i]);
            System.out.println(text.getBytes("utf-8")[i]);
        }
        System.out.println("===============================================");
        
        // Filtruj slova
        String textToFilter = "qwe qwe1 qwe0 qwe- qwe_ qwe qwe 1qwe " +
                "qwe+ qweľ qweš qweč qweť qwež qweý qweá qweí qweé qwe- qweú " +
                "qweä qweň qweô qweř qweě qweď qweĺ qweů qwech";
        StringTokenizer stringTokenizer = new StringTokenizer(textToFilter);
        while (stringTokenizer.hasMoreTokens()) {
            String word = stringTokenizer.nextToken();
            boolean patri = true;
            byte[] wordBytes = word.getBytes("utf-8");
            for (int i = 0; i < wordBytes.length; i++) {
                if (!list.contains(wordBytes[i])) {
                    patri = false;
                    break;
                }
            }
            System.out.println(word + " " + (patri ? "patri" : "nepatri"));
        }
    }
    
    /**
     * Metoda, ktora mi bude vyhladavat vsetky vyskyty atributov "lang" na
     * html stranke. Vsetky najdene potom budem prehladavat znovu a hladat
     * v nich parameter "cz" napr.
     */
    public static void searchLangAttributes() {
        String language = "cz";
        String text = "<doctype head .,.;'/[]\\=-1234670-= lang=\"cz\" alebo" +
                "lang=cz lang=sk, lang='cz' a este lang = cz atd" +
                "xml:lang= 'vz' hojalang = \"cz\"hoja " +
                "xml:lang = 'q3'\" lang=CZ, Lang=Cz; LANG=CZ ";
        String langAttr = "lang ?= ?[\"|']? ?([a-z]){2} ?[\"|']?";
        Pattern pattern = Pattern.compile(langAttr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String lang = matcher.group();
            System.out.println(lang);
            if (lang.indexOf(language.toLowerCase()) != -1 ||
                    lang.indexOf(language.toUpperCase()) != -1) {
                System.out.println("FOUNDED CS LANGUAGE ATTR");
            }
        }
    }
    
    public static void downloadPage() throws MalformedURLException, IOException {
        URL u = new URL("http://zena-in.cz");
        URLConnection connect = u.openConnection();
        // Write download to stdout.
        final int bufferlength = 4096;
        byte [] buffer = new byte [bufferlength];
        InputStream is = connect.getInputStream();
        try {
            for (int count = is.read(buffer, 0, bufferlength);
            (count = is.read(buffer, 0, bufferlength)) != -1;) {
                System.out.write(buffer, 0, count);
            }
            System.out.flush();
        } finally {
            is.close();
        }
    }
    
    public static void downloadPageHTTPConnection() {
        
    }
    
//    public HttpConnection getConnectionWithTimeout(
//        HostConfiguration hostConfiguration, long timeout) {
//
//        HttpConnection conn = new HttpConnection(hostConfiguration);
//        conn.setHttpConnectionManager(this);
//        conn.getParams().setDefaults(this.getParams());
//        return conn;
//    }
//
//    public void releaseConnection(HttpConnection conn) {
//        // ensure connection is closed
//        conn.close();
//        finishLast(conn);
//    }
//
//    static void finishLast(HttpConnection conn) {
//        // copied from superclass because it wasn't made available to subclasses
//        InputStream lastResponse = conn.getLastResponseInputStream();
//        if (lastResponse != null) {
//            conn.setLastResponseInputStream(null);
//            try {
//                lastResponse.close();
//            } catch (IOException ioe) {
//                //FIXME: badness - close to force reconnect.
//                conn.close();
//            }
//        }
//    }
    
    /**
     * Metoda, ktora bude verifikovat predanu url.
     */
    public static URL verifyUrl(String url) {
//        log4j.debug("verifyUrl url=" + url);
        System.out.println("verifyUrl url=" + url);
        if (url == null)
            throw new NullPointerException("url is null.");
        //Only allow HTTP URLs.
        if (!url.toLowerCase().startsWith("http://") || url.toLowerCase().equals("http://")) {
//            log4j.debug("verifyUrl denied, starts with http://");
            System.out.println("verifyUrl denied, doesn't start with http:// or is only http://");
            return null;
        }
        
        // Odstran url, ktore obsahuju retazec "+cgi+"
        if (url.toLowerCase().indexOf("+cgi+") != -1) {
//            log4j.debug("verifyUrl denied, contains +cgi+");
            System.out.println("verifyUrl denied, contains +cgi+");
            return null;
        }
        
        //Verify format of URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
//            log4j.warn("verifyUrl url=" + url + ", " + e.getMessage());
            System.out.println("verifyUrl url=" + url + ", " + e.getMessage());
            return null;
        }
//        log4j.debug("verifyUrl url verified");
        System.out.println("verifyUrl url verified url=" + verifiedUrl);
        return verifiedUrl;
    }
    
    /**
     * Download page by baos.
     */
    public static void downloadPageByBaos(String urlName) {
        try {
            URL url = new URL(urlName);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            String encoding = "utf-8";
            System.out.println("ZISKANIE KODOVANIA");
            System.out.println("urlConnection.getContentEncoding() = " + urlConnection.getContentEncoding());
            System.out.println("urlConnection.getContentType() = " + urlConnection.getContentType());
            System.out.println("urlConnection.getHeaderFields() : ");
            
            InputStream inputstream = urlConnection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputstream);
            int readByte = -1;
            // TODO try to set the size of baos, from connection maybe
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((readByte = bis.read()) != -1) {
                baos.write(readByte);
            }
            System.out.println(baos.toString(encoding));
            // Call method to get <head>...</head>
            getSpecifiedText(baos.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Ziska z textu podtext, ktory je ohraniceny nejakym specifickym tagom
     * Tag by sme mali nastavit spravne nastavit.
     */
    public static void getSpecifiedText(String text2) {
        String text = "njk .1)(&^#%<>>><<>./ <head>opet text<>..<<>>/head>stale" +
                "sem v head <>< hhead> charset= UTF-8 \"<head/></head>a oewpt08042<>>///";
        String specifiedText = null;
        String lowerCaseText = text.toLowerCase();
        int i = -1;
        int j = -1;
        
        if (((i = lowerCaseText.toLowerCase().indexOf("<head>")) != -1) &&
                ((j = lowerCaseText.indexOf("</head>")) != -1)) {
            specifiedText = text.substring(i, j + 7);
        }
        System.out.println(specifiedText);
        // get value of charset
        Pattern pattern = Pattern.compile(
                "charset ?= ?.+ ?\"", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(specifiedText);
        String value = null;
        while (matcher.find()) {
            value = matcher.group(0);
            System.out.println(value);
        }
        System.out.println(value.substring(value.indexOf("=") + 1, value.indexOf("\"")).trim());
    }
    
    /**
     * Metoda na otestovanie toExternalForm metody.
     */
    public static void externalFormOfURL() {
        try {
//            URL url = new URL("http://lkj.lkj.lkj.!+_)(?>=qwe=qew");
            URL url = new URL("http://adserver.itsfogo.com/click.aspx?t=i&v=1&zoneid=7439");
            System.out.println(url.toExternalForm());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void regexp() {
        try {
//            String text = "asdfanecosdfsfdladsfjalfjlsahojdf \"cz'nd";
            String text = "asdfanecosdfsfdladsfjalfjlsahojdf.cz/nd";
//            Pattern p = Pattern.compile(" [\"|']cz[\"|']");
            Pattern p = Pattern.compile(".*(\\.cz[\\W]){1}.*");
            Matcher matcher = p.matcher(text);
            while (matcher.find()) {
                System.out.println(matcher.group());
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Metoda, ktora mi z logoveho suboru odstrani zbytocne znaky a necha len 
     * odkazy, ktore potrebujem
     */
    public static void formatOutlinks() {
        File file = new File("data/outlinks.txt");
        File out = new File("data/out.txt");
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            FileWriter fw = new FileWriter(out);
            BufferedWriter bw = new BufferedWriter(fw);
            String line = null;
            int index;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                if ((index = line.indexOf("http")) != -1) {
                    bw.write(line.substring(index));
                    bw.newLine();
                }
            }
            bw.close();
            br.close();
            fr.close();
            fw.close();
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) throws IOException  {
//        testIPAddress();
//        testURLEncoding()
//        testDelenie();
//        testRemoveInterpunctionFromText();
//        testFilterText();
//        searchLangAttributes();
//        downloadPage();
//        verifyUrl("http://");
//        downloadPageByBaos("http://mapy-pruvodce.cz/banner_click.php?idbanneru=126&amp;idbannerplocha=");
//        externalFormOfURL();
//        getSpecifiedText("bla");
        regexp();
//        formatOutlinks();
    }
    
}
