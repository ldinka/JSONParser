package ru.funbox.jsonparser;

import java.util.HashMap;
import java.util.Map;

public class JObject extends JElement {

    private Map<String, Object> result;
    private String key = "";

    public JObject(String json) {
        parsingState = JParsingState.BEFORE_KEY;
        this.result = new HashMap<String, Object>();
        this.json = json;
        try {
            if (isValidForBegin() && !isEmpty()) {
                parse();
            }
        } catch (JOSyntaxException e) {
            String beginJson = json.substring(0, index);
            String endJson = json.substring(index, json.length());
            if (beginJson.length() > JSON_PART_LENGTH) {
                beginJson = "..." + beginJson.substring(index - JSON_PART_LENGTH, index);
            }
            if (endJson.length() > JSON_PART_LENGTH) {
                endJson = endJson.substring(0, JSON_PART_LENGTH) + "...";
            }
            e = new JOSyntaxException(e.getCode(), index, beginJson, endJson);
            e.printStackTrace();
        }
    }

    private void parse() throws JOSyntaxException {
        for (index = 1; index < json.length(); index++) {
            ch = ((Character)json.charAt(index)).toString();
            switch (parsingState) {
                default:
                case BEFORE_KEY:
                    parseBeforeKeyChar();
                    break;
                case KEY:
                    parseKey();
                    break;
                case AFTER_KEY:
                    parseAfterKeyChar();
                    break;
                case AFTER_DELIMITER:
                    parseAfterDelimiter();
                    break;
                case VALUE:
                    parseValue();
                    break;
                case AFTER_VALUE:
                    parseAfterValue();
                    break;
            }
        }
    }

    private void parseBeforeKeyChar() throws JOSyntaxException {
        ch = ch.trim();
        if (ch.length() > 0) {
            if (ch.equals("\"")) {
                parsingState = JParsingState.KEY;
            } else {
                throw new JOSyntaxException();
            }
        }
    }

    private void parseKey() throws JOSyntaxException {
        if (ch.equals("\"")) {
            parsingState = JParsingState.AFTER_KEY;
            return;
        }
        if (ch.equals("\\")) {
            index++;
            ch += ((Character)json.charAt(index)).toString();
            if (ch.equals("\\u")) {
                ch += json.substring(index + 1, index + 4);
                index += 4;
            }
            Boolean isValid =
                    ch.equals("\\\"") ||
                            ch.equals("\\\\") ||
                            ch.equals("\\b") ||
                            ch.equals("\\f") ||
                            ch.equals("\\n") ||
                            ch.equals("\\r") ||
                            ch.equals("\\t") ||
                            ch.equals("\\f") ||
                            ch.matches("\\\\u[0-9a-fA-F]{4}")
                    ;
            if (!isValid) {
                throw new JOSyntaxException(JOExceptionCode.INVALID_STRING);
            }
        }
        key += ch;
    }

    private void parseAfterKeyChar() throws JOSyntaxException {
        ch = ch.trim();
        if (ch.length() > 0) {
            if (ch.equals(":")) {
                parsingState = JParsingState.AFTER_DELIMITER;
            } else {
                throw new JOSyntaxException();
            }
        }
    }

    private void parseAfterValue() throws JOSyntaxException {
        ch = ch.trim();
        if (ch.length() > 0) {
            if (ch.equals(",")) {
                parsingState = JParsingState.BEFORE_KEY;
            } else if (!ch.equals("}")) {
                throw new JOSyntaxException(JOExceptionCode.EXPECTED_COMMA);
            }
        }
    }

    protected void parseNumber() throws JOSyntaxException {
        super.parseNumber();
        if (ch.equals(",")) {
            parsingState = JParsingState.BEFORE_KEY;
        }
    }

    private Boolean isValidForBegin() throws JOSyntaxException {
        if (json != null) {
            json = json.trim();
            if (json.length() >= 2 && json.startsWith("{") && json.endsWith("}")) {
                return true;
            }
        }
        throw new JOSyntaxException();
    }

    public void input() {
        switch (valueType) {
            case STRING:
                result.put(key, value);
                break;
            case NUMBER:
                result.put(key, new JNumber(value));
                break;
            case OBJECT:
                result.put(key, new JObject(value).getAsMap());
                break;
            case ARRAY:
                result.put(key, new JArray(value).getAsList());
                break;
            case TRUE:
                result.put(key, true);
                break;
            case FALSE:
                result.put(key, false);
                break;
            case NULL:
                result.put(key, null);
                break;
        }
        key = ""; value = "";
        parsingState = JParsingState.AFTER_VALUE;
    }

    public Map<String, Object> getAsMap() {
        return result;
    }

}
