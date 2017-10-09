package com.xuxueli.crawler.test;

import com.xuxueli.crawler.XxlCrawler;

import java.util.Arrays;
import java.util.HashSet;

/**
 * xxl crawler test
 *
 * @author xuxueli 2017-10-09 19:48:48
 */
public class XxlCrawlerTest {

    public static void main(String[] args) {
        XxlCrawler crawler = new XxlCrawler.Builder()
                .setIndexUrl("https://www.qiushibaike.com")
                .setThreadCount(3)
                .setWhiteUrlSet(new HashSet<String>(Arrays.asList("https://www.qiushibaike.com")))
                .build();
        crawler.start();
    }

}
