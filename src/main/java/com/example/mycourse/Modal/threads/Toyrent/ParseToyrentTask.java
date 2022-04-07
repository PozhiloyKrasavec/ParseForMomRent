package com.example.mycourse.Modal.threads.Toyrent;

import com.example.mycourse.Modal.Goods;
import javafx.concurrent.Task;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Exchanger;

public class ParseToyrentTask extends Task<ArrayList<Goods>> {
    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream input = new URL(url).openStream();
        try {
            BufferedReader re = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
            String Text = Read(re);
            JSONObject json = new JSONObject(Text);
            return json;
        } catch (Exception e) {
            return null;
        } finally {
            input.close();
        }
    }

    public static void Change_Label(Exchanger<String> stringExchanger) throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            stringExchanger.exchange("label" + 1);
        }
    }

    private static String Read(Reader re) throws IOException {
        StringBuilder str = new StringBuilder();
        int temp;
        do {
            temp = re.read();
            str.append((char) temp);
        } while (temp != -1);
        return str.toString();
    }

    static String[] urls = {
            "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/detskie-avtokresla.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/kolyaski.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/chemodany.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/ergoryukzaki-i-sumkikenguru.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/videonyani-prokat-radionyani.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/manezhi-i-krovatki.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/vsyo-dlya-kupaniya.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/igrushki-dlya-kupaniya.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/konstruktory.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/razvivayuschie-kovriki.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/mashinki-ruli-i-garazhi.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/elektrokacheli.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/suhie-basseyny.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/detskie-vesy.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/shezlongi-detskie-lyulki.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/playpens.json"
            , "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/sportkompleksy.json"
    };

    @Override
    protected ArrayList<Goods> call() throws Exception {
        this.updateMessage("PARSE START");
        ArrayList<Goods> goods = new ArrayList<>();
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        ArrayList<JSONArray> prodArray = new ArrayList<>();
        int count = urls.length;
        int j=0;
        for (String url : urls) {
            jsonObjects.add(readJsonFromUrl(url));
            j++;
            this.updateProgress(j,count);
        }
        for (JSONObject json : jsonObjects) {
            prodArray.add(json.getJSONObject("pageProps").getJSONObject("searchResult").getJSONArray("products"));
        }
        ArrayList<String> durationList = new ArrayList<>();
        Set<String> tempSet = prodArray.get(0).getJSONObject(0).getJSONObject("price").keySet();
        durationList.add(tempSet.stream().filter(data -> Objects.equals(data, "WeekRentPrice")).findFirst().get());
        durationList.add(tempSet.stream().filter(data -> Objects.equals(data, "MonthRentPrice")).findFirst().get());
        for (JSONArray jsonArray : prodArray) {count+=jsonArray.length();}
        for (JSONArray jsonArray : prodArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                ArrayList<String> pricesList = new ArrayList<>();
                String usrName = jsonArray.getJSONObject(i).getString("usrName");
                pricesList.add(Integer.toString(jsonArray.getJSONObject(i).getJSONObject("price").getInt("WeekRentPrice")));
                pricesList.add(Integer.toString(jsonArray.getJSONObject(i).getJSONObject("price").getInt("MonthRentPrice")));
                goods.add(new Goods(usrName, durationList, pricesList));
                this.updateProgress(i,count);
            }
        }
        this.updateMessage("GOODS PARSED");
        return goods;
    }
}


