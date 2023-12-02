package integration;

import forms.WinChooseCategory;
import model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *  тест для проверки получения пользователей из файла accounts.txt,
 *  сохранения результатов в файл statistics.txt
 *  получения результатов из файла statistics.txt
 */
public class TestUserAndResults {
    /**
     * тест на чтение аккаунтов и на создание списка пользователей
     * <p>
     * для теста в файле accounts.txt должны быть записи admin и matvey12 в виде:
     * Запись в файле админа (<username>~<password>~<accessLevel>):
     * admin~admin255~1
     * Запись в файле пользователя (<username>~<password>~<accessLevel>\n<name>~<group>~<course>):
     * matvey12~fBh23k&ju~0
     * Петров Матвей Михайлович~ИПБ-21~1
     */
    @Test
    public void getListUsersTest() throws MyFileException {
        List<User> listUsers = User.getListUsers();
        assertNotNull(listUsers);
        assertNotEquals(0, listUsers.size());

        User admin = listUsers.get(0); //admin
        assertEquals("admin", admin.getUsername());
        assertEquals("admin255", admin.getPassword());
        assertEquals(1, admin.getAcessLevel()); //уровень доступа

        User user2 = (User) listUsers.get(1);
        assertEquals("matvey12", user2.getUsername());
        assertEquals("fBh23k&ju", user2.getPassword());
        assertEquals(0, user2.getAcessLevel()); //уровень доступа
    }

    //тест для сохранения результатов в файл statistics.txt
    @Test
    public void saveUsersResultsIntoFileTest() throws MyFileException {
        //считаем изначальное содержимое файла statistics.txt
        ArrayList<Results> listResultsBefore = User.getListUsersResults();
        assertNotNull(listResultsBefore);
        assertNotEquals(0, listResultsBefore.size());

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
        //запишем в качестве ответов пользователя все правильные ответы
        for (int i = 0; i < listExerc.size(); i++) {
            Exercise curExerc = listExerc.get(i);
            curExerc.setUserAnswer(curExerc.getRightAnswer());
        }

        //сохраним результаты
        us.saveUsersResultsIntoFile(listExerc);
        //считаем содержимое файла statistics.txt после сохранения новой записи
        ArrayList<Results> listResultsAfter = User.getListUsersResults();
        Results addedRes = listResultsAfter.get(listResultsAfter.size() - 1);
        assertNotNull(listResultsAfter);
        assertNotEquals(0, listResultsAfter.size());
        assertNotEquals(listResultsBefore.size(), listResultsAfter); //размер списков должен отличаться
        assertEquals(us.getUsername(), addedRes.getUser().getUsername());
        assertEquals("природа", addedRes.getNamesCateg().get(0));
        assertEquals("животные", addedRes.getNamesCateg().get(1));
        assertEquals(5, addedRes.getMark());
    }

    /**
     * тест на чтение статистики
     * <p>
     * для теста в файле statistics.txt должна быть запись
     * petr_752~профессии~фрукты и ягоды~украшения~4
     * (<username>~<namesCateg1>~...~<namesCategN>~<mark>)
     */
    @Test
    public void getListUsersResultsTest() throws MyFileException {
        ArrayList<Results> listUsersResults = User.getListUsersResults();
        assertNotNull(listUsersResults);
        assertNotEquals(0, listUsersResults.size());

        Results res1 = listUsersResults.get(0);
        ArrayList<String> res1Categ = res1.getNamesCateg();
        assertEquals("petr_752", res1.getUser().getUsername());
        assertEquals("профессии", res1Categ.get(0));
        assertEquals("фрукты и ягоды", res1Categ.get(1));
        assertEquals("украшения", res1Categ.get(2));
        assertEquals(4, res1.getMark());
    }
}
