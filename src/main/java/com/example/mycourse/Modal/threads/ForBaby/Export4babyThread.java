package com.example.mycourse.Modal.threads.ForBaby;

import com.example.mycourse.Modal.parsers.Parser4Baby;

import java.io.IOException;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeoutException;

public class Export4babyThread extends Thread{
    Exchanger<String> exchanger;

    public Export4babyThread(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
            Parser4Baby.exportCsv(exchanger);
        } catch (IOException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
