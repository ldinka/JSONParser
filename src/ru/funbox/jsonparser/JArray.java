package ru.funbox.jsonparser;

import java.util.ArrayList;
import java.util.List;

public class JArray extends JElement {

    private List<Object> result;

    public JArray(String json) {
        parsingState = JParsingState.AFTER_DELIMITER;
        this.result = new ArrayList<Object>();
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

    private Boolean isValidForBegin() throws JOSyntaxException {
        if (json != null) {
            json = json.trim();
            if (json.length() >= 2 && json.startsWith("[") && json.endsWith("]")) {
                return true;
            }
        }
        throw new JOSyntaxException();
    }

    private void parse() throws JOSyntaxException {
        for (index = 1; index < json.length(); index++) {
            ch = ((Character)json.charAt(index)).toString();
            switch (parsingState) {
                default:
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

    protected void parseAfterValue() throws JOSyntaxException {
        ch = ch.trim();
        if (ch.length() > 0) {
            if (ch.equals(",")) {
                parsingState = JParsingState.AFTER_DELIMITER;
            } else if (!ch.equals("]")) {
                throw new JOSyntaxException();
            }
        }
    }

    protected void parseNumber() throws JOSyntaxException {
        super.parseNumber();
        if (ch.equals(",")) {
            parsingState = JParsingState.AFTER_DELIMITER;
        }
    }

    public void input() {
        switch (valueType) {
            case STRING:
                result.add(value);
                break;
            case NUMBER:
                result.add(new JNumber(value));
                break;
            case OBJECT:
                result.add(new JObject(value).getAsMap());
                break;
            case ARRAY:
                result.add(new JArray(value).getAsList());
                break;
            case TRUE:
                result.add(true);
                break;
            case FALSE:
                result.add(false);
                break;
            case NULL:
                result.add(null);
                break;
        }
        value = "";
        parsingState = JParsingState.AFTER_VALUE;
    }

    public List<Object> getAsList() {
        return result;
    }

    @Override
    public String toString() {
        return result.toString();
    }
}
