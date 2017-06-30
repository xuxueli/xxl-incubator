package com.xxl.util.core.skill.crawler.jsoup.explainpage;

import com.xxl.util.core.skill.crawler.jsoup.JsoupUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *  HtmlExplainUtil(线程池并发操作) + JsoupUtil(根据URL,解析页面文档元素)
 *  Created by xuxueli on 2015-05-14 22:44:43
 */
public class HtmlExplainUtil {
    private static Logger logger = LoggerFactory.getLogger(HtmlExplainUtil.class);

    // ------------------------ url 队列 ------------------------
    private static LinkedBlockingQueue<String> newUrlQueue = new LinkedBlockingQueue<String>();    // 未爬过的链接
    private static Set<String> oldUrlSet = new HashSet<String>();  // 已经爬过的链接
    private static String homeUrl;  // 只爬取该目录下的地址

    public static void run(String homeUrl) {
        HtmlExplainUtil.newUrlQueue = new LinkedBlockingQueue<String>();
        HtmlExplainUtil.oldUrlSet = new HashSet<String>();
        HtmlExplainUtil.homeUrl = homeUrl;
        HtmlExplainUtil.addUrl(homeUrl);
    }

    /**
     * url add
     * @param link
     */
    private static void addUrl(String link) {
        if (link != null && link.trim().length()>0
                && link.startsWith(homeUrl)
                && !newUrlQueue.contains(link)
                && !oldUrlSet.contains(link)) {
            newUrlQueue.add(link);
        }
    }

    /**
     * url take
     * @return
     * @throws InterruptedException
     */
    public static String takeUrl() throws InterruptedException {
        String link = newUrlQueue.take();
        if (link != null) {
            oldUrlSet.add(link);
        }
        return link;
    }

    // ------------------------ url 队列 ------------------------
    private static ExecutorService exec = Executors.newCachedThreadPool();
    static {
        for (int i = 0; i < 3; i++) {
            exec.execute(new Thread(new HtmlExplainUtil.CrawlerThread()));
        }
    }
    private static class CrawlerThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    String link = HtmlExplainUtil.takeUrl();
                    if (!JsoupUtil.isUrl(link)) {
                        continue;
                    }
                    logger.error("-------explain-------:" + link);

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
                    for (Element item : resultAll) {
                        String content = item.getElementsByClass("content").html();
                        String thumb = item.getElementsByClass("thumb").html();
                        String video_holder = item.getElementsByClass("video_holder").html();

                        StringBuffer buffer = new StringBuffer();
                        buffer.append(content);
                        if (StringUtils.hasLength(thumb)) {
                            buffer.append("<hr>");
                            buffer.append(thumb);
                        }
                        if (StringUtils.hasLength(video_holder)) {
                            buffer.append("<hr>");
                            buffer.append(video_holder);
                        }
                        String str = buffer.toString();
                        if (StringUtils.hasLength(str)) {
                            result.add(str);
                        }
                    }
                    for (String content: result) {
                        logger.info(content);
                    }
                    // ------- 解析 糗百页面 end ----------

                    // 爬取子节点：爬取子链接 (FIFO队列,广度优先)
                    Set<String> links = JsoupUtil.findLinks(HtmlExplainUtil.homeUrl, link);
                    if (links!=null && links.size() > 0) {
                        for (String item : links) {
                            HtmlExplainUtil.addUrl(item);
                        }
                    }
                } catch (InterruptedException e) {
                    logger.error("", e);
                }

            }
        }
    }

    public static void main(String[] args) {
        HtmlExplainUtil.run("http://www.qiushibaike.com/");
    }

}
