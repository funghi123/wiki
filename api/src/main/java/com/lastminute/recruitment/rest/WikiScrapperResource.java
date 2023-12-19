package com.lastminute.recruitment.rest;

import com.lastminute.recruitment.domain.WikiScrapper;
import com.lastminute.recruitment.domain.error.WikiPageNotFound;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RequestMapping("/wiki")
@RestController
public class WikiScrapperResource {

    public WikiScrapperResource(WikiScrapper wikiScrapper) {
        this.wikiScrapper = wikiScrapper;
    }

    private final WikiScrapper wikiScrapper;

    @PostMapping("/scrap")
    public void scrapWikipedia(@RequestBody String link) {
        try {
            wikiScrapper.read(link);
        } catch (WikiPageNotFound e) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @ControllerAdvice
    public class WikiScrapperREsourceResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

        @ExceptionHandler({WikiPageNotFound.class})
        public ResponseEntity<Object> handleAccessDeniedException(Exception ex, WebRequest request) {
            return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
        }
    }
}
