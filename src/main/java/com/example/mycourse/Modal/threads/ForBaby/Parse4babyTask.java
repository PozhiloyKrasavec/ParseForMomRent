package com.example.mycourse.Modal.threads.ForBaby;

import com.example.mycourse.Modal.Goods;
import javafx.concurrent.Task;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class Parse4babyTask extends Task<ArrayList<Goods>> {
    static String[] urls4baby = {
            "https://4baby.spb.ru/catalog/tovary-dlya-kormleniya?page="
            ,"https://4baby.spb.ru/catalog/hodunki-i-kacheli?page="
            ,"https://4baby.spb.ru/catalog/vesy-i-elektronika?page="
            ,"https://4baby.spb.ru/catalog/kokon-i-karusel?page="
            ,"https://4baby.spb.ru/catalog/igry-i-razvlecheniya?page="
            ,"https://4baby.spb.ru/catalog/avtokresla-i-perenoski?page="
    };
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
    private static Document getPage(String url) throws IOException {
        Document page = Jsoup.parse(new URL(url),10000);
        return page;
    }
    @Override
    protected ArrayList<Goods> call() throws Exception {
        int count = urls4baby.length;
        this.updateMessage("PARSE START");
        ArrayList<Goods> goods = new ArrayList<>();
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
        this.updateMessage("ITEM LINKS COLLECTED");
        Collections.sort(itemsLinks);
        pages.clear();
        count = itemsLinks.size();
        int j=0;
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
            j++;
            this.updateProgress(j,count);
        }
        this.updateMessage("GOODS PARSED");
        return goods;
    }
}
