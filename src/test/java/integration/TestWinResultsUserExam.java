package integration;

import forms.*;
import model.*;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тест результатов экзамена:
 * проверка корректности отображения пройденных заданий,
 * вариантов ответа;
 * ответов, введенных пользователем;
 * */
public class TestWinResultsUserExam {
    @Test
    public void getCellsResultsUserExamTest() throws MyFileException {
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
        //создадим для теста список с заданиями по этим категориям
        ArrayList<Exercise> listExerc = ExerciseWithVariants.makeExercises();
        //запишем в качестве ответов пользователя все правильные ответы, кроме ответа на первый вопрос
        for (int i = 0; i < listExerc.size(); i++) {
            Exercise curExerc = listExerc.get(i);
            if (i == 0) { //неверный ответ на первый вопрос
                curExerc.setUserAnswer(curExerc.getRightAnswer() + "test");
            } else { //верный ответ на любой другой вопрос
                curExerc.setUserAnswer(curExerc.getRightAnswer());
            }
        }
        WinExercise.setListExercises(listExerc);
        //получаем таблицу из WinResultsUserExam
        WinResultsUserExam winResultsUserExam = new WinResultsUserExam();
        WinResultsUserExam.setFrame(winResultsUserExam);
        JTable table = winResultsUserExam.getTable();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        //считываем данные из первой строки таблицы из окна Результаты экзамена
        //условие
        String task = model.getValueAt(0, 1).toString();
        task.trim().toLowerCase().replaceAll("\\s+", " ");
        //задание
        String wordTask = WinDictionaryGeneral.fromHTML(model.getValueAt(0, 2).toString());
        wordTask.trim().toLowerCase().replaceAll("\\s+", " ");
        //правильный ответ
        String rightAnswer = WinDictionaryGeneral.fromHTML(model.getValueAt(0, 3).toString());
        rightAnswer.trim().toLowerCase().replaceAll("\\s+", " ");
        //ответ пользователя (Ваш ответ)
        String userAnswer = WinDictionaryGeneral.fromHTML(model.getValueAt(0, 4).toString());
        userAnswer.trim().toLowerCase().replaceAll("\\s+", " ");
        //сравниваем первое задание из списка заданий экзамена и первую строку таблицы
        assertEquals(listExerc.get(0).getTask(), task);
        assertEquals(listExerc.get(0).getWordTask(), wordTask);
        assertEquals(listExerc.get(0).getRightAnswer(), rightAnswer);
        assertEquals(listExerc.get(0).getUserAnswer(), userAnswer);
    }
}
