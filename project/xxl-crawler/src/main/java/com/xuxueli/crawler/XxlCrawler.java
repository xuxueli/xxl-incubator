package com.xuxueli.crawler;

import com.xuxueli.crawler.util.JsoupUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *  xxl crawler
 *
 *  Created by xuxueli on 2015-05-14 22:44:43
 */
public class XxlCrawler {
    private static Logger logger = LoggerFactory.getLogger(XxlCrawler.class);

    private String indexUrl;            // 入口URL
    private Set<String> whiteUrlSet;    // URL白名单，非空时只爬去白名单下页面
    private int threadCount = 1;        // 爬虫线程数量
    private volatile LinkedBlockingQueue<String> unVisitedUrlQueue = new LinkedBlockingQueue<String>();  // 未访问过的URL
    private volatile Set<String> visitedUrlSet = Collections.synchronizedSet(new HashSet<String>());;    // 已经访问过的URL
    private ExecutorService crawlers = Executors.newCachedThreadPool();

    // ---------------------- builder ----------------------
    public static class Builder {
        private XxlCrawler crawler = new XxlCrawler();

        public Builder setIndexUrl(String indexUrl) {
            crawler.indexUrl = indexUrl;
            return this;
        }
        public Builder setWhiteUrlSet(Set<String> whiteUrlSet) {
            crawler.whiteUrlSet = whiteUrlSet;
            return this;
        }
        public Builder setThreadCount(int threadCount) {
            crawler.threadCount = threadCount;
            return this;
        }
        public XxlCrawler build() {
            return crawler;
        }
    }

    // ---------------------- crawler url ----------------------

    /**
     * url add
     * @param link
     */
    private void addUrl(String link) {
        if (!JsoupUtil.isUrl(link)) {
            return; // check URL格式
        }
        if (visitedUrlSet.contains(link)) {
            return; // check 未访问过
        }
        if (unVisitedUrlQueue.contains(link)) {
            return; // check 未记录过
        }
        if (whiteUrlSet!=null && whiteUrlSet.size()>0) {
            boolean underWhiteUrl = false;
            for (String whiteUrl: whiteUrlSet) {
                if (link.startsWith(whiteUrl)) {
                    underWhiteUrl = true;
                }
            }
            if (!underWhiteUrl) {
                return; // check 白名单
            }
        }
        unVisitedUrlQueue.add(link);
        logger.info(">>>>>>>>>>> xxl crawler addUrl ： {}", link);
    }

    /**
     * url take
     * @return
     * @throws InterruptedException
     */
    public String takeUrl() throws InterruptedException {
        String link = unVisitedUrlQueue.take();
        if (link != null) {
            visitedUrlSet.add(link);
        }
        return link;
    }

    // ---------------------- crawler thread ----------------------
    public void start(){
        if (!JsoupUtil.isUrl(indexUrl)) {
            throw new RuntimeException("xxl crawler fall, indexUrl[" + indexUrl + "] not valid.");
        }
        if (threadCount < 1) {
            throw new RuntimeException("xxl crawler fall, threadCount[" + threadCount + "] not valid.");
        }
        for (int i = 0; i < threadCount; i++) {
            CrawlerThread crawlerThread = new XxlCrawler.CrawlerThread(this);
            crawlers.execute(crawlerThread);
        }
        logger.info(">>>>>>>>>>> xxl crawler start ...");
        addUrl(indexUrl);
        crawlers.shutdown();
    }
    public void stop(){
        logger.info(">>>>>>>>>>> xxl crawler stop ...");
        crawlers.shutdownNow();
    }

    private static class CrawlerThread implements Runnable {
        private XxlCrawler crawler;
        public CrawlerThread(XxlCrawler crawler) {
            this.crawler = crawler;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    String link = crawler.takeUrl();
                    logger.info(">>>>>>>>>>> xxl crawler, link : {}", link);
                    if (!JsoupUtil.isUrl(link)) {
                        continue;
                    }

                    // ------- 解析 糗百页面 start ----------
                    // 组装规则()
                    Map<Integer, Set<String>> tagMap = new HashMap<Integer, Set<String>>();
                    Set<String> tagNameList = new HashSet<String>();
                    tagNameList.add("article");
                    tagMap.put(2, tagNameList);

                    // 获取html
                    Elements resultAll = JsoupUtil.loadParse(link, null, null, false, tagMap);

                    // 解析html
                    Set<String> result = new HashSet<String>();
                    if (resultAll!=null && resultAll.hasText())
                    for (Element item : resultAll) {
                        String content = item.getElementsByClass("content").html();
                        String thumb = item.getElementsByClass("thumb").html();
                        String video_holder = item.getElementsByClass("video_holder").html();

                        StringBuffer buffer = new StringBuffer();
                        buffer.append(content);
                        if (thumb!=null && thumb.trim().length()>0) {
                            buffer.append("<hr>");
                            buffer.append(thumb);
                        }
                        if (video_holder!=null && video_holder.trim().length()>0) {
                            buffer.append("<hr>");
                            buffer.append(video_holder);
                        }
                        String str = buffer.toString();
                        if (str!=null && str.trim().length()>0) {
                            result.add(str);
                        }
                    }
                    for (String content: result) {
                        logger.info(content);
                    }
                    // ------- 解析 糗百页面 end ----------

                    // 爬取子节点：爬取子链接 (FIFO队列,广度优先)
                    Set<String> links = JsoupUtil.findLinks(link);
                    if (links!=null && links.size() > 0) {
                        for (String item : links) {
                            crawler.addUrl(item);
                        }
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

            }
        }
    }

}
