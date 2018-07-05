package com.xxl.emoji;

import com.xxl.emoji.core.EmojiTrie;
import com.xxl.emoji.core.Fitzpatrick;
import com.xxl.emoji.core.Emoji;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * emoji tool
 */
public class EmojiParser {


    // ------------------------ from unicode ------------------------

    /**
     * replace emoji unicode by one of their first alias (between 2 ':')
     *
     * [unicode emoji >> first alias ]
     *
     * @param input
     * @param fitzpatrickAction     the action to apply for the fitzpatrick modifiers
     * @return
     */
    public static String parseToAliases(String input, final FitzpatrickAction fitzpatrickAction) {
        EmojiTransformer emojiTransformer = new EmojiTransformer() {
            public String transform(UnicodeCandidate unicodeCandidate) {
                switch (fitzpatrickAction) {
                    default:
                    case PARSE:
                        if (unicodeCandidate.hasFitzpatrick()) {
                            return ":" +
                                    unicodeCandidate.getEmoji().getAliases().get(0) +
                                    "|" +
                                    unicodeCandidate.getFitzpatrickType() +
                                    ":";
                        }
                    case REMOVE:
                        return ":" +
                                unicodeCandidate.getEmoji().getAliases().get(0) +
                                ":";
                    case IGNORE:
                        return ":" +
                                unicodeCandidate.getEmoji().getAliases().get(0) +
                                ":" +
                                unicodeCandidate.getFitzpatrickUnicode();
                }
            }
        };

        return parseFromUnicode(input, emojiTransformer);
    }


    /**
     * replace unicode emoji by their html representation.
     *
     * [unicode emoji >> html hex ]
     *
     * @param input
     * @param fitzpatrickAction the action to apply for the fitzpatrick modifiers
     * @return
     */
    public static String parseToHtmlDecimal(String input, final FitzpatrickAction fitzpatrickAction) {
        EmojiTransformer emojiTransformer = new EmojiTransformer() {
            public String transform(UnicodeCandidate unicodeCandidate) {
                switch (fitzpatrickAction) {
                    default:
                    case PARSE:
                    case REMOVE:
                        return unicodeCandidate.getEmoji().getHtmlDecimal();    // parse+remove, will deletec modifier
                    case IGNORE:
                        return unicodeCandidate.getEmoji().getHtmlDecimal() + unicodeCandidate.getFitzpatrickUnicode();     // IGNORE, will ignored and remain modifier
                }
            }
        };

        return parseFromUnicode(input, emojiTransformer);
    }

    /**
     * replace unicode emoji by their html hex representation
     *
     * [unicode emoji >> html hex ]
     *
     * @param input
     * @param fitzpatrickAction     the action to apply for the fitzpatrick modifiers
     * @return
     */
    public static String parseToHtmlHexadecimal(String input, final FitzpatrickAction fitzpatrickAction) {
        EmojiTransformer emojiTransformer = new EmojiTransformer() {
            public String transform(UnicodeCandidate unicodeCandidate) {
                switch (fitzpatrickAction) {
                    default:
                    case PARSE:
                    case REMOVE:
                        return unicodeCandidate.getEmoji().getHtmlHexadecimal();
                    case IGNORE:
                        return unicodeCandidate.getEmoji().getHtmlHexadecimal() + unicodeCandidate.getFitzpatrickUnicode();
                }
            }
        };

        return parseFromUnicode(input, emojiTransformer);
    }

    /**
     * remove all emojis
     *
     * [unicode emoji >> remove(provided) ]
     *
     * @param str
     * @return
     */
    public static String removeAllEmojis(String str) {
        EmojiTransformer emojiTransformer = new EmojiTransformer() {
            public String transform(UnicodeCandidate unicodeCandidate) {
                return "";
            }
        };

        return parseFromUnicode(str, emojiTransformer);
    }

    /**
     * remove provided emojis
     *
     * [unicode emoji >> remove(provided) ]
     *
     * @param str
     * @param emojisToRemove
     * @return
     */
    public static String removeEmojis(String str, final Collection<Emoji> emojisToRemove) {
        EmojiTransformer emojiTransformer = new EmojiTransformer() {
            public String transform(UnicodeCandidate unicodeCandidate) {
                if (!emojisToRemove.contains(unicodeCandidate.getEmoji())) {
                    return unicodeCandidate.getEmoji().getUnicode() + unicodeCandidate.getFitzpatrickUnicode();
                }
                return "";
            }
        };

        return parseFromUnicode(str, emojiTransformer);
    }

    /**
     * remove not provided emojis
     *
     * [unicode emoji >> remove(not provided) ]
     *
     * @param str
     * @param emojisToKeep
     * @return
     */
    public static String removeAllEmojisExcept(String str, final Collection<Emoji> emojisToKeep) {
        EmojiTransformer emojiTransformer = new EmojiTransformer() {
            public String transform(UnicodeCandidate unicodeCandidate) {
                if (emojisToKeep.contains(unicodeCandidate.getEmoji())) {
                    return unicodeCandidate.getEmoji().getUnicode() + unicodeCandidate.getFitzpatrickUnicode();
                }
                return "";
            }
        };

        return parseFromUnicode(str, emojiTransformer);
    }


    /**
     * detects all unicode emojis, and replaces them with the return value of transformer.transform()
     * [unicode emoji >> other]
     *
     * @param input
     * @param transformer   emoji transformer to apply to each emoji
     * @return              input string with all emojis transformed
     */
    public static String parseFromUnicode(String input, EmojiTransformer transformer) {
        int prev = 0;
        StringBuilder sb = new StringBuilder();
        List<UnicodeCandidate> replacements = getUnicodeCandidates(input);
        for (UnicodeCandidate candidate : replacements) {
            sb.append(input.substring(prev, candidate.getEmojiStartIndex()));

            sb.append(transformer.transform(candidate));
            prev = candidate.getFitzpatrickEndIndex();
        }

        return sb.append(input.substring(prev)).toString();
    }

    /**
     * extract emojis
     *
     * @param input
     * @return
     */
    public static List<String> extractEmojis(String input) {
        List<UnicodeCandidate> emojis = getUnicodeCandidates(input);
        List<String> result = new ArrayList<String>();
        for (UnicodeCandidate emoji : emojis) {
            result.add(emoji.getEmoji().getUnicode());
        }
        return result;
    }


    // ------------------------ to unicode ------------------------

    /**
     * replace aliases and html representations by their unicode(modifiers).
     *
     * [unicode emoji << alias | html hex ]
     *
     * @param input
     * @return
     */
    public static String parseToUnicode(String input) {
        // get all alias
        List<AliasCandidate> candidates = getAliasCandidates(input);

        // replace the aliases by their unicode
        String result = input;
        for (AliasCandidate candidate : candidates) {
            Emoji emoji = EmojiFactory.getForAlias(candidate.alias);
            if (emoji != null) {
                if (emoji.supportsFitzpatrick() || (!emoji.supportsFitzpatrick() && candidate.fitzpatrick == null)) {
                    String replacement = emoji.getUnicode();
                    if (candidate.fitzpatrick != null) {
                        replacement += candidate.fitzpatrick.unicode;
                    }
                    result = result.replace(":" + candidate.fullString + ":", replacement);
                }
            }
        }

        // replace the html by their unicode
        for (Emoji emoji : EmojiFactory.getAll()) {
            result = result.replace(emoji.getHtmlHexadecimal(), emoji.getUnicode());
            result = result.replace(emoji.getHtmlDecimal(), emoji.getUnicode());
        }

        return result;
    }


    // ------------------------ inner util ------------------------

    private static final Pattern ALIAS_CANDIDATE_PATTERN = Pattern.compile("(?<=:)\\+?(\\w|\\||\\-)+(?=:)");

    /**
     * find AliasCandidate for each emoji alias
     *
     * @param input
     * @return
     */
    protected static List<AliasCandidate> getAliasCandidates(String input) {
        List<AliasCandidate> candidates = new ArrayList<AliasCandidate>();

        Matcher matcher = ALIAS_CANDIDATE_PATTERN.matcher(input);
        matcher = matcher.useTransparentBounds(true);
        while (matcher.find()) {
            String match = matcher.group();
            if (!match.contains("|")) {
                candidates.add(new AliasCandidate(match, match, null));
            } else {
                String[] splitted = match.split("\\|");
                if (splitted.length == 2 || splitted.length > 2) {
                    candidates.add(new AliasCandidate(match, splitted[0], splitted[1]));
                } else {
                    candidates.add(new AliasCandidate(match, match, null));
                }
            }
        }
        return candidates;
    }

    /**
     * find UnicodeCandidate for each unicode emoji, include Fitzpatrick modifier if follwing emoji.
     *
     *      Finally, it contains start and end index of unicode emoji itself (WITHOUT Fitzpatrick modifier whether it is there or not!).
     *
     * @param input
     * @return
     */
    protected static List<UnicodeCandidate> getUnicodeCandidates(String input) {
        char[] inputCharArray = input.toCharArray();
        List<UnicodeCandidate> candidates = new ArrayList<UnicodeCandidate>();
        UnicodeCandidate next;
        for (int i = 0; (next = getNextUnicodeCandidate(inputCharArray, i)) != null; i = next.getFitzpatrickEndIndex()) {
            candidates.add(next);
        }

        return candidates;
    }

    /**
     * find next UnicodeCandidate after given starting index
     *
     * @param chars
     * @param start
     * @return
     */
    protected static UnicodeCandidate getNextUnicodeCandidate(char[] chars, int start) {
        for (int i = start; i < chars.length; i++) {
            int emojiEnd = getEmojiEndPos(chars, i);

            if (emojiEnd != -1) {
                Emoji emoji = EmojiFactory.getByUnicode(new String(chars, i, emojiEnd - i));
                String fitzpatrickString = (emojiEnd + 2 <= chars.length) ? new String(chars, emojiEnd, 2) : null;
                return new UnicodeCandidate(emoji, fitzpatrickString, i);
            }
        }

        return null;
    }

    /**
     * find end index of a unicode emoji, starting at index startPos, -1 if not found
     *
     * match the longest matching emoji, when emoji contain others
     *
     * @param text
     * @param startPos
     * @return
     */
    protected static int getEmojiEndPos(char[] text, int startPos) {
        int best = -1;
        for (int j = startPos + 1; j <= text.length; j++) {
            EmojiTrie.Matches status = EmojiFactory.isEmoji(Arrays.copyOfRange(text, startPos, j));

            if (status.exactMatch()) {
                best = j;
            } else if (status.impossibleMatch()) {
                return best;
            }
        }

        return best;
    }

    /**
     * unicode candidate
     */
    protected static class UnicodeCandidate {
        private final Emoji emoji;
        private final Fitzpatrick fitzpatrick;
        private final int startIndex;

        private UnicodeCandidate(Emoji emoji, String fitzpatrick, int startIndex) {
            this.emoji = emoji;
            this.fitzpatrick = Fitzpatrick.fitzpatrickFromUnicode(fitzpatrick);
            this.startIndex = startIndex;
        }

        public Emoji getEmoji() {
            return emoji;
        }

        public boolean hasFitzpatrick() {
            return getFitzpatrick() != null;
        }

        public Fitzpatrick getFitzpatrick() {
            return fitzpatrick;
        }

        public String getFitzpatrickType() {
            return hasFitzpatrick() ? fitzpatrick.name().toLowerCase() : "";
        }

        public String getFitzpatrickUnicode() {
            return hasFitzpatrick() ? fitzpatrick.unicode : "";
        }

        public int getEmojiStartIndex() {
            return startIndex;
        }

        public int getEmojiEndIndex() {
            return startIndex + emoji.getUnicode().length();
        }

        public int getFitzpatrickEndIndex() {
            return getEmojiEndIndex() + (fitzpatrick != null ? 2 : 0);
        }
    }

    /**
     * alias candidate
     */
    protected static class AliasCandidate {
        public final String fullString;
        public final String alias;
        public final Fitzpatrick fitzpatrick;

        private AliasCandidate(String fullString, String alias, String fitzpatrickString) {
            this.fullString = fullString;
            this.alias = alias;
            if (fitzpatrickString == null) {
                this.fitzpatrick = null;
            } else {
                this.fitzpatrick = Fitzpatrick.fitzpatrickFromType(fitzpatrickString);
            }
        }
    }

    /**
     * Fitzpatrick action
     */
    public enum FitzpatrickAction {

        /**
         * PARSE Fitzpatrick modifier
         */
        PARSE,

        /**
         * Removes Fitzpatrick modifier
         */
        REMOVE,

        /**
         * IGNORE Fitzpatrick modifier
         */
        IGNORE
    }

    public interface EmojiTransformer {
        String transform(UnicodeCandidate unicodeCandidate);
    }

}
