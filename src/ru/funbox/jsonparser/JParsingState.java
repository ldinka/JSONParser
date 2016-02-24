package ru.funbox.jsonparser;

public enum JParsingState {
    BEFORE_KEY,
    KEY,
    AFTER_KEY,
    AFTER_DELIMITER,
    VALUE,
    AFTER_VALUE
}
