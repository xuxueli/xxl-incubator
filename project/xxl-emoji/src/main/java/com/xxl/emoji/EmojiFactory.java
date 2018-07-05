package com.xxl.emoji;

import com.xxl.emoji.core.EmojiTrie;
import com.xxl.emoji.exception.XxlEmojiException;
import com.xxl.emoji.loader.EmojiDataLoader;
import com.xxl.emoji.loader.impl.LocalEmojiDataLoader;
import com.xxl.emoji.core.Emoji;

import java.util.*;

/**
 * emoji factory
 */
public class EmojiFactory {

    private static List<Emoji> ALL_EMOJIS = null;                   // all emoji
    private static EmojiTrie EMOJI_TRIE = null;                     // tree trie

    private static Map<String, Emoji> EMOJIS_BY_ALIAS = null;       // alias-emoji, 1:1
    private static Map<String, Set<Emoji>> EMOJIS_BY_TAG = null;    // tag-emoji, 1:N

    private static EmojiDataLoader emojiLoader = new LocalEmojiDataLoader();

    public static void init(){
        List<Emoji> emojis = emojiLoader.loadEmojiData();
        if (emojis==null || emojis.size()==0) {
            throw new XxlEmojiException("emoji loader fail");
        }

        ALL_EMOJIS = emojis;
        EMOJI_TRIE = new EmojiTrie(ALL_EMOJIS);

        EMOJIS_BY_ALIAS = new HashMap<String, Emoji>();
        EMOJIS_BY_TAG = new HashMap<String, Set<Emoji>>();

        for (Emoji emoji : ALL_EMOJIS) {
            for (String alias : emoji.getAliases()) {
                EMOJIS_BY_ALIAS.put(alias, emoji);
            }
            for (String tag : emoji.getTags()) {
                if (EMOJIS_BY_TAG.get(tag) == null) {
                    EMOJIS_BY_TAG.put(tag, new HashSet<Emoji>());
                }
                EMOJIS_BY_TAG.get(tag).add(emoji);
            }
        }

    }

    static {
        init();
    }


    // ---------------------- util ----------------------

    public static Emoji getForAlias(String alias) {
        if (alias == null) {
            return null;
        }
        return EMOJIS_BY_ALIAS.get(trimAlias(alias));
    }

    private static String trimAlias(String alias) {
        String result = alias;
        if (result.startsWith(":")) {
            result = result.substring(1, result.length());
        }
        if (result.endsWith(":")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    public static Set<Emoji> getForTag(String tag) {
        if (tag == null) {
            return null;
        }
        return EMOJIS_BY_TAG.get(tag);
    }

    public static Collection<String> getAllTags() {
        return EMOJIS_BY_TAG.keySet();
    }

    public static Emoji getByUnicode(String unicode) {
        if (unicode == null) {
            return null;
        }
        return EMOJI_TRIE.getEmoji(unicode);
    }

    public static Collection<Emoji> getAll() {
        return ALL_EMOJIS;
    }

    public static boolean isEmoji(String string) {
        if (string == null) {
            return false;
        }

        EmojiParser.UnicodeCandidate unicodeCandidate = EmojiParser.getNextUnicodeCandidate(string.toCharArray(), 0);
        return unicodeCandidate != null &&
                unicodeCandidate.getEmojiStartIndex() == 0 &&
                unicodeCandidate.getFitzpatrickEndIndex() == string.length();
    }

    public static boolean isOnlyEmojis(String string) {
        return string != null && EmojiParser.removeAllEmojis(string).length()==0;
    }

    public static EmojiTrie.Matches isEmoji(char[] sequence) {
        return EMOJI_TRIE.isEmoji(sequence);
    }

}
