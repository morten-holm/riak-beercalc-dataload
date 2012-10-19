package dk.muhko;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mholm
 * Date: 12/10/12
 */
public class BeerService {

    public List<Beer> fetchAllBeers() throws IOException {
        List<Beer> beers = new ArrayList<Beer>();
        int i = 0;
        for (String beerName : indexBeers()) {
            try {
                Beer beer = fetchBeer(beerName, i);
                System.out.print(".");
                if (beer != null) {
                    beers.add(beer);
                } else {
                    System.err.println("Unable to fetch: " + beerName);
                }
            } catch (Exception e) {
                throw new RuntimeException("Unable to fetch " + beerName, e);
            }
            i++;
        }
        return beers;
    }

    private List<String> indexBeers() throws IOException {
        String url = "http://www.haandbryg.dk/cgi-bin/beercalc.cgi?startshow=1&numshow=99999";
        Document doc = Jsoup.parse(new URL(url).openStream(), "iso-8859-1", url);

        List<String> result = new ArrayList<String>();

        Elements beerElements = doc.select("tr[class=medium]");
        for (Element beerElement : beerElements) {
            result.add(beerElement.select("td a").text());
        }

        System.out.println("Found: " + result.size() + " beers");
        return result;
    }


    public Beer fetchBeer(String beerName, int i) throws IOException {

        Connection connect = Jsoup.connect("http://www.haandbryg.dk/cgi-bin/beercalc.cgi?pick=" + URLEncoder.encode(beerName, "iso-8859-1") + "&mine=&sort=date&stylesort=0&numshow=100&namesearch=&brewersearch=");
        connect.header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/536.26.14 (KHTML, like Gecko) Version/6.0.1 Safari/536.26.14");
        connect.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        connect.header("Referer", "http://www.haandbryg.dk/cgi-bin/beercalc.cgi");
        connect.header("Accept-Language", "en-us");
        connect.header("Accept-Encoding", "gzip, deflate");
        connect.header("Cookie", "brewerpwd=none;brewer=; brewerpwd=none");
        connect.header("Connection", "keep-alive");

        Document doc = connect.get();

        //FileUtils.writeStringToFile(new File("output/" + i + ".html"), doc.outerHtml());

        return createBeer(doc);
    }

    public Beer createBeer(File file) {
        try {
            return createBeer(Jsoup.parse(file, "iso-8859-1"));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private Beer createBeer(Document doc) {
        if (doc.text().indexOf("publicise") > 0) {
            return null;
        }

        Element mainTable = doc.select("body table table").get(3);

        String name = mainTable.select("input[name=name]").attr("value");
        String yeast = mainTable.select("select[name=yeast]").select("option[selected]").text();

        Beer beer = new Beer(name, "n/a", yeast);

        Elements malts = mainTable.select("select[name=malt]").select("option[selected]");
        for (Element maltElement : malts) {
            String maltName = maltElement.text();
            Elements row = maltElement.parent().parent().parent().select("td");
            String weight = row.select("input[name=weight]").attr("value");
            String ecb = row.select("input[name=col]").attr("value");
            Malt malt = new Malt(maltName, weight, ecb);
            if (malt.include()) {
                beer.addMalt(malt);
            }
        }

        Elements hops = mainTable.select("select[name=hop]").select("option[selected]");
        for (Element hopElement : hops) {
            String hopName = hopElement.text();
            Elements row = hopElement.parent().parent().parent().select("td");
            String alpha = row.select("input[name=acid]").attr("value");
            String weight = row.select("input[name=hopw]").attr("value");
            String boil = row.select("select[name=boil]").select("option[selected]").text();
            Hop hop = new Hop(hopName, weight, alpha, boil);
            if (hop.include()) {
                beer.addHop(hop);
            }
        }

        return beer;
    }
}
