package model;

public class MyFileException extends Exception {
    @Override
    public String getMessage() {
        return "Файл не найден!";
    }
}
