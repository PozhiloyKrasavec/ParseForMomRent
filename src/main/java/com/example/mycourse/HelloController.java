package com.example.mycourse;

import com.example.mycourse.Modal.Goods;
import com.example.mycourse.Modal.threads.ForBaby.Export4babyTask;
import com.example.mycourse.Modal.threads.ForBaby.Parse4babyTask;
import com.example.mycourse.Modal.threads.Toyrent.ExportToyrentTask;
import com.example.mycourse.Modal.threads.Toyrent.ParseToyrentTask;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;

public class HelloController{
    Exchanger<String> exchanger = new Exchanger<>();
    ArrayList<Goods> goods1 = new ArrayList<>();
    ArrayList<Goods> goods2 = new ArrayList<>();
    @FXML
    private Label stateWindow;
    @FXML
    private ProgressBar stateBar;
    public void parse4BabyBtnOn(ActionEvent e) throws IOException, InterruptedException{
        Parse4babyTask parse4babyTask = new Parse4babyTask();
        stateBar.progressProperty().bind(parse4babyTask.progressProperty());
        stateWindow.textProperty().bind(parse4babyTask.messageProperty());

        new Thread(parse4babyTask).start();
        parse4babyTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                stateBar.progressProperty().unbind();
                stateBar.progressProperty().setValue(0);
                stateWindow.textProperty().unbind();
                try {
                    goods2 = parse4babyTask.get();
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
        });

    }
    @FXML
    public void parseToyRentBtnOn(ActionEvent e) throws IOException, InterruptedException {
        ParseToyrentTask parseToyrentTask = new ParseToyrentTask();
        stateBar.progressProperty().bind(parseToyrentTask.progressProperty());
        stateWindow.textProperty().bind(parseToyrentTask.messageProperty());

        new Thread(parseToyrentTask).start();
        parseToyrentTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                stateBar.progressProperty().unbind();
                stateBar.progressProperty().setValue(0);
                stateWindow.textProperty().unbind();
                try {
                    goods1 = parseToyrentTask.get();
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    @FXML
    public void exportToCSV4BabyBtnOn(ActionEvent e) throws IOException, InterruptedException{
        Export4babyTask export4babyTask = new Export4babyTask(goods2);
        stateWindow.textProperty().bind(export4babyTask.messageProperty());

        new Thread(export4babyTask).start();
        export4babyTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                stateWindow.textProperty().unbind();
            }
        });
    }
    @FXML
    public void exportToCSVToyrentBtnOn(ActionEvent e) throws InterruptedException {
        ExportToyrentTask exportToyrentTask = new ExportToyrentTask(goods1);
        stateWindow.textProperty().bind(exportToyrentTask.messageProperty());

        new Thread(exportToyrentTask).start();
        exportToyrentTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                stateWindow.textProperty().unbind();
            }
        });
    }
}
