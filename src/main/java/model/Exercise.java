package model;

public class Exercise {
    private int type; //тип задания 1-3 - с вар, 4 - DragNDrop, 5-6 с вводом ответа
    private String task; //формулировка задания
    private String wordTask; //слово для задания
    private String rightAnswer; //правильный ответ
    private String userAnswer; //ответ пользователя
    private Word word;

    public Exercise() {
        this.type = 0;
        this.task = "";
        this.wordTask = "";
        this.rightAnswer = "";
        this.userAnswer = "";
        this.word = new Word();
    }

    public Exercise(int type, String task, String wordTask, String rightAnswer, String userAnswer, Word word) {
        this.type = type;
        this.task = task;
        this.wordTask = wordTask;
        this.rightAnswer = rightAnswer;
        this.userAnswer = userAnswer;
        this.word = word;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getWordTask() {
        return wordTask;
    }

    public void setWordTask(String wordTask) {
        this.wordTask = wordTask;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }
}
