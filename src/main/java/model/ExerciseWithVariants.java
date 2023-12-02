package model;

import forms.*;
import java.util.ArrayList;
import java.util.Collections;

public class ExerciseWithVariants extends Exercise /*implements ExerciseWithVariantsImpl*/ {
    private String firstVar;
    private String secondVar;
    private String thirdVar;
    private String fourthVar;
    private static final int maxCountExerc = 10;

    public ExerciseWithVariants() {
        super();
        this.firstVar = "";
        this.secondVar = "";
        this.thirdVar = "";
        this.fourthVar = "";
    }

    public ExerciseWithVariants(int type, String task, String wordTask, String rightAnswer, String userAnswer, Word word,
                                String firstVar, String secondVar, String thirdVar, String fourthVar) {
        super(type, task, wordTask, rightAnswer, userAnswer, word);
        this.firstVar = firstVar;
        this.secondVar = secondVar;
        this.thirdVar = thirdVar;
        this.fourthVar = fourthVar;
    }

    public String getFirstVar() {
        return firstVar;
    }

    public void setFirstVar(String firstVar) {
        this.firstVar = firstVar;
    }

    public String getSecondVar() {
        return secondVar;
    }

    public void setSecondVar(String secondVar) {
        this.secondVar = secondVar;
    }

    public String getThirdVar() {
        return thirdVar;
    }

    public void setThirdVar(String thirdVar) {
        this.thirdVar = thirdVar;
    }

    public String getFourthVar() {
        return fourthVar;
    }

    public void setFourthVar(String fourthVar) {
        this.fourthVar = fourthVar;
    }

    public static int getMaxCountExerc() {
        return maxCountExerc;
    }

    public static ExerciseWithVariants makeTrainExercise(int i, Word mainWord, ArrayList<Word> variantsWords) {
        /* создаем задания:
        1 - англ.слово, варианты - русские слова
        2 - русское слово, варианты - англ. слова
        3 - русское слово, варианты - англ. транскрипции
        4 - русское слово, варианты - англ. слова с перетаскиванием */
        ExerciseWithVariants exerc = new ExerciseWithVariants();
        if (i % 4 == 1) {
            exerc = new ExerciseWithVariants(1, WinResultsGeneral.toHTML("Выберите правильный перевод: ", "left"),
                    mainWord.getEngValue(), mainWord.getRuValue(), "", mainWord,
                    variantsWords.get(0).getRuValue(), variantsWords.get(1).getRuValue(),
                    variantsWords.get(2).getRuValue(), variantsWords.get(3).getRuValue());
        } else if (i % 4 == 2) {
            exerc = new ExerciseWithVariants(2, WinResultsGeneral.toHTML("Выберите правильный перевод: ", "left"),
                    mainWord.getRuValue(), mainWord.getEngValue(), "", mainWord,
                    variantsWords.get(0).getEngValue(), variantsWords.get(1).getEngValue(),
                    variantsWords.get(2).getEngValue(), variantsWords.get(3).getEngValue());
        } else if (i % 4 == 3) {
            exerc = new ExerciseWithVariants(3,
                    WinResultsGeneral.toHTML("Выберите правильную транскрипцию английского варианта слова: ", "left"),
                    mainWord.getRuValue(), mainWord.getRightTransc(), "", mainWord,
                    variantsWords.get(0).getRightTransc(), variantsWords.get(1).getRightTransc(),
                    variantsWords.get(2).getRightTransc(), variantsWords.get(3).getRightTransc());
        } else if (i % 4 == 0) {
            exerc = new ExerciseWithVariants(4,
                    WinResultsGeneral.toHTML("Выберите правильный перевод и перетащите в поле для ввода: ", "left"),
                    mainWord.getRuValue(), mainWord.getEngValue(), "", mainWord,
                    variantsWords.get(0).getEngValue(), variantsWords.get(1).getEngValue(),
                    variantsWords.get(2).getEngValue(), variantsWords.get(3).getEngValue());
        }
        return exerc;
    }

    public static Exercise makeExamExercise(int i, Word mainWord, ArrayList<Word> variantsWords) {
        /* создаем задания:
        1 - англ.слово, варианты - русские слова
        2 - русское слово, варианты - англ. слова
        3 - русское слово, варианты - англ. транскрипции
        4 - русское слово, варианты - англ. слова с перетаскиванием
        5 - русское слово, ответ - англ. слово
        6 - англ. слово, ответ - русское слово */
        Exercise exerc = new Exercise();
        if (i % 6 == 1) {
            exerc = new ExerciseWithVariants(1, WinResultsGeneral.toHTML("Выберите правильный перевод: ", "left"),
                    mainWord.getEngValue(), mainWord.getRuValue(), "", mainWord,
                    variantsWords.get(0).getRuValue(), variantsWords.get(1).getRuValue(),
                    variantsWords.get(2).getRuValue(), variantsWords.get(3).getRuValue());
        } else if (i % 6 == 2) {
            exerc = new ExerciseWithVariants(2, WinResultsGeneral.toHTML("Выберите правильный перевод: ", "left"),
                    mainWord.getRuValue(), mainWord.getEngValue(), "", mainWord,
                    variantsWords.get(0).getEngValue(), variantsWords.get(1).getEngValue(),
                    variantsWords.get(2).getEngValue(), variantsWords.get(3).getEngValue());
        } else if (i % 6 == 3) {
            exerc = new ExerciseWithVariants(3,
                    WinResultsGeneral.toHTML("Выберите правильную транскрипцию английского варианта слова: ", "left"),
                    mainWord.getRuValue(), mainWord.getRightTransc(), "", mainWord,
                    variantsWords.get(0).getRightTransc(), variantsWords.get(1).getRightTransc(),
                    variantsWords.get(2).getRightTransc(), variantsWords.get(3).getRightTransc());
        } else if (i % 6 == 4) {
            exerc = new ExerciseWithVariants(4,
                    WinResultsGeneral.toHTML("Выберите правильный перевод и перетащите в поле для ввода: ", "left"),
                    mainWord.getRuValue(), mainWord.getEngValue(), "", mainWord,
                    variantsWords.get(0).getEngValue(), variantsWords.get(1).getEngValue(),
                    variantsWords.get(2).getEngValue(), variantsWords.get(3).getEngValue());
        } else if (i % 6 == 5) {
            exerc = new Exercise(5, WinResultsGeneral.toHTML("Введите правильный перевод: ", "left"),
                    mainWord.getRuValue(), mainWord.getEngValue(), "", mainWord);
        } else if (i % 6 == 0) {
            exerc = new Exercise(6, WinResultsGeneral.toHTML("Введите правильный перевод: ", "left"),
                    mainWord.getEngValue(), mainWord.getRuValue(), "", mainWord);
        }
        return exerc;
    }

    public static ArrayList<Exercise> makeExercises() {
        DictionaryWords dict = new DictionaryWords();
        dict = WinChooseCategory.getTestDict();
        ArrayList<Exercise> listExercTrain = new ArrayList<>(); //список заданий
        ArrayList<Category> listTaskCateg = new ArrayList<>(dict.getListCategory()); //список выбранных категорий
        ArrayList<Word> listWordsTask = new ArrayList<>(); //список всех слов для заданий из выбранных категорий
        ArrayList<Word> listWordsVariants = new ArrayList<>(); //список всех слов для вариантов ответа из выбранных категорий
        boolean isTrain = WinChooseCategory.getIsTraining();
        int count = 0; //количество слов во всех категориях
        for (int i = 0; i < listTaskCateg.size(); i++) {
            count += listTaskCateg.get(i).getListWord().size();
            //заполнение списков слов по выбранным категориям
            for (int j = 0; j < listTaskCateg.get(i).getListWord().size(); j++) {
                listWordsTask.add(listTaskCateg.get(i).getListWord().get(j));
                listWordsVariants.add(listTaskCateg.get(i).getListWord().get(j));
            }
        }
        //если слов во всех категориях достаточно,
        //тогда тренировка состоит из maxCountExerc вопросов
        if (count >= maxCountExerc) {
            count = maxCountExerc;
        }
        if (count >= 4) { //достаточно для создания одного задания  любого вида
            for (int i = 1; i <= count; i++) {
                Collections.shuffle(listWordsTask); //перемешали список слов для заданий
                Word mainWord = new Word(); //слово-задание
                mainWord = listWordsTask.get(0);
                listWordsTask.remove(0);

                ArrayList<Word> variantsWords = new ArrayList<>(); //варианты ответа
                variantsWords.add(mainWord); //один из вариантов ответа - ответ на задание
                //получаем еще 3 варианта ответа
                for (int k = 0; k < 3; k++) {
                    Collections.shuffle(listWordsVariants); //перемешали список слов-вариантов ответа
                    Word word = new Word(); //слово-задание
                    word = listWordsVariants.get(0);
                    while (variantsWords.contains(word)) { //если такой вариант уже есть
                        Collections.shuffle(listWordsVariants);
                        word = listWordsVariants.get(0);
                    }
                    variantsWords.add(word);
                }
                Collections.shuffle(variantsWords); //перемешать варианты ответа
                if (isTrain) {
                    ExerciseWithVariants exerc = makeTrainExercise(i, mainWord, variantsWords);
                    listExercTrain.add(exerc);
                } else {
                    Exercise exerc = makeExamExercise(i, mainWord, variantsWords);
                    listExercTrain.add(exerc);
                }
            }
        }
        return listExercTrain;
    }
}
