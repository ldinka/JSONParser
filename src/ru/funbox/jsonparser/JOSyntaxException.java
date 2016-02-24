package ru.funbox.jsonparser;

public class JOSyntaxException extends Exception {

    private JOExceptionCode code = JOExceptionCode.INCORRECT_SYNTAX_NEAR;

    public JOSyntaxException() {
        super();
    }

    public JOSyntaxException(String message) {
        super(message);
    }

    public JOSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public JOSyntaxException(Throwable cause) {
        super(cause);
    }

    public JOSyntaxException(JOExceptionCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public JOSyntaxException(JOExceptionCode code, int i, String beginJson, String endJson) {
        super(code.getMessage() + i + "\n" + beginJson + " <!> " + endJson);
        this.code = code;
    }

    public JOExceptionCode getCode() {
        return code;
    }

}
