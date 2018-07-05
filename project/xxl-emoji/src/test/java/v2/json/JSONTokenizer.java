package v2.json;

import java.util.LinkedList;
import java.util.List;

/**
 * json tokenizer
 */
public class JSONTokenizer {

    private String json;        // json data
    private int index = 0;      // json index

    private List<Token> tokens = new LinkedList<Token>();   // analyse token result
    private int tokenIndex = 0;                         // analyse token index

    /**
     * construct json tokenizer, and analyse
     */
    public JSONTokenizer(String json) throws Exception {
        this.json = json;

        // analyse token
        Token token = null;
        while ((token = token()) != null) {
            tokens.add(token);
        }
    }

    /**
     * read next token
     */
    private Token token() throws Exception {

        Character c = read();

        if (c == null) {
            return null;
        }
        while (isBlankChar(c)) {
            c = read();
        }


        if (c == '{') {
            return new Token(TokenType.ObjectStart, "{");
        }
        if (c == '}') {
            return new Token(TokenType.ObjectEnd, "}");
        }
        if (c == '[') {
            return new Token(TokenType.ArrayStart, "[");
        }
        if (c == ']') {
            return new Token(TokenType.ArrayEnd, "]");
        }
        if (c == ',') {
            return new Token(TokenType.Comma, ",");
        }
        if (c == ':') {
            return new Token(TokenType.Colon, ":");
        }
        if (c == '"') {
            return new Token(TokenType.String, readString());
        }
        if (isNum(c)) {
            unread();
            return new Token(TokenType.Number, readNum());
        }
        if (isNull(c)) {
            return new Token(TokenType.Null, null);
        }
        if (isTrue(c)) {
            return new Token(TokenType.Boolean, "true");
        }
        if (isFalse(c)) {
            return new Token(TokenType.Boolean, "false");
        }
        throw new Exception("analyse token fail");
    }

    /**
     * read char, index +1
     */
    private Character read() {
        if (index < json.length()) {
            return json.charAt(index++);
        } else {
            return null;
        }
    }

    /**
     * read back, index -1
     */
    private void unread() {
        index--;
    }

    /**
     * is blank char
     */
    private boolean isBlankChar(char c) {
        if (c == '\t'
                || c == '\n'
                || c == '\r'
                || c == '\0'
                || c == ' ') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * is escape char
     */
    private boolean isEscapeChar(char c) {
        if (c == '\\') {
            return true;
        }
        return false;
    }

    /**
     * read string
     */
    private String readString() {
        char c = read();
        StringBuffer sb = new StringBuffer();
        while (c != '"') {
            sb.append(c);
            if (isEscapeChar(c)) {
                c = read();
                sb.append(c);
            }
            c = read();
        }
        return sb.toString();
    }

    /**
     * is num
     */
    private boolean isNum(char c) {
        if (c == '-' || ('0' <= c && c <= '9')) {
            return true;
        }
        return false;
    }

    /**
     * read num, TODO, not support all num type
     */
    private String readNum() {
        char c = read();
        StringBuffer sb = new StringBuffer();
        while (c != '"' && c != ':' && c != ',' && c != ']' && c != '}') {
            sb.append(c);
            c = read();
        }
        unread();
        return sb.toString();
    }

    /**
     * is true
     */
    private boolean isTrue(char c) throws Exception {
        if (c == 't') {
            c = read();
            if (c == 'r') {
                c = read();
                if (c == 'u') {
                    c = read();
                    if (c == 'e') {
                        return true;
                    } else {
                        throw new Exception("Invalid JSON input.");
                    }
                } else {
                    throw new Exception("Invalid JSON input.");
                }
            } else {
                throw new Exception("Invalid JSON input.");
            }
        } else {
            return false;
        }
    }

    /**
     * is false
     */
    private boolean isFalse(char c) throws Exception {
        if (c == 'f') {
            c = read();
            if (c == 'a') {
                c = read();
                if (c == 'l') {
                    c = read();
                    if (c == 's') {
                        c = read();
                        if (c == 'e') {
                            return true;
                        } else {
                            throw new Exception("Invalid JSON input.");
                        }
                    } else {
                        throw new Exception("Invalid JSON input.");
                    }
                } else {
                    throw new Exception("Invalid JSON input.");
                }
            } else {
                throw new Exception("Invalid JSON input.");
            }
        } else {
            return false;
        }
    }

    /**
     * is null
     */
    private boolean isNull(char c) throws Exception {
        if (c == 'n') {
            c = read();
            if (c == 'u') {
                c = read();
                if (c == 'l') {
                    c = read();
                    if (c == 'l') {
                        return true;
                    } else {
                        throw new Exception("Invalid JSON input.");
                    }
                } else {
                    throw new Exception("Invalid JSON input.");
                }
            } else {
                throw new Exception("Invalid JSON input.");
            }
        } else {
            return false;
        }
    }

    /**
     * get next token
     */
    public Token next() {
        if (tokenIndex + 1 < tokens.size())
            return tokens.get(++tokenIndex);
        return null;
    }

    /**
     * get cur token
     */
    public Token get() {
        if (tokenIndex < tokens.size())
            return tokens.get(tokenIndex);
        return null;
    }

    /**
     * token type
     */
    public enum TokenType {

        /**
         * object start
         */
        ObjectStart,

        /**
         * object end
         */
        ObjectEnd,

        /**
         * array start
         */
        ArrayStart,

        /**
         * array end
         */
        ArrayEnd,

        /**
         * string
         */
        String,

        /**
         * number
         */
        Number,

        /**
         * boolean
         */
        Boolean,

        /**
         * null
         */
        Null,

        /**
         * ,
         */
        Comma,

        /**
         * :
         */
        Colon

    }

    /**
     * token
     */
    public static class Token {

        public TokenType type;
        public String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }
    }

}