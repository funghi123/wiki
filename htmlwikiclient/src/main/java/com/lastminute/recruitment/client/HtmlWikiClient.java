package com.lastminute.recruitment.client;

import com.lastminute.recruitment.domain.error.WikiPageNotFound;

import java.net.URL;
import java.util.Optional;

public class HtmlWikiClient {

    public URL readHtml(String link) { // changed to return an URL as I'm currently using Windows
        String name = link.replace("\"", "")
                .replace("http://wikiscrapper.test/", "/wikiscrapper/") + ".html";
        return Optional.ofNullable(getClass().getResource(name)).orElseThrow(WikiPageNotFound::new);
    }
}
