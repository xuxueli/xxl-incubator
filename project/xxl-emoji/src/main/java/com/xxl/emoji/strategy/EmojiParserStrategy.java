package com.xxl.emoji.strategy;

import com.xxl.emoji.strategy.impl.AliasEmojiParser;
import com.xxl.emoji.strategy.impl.HtmlDecimalEmojiParser;
import com.xxl.emoji.strategy.impl.UnicodeEmojiParser;

/**
 * @author xuxueli 2018-07-04 20:57:14
 */
public enum EmojiParserStrategy {

    /**
     * parse emoji to decimal
     */
    HTML_DECIMAL(new HtmlDecimalEmojiParser()),

    /**
     * parse emoji to unicode
     */
    UNICODE(new UnicodeEmojiParser()),

    /**
     * parse emoji to alias
     */
    ALIAS(new AliasEmojiParser());

    private EmojiParser emojiParser;
    EmojiParserStrategy(EmojiParser emojiParser) {
        this.emojiParser = emojiParser;
    }

    public EmojiParser getEmojiParser() {
        return emojiParser;
    }

    public static EmojiParserStrategy match(String name){
        for (EmojiParserStrategy item:EmojiParserStrategy.values()) {
            if (item.name().equals(name)) {
                return item;
            }
        }
        return null;
    }
}