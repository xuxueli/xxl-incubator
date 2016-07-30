package com.xxl.util.core.skill.crawler.htmlparser.downpage;

import com.xxl.util.core.skill.crawler.htmlparser.HtmlParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * DownPageUtil(线程池并发操作) + HtmlParser(解析统计link) + PageDownLoader(根据URL下载整个页面,保存本地文件)
 * Created by xuxueli on 16/7/30.
 */
public class DownPageUtil {
    private static Logger logger = LoggerFactory.getLogger(DownPageUtil.class);

    // ------------------------ url 队列 ------------------------
    private static LinkedBlockingQueue<String> newUrlQueue = new LinkedBlockingQueue<String>();    // 未爬过的链接
    private static Set<String> oldUrlSet = new HashSet<String>();  // 已经爬过的链接
    private static String homeUrl;  // 只爬取该目录下的地址

    public static void run(String homeUrl) {
        DownPageUtil.newUrlQueue = new LinkedBlockingQueue<String>();
        DownPageUtil.oldUrlSet = new HashSet<String>();
        DownPageUtil.homeUrl = homeUrl;
        DownPageUtil.addUrl(homeUrl);
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
            exec.execute(new Thread(new CrawlerThread()));
        }
    }
    private static class CrawlerThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                String link = null;
                try {
                    link = DownPageUtil.takeUrl();
                } catch (InterruptedException e) {
                    logger.error("", e);
                }
                if (!(link != null && !"".equals(link) && link.startsWith("http://"))) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(20);
                    } catch (InterruptedException e) {
                        logger.error("", e);
                    }
                    continue;
                }

                logger.error("-------downer-------:" + link);
                PageDownLoader downer = new PageDownLoader();
                downer.downloadFile(link);

                Set<String> links = HtmlParserUtil.extracLinks(link);
                if (links!=null && links.size() > 0) {
                    for (String item : links) {
                        if (link != null && !"".equals(link) && link.startsWith("http://")) {
                            DownPageUtil.addUrl(item);
                        }
                    }
                }
            }
        }

    }

    public static void main(String[] args) {
        DownPageUtil.run("http://www.pook.com/");
    }

}
