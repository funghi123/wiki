    package com.lastminute.recruitment.domain;

import java.util.HashSet;
import java.util.Set;

public class WikiScrapper {

    private final WikiReader wikiReader;
    private final WikiPageRepository repository;

    public WikiScrapper(WikiReader wikiReader, WikiPageRepository repository) {
        this.wikiReader = wikiReader;
        this.repository = repository;
    }

    public void read(String link) {
        visit(wikiReader.read(link), new HashSet<>());
    }

    private void visit(WikiPage wikiPage, Set<String> alreadyVisited) {
        alreadyVisited.add(wikiPage.getSelfLink());
        repository.save(wikiPage);
        visitChildren(wikiPage, alreadyVisited);
    }

    private void visitChildren(WikiPage wikiPage, Set<String> alreadyVisited) {
        wikiPage.getLinks()
                .stream()
                .filter(link -> !alreadyVisited.contains(link))
                .forEach(link -> visit(wikiReader.read(link), alreadyVisited));
    }
}
