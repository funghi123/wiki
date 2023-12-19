package com.lastminute.recruitment.client;

import com.lastminute.recruitment.domain.error.WikiPageNotFound;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;

public class JsonWikiClient {

    public URL readJson(String link) {
        String name = link.replace("\"", "")
                .replace("http://wikiscrapper.test/", "/wikiscrapper/") + ".json";
        return Optional.ofNullable(getClass().getResource(name)).orElseThrow(WikiPageNotFound::new);
    }
}
