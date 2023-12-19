package integration;

import forms.*;
import model.*;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тест результатов экзамена по всем студентам:
 * проверка корректного отображения обучающихся, проходивших экзамен,
 * их категорий для опроса, оценок
 */
public class TestWinResultsGeneral {
    @Test
    public void getCellsResultsGeneralTest() throws MyFileException {
        WinResultsGeneral winResultsGeneral = new WinResultsGeneral();
        WinResultsGeneral.setFrame(winResultsGeneral);
        JTable table = winResultsGeneral.getTable();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        //считываем данные из первой строки таблицы из окна Результаты
        //фио пользователя
        String name = WinDictionaryGeneral.fromHTML(model.getValueAt(0, 1).toString());
        name.trim().toLowerCase().replaceAll("\\s+", " ");
        //логин
        String username = WinDictionaryGeneral.fromHTML(model.getValueAt(0, 2).toString());
        username.trim().toLowerCase().replaceAll("\\s+", " ");
        //группа
        String group = WinDictionaryGeneral.fromHTML(model.getValueAt(0, 3).toString());
        group.trim().toLowerCase().replaceAll("\\s+", " ");
        //курс
        String course = WinDictionaryGeneral.fromHTML(model.getValueAt(0, 4).toString());
        course.trim().toLowerCase().replaceAll("\\s+", " ");
        //категории для опроса
        String categ = model.getValueAt(0, 5).toString();
        String[] categories = categ.split("<br>");
        for (int i = 0; i < categories.length; i++) {
            categories[i] = Jsoup.parse(categories[i]).text();
            categories[i].trim().toLowerCase().replaceAll("\\s+", " ");
        }
        //оценка
        String mark = WinDictionaryGeneral.fromHTML(model.getValueAt(0, 6).toString());
        mark.trim().toLowerCase().replaceAll("\\s+", " ");

        //считываем результаты из файла statistics.txt, чтобы сравнить на идентичность с табличным отображением
        ArrayList<Results> listUsersResults = User.getListUsersResults(); //получить все результаты
        assertNotNull(listUsersResults);
        assertNotEquals(0, listUsersResults.size());
        Results res1 = listUsersResults.get(0);
        ArrayList<User> listStudents = Student.getListUsers(); //получить все аккаунты
        Student stud = (Student) User.seekUserInSystem(res1.getUser().getUsername(), listStudents);
        //сравниваем результаты первой строки
        ArrayList<String> res1Categ = res1.getNamesCateg();
        assertEquals(stud.getName(), name);
        assertEquals(stud.getUsername(), username);
        assertEquals(stud.getGroup(), group);
        assertEquals(stud.getCourse().toString(), course);
        assertEquals(res1Categ.get(0), categories[0]);
        assertEquals(res1Categ.get(1), categories[1]);
        assertEquals(res1Categ.get(2), categories[2]);
        assertEquals(String.valueOf(res1.getMark()), mark);
    }
}
