package com.example.mycourse.Modal.threads.Toyrent;

import com.example.mycourse.Modal.Goods;
import javafx.concurrent.Task;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ExportToyrentTask extends Task {
    public static ArrayList<Goods> goods = new ArrayList<>();
    public ExportToyrentTask(ArrayList<Goods> introGoods) {
        goods = introGoods;
    }
    @Override
    protected Object call() throws Exception {
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
        this.updateMessage("EXPORT COMPLETE");
       return null;
    }
}
