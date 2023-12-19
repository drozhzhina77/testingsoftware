package integration;

import forms.*;
import model.*;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тест результатов тестирования:
 * проверка корректности отображения списка выученных слов и
 * списка добавленных слов.
 */
public class TestWinResultsUserTrain {
    @Test
    public void getCellsResultsUserTrainTest() throws MyFileException {
        //создадим пользователя и зададим 2 категории: природа и животные
        User us = new User("petr_752", "pwAsfg+64", 0);
        User.setCurUser(us);
        WinChooseCategory winChooseCategory = new WinChooseCategory();
        DictionaryWords dict = new DictionaryWords();
        ArrayList<Category> genListCateg = winChooseCategory.getGeneralListCateg();
        ArrayList<Category> genSublistCateg = new ArrayList<>();
        genSublistCateg.add(genListCateg.get(0));
        genSublistCateg.add(genListCateg.get(1));
        dict.setListCategory(genSublistCateg);
        WinChooseCategory.setTestDict(dict);
        ArrayList<Word> newWords = new ArrayList<>();
        ArrayList<Word> learnedWords = new ArrayList<>();
        //создадим для теста список с заданиями по этим категориям
        ArrayList<Exercise> listExerc = ExerciseWithVariants.makeExercises();
        WinExercise.setListExercises(listExerc);
        //добавляем 2 слова в список новых слов и 1 в список выученных
        newWords.add(listExerc.get(0).getWord());
        newWords.add(listExerc.get(1).getWord());
        learnedWords.add(listExerc.get(2).getWord());

        WinResultsUserTrain winResultsUserTrain = new WinResultsUserTrain(newWords, learnedWords);
        //проверка списка новых слов
        JList<String> newList = winResultsUserTrain.getNewList();
        String newWord1 = newList.getModel().getElementAt(0).trim().toLowerCase().replaceAll("\\s+", " ");
        String newWord2 = newList.getModel().getElementAt(1).trim().toLowerCase().replaceAll("\\s+", " ");
        assertEquals(newWords.get(0).getEngValue(), newWord1);
        assertEquals(newWords.get(1).getEngValue(), newWord2);
        //проверка списка выученных слов
        JList<String> learnedList = winResultsUserTrain.getLearnedList();
        String learnedWord1 = learnedList.getModel().getElementAt(0).trim().toLowerCase().replaceAll("\\s+", " ");
        assertEquals(learnedWords.get(0).getEngValue(), learnedWord1);
    }
}
