/*
 * Class that tests threads and memory use netbeans profiler to run this.
 */
package cz.webarchiv.webanalyzer.multithread.testobj;

import cz.webarchiv.webanalyzer.multithread.WebAnalyzer;
import cz.webarchiv.webanalyzer.multithread.analyzer.UrlAnalyzer;
import cz.webarchiv.webanalyzer.multithread.managers.WebAnalyzerManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author praso
 */
public class ProfileWebAnalyzer {

    private static final Logger log4j =
            Logger.getLogger(ProfileWebAnalyzer.class);

    private static void dictionarySearching() throws Exception {
        log4j.debug("dictionarySearching");
        WebAnalyzerManager.getInstance().initializeManagers();

        MakeTestCrawlURI maker = new MakeTestCrawlURI();
        SimpleTestCrawlURI curi = maker.createEmptyCrawlURI("http://seznam.cz");
        UrlAnalyzer analyzer = new UrlAnalyzer();
        analyzer.analyze(curi.getUrl().toString(),
                curi.getContent(), curi.getOutLinks(), null);

        log4j.debug("reached points=" +
                analyzer.getPointsCounter().getPoints());
        WebAnalyzerManager.getInstance().closeManagers();
        log4j.debug("dictionarySearching");
    }

    private static void multiThreadSearching() throws Exception {
        try {
            log4j.debug("multiThreadSearching");
            WebAnalyzer.getInstance().initialize();

            // todo urobit crawl pre search(curi) aj seach(curi.name, points, ...)
            List<SimpleTestThread> toes = new ArrayList<SimpleTestThread>();
            List<UrlAnalyzer> analyzers = new ArrayList<UrlAnalyzer>();
            MakeTestCrawlURI maker = new MakeTestCrawlURI();
            for (int i = 0; i < 50; i++) {
                SimpleTestCrawlURI curi = maker.createEmptyCrawlURI();
                curi.setOutLinks(maker.createTestURIs());
                // create a thread
                log4j.debug("url is contentType=" + WebAnalyzer.getInstance().isContetTypeText(curi.getUrl().toString()));
                SimpleTestThread toe = new SimpleTestThread();
                toe.setSimpleTestCrawlURI(curi);
                toe.setAnalyzers(analyzers);
                toes.add(toe);
            }

            for (SimpleTestThread t : toes) {
                t.start();
            }

            // pause main thread for a while, so that the toe thread has enough
            // time to search URL.
            // use join method instead of this sleep method. See DBTest.java
            Thread.currentThread().sleep(20 * 1000);

            for (UrlAnalyzer analyzer : analyzers) {
                log4j.debug(analyzer.getPointsCounter().getPoints());
            }

            WebAnalyzer.getInstance().close();
            log4j.debug("multiThreadSearching");
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(ProfileWebAnalyzer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /** New method for testin DictionarySearcher
     *
     *
     * @param args
     * @throws Exception
     */
    public static void testDictionarySearcher() throws Exception {

//        String text = "ahoj žžže svaz";
//        String text = "svaz republiky svaz republiky the marbles association of the czech republic www kulicky eu prodej úvod aktuality kluby kontakty turnaje termíny výsledky výsledky tréninky statistiky s rozhozem cvrnkaná statistiky vzájemné zápasy pravidla hry do ringer fotogalerie fotogalerie z jak se hraje ve videa jakhrát o v proti hádej kdo jsem registrace do svazu registrace emailové adresy o historie prodej v nových kardinál bílý tygr hroch v chotovicích u nového boru se chotovické se v kategorii a výsledky a fotogalerie zde sleva od ks jen u nás si vybrat z za ceny mistrovství republiky v v cvrnkaná fojtík na jan a na milan novák disciplínu s rozhozem vyhrál jakub fuss na se umístil fojtík a na milan novák fotogalerie zde mensovního mistrovství republiky v vyhrál fojtík josefem drobným a michalem mistrovství prahy se konalo v letenských sadech za systémem na martin na druhém vladimír karel v tv roku hádej kdo jsem o ve zprávách na http www youtube com watch v svaz na ladronce první jarní turnaj v kterého se po napínavém finále o jedinou fojtík erikem ráczem na milan novák zde draci z roztok které se letos kategorií vyhrál fojtík let bláha do let tereza kompletní výsledky a foto novinka v výzva na souboj mají získat body i mimo turnaje a to vyzváním postaveného v na souboj tato je výhodná pro kterým se nehodí termíny svazových mají málo a myslí si jsou postavení na pokud vyzvaného porazí se jim do koeficient vyzvaného a to hned dvakrát prakticky to bude ve výsledcích vypadat jakoby vyhráli dva turnaje po a za získají tolik kolik je koeficient vyzvaný získá nuly jako by taky odehrál dva turnaje a za získal cílem tohoto je dosáhnou ve kterém budou rozhodovat vzájemné zápasy podrobnosti viz aktuality pokud chcete dostávat pozvánky na turnaje zaregistrujte si svou emailovou adresu registrace emailové adresy chcete zdarma k hry s rozhozem fuss jakub fojtík novák milan vladimír svatoslav petra jan martin feber martin kompletní hry cvrnkaná fojtík jan michal novák milan svatoslav blecha fuss jakub jaroslav holatová dana prouza milan kompletní kuli ky jej m autorem je zbyn k fojt k podl h licenci creative commons uve te do d la esko src http www webarchiv cz images jpg width height style border alt webarchiv archiv webu c sportovní stránky spanker svaz repbliky oddíly v republice má ve vlastní turnaje prakticky týden dále jsem a na objednávku nabízíme na prodejkteré se dají sehnat u nás se ale tyto se objeví z starých zásob";
        String text = "svaz republiky svaz republiky the marbles association of the czech republic www kulicky eu prodej úvod aktuality kluby kontakty turnaje termíny výsledky výsledky tréninky statistiky s rozhozem cvrnkaná statistiky vzájemné zápasy pravidla hry do ringer fotogalerie fotogalerie z jak se hraje ve videa jakhrát o v proti hádej kdo jsem registrace do svazu registrace emailové adresy o historie prodej new page svazu republiky svaz vedeher do se turnaje které následující kritéria turnaj svaz turnaj byl schválen do vedením svazu turnaj podle pravidel svazu turnajese ve do nebo min ve ringer turnaj se hrál systémem nebo systémem s výsledky turnaje vzájemných byly svazu do po turnaje základní bodování v turnaji pro místo je tyto body se násobí koeficientem kde h je celkový kde p je pro hru kde z je z první dvacítky kde jsou koeficienty se turnaje pro mistrovství republiky koeficient se jako z posledních absolvovaných se po pátém absolvovaném turnaji se jeden výsledek za dní bez absolvování turnaje se jeden turnaj s nulovým ziskem o na turnajích rozhoduje rozdíl her vyhranýchher rozdíl vyhraných vzájemné zápasy podle nová hra na nasazování se nasazují do podle osobního koeficientu ostatním se vylosuje výzva na souboj mají získat body i mimo turnaje a to vyzváním postaveného v na souboj tato je výhodná pro kterým se nehodí termínysvazových mají málo a myslí si jsou postavení na pokud vyzvaného porazí se jim do koeficient vyzvaného a to hned dvakrát prakticky to bude ve výsledcích vypadat jakoby vyhráli dva turnaje po a za získají tolik kolik je koeficient vyzvaný získá nuly jako by taky odehrál dva turnaje a za získal cílem tohoto je dosáhnou ve kterém budou rozhodovat vzájemné zápasy který bude chtít postoupit na na pozici vyzve postaveného na souboj emailovou zprávou kopii na mail svazu ten musí výzvu do pokud zasahuje do srpen listopad prosinec leden únor se na pokud se souboj z na vyzvaného se to za vyzyvatele termín vyzvaný s ohledem na vyzyvatele vyzvaný na kterém se bude hrát a má právo odehrání souboje v trvalého pokud se dohodnou na jiném pak je to pro závazné dohadování o souboji se vede jedním emailem ve kterém je historie vzájemných zpráv pro spory na je neodehraní souboje a na kterého turnaje rozhodne o výsledku má právo odmítnou souboj pokud maximální denní teplota soubojem bude jak na www ceskatelevize cz teletext str denní teploty nazítra souboj nebude ale bude tak souboj platí jeden souboj se bude hrát na dva zápasy na hry s pokud souboj na zápasy na druhém orozhoduje celkový a na vyhraných her smyslem je aby po prvním prohraném zápase druhý zápas nevzdal po první prohrané ale se vyhrát hry a vyhrát na pokud vyzyvatel nevyhraje body nezískají ani nulové t j jakoby nehráli v jednom být stejný vyzván jen jedním vyzyvatelem vyzyvatelem být vyzván po odehrání zápasu stejná dvojice se vyzvat dní po odehrání souboje aby i tyto vyzvat vyzyvatel který vyzve na souboj zaplatí startovné svazu a to za místo o které je vyzvaný vyzyvatelem t j pokud vyzve sebou zaplatí o místa sebou o ale pro rozdíly nad a nové podle aktuálního v den kdy vyzval poslal email v získává koeficient platný v den souboje výsledek souboje se skórem her oba svazu do po odehrání ale do data turnaje t j pokud bude souboj den turnajem musí výsledek odeslat ihned ten den mailovou komunikaci posílají i v kopii na svaz svaz bude na stránkách www kulicky eu aktuální stav výzev pro v termínovce svaz má právo delegovat na souboj pokud sám bude delegování zaplatí svazu startovné je nutné uhradit svazu do po vyzvání do dne souboje v platby na jako variabilní symbol prvních cifer rodného datum narození na který posílat startovné platby dary a jiné penízek hry s rozhozem fuss jakub fojtík novák milan vladimír svatoslav petra jan martin feber martin kompletní hry cvrnkaná fojtík janmichal novák milan svatoslav blecha fuss jakub jaroslav holatová dana prouza milan kompletní kuli ky jej m autorem je zbyn k fojtk podl h licenci creative commons uve te do d la esko src http www webarchiv cz images jpg width height style border alt webarchiv archiv webu c sportovní stránky spanker";




         WebAnalyzer.getInstance().initialize();

         WebAnalyzer.getInstance().run("http://seznam.cz", text, null, "text/html");

         WebAnalyzer.getInstance().close();

    }


    public static void main(String[] args) throws Exception {
//        dictionarySearching();
//        multiThreadSearching();
        testDictionarySearcher();
    }
}
