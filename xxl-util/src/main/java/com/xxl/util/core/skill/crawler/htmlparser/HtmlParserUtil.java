package com.xxl.util.core.skill.crawler.htmlparser;

import com.xxl.util.core.skill.crawler.htmlparser.downpage.PageDownLoader;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by xuxueli on 2015-05-14 22:44:43
 */
public class HtmlParserUtil {
    private static Logger logger = LoggerFactory.getLogger(PageDownLoader.class);

    /**
     * 获取一个网站上的a链接
     * @param url
     * @return
     */
    public static Set<String> extracLinks(String url) {
        Set<String> links = new HashSet<String>();

        try {
            Parser parser = new Parser(url);
            parser.setEncoding("utf-8");
            // 过滤 <frame >标签的 filter，用来提取 frame 标签里的 src 属性所表示的链接
            @SuppressWarnings("serial")
            NodeFilter frameFilter = new NodeFilter() {
                public boolean accept(Node node) {
                    if (node.getText().startsWith("frame src=")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };
            // OrFilter 来设置过滤 <a> 标签，和 <frame> 标签
            OrFilter linkFilter = new OrFilter(new NodeClassFilter(LinkTag.class), frameFilter);
            // 得到所有经过过滤的标签
            NodeList list = parser.extractAllNodesThatMatch(linkFilter);
            for (int i = 0; i < list.size(); i++) {
                Node tag = list.elementAt(i);
                if (tag instanceof LinkTag) {
                    // <a> 标签
                    LinkTag link = (LinkTag) tag;
                    String linkUrl = link.getLink();
                    links.add(linkUrl);
                } else {
                    // 提取 frame 里 src 属性的链接如 <frame src="test.html"/>
                    String frame = tag.getText();
                    int start = frame.indexOf("src=");
                    frame = frame.substring(start);
                    int end = frame.indexOf(" ");
                    if (end == -1) {
                        end = frame.indexOf(">");
                    }
                    String frameUrl = frame.substring(5, end - 1);
                    links.add(frameUrl);
                }
            }
        } catch (ParserException e) {
            logger.error("", e);
        }
        return links;
    }

    public static void main(String[] args) {
        Set<String> links = HtmlParserUtil.extracLinks("http://www.baidu.com/");
        for (String link : links) {
            logger.error(link);
        }
    }

}
