package forms;

import model.*;
import java.util.ArrayList;

/* Класс для вызова разных окон заданий */
public class WinExercise {
    private static ArrayList<Exercise> listExercises;
    private static int iCurExerc;

    public WinExercise() {
    }

    public static ArrayList<Exercise> getListExercises() {
        return WinExercise.listExercises;
    }

    public static int getICurExerc() {
        return WinExercise.iCurExerc;
    }

    public static void setICurExerc(int iCurExerc) {
        WinExercise.iCurExerc = iCurExerc;
    }

    public boolean init() {
        listExercises = ExerciseWithVariants.makeExercises();
        iCurExerc = 0;
        return listExercises.size() > 0;
    }

    public void run() throws MyFileException {
        int ind = WinExercise.getICurExerc();
        if (ind == WinExercise.getListExercises().size()) { //после опроса
            boolean isTrain = WinChooseCategory.getIsTraining();
            if (isTrain) { //если тренировка, выводятся список выученных слов и слов, добавленных в словарь
                ArrayList<Word> newWords = new ArrayList<>();
                ArrayList<Word> learnedWords = new ArrayList<>();
                //в тренировке отслеживаем ошибки и верные ответы
                for (int i = 0; i < WinExercise.getListExercises().size(); i++) {
                    Exercise exerc = WinExercise.getListExercises().get(i);
                    DictionaryWords dict = User.getCurUser().getDictionary();
                    Word word = dict.getWordInDict(exerc.getWord());
                    if (!exerc.getUserAnswer().equals(exerc.getRightAnswer())) { //если ошибка
                        //если слово есть в словаре пользователя, то количество верных попыток уменьшаем на 1
                        if (word != null) {
                            if (word.getkRightAnsw() > 0) {
                                word.setkRightAnsw(word.getkRightAnsw() - 1);
                                DictionaryWords.apdateFileDictionary(dict, User.getCurUser().getUsername() + ".txt");
                            }
                        } else { //иначе добавляем в словарь пользователя
                            newWords.add(exerc.getWord());
                            dict.addWordIntoUserDictionary(exerc.getWord());
                        }
                    } else { //если верный ответ
                        if (word != null) {
                            //если количество верных попыток меньше 3х, увеличиваем на 1
                            if (word.getkRightAnsw() < 3) {
                                word.setkRightAnsw(word.getkRightAnsw() + 1);
                                DictionaryWords.apdateFileDictionary(dict, User.getCurUser().getUsername() + ".txt");
                            }
                            if (word.getkRightAnsw() == 3) { //иначе удаляем слово из словаря пользователя, т.к. оно выучено
                                learnedWords.add(word);
                                dict.delWordFromUserDictionary(word);
                            }
                        }
                    }
                }
                WinResultsUserTrain.setFrame(new WinResultsUserTrain(newWords, learnedWords));

            } else { //если экзамен, результаты сохраняются и выводится таблица результатов
                //в экзамене отслеживаем только ошибки
                for (int i = 0; i < WinExercise.getListExercises().size(); i++) {
                    Exercise exerc = WinExercise.getListExercises().get(i);
                    DictionaryWords dict = User.getCurUser().getDictionary();
                    Word word = dict.getWordInDict(exerc.getWord());
                    if (!exerc.getUserAnswer().equals(exerc.getRightAnswer())) { //если ошибка
                        if (word == null) { //если слово отсутствует в словаре пользователя, добавляем
                            dict.addWordIntoUserDictionary(exerc.getWord());
                        }
                    }
                }
                User.getCurUser().saveUsersResultsIntoFile(WinExercise.getListExercises());
                WinResultsUserExam.setFrame(new WinResultsUserExam());
            }
        }
        //формируем опрос: в зависимости от типа задания выводим нужный фрейм
        if (ind < WinExercise.getListExercises().size()) {
            Exercise nextExerc = WinExercise.getListExercises().get(ind);
            ExerciseWithVariants nextExercWithVar = null;
            if (nextExerc instanceof ExerciseWithVariants) {
                nextExercWithVar = (ExerciseWithVariants) nextExerc;
                if ((nextExercWithVar.getType() >= 1) && (nextExercWithVar.getType() <= 3)) {
                    WinExerciseWithVariants.setFrame(new WinExerciseWithVariants(nextExercWithVar));
                } else if (nextExercWithVar.getType() == 4) {
                    WinExerciseWithVariantsDnD.setFrame(new WinExerciseWithVariantsDnD(nextExercWithVar));
                }
            } else {
                WinExerciseWithOpenAswer.setFrame(new WinExerciseWithOpenAswer(nextExerc));
            }
            ind++;
            WinExercise.setICurExerc(ind);
        }
    }
}
