/*
 * Crawler.java
 *
 * Created on Streda, 2007, j�n 13, 19:51
 *
 * Skompilovat pomocou -Xlint a osetrit pripadne varovania.
 *
 * TODO este vytvorit slovniky pre forbiddenDictinary aj pre mesta aj pre
 * pre LUCENE ForbiddenURLs a skontrolovat aj ostatne. Skontrolovat preco
 * nefunguje LUCENE asi zly url formater (na seznam.cz nenasiel nic s adamovho
 * suboru odkazy.seznma.cz
 *
 */

package org.archive.analyzer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.archive.analyzer.criteria.Email;
import org.archive.analyzer.criteria.ForbiddenUrls;
import org.archive.analyzer.criteria.ForbiddenWords;
import org.archive.analyzer.criteria.ForbiddenUrlName;
import org.archive.analyzer.criteria.LangAttr;
import org.archive.analyzer.criteria.Telephone;
import org.archive.analyzer.criteria.UrlName;
import org.archive.analyzer.criteria.Urls;
import org.archive.analyzer.criteria.Words;
import org.archive.analyzer.dictionary.DictLoader;
import org.archive.analyzer.dictionary.ForbiddenDictLoader;
import org.archive.analyzer.dictionary.ForbiddenIndexLoader;
import org.archive.analyzer.dictionary.IndexLoader;
import org.archive.analyzer.criteria.GeoIPCountry;

/**
 *
 * @author Ivan Vlcek
 */
public class Crawler {
    
    /* Jedina instancia triedy Crawler. */
    private static final Crawler INSTANCE = new Crawler();
    
    /* Logovanie. */
    private static final Logger log4j = Logger.getLogger(Crawler.class);
    
    /* Odkazy na jedinacikov, ktori budu prehladavat textovy obsah URL */
    private static final DictLoader DICT_LOADER = DictLoader.getInstance();
    private static final ForbiddenDictLoader FORBIDDEN_DICT_LOADER =
            ForbiddenDictLoader.getInstance();
    private static final IndexLoader INDEX_LOADER = IndexLoader.getInstance();
    private static final ForbiddenIndexLoader FORBIDDEN_INDEX_LOADER =
            ForbiddenIndexLoader.getInstance();
    private static final Counter COUNTER = Counter.getInstance();
    private static final FilterText FILTER_TEXT = FilterText.getInstance();
    
    private static final Email EMAIL = Email.getInstance();
    private static final Telephone TELEPHONE = Telephone.getInstance();
    private static final LangAttr LANG_ATTR = LangAttr.getInstance();
    private static final Words WORDS = Words.getInstance();
    private static final UrlName URL_NAME = UrlName.getInstance();
    private static final Urls URLS = Urls.getInstance();
    private static final ForbiddenWords FORBIDDEN_WORDS = ForbiddenWords.getInstance();
    private static final ForbiddenUrlName FORBIDDEN_URL_NAME = ForbiddenUrlName.getInstance();
    private static final ForbiddenUrls FORBIDEN_URLS = ForbiddenUrls.getInstance();
    private static final GeoIPCountry GEO_IP_COUNTRY = GeoIPCountry.getInstance();
    
    
    
    /* Konstanta, udava max URL, ktore bude Crawler analyzovat pre danu URL. */
    private final int MAX_URLS = 1000;
    /* Udavaju aby sa kolekcie najdenych linkocv uz nenaplnovali .*/
    private boolean toCrawlListNotFilled = true;
    private boolean outOfHostLinksNotFilled = true;
    
    
    /* Konstanta, ktora udava timeout pri metode. */
    private final int TIMEOUT = 4000;
    
//    /* Vzor na vyhladanie bielych miest v texte. */
//    private Pattern pattern = Pattern.compile("[\\s]+|\\.|!|_|:|\\?|&|\\$|\\(|\\)|/|,|;|\\\\|\"|\\{|\\}|\\[|\\]|=");
    
    /* Zastavka, ktora nastavuje stop metody crawl na true */
    private boolean stop = false;
    
    /* Retazec, kde sa uklada prefiltrovany text od tagov. */
    // uprava metody
//    private String filteredText;
    
    /* Cache of robot disallow lists. */
    private HashMap disallowListCache = new HashMap();
    
//    /* Pole listov, v ktoych su linky(URL) troch typov. */
//    private List[] arrayOfListsOfLinks = new List[3];
    
//    /* outOfHostLinks linky, ktore ukazuju mimo host. */
//    private ArrayList<String> outOfHostLinks = new ArrayList<String>();
    
    
//    /* Zapis ceskych URL do "logu". */
//    private File logFile = new File("logFile.txt");
//    private RandomAccessFile randomAccessFile;
    
    /**
     * Staticka tovarna metoda, ktora vrati odkaz na jedninu instanciu
     * Crawlera.
     *
     * @return crawler   jedina instancia Crawlera.
     */
    static Crawler getInstance() {
        return INSTANCE;
    }
    
    /**
     * Vytvori instanciu Crawlera.
     */
    private Crawler() {
    }
    
    /**
     * Metoda, ktora bude crawlovat predanu URI podla paramettru language.
     * Podla language sa nainicializuju tie spravne slovniky a indexy pre
     * analyzovanie stranky. Hodnoty parametra language su jednoduche retazce
     * typu "cz", "sk", "
     *
     * @param uri   uri na crawlovanie.
     * @param language   nastavenie crawlera pre analyzovanie daneho jazyka.
     * @return true   ak je stranka v oblasti zaujmu.
     */
    boolean crawl(String uri, String language) {
        
        log4j.debug("crawl uri=" + uri);
        
        if (uri == null) {
            throw new NullPointerException("uri is null.");
        }
        // Otvorenie streamov na citanie zo slovnika.
        // Mal by som nahrarit nejakou inicializacnou metodou nieco ako stop
        // ale initialize, asi by som mal prerobit metody na hladanie v texte
        // (ako je Email, UrlName, Telephone na staticke metody, a umiestnit ich
        // do noveho package ) tak aby som tieto metody mohol volat v API
        // + nahradit logovanie do randomSuboru do osobitneho logu,kde budu len
        //   validne ceske url.
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        
        // Inicializacna metoda, ktora nastavi vsetko potrebne.
        initializeCrawler(language);
        
        // Zapisanie URL adresy do "logu" ak je ceska.
        if (crawl(uri, MAX_URLS, true)) {
//            try {
            log4j.debug("URL is in scope url=" + uri);
            System.out.println("=========================" + uri + "=========================");
//                randomAccessFile = new RandomAccessFile(logFile, "rw");
//                randomAccessFile.seek(logFile.length());
//                randomAccessFile.writeBytes(uri + "\n");
//                randomAccessFile.close();
            return true;
//            } catch (IOException ioe) {
//                log4j.error(ioe.getMessage());
//                ioe.printStackTrace();
//            }
        }
        return false;
    }
    
    /**
     * Metoda, ktora nastavi stop na true. Metoda crawl sa zastavi pri
     * crawlovani nasledujucej stranky.
     */
    void stop() {
        stop = true;
    }
    
    /**
     * Inicializacna metoda, ktora nastavi vsetko potrebne pre Crawling.
     * Mala by nastavovat umiestnenie indexov potrebnych pri vyhladavani
     * v odkazoch pomocou LUCENE. Mozno ak prerobim slovniky (premiestnim z
     * .jaru do pomocnych suborov aplikacie.
     * !!! Mozno by som mal metodu premiestnit do triedy WebAnalyzer a tam
     * spustit inicializovanie Crawlera.
     * Asi by som to mal spravit tak aby bol vstupny paramter iba retazec napr:
     * "cz" pre crawling ceskych, "sk" atd, a defaultne nastavit umiestnie
     * indexov pre URLS, FORBIDEN_URLS, ...
     *
     * @param language   urcuje cestu pre umiestnie potrebnych indexov napr. cz
     */
    public void initializeCrawler(String language) {
        COUNTER.resetPoints();
        INDEX_LOADER.initialize(language);
        DICT_LOADER.openStreams(language);
        FORBIDDEN_INDEX_LOADER.initialize(language);
        FORBIDDEN_DICT_LOADER.openStreams(language);
        URLS.inicializeLuceneIndex(language);
        FORBIDEN_URLS.inicializeLuceneIndex(language);
        GEO_IP_COUNTRY.initializeGeoIPCountry(language);
        LANG_ATTR.inicializeLangAttr(language);
        EMAIL.inicialize(language);
        
        // Vypisat najdene objekty na standardny vystup.
        boolean sout = true;
        EMAIL.setSout(sout);
        FORBIDDEN_WORDS.setSout(sout);
        FORBIDEN_URLS.setSout(sout);
        FORBIDDEN_URL_NAME.setSout(sout);
        GEO_IP_COUNTRY.setSout(sout);
        LANG_ATTR.setSout(sout);
        TELEPHONE.setSout(sout);
        URL_NAME.setSout(sout);
        URLS.setSout(sout);
        WORDS.setSout(sout);
    }
    
    /**
     * Metoda, ktora resetuje parametre, aby boli pripravene na dalsie
     * crawlovanie. Resetuje hodnotu Countera na 0, mapu dissallowList, stop
     * nastavi na false aby sa zacalo crawlovanie. Vyprazdni sa kolekcia
     * outOfHostLinks.
     */
    public void resetCrawler() {
        stop = false;
        
        writeStatistics();
        
        disallowListCache.clear();
//        outOfHostLinks.clear();   // pridane do lokalnej metody
        COUNTER.resetPoints();
        DICT_LOADER.closeStreams();
        FORBIDDEN_DICT_LOADER.closeStreams();
        URLS.closeLuceneIndexSearcher();
        FORBIDEN_URLS.closeLuceneIndexSearcher();
        GEO_IP_COUNTRY.closeGeoIPCountry();
    }
    
    /**
     * Metoda, ktora zastavi Crawlera a pozarvara vsetky streamy.
     */
    public void closeCrawler() {
        writeStatistics();
        
        disallowListCache.clear();
//        outOfHostLinks.clear();   // pridane do lokalnej metody
        COUNTER.resetPoints();
        DICT_LOADER.closeStreams();
        FORBIDDEN_DICT_LOADER.closeStreams();
        URLS.closeLuceneIndexSearcher();
        FORBIDEN_URLS.closeLuceneIndexSearcher();
        GEO_IP_COUNTRY.closeGeoIPCountry();
        
    }
    
    /**
     * Metoda, ktora resetuje crawler pre dalsiu analyzu, resetuje hlavne
     * Counter.
     */
    public void resetCounter() {
        COUNTER.resetPoints();
    }
    
    private void writeStatistics() {
        System.out.println(
                "Statistics of analyzing" +
                "\nEMAIL : " + EMAIL.statisticsInPercent() +
                "\nFORBIDDEN_WORDS : " + FORBIDDEN_WORDS.statisticsInPercent() +
                "\nFORBIDDEN_URL_NAME : " + FORBIDDEN_URL_NAME.statisticsInPercent() +
                "\nFORBIDDEN_URLS : " + FORBIDEN_URLS.statisticsInPercent() +
                "\nGEO_IP_COUNTRY : " + GEO_IP_COUNTRY.statisticsInPercent() +
                "\nLANG_ATTR : " + LANG_ATTR.statisticsInPercent() +
                "\nTELEPHONE : " + TELEPHONE.statisticsInPercent() +
                "\nURL_NAME : " + URL_NAME.statisticsInPercent() +
                "\nURLS : " + URLS.statisticsInPercent() +
                "\nWORDS : " + WORDS.statisticsInPercent());
        
    }
    
    /**
     * Check if robot is allowed to access the given URL.
     *
     * @param urlToCheck    url to check if it is allowed to crawl.
     * @return              true if crawling is allowed, false otherwise
     */
    private boolean isRobotAllowed(URL urlToCheck,
            HashSet<String> hostsWithoutRobotTxtFile) {
        log4j.debug("isRobotAllowed urlToCheck=" + urlToCheck.toExternalForm());
        if (urlToCheck == null)
            throw new NullPointerException("urlToCheck is null.");
        String host = urlToCheck.getHost().toLowerCase();
        log4j.debug("isRobotAllowed host=" + urlToCheck.getHost());
        // Overit ci nieje host v zozname hostov bez suboru robots.txt
        if (hostsWithoutRobotTxtFile.contains(host)) {
            log4j.debug("isRobotAllowed host is in set hostsWithoutRobotFile");
            return true;
        }
        
        //Retrieve host's disallow list from cache.
        ArrayList disallowList =
                (ArrayList) disallowListCache.get(host);
        
        //If list is not in the cache, download and cache it.
        if (disallowList == null) {
            disallowList = new ArrayList();
            
            try {
                URL robotsFileUrl =
                        new URL("http://" + host + "/robots.txt");
                log4j.debug("isRobotAllowed url created robotsFileUrl=" +
                        robotsFileUrl);
                
                // TODO este upravit nacitanie connection a vyhadzovanie
                // vynimiek v try catch bloku.
                // Nahrada namiesto BufferedReaderu.
                URLConnection urlConnection = robotsFileUrl.openConnection();
                urlConnection.setReadTimeout(TIMEOUT);
                urlConnection.setConnectTimeout(TIMEOUT);
                
                //Open connection to robot file URL for reading.
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
                log4j.debug("isRobotAllowed connection for reading opened");
                log4j.debug("isRobotAllowed BBBBB robots.txt in url=" + urlToCheck);
                //Read robot file, creating list of disallowed paths.
                String line;
                while((line = reader.readLine()) != null) {
                    if (line.indexOf("Disallow:") == 0) {
                        String disallowPath =
                                line.substring("Disallow:".length());
                        
                        //Check disallow path for comments and remove if present
                        int commentIndex = disallowPath.indexOf("#");
                        if (commentIndex != -1) {
                            disallowPath =
                                    disallowPath.substring(0, commentIndex);
                        }
                        log4j.debug("isRobotAllowed disallowPath=" +
                                disallowPath);
                        //Remove leading or trailing spaces from disallow path.
                        disallowPath = disallowPath.trim();
                        
                        //Add disallow path to list.
                        disallowList.add(disallowPath);
                    }
                }
                // Zavriem stream.
                reader.close();
                log4j.debug("isRobotAllowed connection closed");
                
                //Add new disallow list to cache.
                disallowListCache.put(host, disallowList);
            } catch (Exception e) {
                log4j.warn("Is robotAllowed urlToCheck=" + urlToCheck +
                        " Assume robot is allowed since an exception is " +
                        "thrown if the robot file doesn't exist. " +
                        e.getCause());
                        /* Assume robot is allowed since an exception
                         *is thrown if the robot file doesn't exist. */
                /* Poznamena si host do zoznamu hostov bez robots.txt. */
                // todo
                hostsWithoutRobotTxtFile.add(host);
                return true;
            }
        }
        
        /*
         * Loop through disallow list to see if the
         * crawling is allowed for the given URL.
         */
        String file = urlToCheck.getFile();
        for (int i = 0; i < disallowList.size(); i++) {
            String disallow = (String) disallowList.get(i);
            if (file.startsWith(disallow)) {
                log4j.debug("isRobotAllowed crawling is NOT allowed for given URL.");
                return false;
            }
        }
        log4j.debug("isRobotAllowed crawling is allowed");
        return true;
    }
    
    /**
     * Verify URL format.
     */
    private URL verifyUrl(String url) {
//        log4j.debug("verifyUrl url=" + url);
        if (url == null)
            throw new NullPointerException("url is null.");
        //Only allow HTTP URLs.
        if (!url.toLowerCase().startsWith("http://") ||
                url.toLowerCase().equals("http://")) {
            log4j.debug("verifyUrl url isn't valid");
            return null;
        }
        
        // Odstran url, ktore obsahuju retazec "+cgi+"
        if (url.toLowerCase().indexOf("+cgi+") != -1) {
            log4j.debug("verifyUrl denied, contains string \"+cgi+\"");
            return null;
        }
        
        //Verify format of URL.
        URL verifiedUrl = null;
        try {
            verifiedUrl = new URL(url);
        } catch (Exception e) {
            log4j.warn("verifyUrl url=" + url + ", " + e.getMessage());
            return null;
        }
//        log4j.debug("verifyUrl url verified url=" + verifiedUrl);
        return verifiedUrl;
    }
    
    /**
     * Metoda, ktora z predaneho retazca s bielymi medzerami vrati retazec s
     * jednou bielou medzerou medzi slovami.
     *
     * @param text   text s white spaces.
     * @return   text s jednou bielou medzerou medzi slovami.
     */
    private String withoutWhiteSpaces(String text) {
        
        if (text == null)
            throw new NullPointerException("text si null.");
        
        //Split search string into individual terms.
        Pattern pattern = Pattern.compile("[\\s]+|\\.|!|_|:|\\?|&|\\$|\\(|\\)|/|,|;|\\\\|\"|\\{|\\}|\\[|\\]|=");
        String[] terms = pattern.split(text);
        //Check to see if each term matches.
        StringBuffer pageBuffer = new StringBuffer();
        for (int i = 0; i < terms.length; i++) {
            pageBuffer.append(terms[i] + " ");
        }
        return pageBuffer.toString();
    }
    
    /**
     * Metoda, ktora z predaneho textu odstrani vsetky tagy, ktore su uzavrene
     * v znackach &lt; a &gt; vrati len cisty text. Odstrani aj javascript.
     *
     * @param text   retazec s tagovacimi znackami, ktore treba odstranit.
     * @return   text bez tagov.
     */
    private String removeTags(String text) {
        if (text == null)
            throw new NullPointerException("text is null.");
        // TODO otestovat chovanie novej metody
//        filteredText = text.replaceAll("\\<script.*?\\</script>", " ");
//        return filteredText.replaceAll("\\<.*?\\>", " ");
        return text.replaceAll("\\<script.*?\\</script>", " ").replaceAll("\\<.*?\\>", " ");
    }
    
    //TODO ivlcek, odstranit metodu getPlainContents, nepouziva sa
    
//    /**
//     * Metoda, ktora z predanej url stiahne text a prefiltruje aby odstranila
//     * html znacky. Prefiltrovany text vrati ako retazec. Vyhadzuje vynimky
//     * pri filtrovani css. NEPOUZIVAT zatial.
//     *
//     * @param pageUrl   url, z ktorej sa ziska text bez html tagov.
//     * @return retazec obsahu stranky bez html tagov.
//     */
//    private String getPlainPageContext(URL pageUrl) {
//
//        if (pageUrl == null)
//            throw new NullPointerException("pageUrl is null.");
//        try {
//            // Nastavim potrebne nastroje na parsovanie html textu.
//            EditorKit kit = new HTMLEditorKit();
//            Document doc = kit.createDefaultDocument();
//
//            // The Document class does not yet handle charset's properly.
//            doc.putProperty("IgnoreCharsetDirective", Boolean.TRUE);
//
//            //open connection to URL
//            URLConnection conn = pageUrl.openConnection();
//            Reader reader = new InputStreamReader
//                    (conn.getInputStream(), "windows-1250");
//
//            //Parse the HTML
//            kit.read(reader,doc, 0);
//            //The HTML text is now stored in the document
//            reader.close();
//
//            //Text ulozeny v retazci plainContent uz ocisteny od White Spaces
//            return withoutWhiteSpaces(doc.getText(0, doc.getLength()));
//        } catch (Exception e) {
////            e.printStackTrace();
//        }
//        return null;
//    }
    
    
    //Download page at given URL.
    private String downloadPageOrig(URL pageUrl) {
        try {
            //Open connection to URL for reading.
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(
                    pageUrl.openStream(), ""));
            
            //Read page into buffer.
            String line;
            StringBuffer pageBuffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                
                pageBuffer.append(line);
            }
            
            String inString = pageBuffer.toString();
            // Create the encoder and decoder for the character encoding
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            CharsetEncoder encoder = charset.newEncoder();
            // This line is the key to removing "unmappable" characters.
            encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
            String result = inString;
            
            try {
                // Convert a string to bytes in a ByteBuffer
                ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(inString));
                
                // Convert bytes in a ByteBuffer to a character ByteBuffer and then to a string.
                CharBuffer cbuf = decoder.decode(bbuf);
                result = cbuf.toString();
            } catch (CharacterCodingException cce) {
                String errorMessage = "Exception during character encoding/decoding: " + cce.getMessage();
                log4j.error(errorMessage, cce);
            }
            log4j.info(result);
            return result;
            
            
//            log4j.info(pageBuffer.toString());
//            return pageBuffer.toString();
        } catch (Exception e) {
        }
        
        return null;
    }
    
    /**
     * Metoda, ktore downloaduje subor zadanej. Vytvori spojenie a zo streamu
     * si stiahne cely text, ktory vrati ako String. Pred downloadom si zisti
     * kodovanie stranky, ak je null, tak sa pouzije defaultne
     * "windows-1250".
     *
     * @param pageUrl url, z ktoreho sa zdownloaduje text.
     * @return text aj s html tagmi, ktory sa stiahol z predanej URL.
     */
    private String downloadPage(URL pageUrl) {
        log4j.debug("download started pageUrl=" + pageUrl);
        
        if (pageUrl == null)
            throw new NullPointerException("pageUrl is null.");
        
        try {
            // Ziskam kodovanie textu.
            String encoding = "windows-1250";
            String url = pageUrl.toString();
            if (url.indexOf("www") == -1) {
                url = "http://www." + url.toString().substring(7);
            }
            URL urlCopy = new URL(url);
            URLConnection conn = urlCopy.openConnection();
            String type = conn.getContentType();
            // todo vyhodilo null pointer neviem preco ale predtym islo ok.
            if (type != null && type.indexOf("charset") != -1) {
                int index = type.indexOf('=') + 1;
                encoding = type.substring(index).toUpperCase();
            }
            
            log4j.debug("DOWNLOAD PAGE encoding=" + encoding);
            //Open connection to URL for reading.
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), encoding));
            log4j.debug(" CONNECTION CREATED");
            
            //Read page into buffer.
            String line;
            StringBuffer pageBuffer = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                
                System.out.println(line);
                
                pageBuffer.append(line);
            }
            // Zavriem stream.
            reader.close();
            log4j.debug("CONNECTION CLOSED");
            
//            System.out.println("downloadPage pageUrl=" + pageUrl.toString());
            
            log4j.debug("page succesfully downloaded");
            return pageBuffer.toString().toLowerCase();
        } catch (IOException ioe) {
            log4j.warn("downloadPage pageUrl=" + pageUrl + " caused Error, "
                    + ioe.getMessage());
            ioe.printStackTrace();
        }
        return null;
    }
    
    /**
     * Metoda, ktora je nahradou downloadPage, pretoze ta zliha pri citani z
     * BufferedReaderu.
     * TODO otestovat ako sa bude tato varianta spravat.
     *
     * @param pageUrl       url z ktorej sa bude stahovat html obsah.
     * @return              html text danej url.
     */
    private String downloadPageByBytes(URL pageUrl) {
        log4j.debug("downloadInBytes started pageUrl=" + pageUrl);
        
        if (pageUrl == null)
            throw new NullPointerException("pageUrl is null.");
        
        try {
            // Ziskam kodovanie textu.
            String encoding = "utf-8";
            String url = pageUrl.toString();
            if (url.indexOf("www") == -1) {
                try {
                    url = "http://www." + url.toString().substring(7);
                } catch(StringIndexOutOfBoundsException sioobe) {
                    log4j.warn("downloadPageByBytes adding www substring to " +
                            "url", sioobe.getCause());
                    return null;
                }
            }
            log4j.debug("DOWNLOAD_PAGE_BY_BYTES create URL.");
            URL urlCopy = new URL(url);
            log4j.debug("DOWNLOAD_PAGE_BY_BYTES create connection");
            URLConnection conn = urlCopy.openConnection();
            
            // TODO overit pokusne stahovanie pri encoding 8859_1 ???
            StringBuffer stringbuffer = new StringBuffer();
            int readByte;
            InputStream inputStream = null;
            // TODO overit ci bude stale treba pouzivat metodu available pri
            // citani z inputstreamu, mozno to nebude ak sa vycerpa limit pri
            // ziskavani z connection.
            try {
                log4j.debug("DOWNLOAD_PAGE set Timeout");
                conn.setReadTimeout(TIMEOUT);
                log4j.debug("DOWNLOADE_PAGE getInputStream");
                inputStream = conn.getInputStream();
            } catch (UnknownServiceException use) {
                log4j.warn("DOWNLOAD_PAGE_BY_BYTES ,UnknownServiceException ", use.getCause());
                return null;
            } catch (IOException ioe) {
                log4j.warn("DOWNLOAD_PAGE_BY_BYTES ,IOExcepion ", ioe.getCause());
                return null;
            }
            log4j.debug("DOWNLOAD_PAGE create ByfferedInputStream");
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            log4j.debug("DOWNLOAD_PAGE read");
            // TODO otestovat metodu available. Mozno preto preskocila par bytov.?????
//            while ((bufferedInputStream.available() > 0) && (readByte = bufferedInputStream.read()) > -1) {
            // TODO otestovat read, ci sa zase zamrzne. Skusit otestovat timeoutom na UrlConnnection.
            while ((readByte = bufferedInputStream.read()) > -1) {
                stringbuffer.append((char) readByte);
            }
            log4j.debug("DOWNLOAD_PAGE close");
            bufferedInputStream.close();
            inputStream.close();
            
//            System.out.println(stringbuffer.toString());
//            System.out.println("================================================================================");
//            System.out.println("http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4449371");
            
            // Ziska kodovanie stranky.
            log4j.debug("DOWNLOAD_PAGE_BY_BYTES getContentType");
            String type = conn.getContentType();
            log4j.debug("DOWNLOAD_PAGE_BY_BYTES get encoding");
            // todo vyhodilo null pointer neviem preco ale predtym islo ok.
            if (type != null && type.indexOf("charset") != -1) {
                int index = type.indexOf('=') + 1;
                encoding = type.substring(index).toUpperCase();
            }
            log4j.debug("DOWNLOAD_IN_BYTES PAGE encoding=" + encoding);
            
            byte [] inputBytes = null;
            String result = stringbuffer.toString();
            try {
                // TODO otestovat kodovanie retazca.
                inputBytes = result.getBytes("ISO8859_1");
//                inputBytes = result.getBytes();
            } catch (UnsupportedEncodingException uee) {
                // Program by tu nemal skoncit, ale ak ano tak osetrit aby sa
                // spravne kodoval text, ziskany z URLConnection. Ak sa tu
                // dostane, tak sa pouzije JVM's default encoding.
                inputBytes = result.getBytes();
                log4j.warn("DOWNLOAD_PAGE_BYTE nerozpoznal kodovanie pri " +
                        "ziskani textu z URLConnection, pouzije sa defaultne " +
                        "kodovanie JVM.", uee.getCause());
            }
            try {
                String downloadedPage = new String(inputBytes, "UTF-8");
//                System.out.println(downloadedPage);
                log4j.debug("page succesfully downloaded");
                // TODO vraciam lowerCase otestovat nasledky
//                log4j.debug("====================================================OBSAH STRANKY=================================================\n" + downloadedPage);
                return downloadedPage.toLowerCase();
            } catch (UnsupportedEncodingException uee) {
                // uee.printStackTrace();
                // Use default JVM encoding.
                log4j.warn("DOWNLOAD_PAGE_IN_BYTES nerozpoznal kodovanie, " +
                        "pouzije sa defaultne kodovanie JVM.", uee.getCause());
                String downloadedPage = new String(inputBytes);
//                System.out.println(downloadedPage);
                // TODO vraciam lowerCase otestovat nasledky.
//                log4j.debug("====================================================OBSAH STRANKY=================================================\n" + downloadedPage);
                return downloadedPage.toLowerCase();
            }
        } catch (IOException ioe) {
            log4j.warn("downloadPage pageUrl=" + pageUrl + " caused Error, "
                    + ioe.getMessage());
            ioe.printStackTrace();
        }
        return null;
    }
    
    /**
     * Metoda, ktora ziska html text z danej URL. Pouziva na ziszkanie textu
     * URLConnection a ziskany byty zapisuje do ByteArrayOutpoutStream. Html
     * text, ktory tato metoda vracia by mal byt vrateny v kodovani, ktore
     * bolo na url alebo html stranke najdene. Ak nebude najdene ziadne
     * kodovanie (charset), tak sa pouzije nejake defaultne, zatial
     * windows-1250.
     *
     * @param url       url z ktorej sa ma ziskat html text.
     * @return          html text ziskany zo zadanej url.
     */
    // Podla Jirku by som mal vracat string jednym returnom, nie dvoma
    public String downloadPageByBaos(URL url) {
        String output = null;
        log4j.debug("DownloadPageByBytes url=" + url.toExternalForm());
        
        try {
            URLConnection urlConnection = url.openConnection();
            urlConnection.setReadTimeout(TIMEOUT);
            urlConnection.setConnectTimeout(TIMEOUT);
            log4j.debug("urlConnection opened, timeouts set.");
            InputStream inputstream = null;
            try {
                inputstream = urlConnection.getInputStream();
            } catch (java.net.SocketTimeoutException ste) {
                log4j.warn("DownloadPageByBaos timeout at getConnection()",
                        ste.getCause());
                return null;
            }
            BufferedInputStream bis = new BufferedInputStream(inputstream);
            int readByte = -1;
            // TODO try to set the size of baos, from connection maybe
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            log4j.debug("inputstream was set, while loop, bis.read starts");
            while ((readByte = bis.read()) != -1) {
                baos.write(readByte);
            }
            
            log4j.debug("while loop finished, now set charset!");
            // TODO otestovat metodu na ziskanie kodovania url.
            String encoding = null;
            // get encoding from urlConnection.getEncoding().
            encoding = urlConnection.getContentEncoding();
            if (encoding == null) {
                // Search charset value from content type of URLConnection.
                String content = urlConnection.getContentType();
                if (content != null && content.indexOf("charset") != -1) {
                    encoding = content.substring(
                            content.indexOf("charset=") + 8).toUpperCase();
                }
            }
            if (encoding == null) {
                // Search charset value from html text.
                encoding = getCharsetValue(baos.toString().toLowerCase());
            }
            if (encoding == null) {
                // Set default charset if none was found.
                encoding = "utf-8";
            }
            log4j.debug("charset was set charset=" + encoding);
            output = baos.toString(encoding).toLowerCase();
            // TODO logovat poriadne vynimky, ako pri metode
            // downloadPageBYBytes(), inak neviem co sa stalo
        } catch (Exception e) {
            log4j.error("downloadPageByBaos url=" + url, e.getCause());
            //e.printStackTrace();
        }
        
//        log4j.debug("downloadPageByBaos return " + output);
        
        return output;
    }
    
    /**
     * Metoda, ktora hlada kodovanie html textu, podla slova charset="neco"
     *
     * @param htmlText      text v ktorom sa bude vyhladavat
     * @return hodnota atributu charset, ak sa nachadza v htmlTexte, null inak
     */
    private String getCharsetValue(String htmlText) {
        log4j.debug("getCharsetValue htmlText=htmlText");
        if (htmlText == null) {
            throw new NullPointerException("htmlText is null");
        }
        // Regexpom ziskam text charset=kodovanie"
        Pattern pattern = Pattern.compile("charset ?= ?.+ ?\"", Pattern.CASE_INSENSITIVE);
        // Ziska pozadovany podtext textu htmlText metodou getSpecifiedText()
        Matcher matcher = pattern.matcher(getSpecifiedText(htmlText));
        String charsetValue = null;
        while (matcher.find()) {
            charsetValue = matcher.group(0);
        }
        if (charsetValue == null) {
            return null;
        }
        return charsetValue.substring(charsetValue.indexOf("=") + 1,
                charsetValue.indexOf("\"")).trim();
    }
    
    /**
     * Ziska z textu podtext, ktory je ohraniceny nejakym specifickym tagom
     * Tag by sme mali nastavit spravne nastavit pred volanim tejto metody
     * Zatial sa hlada len obsah tagu <head>...</head>
     *
     * @param text      text, z ktoreho sa ziska specifikovany podtext
     * @return          podtext vstupneho textu, napr. obsah tagu head
     */
    private String getSpecifiedText(String text) {
        String specifiedText = null;
        int i = -1;
        int j = -1;
        if (((i = text.indexOf("<head>")) != -1) &&
                ((j = text.indexOf("</head>")) != -1)) {
            return text.substring(i, j + 7);
        }
        return null;
    }
    
    
    
//Remove leading "www" from a URL's host if present.
    private String removeWwwFromUrl(String url) {
        if (url == null)
            throw new NullPointerException("uri is null.");
        int index = url.indexOf("://www.");
        if (index != -1) {
            return url.substring(0, index + 3) +
                    url.substring(index + 7);
        }
        
        return (url);
    }
    
//Parse through page contest and retrieve links.
    // zmenit metodu, aby vracala pole listov, retrievedLinks,
    // linksForLuceneSearching a outOfHostLinks.
    
    /**
     * Metoda, krora vrati pole listov, v ktorom sa budu nachadzat zoznamy
     * linkov (odkazov). Su umiestnene na tychto poziciach.
     * pole[0] = retrievedLinks - linky najdene v kontexte stranky.
     * pole[1] = outOfHostLinks - linky ktore odkazuju mimo host.
     * pole[2] = linkForLuceneSearhing - linky,
     * na prehladavanie v linkoch portalov.
     *
     *
     * @param pageUrl   URL analyzovanej stranky.
     * @param pageContents   kontext analyzovanej stranky v html kode.
     * @param crawledList   URL, ktore sa uz zcrawlovali.
     * @param limitHost   hladat aj odkazy mimo host
     * @param outOfHostLinks   odkazy mimo host
     * @return pole listov, jednotlivych linkov.
     *
     */
    private List[] retrieveLinks(
            URL pageUrl, String pageContents, HashSet crawledList,
            boolean limitHost, LinkedHashSet outOfHostLinks) {
        
        log4j.debug("retrieveLinks url=" + pageUrl.toExternalForm());
        // Check atribute
        if (pageUrl == null)
            throw new NullPointerException("pageUrl is null.");
        if (pageContents == null)
            throw new NullPointerException("pageContents is null");
        if (crawledList == null)
            throw new NullPointerException("crawlerList is null");
        
        // Nastavim pole listov
        List[] arrayOfLists = new List[2];
        
        // Nastavi jednotlive typy listov.
        ArrayList linksToCrawl = new ArrayList();
        ArrayList linksForLucene = new ArrayList();
        
        // Premenna ukazuje ci je host zhodny s hostom startUrl.
        boolean theSameHosts = false;
        
        //Compile link matching pattern.
        Pattern p =
                Pattern.compile("<a\\s+href\\s*=\\s*\"?(.*?)[\"|>]",
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(pageContents);
        
        while (m.find()) {
            String link = m.group(1).trim();
            
            //Skip empty links.
            if (link.length() < 1) {
                continue;
            }
            
            // Overnie...!!!!!!!!!!!!
//            if (link.startsWith("'"))
//                continue;
            
            //Musim overit linky, ktore nekoncia na .mp3 .pdf atd
            if   ((link.endsWith(".jpg")) || (link.endsWith(".pdf")) ||
                    link.endsWith(".wmv") || (link.endsWith(".mpg")) ||
                    link.endsWith(".mov" ) || (link.endsWith(".gif")) ||
                    link.endsWith(".mp3") || (link.endsWith(".swf")) ||
                    link.endsWith(".avi") || (link.endsWith(".doc")) ||
                    link.endsWith(".mid") || (link.endsWith(".zip")) ||
                    link.endsWith(".ps") || (link.endsWith(".qt") ) ||
                    link.endsWith(".rar") || (link.endsWith(".txt")) ||
                    link.endsWith(".ppt") || (link.endsWith(".tif") ) ||
                    link.endsWith(".tar") || (link.endsWith(".bin")) ||
                    link.endsWith(".png") || (link.endsWith(".exe"))) {
//                    System.out.println("Neplatny LINK " + link);
                continue;
            }
            
            //Skip links that are just page anchors.
            if (link.charAt(0) == '#') {
                continue;
            }
            
            //Skip mailto links.
            if (link.indexOf("mailto:") != -1) {
                //I have to store these emails !!!
                continue;
            }
            
            //Skip JavaScript links.
            if (link.toLowerCase().indexOf("javascript") != -1) {
                continue;
            }
            
            //Prefix absolute and relative URLs if necesary.
            if (link.indexOf("://") == -1) {
                //Handle absolute URLs.
                if (link.charAt(0) == '/') {
                    link = "http://" + pageUrl.getHost() + link;
                    //Handle relative URLs.
                } else {
                    String file = pageUrl.getFile();
                    if (file.indexOf('/') == -1) {
                        link = "http://" + pageUrl.getHost() + "/" + link;
                    } else {
                        String path =
                                file.substring(0, file.lastIndexOf('/') + 1);
                        link = "http://" + pageUrl.getHost() + path + link;
                    }
                }
            }
            
            //Remove anchors form link.
            int index = link.indexOf('#');
            if (index != -1) {
                link = link.substring(0, index);
            }
            
            //Remove leading "www" from URL's host if present.
            link = removeWwwFromUrl(link);
            
            //Verify link and skip invalid.
            URL verifiedLink = verifyUrl(link);
            if (verifiedLink == null) {
                continue;
            }
            
            //Skip link if it has already been crawled.
            if (crawledList.contains(link)) {
                continue;
            }
            
            /*
             * Pokym je limitHost true, tak sa budu pridavat najdene linky
             * mimo host do kolekcie outOfHostLinks. Ked sa uz vycerpaju linky
             * z hostu, tak sa pridaju linky z outOfHostLinks do kolekcie
             * toCrawlList (overit aby sa nepridavali dve rovnake). Nasledne sa
             * nastavi limitHost na FALSE. Potom uz nemusim ukladat najdene
             * linky mimo host do outOfHostLInks (uz to nema vyznam), ale
             * vsetky co najdem pridam do kolekcie toCrawlList.
             * Overim podmienkou limitHost prave tu
             */
            if (limitHost) {
                // Ukladam najdeny link aj do outOfHostLinks ak je mimo host.
                
                // Maju rovnake hosty so startUrl ?
                theSameHosts = pageUrl.getHost().toLowerCase().equals(
                        verifiedLink.getHost().toLowerCase());
                // Ak niesu zhodne hosty, tak prirad url do outOfHostLinks aj do
                // linksForLucene.
                if (!theSameHosts) {
                    // Add links out of host to lists
//                    log4j.debug("retrieveLinks add outOfHostLink link=" + link);
                    
                    // zbytocne nepridavaj linky, ktore sa aj tak nebudu crawlovat.
                    if (outOfHostLinksNotFilled && outOfHostLinks.size() < MAX_URLS) {
                        outOfHostLinks.add(verifiedLink.toExternalForm());
                        if (outOfHostLinks.size() > MAX_URLS) {
                            outOfHostLinksNotFilled = false;
                        }
                    }
                    
                    if (!linksForLucene.contains(link)) {
                        linksForLucene.add(link);
                    }
                } else {
                    //Add link to lists linksToCrawl, linksForLucene
//                    log4j.debug("retrieveLinks add link=" + link);
                    if (!linksToCrawl.contains(link)) {
                        linksToCrawl.add(link);
                    }
                    if (!linksForLucene.contains(link)) {
                        linksForLucene.add(link);
                    }
                }
            } else {
                // Ukladam kazdy najdeny link do kolekcie linksToCrawl.
                if (!linksToCrawl.contains(link)) {
                    linksToCrawl.add(link);
                }
                if (!linksForLucene.contains(link)) {
                    linksForLucene.add(link);
                }
            }
        }
        arrayOfLists[0] = linksToCrawl;
        arrayOfLists[1] = linksForLucene;
        
        // TODO odstranit
//        for (Iterator i = linksToCrawl.iterator(); i.hasNext();) {
//            String link = (String) i.next();
//            log4j.debug("retrieveLinks linksToCrawl link=" + link);
//        }
//        for (Iterator i = outOfHostLinks.iterator(); i.hasNext();) {
//            String link = (String) i.next();
//            log4j.debug("retrieveLinks outOfHostLinks link=" + link);
//        }
        
        log4j.debug("retrieveLinks finished url=" + pageUrl.toExternalForm());
        return arrayOfLists;
    }
    
    
    
//    /* Determine wheather or not search string is matched in teh
//     * given page contents.
//     */
//    private boolean searchStringMatches(
//            String pageContents, String searchString,
//            boolean caseSensitive) {
//        String searchContents = pageContents;
//
//        /* If case sensitive search, lowercase page contents
//         * for comparison.
//         */
//        if (!caseSensitive) {
//            searchContents = pageContents.toLowerCase();
//        }
//
//        //Split search string into individual terms.
//        Pattern p = Pattern.compile("[\\s]+");
//        String[] terms = p.split(searchString);
//
//        //Check to see if each term matches.
//        for (int i = 0; i < terms.length; i++) {
//            if (caseSensitive) {
//                if (searchContents.indexOf(terms[i]) == -1) {
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }
    
    /**
     * Perform the actual crawling.
     *
     * @param startUrl   crawling starts from this startUrl.
     * @param limitHost   crawling urls only from host of startUrl.
     * @param maxUrls   crawling ends after maxUrls number are crawled.
     *
     */
    private boolean crawl(String startUrl, int maxUrls, boolean limitHost) {
        log4j.debug("crawl startUrl=" + startUrl + ", limitHost=" + limitHost);
        
        // Kontrola platnosti parametrov.
        if (startUrl == null)
            throw new NullPointerException("startUrl");
        if (maxUrls < 1)
            throw new IllegalArgumentException("maxUrl is less then zero.");
        //Setup crawl lists.
        
        /* Mnozina zcrawlovanych url. */
        HashSet crawledList = new HashSet();
        /* Mnozina url, ktore cakaju na crawlovanie. */
        LinkedHashSet toCrawlList = new LinkedHashSet();
        /* Zoznam url, ktore su mimo host. */
        // TODO otestovat zmenu outOfHostLinks na LinkedHashSet.
        LinkedHashSet outOfHostLinks = new LinkedHashSet();
        /* Zoznam kritickych HOSTov bez suboru robots.txt. */
        HashSet<String> hostsWithoutRobotTxtFile = new HashSet<String>();
        
        // Pociatocne crawlovnie startUrl
        boolean crawlStartUrl = true;
        
        //Add start URL to the to crawl list.
        toCrawlList.add(startUrl);
        
        /* Perform actual crawling by looping through the to crawl
         * list. Ak je stop true tak sa skonci crawling.
         */
        while (toCrawlList.size() > 0) {
            
            /* Check to see if the max URL has been reached, if it
             * was specified.
             */
            log4j.debug("MAX_URLS=" + MAX_URLS +
                    " crawled=" + crawledList.size());
            log4j.debug("toCrawlList    size=" + toCrawlList.size());
            if (outOfHostLinks  != null) {
                log4j.debug("outOfHostLinks size=" + outOfHostLinks.size());
            } else {
                log4j.debug("outOfHostLinks is null links were added to toCrawlList");
            }
            log4j.debug("hostsWithoutRobotTxtFile size=" + hostsWithoutRobotTxtFile.size());
            if (crawledList.size() == maxUrls) {
                log4j.debug("MAX_URLS number have been reached");
                break;
            }
            
            
            /* Ak Counter dosiahol hranicu, zavolal metodu stop a metoda
             * crawl() vrati true tj. url je v scope.
             */
            if (stop) {
                // Zavolat resetujucu metodu.
                resetCrawler();
                log4j.debug("Crawler was stopped and reseted page is valid");
                return true;
            }
            
            //Get URL at bottom of the list.
            String url = (String) toCrawlList.iterator().next();
            
            // Ak sa stranka uz analyzovala tak preskoc na dalsi cyklus. Ale
            // predtym odstran url zo zoznamu toCrawlList.
            if (crawledList.contains(url)) {
                toCrawlList.remove(url);
                continue;
            }
            
            // Logovanie kvoli vynimke.
            log4j.debug("Dalsia stranka na analyzu. url=" + url.toString());
            
            //Remove URL from the to crawl list.
            toCrawlList.remove(url);
            
            //Convert string url to URL object.
            URL verifiedUrl = verifyUrl(url);
            if (verifiedUrl == null) {
                continue;
            }
            
            //Skip URL if robots are not allowed to access it.
            if (!isRobotAllowed(verifiedUrl, hostsWithoutRobotTxtFile)) {
                continue;
            }
            
            //Add page to the crawled list.
            crawledList.add(url);
            log4j.debug("Url was added to crawledList url=" + url.toString());
            
            //Download the page at the given url.
//            String pageContents = downloadPage(verifiedUrl);
            // DownloadPageInBytes pokusne stahovanie stranky, treba otestovat
            // ci funguje OK a otestovat spravne kodovanie ziskaneho textu.
            // TODO otestovat metodu downloadPageInBytes(URL pageUrl). Dobre by
            // bolo otestovat zhodu vrateneho obsahu z oboch metod download...
            String pageContents = downloadPageByBaos(verifiedUrl);
            
//            System.out.println(pageContents);
            
            /* If the page was downloaded succesfully, retrieve all of its
             * links and then see if it contains the search string.
             */
            if (pageContents != null && pageContents.length() > 0) {
                
                
                List[] arrayOfListsOfLinks = new List[2];
                arrayOfListsOfLinks = retrieveLinks(verifiedUrl, pageContents,
                        crawledList, limitHost, outOfHostLinks);
                
                // Zbytocne nepridavaj odkazy, ktore sa aj tak nebudu crawlovat
                if (toCrawlListNotFilled && toCrawlList.size() < MAX_URLS) {
                    //Add links to the to crawl list.
                    toCrawlList.addAll(arrayOfListsOfLinks[0]);
                    if (toCrawlList.size() > MAX_URLS) {
                        toCrawlListNotFilled = false;
                    }
                }
                
                /*
                 * Prehladavanie obsahu stranky pageContents pomocou
                 * jedinacikov email, words... Mohol by som potom vyskusat
                 * volat prehladavanie v novom vlakne a otestovat co je
                 * rychlejsie.
                 */
                
                /*
                 * Asi bude dobre uvazovat analyzu prvej startUrl za cennejsie
                 * ako analyzy dalsich ponachadzanych odkazov.
                 */
                if (crawlStartUrl) {
                    // Analyza prvej startUrl strank//.
                    // TODO nastavit geoIpCountry urcite, aj nazov url urcite
                    // asi aj slova atd.
                    
                    // Hladanie start Url, ak sa najde tak pripocitaj x bodov
//                    GEO_IP_COUNTRY.search(verifiedUrl, 1);
                    crawlStartUrl = false;
                }
                
                
                EMAIL.search(pageContents);
                TELEPHONE.search(pageContents);
                LANG_ATTR.search(pageContents);
//                URLS.search(arrayOfListsOfLinks[1]);
//                FORBIDEN_URLS.search(arrayOfListsOfLinks[1]);
                GEO_IP_COUNTRY.search(arrayOfListsOfLinks[0]);
                
                // Prehladava prefiltrovany text od tagovacich znaciek.
                // TODO skontrolovanie ci je text nula alebo null do jedinackov.
                String pagePlainContents = withoutWhiteSpaces(
                        removeTags(pageContents));
                
                // Zlikvidovat objekt pageContents
                pageContents = null;
                
//                System.out.println("==================================================================================");
//                System.out.println(pagePlainContents);
//                System.out.println("==================================================================================");
                
                if (pagePlainContents != null &&
                        pagePlainContents.length() > 0) {
                    // odstran neplatne slova
                    String filteredWords =
                            FILTER_TEXT.getFilteredText(pagePlainContents);
                    
                    // Zlikvidovat objekt pagePlainContents
                    pagePlainContents = null;
                    
                    StringTokenizer stringTokenizer = new StringTokenizer(filteredWords);
                    String word;
                    while (stringTokenizer.hasMoreTokens()) {
                        word = stringTokenizer.nextToken();
                        WORDS.search(word);
//                        FORBIDDEN_WORDS.search(word);
//                        System.out.println(word);
                    }
                    URL_NAME.search(verifiedUrl);
//                    FORBIDDEN_URL_NAME.search(verifiedUrl);
                }
            }
            
//            if (toCrawlList.size() < 1) {
//                limitHost = false;
//
//                // TODO skontrolovat ci sa vyhadzuje vynimka NullPointerException
//                // vyhodi sa ak je niektory z objektov null a kolekcia nepovoluje
//                // null hodnoty, tak sa vyhodi NullPOinterException.
//                try {
//                    toCrawlList.addAll(arrayOfListsOfLinks[1]);
//                } catch (NullPointerException npe) {
//                    npe.printStackTrace();
//                    log4j.error("Crawl toCrawlList.addAll(collection) some " +
//                            "of the elements are null", npe.getCause());
//                }
//                log4j.debug("OutOfHostLinks were added to crawledList");
//            }
            
            // TODO upravit metodu, tak aby nevyhadzovala NullPointerException.
            // Preiteruje kolekciu, ktoru pridavam a ak budem elem null, tak
            // break a pokracujem dalsim elementom.
            // Dostane sa tu ak sa vycerpali linky z rovnakeho hostu ako
            // startUrl, vtedy odstranim outOfHostLinks, uz ju nebudem
            // potrebovat
            if ((toCrawlList.size() < 1) && limitHost) {
                limitHost = false;
                // TODO otestovat pridavanie outOfHostLinks do kolekcie toCrawlList
                if (outOfHostLinks != null) {
                    // Zmena na linkedHashSet
                    
                    toCrawlList.addAll(outOfHostLinks);
                    if (toCrawlList.size() > MAX_URLS) {
                        toCrawlListNotFilled = false;
                    }
//                    String link;
//                    Object object;
//                    for (Iterator i = outOfHostLinks.iterator(); i.hasNext();) {
//                        if ((object = i.next()) == null) {
//                            log4j.warn("CRAWL outOfHostLinks contains null element");
//                            break;
//                        }
//                        link = (String) object;
//                        toCrawlList.add(link);
//                    }
                }
                // outOfHostLinks uz nebudem potrebovat, odteraz pridavam
                // vsetky linky do toCrawlList.
                outOfHostLinks.clear();
                outOfHostLinks = null;
                
            }
            
        }
        // Tu sa dostane ak nastane break z cyklu, pri presiahnuti maxUrls.
        // Musim zavolat uckoncovaciu metodu, ktora resetuje parametre !!!
        
        resetCrawler();
        log4j.debug("Crawler was reseted");
        return false;
    }
    
    /**
     * Nova metoda, ktora sluzi pre novsiu verziu Heritrixu, s prehladavanim
     * 50 stranok pre kazde semienko. Ziska zo SimpleExtractora potrebne
     * parametre a z nich urci, ci je stranka ceska alebo nie. Pri kazdom
     * najdeni hladanej hodntoty sa inkrementuje pocet bodov points v objekte
     * Counter. Na konci sa points porovna ci, dosiahol hranicu pre czechBorder.
     *
     * @param url stranky
     * @param content stranky
     * @param odkazy na stranke
     * @reutrn true prave vtedy, ked je stranka ceska.
     */
    public boolean analyzePage(String url, String content, Set links) {
        log4j.info("analyzePage method started");
        resetCounter();
        boolean czech = false;
//        System.out.println("content=" + content);
        /*
         * It is important to initialize all necessary classes. It can be good
         * idea to initialize the instance in frontier at the position of
         * loading the first seed.
         */
        URL urlInstance = null;
        try {
            urlInstance = new URL(url);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }
//        if (content != null) {
//            EMAIL.search(content);
//            TELEPHONE.search(content);
//            LANG_ATTR.search(content);
//        }
        GEO_IP_COUNTRY.search(urlInstance, 999999);
        
        
//        if (links != null && links.size() > 0) {
//            GEO_IP_COUNTRY.search(links);
////                URLS.search(arrayOfListsOfLinks[1]);
////                FORBIDEN_URLS.search(arrayOfListsOfLinks[1]);
//        }
        
        // TODO hladanie nazvu v URL
//        URL_NAME.search(urlInstance);
//        FORBIDDEN_URL_NAME.search(verifiedUrl);
        
        
        // TODO este pridat hladanie v slovniku.
        // Prehladava prefiltrovany text od tagovacich znaciek.
        // TODO skontrolovanie ci je text nula alebo null do jedinackov.
//        String pagePlainContents = withoutWhiteSpaces(
//                removeTags(content));
//        
//        if (pagePlainContents != null &&
//                pagePlainContents.length() > 0) {
//            // odstran neplatne slova
//            String filteredWords =
//                    FILTER_TEXT.getFilteredText(pagePlainContents);
//            
//            // Zlikvidovat objekt pagePlainContents
//            pagePlainContents = null;
//            
//            StringTokenizer stringTokenizer = new StringTokenizer(filteredWords);
//            String word;
//            while (stringTokenizer.hasMoreTokens()) {
//                word = stringTokenizer.nextToken();
//                WORDS.search(word);
////                        FORBIDDEN_WORDS.search(word);
////                        System.out.println(word);
//            }
//        }
//        
        
        /*
         * After analyzing the crawler must be stopped(reseted). The instance
         * will be canceled in frontier at the position of calling the stop
         * method, after all seeds are processed.
         */
        
        writeStatistics();
        czech = COUNTER.reachedCzechBorder(999999);
        log4j.info("********************************************* " + url + ",===CZ=" +
                czech + " ==POINTS=" + COUNTER.getReachedPoints() +
                "*********************************************");
        resetCounter();
        return czech;
    }
}
