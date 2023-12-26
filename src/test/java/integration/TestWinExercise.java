package integration;

import forms.*;
import model.*;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тест для тестирования/экзамена:
 * проверка корректности отображения задания, вариантов ответа,
 * поля ответа; а также подсчета правильных и неправильных ответов при нажатии на кнопку «Далее».
 */
public class TestWinExercise {
    @Test
    //тест для окна с вариантами ответа и для окна с открытым ответом
    public void getExerciseFillAndAnswerTest() throws MyFileException {
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
        WinChooseCategory.setIsTraining(false);
        //создадим для теста список с заданиями по этим категориям
        ArrayList<Exercise> listExerc = ExerciseWithVariants.makeExercises();
        WinExercise.setListExercises(listExerc);
        ExerciseWithVariants exercWithVar = null;
        Exercise exercWithOpenAnswer = null;
        for (int i = 0; i < listExerc.size(); i++) {
            Exercise exerc = listExerc.get(i);
            if (exerc instanceof ExerciseWithVariants) {
                exercWithVar = (ExerciseWithVariants) exerc;
                i = listExerc.size();
            }
        }
        for (int i = 0; i < listExerc.size(); i++) {
            Exercise exerc = listExerc.get(i);
            if (!(exerc instanceof ExerciseWithVariants)) {
                exercWithOpenAnswer = exerc;
                i = listExerc.size();
            }
        }
        //передаем задание с неверным ответом в окно задания с вариантами ответа
        exercWithVar.setUserAnswer(exercWithVar.getRightAnswer() + "test");
        WinExerciseWithVariants winExercWithVar = new WinExerciseWithVariants(exercWithVar);
        assertEquals(exercWithVar.getTask(), winExercWithVar.getLabelTask().getText());
        assertEquals(exercWithVar.getWordTask(), winExercWithVar.getLabelWordTask().getText());
        assertEquals(exercWithVar.getFirstVar(), winExercWithVar.getRadioButton1().getText());
        assertEquals(exercWithVar.getSecondVar(), winExercWithVar.getRadioButton2().getText());
        assertEquals(exercWithVar.getThirdVar(), winExercWithVar.getRadioButton3().getText());
        assertEquals(exercWithVar.getFourthVar(), winExercWithVar.getRadioButton4().getText());
        JButton btnWithVar = winExercWithVar.getButtonOk();
        btnWithVar.doClick();
        assertEquals("Неверный ответ!", winExercWithVar.getMessage());

        //передаем задание с неверным ответом в окно задания с открытым ответом
        WinExerciseWithOpenAswer winExercWithOpenAswer = new WinExerciseWithOpenAswer(exercWithOpenAnswer);
        assertEquals(exercWithOpenAnswer.getTask(), winExercWithOpenAswer.getLabelTask().getText());
        assertEquals(exercWithOpenAnswer.getWordTask(), winExercWithOpenAswer.getLabelWordTask().getText());
        JTextField txtFld = winExercWithOpenAswer.getTxtFldUsAnswer();
        txtFld.setText(exercWithOpenAnswer.getRightAnswer() + "test");
        winExercWithOpenAswer.setTxtFldUsAnswer(txtFld);
        JButton btnWithOpen = winExercWithOpenAswer.getButtonOk();
        btnWithOpen.doClick();
        assertEquals("Неверный ответ!", winExercWithOpenAswer.getMessage());
    }
}
