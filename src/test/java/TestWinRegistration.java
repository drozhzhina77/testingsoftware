import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
   тест окна Зарегистрироваться:
   проверка корректности введенных данных для регистрации и
   создания и сохранения нового пользователя
 */
public class TestWinRegistration {
    private JTextField username;
    private JTextField password;
    private JTextField name;
    private JTextField group;
    private JTextField course;
    private WinRegistration winRegistration;

    //инициализация окна Зарегистрироваться и его текстовых полей
    public void initWindowAndTextFields() {
        winRegistration = new WinRegistration();
        username = new JTextField("", 25);
        password = new JPasswordField("", 25);
        name = new JTextField("", 25);
        group = new JTextField("", 25);
        course = new JTextField("", 25);
    }

    //задать текст полям для ввода
    public void setFields(String userText, String passText, String nameText, String groupText, String courseText) {
        username.setText(userText);
        password.setText(passText);
        name.setText(nameText);
        group.setText(groupText);
        course.setText(courseText);
        winRegistration.setUsername(username);
        winRegistration.setPassword(password);
        winRegistration.setName(name);
        winRegistration.setGroup(group);
        winRegistration.setCourse(course);
    }

    @Test
    // тест на регистрацию с пустыми полями
    public void getStudentWithEmptyFieldsTest() throws MyFileException {
        initWindowAndTextFields();
        setFields("", "", "", "", "");
        winRegistration.getStudent();
        assertEquals("Не все поля заполнены!", winRegistration.getMessageJOptionPane());

        username.setText("kira_355");
        assertEquals("Не все поля заполнены!", winRegistration.getMessageJOptionPane());

        password.setText("efjkUE049+");
        assertEquals("Не все поля заполнены!", winRegistration.getMessageJOptionPane());

        name.setText("Иванова Кира Сергеевна");
        assertEquals("Не все поля заполнены!", winRegistration.getMessageJOptionPane());

        group.setText("ИЭБ-18");
        assertEquals("Не все поля заполнены!", winRegistration.getMessageJOptionPane());

        group.setText("");
        course.setText("4");
        assertEquals("Не все поля заполнены!", winRegistration.getMessageJOptionPane());
    }

    @Test
    // тест на регистрацию с некорректным ФИО (содержащим цифры)
    public void getStudentWithIncorrectNameTest() throws MyFileException {
        initWindowAndTextFields();
        setFields("kira_355", "efjkUE049+", "Иванова356 Кира Сергеевна", "ИЭБ-18", "4");
        winRegistration.getStudent();
        assertEquals("Некорректный ввод ФИО!", winRegistration.getMessageJOptionPane());
    }

    @Test
    // тест на регистрацию с существующим логином
    public void getStudentWithExistedUsernameTest() throws MyFileException {
        initWindowAndTextFields();
        setFields("matvey12", "efjkUE049+", "Иванов Матвей Сергеевич", "ИЭБ-18", "4");
        winRegistration.getStudent();
        assertEquals("Такой логин уже существует!", winRegistration.getMessageJOptionPane());
    }

    @Test
    // тест на регистрацию с некорректным логином (отсуствуют цифры и спец.символ, длина меньше 8)
    public void getStudentWithIncorrectUsernameTest() throws MyFileException {
        initWindowAndTextFields();
        setFields("kira", "efjkUE049+", "Иванова Кира Сергеевна", "ИЭБ-18", "4");
        winRegistration.getStudent();
        assertEquals("Некорректный ввод логина!", winRegistration.getMessageJOptionPane());
    }

    @Test
    // тест на регистрацию с некорректным паролем (отсуствуют прописные буквы)
    public void getStudentWithIncorrectPassTest() throws MyFileException {
        initWindowAndTextFields();
        setFields("kira_355", "efjk049+", "Иванова Кира Сергеевна", "ИЭБ-18", "4");
        winRegistration.getStudent();
        assertEquals("Некорректный ввод пароля!", winRegistration.getMessageJOptionPane());
    }

    @Test
    // тест на регистрацию с корректными данными и на создание и сохранение нового пользователя
    public void getStudentWithCorrectFieldsTest() throws MyFileException {
        initWindowAndTextFields();
        setFields("kira_389", "efjkUE049+", "Иванова Кира Сергеевна", "ИЭБ-18", "4");
        winRegistration.getStudent();
        assertEquals("Регистрация прошла успешно!", winRegistration.getMessageJOptionPane());
        ArrayList<User> listUsers = new ArrayList<>(Student.getListUsers());
        User lastUser = listUsers.get(listUsers.size() - 1);
        assertEquals("kira_389", lastUser.getUsername());
        assertEquals("efjkUE049+", lastUser.getPassword());
    }
}
