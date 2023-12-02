package integration;

import model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// тест класса Student - проверка чтения аккаунтов и создания списка студентов
public class TestStudent {
    /*
       тест на чтение аккаунтов и на создание списка студентов

       для теста в файле accounts.txt должны быть записи admin и matvey12 в виде:
       Запись в файле админа (<username>~<password>~<accessLevel>):
       admin~admin255~1
       Запись в файле студента (<username>~<password>~<accessLevel>\n<name>~<group>~<course>):
       matvey12~fBh23k&ju~0
       Петров Матвей Михайлович~ИПБ-21~1
    */
    @Test
    public void getListUsers() throws MyFileException {
        List<User> listUsers = Student.getListUsers();
        assertNotNull(listUsers);
        assertNotEquals(0, listUsers.size());

        User admin = listUsers.get(0); //admin
        assertEquals("admin", admin.getUsername());
        assertEquals("admin255", admin.getPassword());
        assertEquals(1, admin.getAcessLevel()); //уровень доступа

        Student stud1 = (Student) listUsers.get(1);
        assertEquals("matvey12", stud1.getUsername());
        assertEquals("fBh23k&ju", stud1.getPassword());
        assertEquals(0, stud1.getAcessLevel()); //уровень доступа
        assertEquals("Петров Матвей Михайлович", stud1.getName());
        assertEquals("ИПБ-21", stud1.getGroup());
        assertEquals("1", stud1.getCourse());
    }
}
