package com.lastminute.recruitment.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lastminute.recruitment.domain.WikiPage;
import com.lastminute.recruitment.domain.WikiReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class JsonWikiReader implements WikiReader {

    private final JsonWikiClient jsonWikiClient;

    public JsonWikiReader(JsonWikiClient jsonWikiClient) {
        this.jsonWikiClient = jsonWikiClient;
    }

    @Override
    public WikiPage read(String link) {
        try {
            String content = Files.readString(Paths.get(jsonWikiClient.readJson(link).toURI()));
            WikiPageJson wikiPageJson = new ObjectMapper().readValue(content, WikiPageJson.class);
            return new WikiPage(
                wikiPageJson.getTitle(),
                wikiPageJson.getContent(),
                wikiPageJson.getSelfLink(),
                wikiPageJson.getLinks()
            );
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class WikiPageJson {
        private final String title;
        private final String content;
        private final String selfLink;
        private final List<String> links;

        @JsonCreator
        public WikiPageJson(
            @JsonProperty("title") String title,
            @JsonProperty("content") String content,
            @JsonProperty("selfLink") String selfLink,
            @JsonProperty("links") List<String> links) {
            this.title = title;
            this.content = content;
            this.selfLink = selfLink;
            this.links = links;
        }

        public List<String> getLinks() {
            return links;
        }

        public String getSelfLink() {
            return selfLink;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
    }
}
