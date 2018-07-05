package com.xxl.emoji.loader.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxl.emoji.model.Emoji;
import com.xxl.emoji.loader.EmojiDataLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * emoji data loader
 */
public class LocalEmojiDataLoader extends EmojiDataLoader {

    private static final String PATH = "/xxl-emoji.json";

    public List<Emoji> loadEmojiData()  {

        InputStream stream = null;
        try {
            // json
            stream = LocalEmojiDataLoader.class.getResourceAsStream(PATH);
            String emojiJson = inputStreamToString(stream);

            // emoji data
            ObjectMapper objectMapper = new ObjectMapper();
            List<Object> emojiArr = objectMapper.readValue(emojiJson, List.class);
            if (emojiArr==null || emojiArr.size()==0) {
                return null;
            }

            // parse dto
            List<Emoji> emojis = new ArrayList<Emoji>();
            for (Object emojiItem: emojiArr) {
                if (emojiItem instanceof Map){

                    Map<String, Object> emojiItemMap = (Map<String, Object>) emojiItem;

                    if (!emojiItemMap.containsKey("emoji")) {
                        continue;
                    }

                    String emojiChar = String.valueOf(emojiItemMap.get("emojiChar"));
                    String emoji = String.valueOf(emojiItemMap.get("emoji"));
                    String description = String.valueOf(emojiItemMap.get("description"));

                    List<String> aliases = null;
                    if (emojiItemMap.containsKey("aliases") && emojiItemMap.get("aliases") instanceof List) {
                        aliases = (List<String>) emojiItemMap.get("aliases");
                    }
                    List<String> tags = null;
                    if (emojiItemMap.containsKey("tags") && emojiItemMap.get("tags") instanceof List) {
                        tags = (List<String>) emojiItemMap.get("tags");
                    }

                    boolean supports_fitzpatrick = false;
                    if (emojiItemMap.containsKey("supports_fitzpatrick") && emojiItemMap.get("supports_fitzpatrick") instanceof Boolean) {
                        supports_fitzpatrick = Boolean.valueOf(String.valueOf(emojiItemMap.get("supports_fitzpatrick")));
                    }

                    Emoji emojiObj = new Emoji(description, supports_fitzpatrick, aliases, tags, emoji);
                    emojis.add(emojiObj);

                }
            }

            return emojis;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream!=null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private static String inputStreamToString(InputStream stream) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader isr = new InputStreamReader(stream, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String read;
        while ((read = br.readLine()) != null) {
            sb.append(read);
        }
        br.close();
        return sb.toString();
    }

}
