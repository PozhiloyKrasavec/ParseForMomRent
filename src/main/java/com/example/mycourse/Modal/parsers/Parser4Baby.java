package com.example.mycourse.Modal.parsers;

import com.example.mycourse.Modal.Goods;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeoutException;

public class Parser4Baby implements Parser {
    static String[] urls4baby = {
            "https://4baby.spb.ru/catalog/tovary-dlya-kormleniya?page="
            ,"https://4baby.spb.ru/catalog/hodunki-i-kacheli?page="
            ,"https://4baby.spb.ru/catalog/vesy-i-elektronika?page="
            ,"https://4baby.spb.ru/catalog/kokon-i-karusel?page="
            ,"https://4baby.spb.ru/catalog/igry-i-razvlecheniya?page="
            ,"https://4baby.spb.ru/catalog/avtokresla-i-perenoski?page="
    };
    static ArrayList<Goods> goods = new ArrayList<>();


    private static Document getPage(String url) throws IOException {
        Document page = Jsoup.parse(new URL(url),10000);
        return page;
    }
    public static void parse(Exchanger<String> exchanger) throws IOException, InterruptedException, TimeoutException {
        String stateText = "ParseStart";
        exchanger.exchange(stateText);
        ArrayList<Document> pages = new ArrayList<>();
        Document page = Document.createShell("");
        for (int i=0;i< urls4baby.length;i++)
        {
            int j=1;
            page = Document.createShell("");
            while (page!=null){
                try {
                    page = getPage(urls4baby[i]+j);
                } catch (IOException e) {
                    page = null;
                }
                if (page!=null) pages.add(page);
                j++;
            }
        }
        ArrayList<String> itemsLinks  = new ArrayList<>();
        for (Document docs : pages){
            Elements tempItemLinksCollection = docs.select("a[class=b-item__caption]");
            for (Element link : tempItemLinksCollection){
                itemsLinks.add("https://4baby.spb.ru" + link.attr("href"));
            }
        }
        Collections.sort(itemsLinks);
        stateText = "ITEM LINKS COLLECTED";
        exchanger.exchange(stateText);
        System.out.println("ITEM LINKS COLLECTED");
        pages.clear();
        for (String link : itemsLinks){
            page = getPage(link);
            String goodsName = page.select("h1[class=b-title-3]").text();
            Elements  durations = page.select("span[class=b-cost__duration]");
            Elements costs = page.select("div[class=b-cost__wrap-bg]");
            costs = clearFromDuration(costs);
            ArrayList<String> cost_duration = new ArrayList<>();
            ArrayList<String> cost_num = new ArrayList<>();
            for (int i=0;i< durations.size();i++)
            {
                cost_duration.add(durations.get(i).text());
                cost_num.add(transformCost(costs.get(i)));
            }
            goods.add(new Goods(goodsName,cost_duration,cost_num));
        }
        stateText = "GOODS PARSED";
        exchanger.exchange(stateText);
        System.out.println("GOODS PARSED");
    }
    private static String transformCost(Element cost){
        StringBuilder temp = new StringBuilder(cost.text());
        temp.delete(temp.indexOf("р"),temp.length());
        return temp.toString();
    }
    private static Elements clearFromDuration(Elements costs){
        Elements newCosts = new Elements();
        for (int i=0;i< costs.size();i++){
            Element temp = null;
            try {
                temp = costs.get(i).child(0);
            } catch (Exception e) {
            }
            if (temp==null) newCosts.add(costs.get(i));
            else{
                Element temp2 = costs.get(i).before(temp);
                if (!temp2.text().isEmpty()) newCosts.add(temp2);
            }
        }
        return newCosts;
    }
    public static void clearGoodsList(Exchanger<String> exchanger) throws InterruptedException, TimeoutException {
        String stateText = "GOODS LIST CLEARED";
        goods.clear();
        exchanger.exchange(stateText);
        System.out.println("GOODS LIST CLEARED");
    }
    public static void exportCsv(Exchanger<String> exchanger) throws IOException, InterruptedException, TimeoutException {
        File file = new File("C:\\Users\\user\\IdeaProjects\\MyCourse\\src\\main\\CSV");
        if (!file.exists()) file.mkdirs();
        Appendable printWriter = new PrintWriter(file+"/4baby.csv","cp1251");

        CSVPrinter csvPrinter = CSVFormat.EXCEL.withHeader ("Название товара", "1срок", "2срок", "3срок","4срок","5 срок","6 срок","1цена", "2цена", "3цена","4цена","5цена","6цена").print(printWriter);
        for (Goods gds : goods){
           if (gds.getCost_num().isEmpty()) csvPrinter.printRecord(gds.getName(),null,null,null,null,null,null,null,null,null,null,null,null);
           else if(gds.getCost_num().size()==3) csvPrinter.printRecord(gds.getName()
                   ,gds.getCost_duration().get(0)
                   ,gds.getCost_duration().get(1)
                   ,gds.getCost_duration().get(2)
                   ,null
                   ,null
                   ,null
                   ,gds.getCost_num().get(0)
                   ,gds.getCost_num().get(1)
                   ,gds.getCost_num().get(2)
                   ,null
                   ,null
                   ,null);
           else if (gds.getCost_num().size()==4) csvPrinter.printRecord(gds.getName()
                   ,gds.getCost_duration().get(0)
                   ,gds.getCost_duration().get(1)
                   ,gds.getCost_duration().get(2)
                   ,gds.getCost_duration().get(3)
                   ,null
                   ,null
                   ,gds.getCost_num().get(0)
                   ,gds.getCost_num().get(1)
                   ,gds.getCost_num().get(2)
                   ,gds.getCost_num().get(3)
                   ,null
                   ,null);
           else if (gds.getCost_num().size()==5) csvPrinter.printRecord(gds.getName()
                   ,gds.getCost_duration().get(0)
                   ,gds.getCost_duration().get(1)
                   ,gds.getCost_duration().get(2)
                   ,gds.getCost_duration().get(3)
                   ,gds.getCost_duration().get(4)
                   ,null
                   ,gds.getCost_num().get(0)
                   ,gds.getCost_num().get(1)
                   ,gds.getCost_num().get(2)
                   ,gds.getCost_num().get(3)
                   ,gds.getCost_num().get(4)
                   ,null);
           else if (gds.getCost_num().size()==6) csvPrinter.printRecord(gds.getName()
                   ,gds.getCost_duration().get(0)
                   ,gds.getCost_duration().get(1)
                   ,gds.getCost_duration().get(2)
                   ,gds.getCost_duration().get(3)
                   ,gds.getCost_duration().get(4)
                   ,gds.getCost_duration().get(5)
                   ,gds.getCost_num().get(0)
                   ,gds.getCost_num().get(1)
                   ,gds.getCost_num().get(2)
                   ,gds.getCost_num().get(3)
                   ,gds.getCost_num().get(4)
                   ,gds.getCost_num().get(5));
        }
        csvPrinter.flush();
        csvPrinter.close();
        String stateText = "EXPORT COMPLETE";
        exchanger.exchange(stateText);
        System.out.println("EXPORT COMPLETE");
    }
}
