package unit;

import forms.WinChooseCategory;
import model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 *  тест для проверки логина и пароля на корректность и для
 *  подсчета количества верных ответов и оценки
 */
public class TestUserAndResults {
    /**
     * тест для вычисления оценки (процентов верных ответов в выполненных заданиях)
     */
    @Test
    public void calcMarkTest() throws MyFileException {
        User us = new User();
        WinChooseCategory winChooseCategory = new WinChooseCategory();
        double mark = ExerciseWithVariants.getMaxCountExerc();
        //создадим для теста список с заданиями
        ArrayList<Exercise> listExerc = ExerciseWithVariants.makeExercises();
        //запишем в качестве ответов пользователя все правильные ответы
        for (int i = 0; i < listExerc.size(); i++) {
            Exercise curExerc = listExerc.get(i);
            curExerc.setUserAnswer(curExerc.getRightAnswer());
        }
        assertEquals(listExerc.size() / mark, us.calcMark(listExerc)); //процент эл-в в списке равно проценту верных ответов
        //изменим каждый четный ответ пользователя на неверный и проверим процент верных ответов
        int kRight = listExerc.size();
        for (int i = 0; i < listExerc.size(); i++) {
            if (i % 2 == 0) {
                String testAnswer = "testAnswer" + i;
                listExerc.get(0).setUserAnswer(testAnswer);
                kRight--;
            }
        }
        assertEquals(kRight / mark, us.calcMark(listExerc));
    }

    /***
     *  тест для перевода баллов в оценку
     *  [0..0.61) - 2; [0.61..0.76) - 3; [0.76..0.91) - 4; [0.91..1] - 5
     */
    @Test
    public void convertBallsToMarkTest() {
        User us = new User();
        assertEquals(2, us.convertBallsToMark(0.00));
        assertEquals(2, us.convertBallsToMark(0.31));
        assertEquals(2, us.convertBallsToMark(0.60));
        assertEquals(3, us.convertBallsToMark(0.61));
        assertEquals(3, us.convertBallsToMark(0.70));
        assertEquals(3, us.convertBallsToMark(0.75));
        assertEquals(4, us.convertBallsToMark(0.76));
        assertEquals(4, us.convertBallsToMark(0.81));
        assertEquals(4, us.convertBallsToMark(0.90));
        assertEquals(5, us.convertBallsToMark(0.91));
        assertEquals(5, us.convertBallsToMark(0.95));
        assertEquals(5, us.convertBallsToMark(1.00));
    }

    /**
     * тест для вычисления количества верных ответов в выполненных заданиях
     */
    @Test
    public void calcMark2Test() throws MyFileException {
        User us = new User();
        WinChooseCategory winChooseCategory = new WinChooseCategory();
        //создадим для теста список с заданиями
        ArrayList<Exercise> listExerc = ExerciseWithVariants.makeExercises();
        //запишем в качестве ответов пользователя все правильные ответы
        for (int i = 0; i < listExerc.size(); i++) {
            Exercise curExerc = listExerc.get(i);
            curExerc.setUserAnswer(curExerc.getRightAnswer());
        }
        assertEquals(listExerc.size(), us.calcMark(listExerc, 0)); //кол-во эл-в в списке равно кол-ву верных ответов
        //изменим каждый четный ответ пользователя на неверный и проверим количество верных ответов
        int kRight = listExerc.size();
        for (int i = 0; i < listExerc.size(); i++) {
            if (i % 2 == 0) {
                String testAnswer = "testAnswer" + i;
                listExerc.get(0).setUserAnswer(testAnswer);
                kRight--;
            }
        }
        assertEquals(kRight, us.calcMark(listExerc, 0));
    }

    //тест для поиска пользователя в системе
    @Test
    public void seekUserInSystemTest() {
        ArrayList<User> listUsers = new ArrayList<>();
        User user1 = new User("matvey12", "fBh23k&ju", 0);
        User user2 = new User("vasiliev8", "1q2J4+5d", 0);
        User user3 = new User("sasha_22", "14lJ=4ee", 0);
        listUsers.add(user1);
        listUsers.add(user2);
        listUsers.add(user3);
        User seekUser = User.seekUserInSystem("vasiliev8", listUsers);
        assertNotNull(seekUser);
        assertEquals(user2, seekUser);
    }

    /***
     *  тест для проверки логина на корректность:
     *  логин должен содержать хотя бы одну цифру, хотя бы одну строчную лат.букву,
     *  не содержать пробелов, быть не менее 8 и не более 10 символов
     */
    @Test
    public void isUsernameValidTest() {
        assertTrue(User.isUsernameValid("matvey12"));
        assertFalse(User.isUsernameValid("matveypetr"));
        assertFalse(User.isUsernameValid("12345678"));
        assertFalse(User.isUsernameValid("matvey 12"));
        assertFalse(User.isUsernameValid("m12"));
        assertFalse(User.isUsernameValid("matvey1234567"));
    }

    /**
     * тест для проверки пароля на корректность:
     * пароль должен содержать хотя бы одну цифру, хотя бы одну лат.букву (прописную и строчную),
     * хотя бы один специальный символ, не содержать пробелов, быть не менее 8 и не более 10 символов
     */
    @Test
    public void isPasswordValidTest() {
        assertTrue(User.isPasswordValid("fBh23k&ju"));
        assertFalse(User.isPasswordValid("fBhqwk&ju"));
        assertFalse(User.isPasswordValid("1234567&q"));
        assertFalse(User.isPasswordValid("1234567&Q"));
        assertFalse(User.isPasswordValid("1234567qQ"));
        assertFalse(User.isPasswordValid("123456 &qQ"));
        assertFalse(User.isPasswordValid("fB&ju"));
        assertFalse(User.isPasswordValid("fBhqwk&jufBhqwk&ju"));
    }
}
