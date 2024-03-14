package com.online_shop.onlineShop.User.controllers;

import com.online_shop.onlineShop.Scraper.ScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hello_world")
public class DemoController {
    private final ScraperService scraperService;
    @GetMapping
    public String example() throws IOException {
        scraperService.scrapeData();
        //scraperService.contentScraper("https://sergeev-store.ru/obuv/nike");
        return "good";
    }
}
