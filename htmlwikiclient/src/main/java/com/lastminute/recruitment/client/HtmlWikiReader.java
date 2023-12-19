package com.lastminute.recruitment.client;

import com.lastminute.recruitment.domain.WikiPage;
import com.lastminute.recruitment.domain.WikiReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class HtmlWikiReader implements WikiReader {

    private final HtmlWikiClient htmlWikiClient;

    public HtmlWikiReader(HtmlWikiClient htmlWikiClient) {
        this.htmlWikiClient = htmlWikiClient;
    }

    @Override
    public WikiPage read(String link) {
        String content;
        try {
            content = Files.readString(Paths.get(htmlWikiClient.readHtml(link).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return toPage(content);
    }

    private WikiPage toPage(String content) {
        Document document = Jsoup.parse(content);
        return new WikiPage(
            document.body().select("h1.title").text(),
            document.body().select("p.content").text(),
            document.getElementsByAttribute("selfLink").attr("selfLink"),
            getLinks(document)
        );
    }

    private List<String> getLinks(Document document) {
        Element ul = document.select("ul").first();
        return ul == null ?
            Collections.emptyList() :
            ul.select("li")
                .stream()
                .map(this::getHref)
                .collect(toList());
    }

    private String getHref(Element li) {
        return Objects.requireNonNull(li.select("a").first()).attr("href");
    }
}
