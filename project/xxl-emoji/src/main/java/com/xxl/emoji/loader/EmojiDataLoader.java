package com.xxl.emoji.loader;

import com.xxl.emoji.model.Emoji;

import java.util.List;

/**
 * emoji loader
 */
public abstract class EmojiDataLoader {

    public abstract List<Emoji> loadEmojiData();

}
