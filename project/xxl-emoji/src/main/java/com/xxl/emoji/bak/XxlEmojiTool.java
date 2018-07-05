//package com.xxl.emoji.bak;
//
//import com.xxl.emoji.exception.XxlEmojiException;
//import com.xxl.emoji.bak.strategy.EmojiParser;
//import com.xxl.emoji.bak.strategy.EmojiParserStrategy;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * xxl emoji tool
// *
// * @author xuxueli 2018-07-04 18:27:14
// */
//public class XxlEmojiTool {
//    private static Logger logger = LoggerFactory.getLogger(XxlEmojiTool.class);
//
//    public static String encode(String input, String strategy) {
//        return matchEmojiParser(strategy).encode(input);
//    }
//
//    public static String decode(String input, String strategy) {
//        return matchEmojiParser(strategy).decode(input);
//    }
//
//    private static EmojiParser matchEmojiParser(String strategy){
//        EmojiParserStrategy emojiParserStrategy = EmojiParserStrategy.match(strategy);
//        if (emojiParserStrategy == null) {
//            throw new XxlEmojiException("xxl emoji strategy illegal.");
//        }
//        return emojiParserStrategy.getEmojiParser();
//    }
//
//
//    public static void main(String[] args) {
//
//
//        if (true) {
//            System.out.println("----------- "+ EmojiParserStrategy.HTML_DECIMAL.name() +" ------------");
//            String emoji = "  An 😀😃awesome 😃😃string with a few 😉😃emojis!";
//            String desc = "&#128581; &#128582; &#128145;An &#128512;&#128515;awesome &#128515;&#128515;string with a few &#128521;&#128515;emojis!";
//
//            String decimal = encode(emoji, EmojiParserStrategy.HTML_DECIMAL.name());
//            System.out.println(decimal);
//
//        }
//
//        if (false) {
//            System.out.println("----------- toAlias ------------");
//            String str2 = "  An 😃😀awesome 😃😃string with a few 😃😉emojis!";
//            System.out.println(":no_good: :ok_woman: :couple_with_heart:An :smiley::grinning:awesome :smiley::smiley:string with a few :smiley::wink:emojis!".equals(null));
//        }
//
//        if (false) {
//            System.out.println("----------- toUnicode ------------");
//            String str3 = "   :smiley: :grinning: :wink:";
//            System.out.println("🙅 🙆 💑 😃 😀 😉".equals(null));
//        }
//
//    }
//
//
//}
