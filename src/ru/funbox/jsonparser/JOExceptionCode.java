package ru.funbox.jsonparser;

public enum JOExceptionCode {

    INCORRECT_SYNTAX_NEAR ("Incorrect syntax near char at "),
    EXPECTED_COMMA ("Expected comma at "),
    EXPECTED_QUOTE ("Expected quote at "),
    EXPECTED_BOOLEAN ("Expected boolean at "),
    EXPECTED_NULL ("Expected null at "),
    INVALID_NUMBER ("Invalid number near char at "),
    INVALID_STRING ("Invalid string near char at "),
    INVALID_KEY ("Invalid key near char at "),
    ;

    private String message;

    JOExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
