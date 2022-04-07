package com.example.mycourse.Modal.parsers;

import com.example.mycourse.Modal.Goods;
import javafx.scene.control.Label;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeoutException;

public class ParserToyrent implements Parser {
    static String[] urls = {
            "https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/detskie-avtokresla.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/kolyaski.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/chemodany.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/ergoryukzaki-i-sumkikenguru.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/videonyani-prokat-radionyani.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/manezhi-i-krovatki.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/vsyo-dlya-kupaniya.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/igrushki-dlya-kupaniya.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/konstruktory.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/razvivayuschie-kovriki.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/mashinki-ruli-i-garazhi.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/elektrokacheli.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/suhie-basseyny.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/detskie-vesy.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/shezlongi-detskie-lyulki.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/playpens.json"
            ,"https://toyrent.ru/_next/data/8HzDQX4yHjJ3BGe39pEjn/catalog/sportkompleksy.json"
    };
    public static ArrayList<Goods> goods = new ArrayList<>();
    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException{
        InputStream input = new URL(url).openStream();
        try{
            BufferedReader re = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
            String Text = Read(re);
            JSONObject json = new JSONObject(Text);
            return json;
        } catch (Exception e){
            return null;
        }finally {
            input.close();
        }
    }
    public static void Change_Label(Exchanger<String> stringExchanger) throws InterruptedException {
        for (int i=0;i<1000;i++){
            stringExchanger.exchange("label" + 1);
        }
    }
    private static String Read(Reader re) throws IOException{
        StringBuilder str = new StringBuilder();
        int temp;
        do{
            temp=re.read();
            str.append((char) temp);
        }while (temp!=-1);
        return str.toString();
    }
    public static void  parse() throws IOException, InterruptedException {
        ArrayList<JSONObject> jsonObjects = new ArrayList<>();
        ArrayList<JSONArray> prodArray = new ArrayList<>();
        for (String url : urls){
            jsonObjects.add(readJsonFromUrl(url));
        }
        for (JSONObject json:jsonObjects){
             prodArray.add(json.getJSONObject("pageProps").getJSONObject("searchResult").getJSONArray("products"));
        }
        ArrayList<String> durationList = new ArrayList<>();
        Set<String> tempSet =  prodArray.get(0).getJSONObject(0).getJSONObject("price").keySet();
        durationList.add(tempSet.stream().filter(data-> Objects.equals(data,"WeekRentPrice")).findFirst().get());
        durationList.add(tempSet.stream().filter(data-> Objects.equals(data,"MonthRentPrice")).findFirst().get());
        for (JSONArray jsonArray : prodArray){
            for (int i=0;i<jsonArray.length();i++){
                ArrayList<String> pricesList = new ArrayList<>();
                String usrName = jsonArray.getJSONObject(i).getString("usrName");
                pricesList.add(Integer.toString(jsonArray.getJSONObject(i).getJSONObject("price").getInt("WeekRentPrice")));
                pricesList.add(Integer.toString(jsonArray.getJSONObject(i).getJSONObject("price").getInt("MonthRentPrice")));
                goods.add(new Goods(usrName,durationList,pricesList));
            }
        }
    }
    public static void clearGoodsList(Exchanger<String> exchanger) throws InterruptedException, TimeoutException {
        String stateText = "GOODS LIST CLEARED";
        goods.clear();
        exchanger.exchange(stateText);
        System.out.println("GOODS LIST CLEARED");
    }
    public static void exportCsv(Exchanger<String> exchanger) throws IOException, InterruptedException {
        File file = new File("C:\\Users\\user\\IdeaProjects\\MyCourse\\src\\main\\CSV");
        if (!file.exists()) file.mkdirs();
        Appendable printWriter = new PrintWriter(file+"/Toyrent.csv","cp1251");
        CSVPrinter csvPrinter = CSVFormat.EXCEL.withHeader ("Название товара", "1срок", "2срок","1цена", "2цена").print(printWriter);
        for (Goods gds : goods){
            if (gds.getCost_num().isEmpty()) csvPrinter.printRecord(gds.getName(),null,null,null,null);
            else csvPrinter.printRecord(gds.getName()
                    ,gds.getCost_duration().get(0)
                    ,gds.getCost_duration().get(1)
                    ,gds.getCost_num().get(0)
                    ,gds.getCost_num().get(1));
        }
        csvPrinter.flush();
        csvPrinter.close();
        String stateText = "EXPORT COMPLETE";
        exchanger.exchange(stateText);
        System.out.println("EXPORT COMPLETE");
    }
}
