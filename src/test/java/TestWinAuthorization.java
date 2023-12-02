import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

// тест окна Войти - проверка авторизации пользователя
public class TestWinAuthorization {
    private WinAuthorization winAuthorization;
    private JTextField username;
    private JTextField password;

    //инициализация окна Войти и его текстовых полей
    public void initWindowAndTextFields() {
        winAuthorization = new WinAuthorization();
        username = new JTextField("", 20);
        password = new JPasswordField("", 20);
        User.setCurUser(null);
    }

    //задать текст полям для ввода
    public void setFields(String userText, String passText){
        username.setText(userText);
        password.setText(passText);
        winAuthorization.setUsername(username);
        winAuthorization.setPassword(password);
    }

    @Test
    // тест на вход с пустыми полями логина и пароля
    public void getUserWithEmptyFieldsTest() throws MyFileException {
        initWindowAndTextFields();
        setFields("", "");
        boolean isUser = winAuthorization.getUser();
        assertEquals(isUser, false);
        assertNull(User.getCurUser());
    }

    @Test
    // тест на вход с пустым полем логина
    public void getUserWithEmptyFieldUsernameTest() throws MyFileException {
        initWindowAndTextFields();
        setFields("", "fBh23k&ju");
        boolean isUser = winAuthorization.getUser();
        assertEquals(isUser, false);
        assertNull(User.getCurUser());
    }

    @Test
    // тест на вход с пустым полем пароля
    public void getUserWithEmptyFieldPasswordTest() throws MyFileException {
        initWindowAndTextFields();
        setFields("matvey12","");
        boolean isUser = winAuthorization.getUser();
        assertEquals(isUser, false);
        assertNull(User.getCurUser());
    }

    @Test
    // тест на вход несуществующего пользователя
    public void getUserWithIncorrectFieldsTest() throws MyFileException {
        initWindowAndTextFields();
        setFields("matvey123","fBh23k&ju");
        boolean isUser = winAuthorization.getUser();
        assertEquals(isUser, false);
        assertNull(User.getCurUser());
    }

    @Test
    // тест на вход существующего пользователя
    public void getUserWithCorrectFieldsTest() throws MyFileException {
        initWindowAndTextFields();
        setFields("matvey12", "fBh23k&ju");
        boolean isUser = winAuthorization.getUser();
        assertEquals(isUser, true);
        assertNotNull(User.getCurUser());
    }
}
