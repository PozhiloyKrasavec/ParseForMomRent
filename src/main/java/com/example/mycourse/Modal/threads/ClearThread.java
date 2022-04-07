package com.example.mycourse.Modal.threads;

import com.example.mycourse.Modal.parsers.Parser4Baby;
import com.example.mycourse.Modal.parsers.ParserToyrent;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeoutException;

public class ClearThread extends Thread {
    Exchanger<String> exchanger;

    public ClearThread(Exchanger<String> exchanger) {
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        try {
                Parser4Baby.clearGoodsList(exchanger);
                ParserToyrent.clearGoodsList(exchanger);
                join();
        } catch (InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
