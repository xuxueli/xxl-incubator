package com.xxl.emoji.core;

/**
 * Fitzpatrick scale: https://en.wikipedia.org/wiki/Fitzpatrick_scale
 *
 * for Unicode, a numerical classification schema for human skin color.
 */
public enum Fitzpatrick {

    /**
     * Type I (scores 0–6) always burns, never tans (pale white; blond or red hair; blue eyes; freckles).
     * Type II (scores 7–13) usually burns, tans minimally (white; fair; blond or red hair; blue, green, or hazel eyes)
     */
    TYPE_1_2("\uD83C\uDFFB"),

    /**
     * Type III (scores 14–20) sometimes mild burn, tans uniformly (cream white; fair with any hair or eye color)
     */
    TYPE_3("\uD83C\uDFFC"),

    /**
     * Type IV (scores 21–27) burns minimally, always tans well (moderate brown)
     */
    TYPE_4("\uD83C\uDFFD"),

    /**
     * Type V (scores 28–34) very rarely burns, tans very easily (dark brown)
     */
    TYPE_5("\uD83C\uDFFE"),

    /**
     * Fitzpatrick modifier of type 6 (black)
     */
    TYPE_6("\uD83C\uDFFF");

    /**
     * Type VI (scores 35–36) Never burns, never tans (deeply pigmented dark brown to darkest brown)
     */
    public final String unicode;

    Fitzpatrick(String unicode) {
        this.unicode = unicode;
    }


    public static Fitzpatrick fitzpatrickFromUnicode(String unicode) {
        for (Fitzpatrick item : values()) {
            if (item.unicode.equals(unicode)) {
                return item;
            }
        }
        return null;
    }

    public static Fitzpatrick fitzpatrickFromType(String type) {
        try {
            return Fitzpatrick.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
