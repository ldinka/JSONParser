package ru.funbox.jsonparser;

/**
 * @author Dina Efremova
 * Created on 18.02.16.
 */
public abstract class JElement {

    protected JParsingState parsingState;
    protected String json;
    protected Integer index;
    protected String ch;
    protected JValueType valueType = null;
    protected String value = "";

    protected final Integer JSON_PART_LENGTH = 100;

    protected Boolean isEmpty() {
        String body = json.substring(1, json.length() - 1);
        body = body.trim();
        return body.isEmpty();
    }

    protected void parseValue() throws JOSyntaxException {
        int j;
        String oChar;
        Boolean isInString = false;
        int level = 0;
        switch (valueType) {
            case STRING:
                if (ch.equals("\"")) {
                    input();
                    break;
                }
                value += checkStringChar();
                break;
            case NUMBER:
                parseNumber();
                break;
            case OBJECT:
                for (j = index; j < json.length() - 1; j++) {
                    oChar = ((Character) json.charAt(j)).toString();
                    value += oChar;
                    if (oChar.equals("}") && !isInString) {
                        if (level == 0) {
                            index = j;
                            input();
                            break;
                        } else {
                            level--;
                        }
                    }
                    if (oChar.equals("\"")) {
                        String prevChar = ((Character) json.charAt(j - 1)).toString();
                        if (!prevChar.equals("\\")) {
                            isInString = !isInString;
                        }
                    }
                    if (oChar.equals("{") && !isInString) {
                        level++;
                    }
                }
                break;
            case ARRAY:
                for (j = index; j < json.length() - 1; j++) {
                    oChar = ((Character) json.charAt(j)).toString();
                    value += oChar;
                    if (oChar.equals("]") && !isInString) {
                        if (level == 0) {
                            index = j;
                            input();
                            break;
                        } else {
                            level--;
                        }
                    }
                    if (oChar.equals("\"")) {
                        String prevChar = ((Character) json.charAt(j - 1)).toString();
                        if (!prevChar.equals("\\")) {
                            isInString = !isInString;
                        }
                    }
                    if (oChar.equals("[") && !isInString) {
                        level++;
                    }
                }
                break;
            case TRUE:
            case FALSE:
            case NULL:
                String possibleValue = valueType.name();
                value += json.substring(index, index + possibleValue.length() - 1);
                index += possibleValue.length() - 2;
                if (value.equalsIgnoreCase(possibleValue)) {
                    input();
                } else {
                    throw new JOSyntaxException();
                }
                break;
        }
    }

    protected void parseNumber() throws JOSyntaxException {
        if (isValidNumberChar(ch)) {
            value += ch;
        } else {
            if (isValidNumberValue(value)) {
                input();
            } else {
                throw new JOSyntaxException();
            }
        }
    }

    protected void parseAfterDelimiter() throws JOSyntaxException {
        ch = ch.trim();
        if (ch.length() > 0) {
            parsingState = JParsingState.VALUE;
            if (ch.equals("\"")) {
                valueType = JValueType.STRING;
            } else if (ch.equals("-") || ch.matches("\\d")) {
                valueType = JValueType.NUMBER;
            } else if (ch.equals("{")) {
                valueType = JValueType.OBJECT;
            } else if (ch.equals("[")) {
                valueType = JValueType.ARRAY;
            } else if (ch.equalsIgnoreCase("t")) {
                valueType = JValueType.TRUE;
            } else if (ch.equalsIgnoreCase("f")) {
                valueType = JValueType.FALSE;
            } else if (ch.equalsIgnoreCase("n")) {
                valueType = JValueType.NULL;
            } else {
                throw new JOSyntaxException();
            }
            if (valueType != JValueType.STRING) {
                value += ch;
            }
        }
    }

    private String checkStringChar() throws JOSyntaxException {
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
        return ch;
    }

    private Boolean isValidNumberChar(String ch) {
        return ch.matches("[0-9eE+\\-\\.]");
    }

    private Boolean isValidNumberValue(String value) {
        Boolean isValid;
        isValid =
                value.matches("\\-?0(\\.\\d+)?([eE][+\\-]?\\d+)?") ||
                        value.matches("\\-?[1-9]+\\d*(\\.\\d+)?([eE][+\\-]?\\d+)?")
        ;
        return isValid;
    }

    protected abstract void input();
}
