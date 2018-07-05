package com.xxl.emoji.core;

import java.util.Collections;
import java.util.List;

/**
 * emoji obj
 */
public class Emoji {

    private final String description;
    private final boolean supportsFitzpatrick;
    private final List<String> aliases;
    private final List<String> tags;
    private final String unicode;
    private final String htmlDec;
    private final String htmlHex;


    public Emoji(String description,
                 boolean supportsFitzpatrick,
                 List<String> aliases,
                 List<String> tags,
                 String unicode) {

        // fill data
        this.description = description;
        this.supportsFitzpatrick = supportsFitzpatrick;
        this.aliases = Collections.unmodifiableList(aliases);   // refuse modefy
        this.tags = Collections.unmodifiableList(tags);
        this.unicode = unicode;

        // make dec/hex data
        int stringLength = getUnicode().length();
        String[] pointCodes = new String[stringLength];
        String[] pointCodesHex = new String[stringLength];

        int count = 0;
        for (int offset = 0; offset < stringLength; ) {
            final int codePoint = getUnicode().codePointAt(offset);

            pointCodes[count] = String.format("&#%d;", codePoint);
            pointCodesHex[count++] = String.format("&#x%x;", codePoint);

            offset += Character.charCount(codePoint);
        }
        this.htmlDec = stringJoin(pointCodes, count);
        this.htmlHex = stringJoin(pointCodesHex, count);

    }

    /**
     * string join, like "String.join" in java8
     */
    private String stringJoin(String[] array, int count) {
        String joined = "";
        for (int i = 0; i < count; i++)
            joined += array[i];
        return joined;
    }


    // ---------------------- get ----------------------

    public String getDescription() {
        return description;
    }

    public boolean supportsFitzpatrick() {
        return this.supportsFitzpatrick;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public String getUnicode() {
        return this.unicode;
    }

    public String getHtmlDecimal() {
        return this.htmlDec;
    }

    public String getHtmlHexadecimal() {
        return this.htmlHex;
    }


    // ---------------------- other ----------------------

    public String getUnicode(Fitzpatrick fitzpatrick) {
        if (!this.supportsFitzpatrick()) {
            throw new UnsupportedOperationException("Cannot get the unicode with a fitzpatrick modifier, the emoji doesn't support fitzpatrick.");
        } else if (fitzpatrick == null) {
            return this.getUnicode();
        }
        return this.getUnicode() + fitzpatrick.unicode;
    }

    @Override
    public boolean equals(Object other) {
        return !(other == null || !(other instanceof Emoji)) &&
                ((Emoji) other).getUnicode().equals(getUnicode());
    }

    @Override
    public int hashCode() {
        return unicode.hashCode();
    }

}
