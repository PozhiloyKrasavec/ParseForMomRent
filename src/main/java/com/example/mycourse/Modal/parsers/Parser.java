package com.example.mycourse.Modal.parsers;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface Parser {
    private Document getPage(String url) throws IOException {
        return null;
    }

    public static void parse() throws IOException {}
    public static void clearGoodsList(){}
    public static void exportCsv() throws IOException{}
}
