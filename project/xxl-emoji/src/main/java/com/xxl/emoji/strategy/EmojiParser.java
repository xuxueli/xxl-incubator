package com.xxl.emoji.strategy;

/**
 * emoji parser
 *
 * @author xuxueli 2018-07-04 20:42:53
 */
public abstract class EmojiParser {

    public abstract String encode(String input);

    public abstract String decode(String input);

}
