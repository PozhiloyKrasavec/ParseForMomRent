package com.example.mycourse.Modal.threads.ForBaby;

import com.example.mycourse.Modal.Goods;
import javafx.concurrent.Task;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Export4babyTask extends Task {
    public ArrayList<Goods> goods = new ArrayList<>();

    public Export4babyTask(ArrayList<Goods> goods) {
        this.goods = goods;
    }

    @Override
    protected Object call() throws Exception {
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
        this.updateMessage("EXPORT COMPLETE");
        return null;
    }
}
