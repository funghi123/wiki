package com.lastminute.recruitment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
    classes=WikiScrapperApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class WikiScrapperResourceBaseTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void canScrap() throws Exception {
        // when
        ResponseEntity<String> result =
            restTemplate.postForEntity("/wiki/scrap", "http://wikiscrapper.test/site5", String.class);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
        // this test should also check what data has been added to repo, but current interface
        // and it's implementation don't make it possible
    }

    @Test
    public void returnsNotFoundForNonexistingLink() throws Exception {
        // when
        ResponseEntity<String> result =
            restTemplate.postForEntity("/wiki/scrap", "http://wikiscrapper.test/site6", String.class);

        // then
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}
