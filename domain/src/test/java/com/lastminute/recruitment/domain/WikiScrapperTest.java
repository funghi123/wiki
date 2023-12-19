package com.lastminute.recruitment.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.lastminute.recruitment.domain.WikiScrapperTest.WikiPageBuilder.builder;
import static org.mockito.Mockito.*;

public class WikiScrapperTest {
    // no test for page not found case as it's handled by a lower layer and transparent for this class

    WikiReader wikiReader;
    WikiPageRepository repository;

    @Test
    public void canScrap() {
        // given
        wikiReader = mock(WikiReader.class);
        repository = mock(WikiPageRepository.class);
        WikiScrapper scrapper = new WikiScrapper(wikiReader, repository);

        // and
        // 1 -> 2 <-> 3 -> 4
        WikiPage page1 = builder().title("title1").content("content1").selfLink("link1").links("link2").build();
        WikiPage page2 = builder().title("title2").content("content2").selfLink("link2").links("link3").build();
        WikiPage page3 = builder().title("title3").content("content3").selfLink("link3").links("link2", "link4").build();
        WikiPage page4 = builder().title("title4").content("content4").selfLink("link4").links().build();
        when(wikiReader.read("link1")).thenReturn(page1);
        when(wikiReader.read("link2")).thenReturn(page2);
        when(wikiReader.read("link3")).thenReturn(page3);
        when(wikiReader.read("link4")).thenReturn(page4);

        // when
        scrapper.read("link2");

        // verify
        verify(repository, never()).save(page1);
        verify(repository).save(page2);
        verify(repository).save(page3);
        verify(repository).save(page4);
    }

    static class WikiPageBuilder {
        private String title;
        private String content;
        private String selfLink;
        private List<String> links;

        static WikiPageBuilder builder() {
            return new WikiPageBuilder();
        }

        WikiPageBuilder title(String title) {
            this.title = title;
            return this;
        }

        WikiPageBuilder content(String content) {
            this.content = content;
            return this;
        }

        WikiPageBuilder selfLink(String selfLink) {
            this.selfLink = selfLink;
            return this;
        }

        WikiPageBuilder links(String... links) {
            this.links = Arrays.stream(links).toList();
            return this;
        }

        WikiPage build() {
            return new WikiPage(title, content, selfLink, links);
        }
    }
}
