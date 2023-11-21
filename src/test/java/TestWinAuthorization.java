import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

// тест окна Войти - проверка авторизации пользователя
public class TestWinAuthorization {
    WinAuthorization winAuthorization;
    JTextField username;
    JTextField password;

    public void initWindowAndTextFields() {
        winAuthorization = new WinAuthorization();
        username = new JTextField("", 20);
        password = new JPasswordField("", 20);
        User.setCurUser(null);
    }

    @Test
    // тест на вход с пустыми полями логина и пароля
    public void getUserWithEmptyFieldsTest() throws MyFileException {
        initWindowAndTextFields();
        username.setText("");
        password.setText("");
        winAuthorization.setUsername(username);
        winAuthorization.setPassword(password);
        boolean isUser = winAuthorization.getUser();
        assertEquals(isUser, false);
        assertNull(User.getCurUser());
    }

    @Test
    // тест на вход с пустым полем логина
    public void getUserWithEmptyFieldUsername() throws MyFileException {
        initWindowAndTextFields();
        username.setText("");
        password.setText("fBh23k&ju");
        winAuthorization.setUsername(username);
        winAuthorization.setPassword(password);
        boolean isUser = winAuthorization.getUser();
        assertEquals(isUser, false);
        assertNull(User.getCurUser());
    }

    @Test
    // тест на вход с пустым полем пароля
    public void getUserWithEmptyFieldPassword() throws MyFileException {
        initWindowAndTextFields();
        username.setText("matvey12");
        password.setText("");
        winAuthorization.setUsername(username);
        winAuthorization.setPassword(password);
        boolean isUser = winAuthorization.getUser();
        assertEquals(isUser, false);
        assertNull(User.getCurUser());
    }

    @Test
    // тест на вход несуществующего пользователя
    public void getUserWithIncorrectFields() throws MyFileException {
        initWindowAndTextFields();
        username.setText("matvey123");
        password.setText("fBh23k&ju");
        winAuthorization.setUsername(username);
        winAuthorization.setPassword(password);
        boolean isUser = winAuthorization.getUser();
        assertEquals(isUser, false);
        assertNull(User.getCurUser());
    }

    @Test
    // тест на вход существующего пользователя
    public void getUserWithCorrectFields() throws MyFileException {
        initWindowAndTextFields();
        username.setText("matvey12");
        password.setText("fBh23k&ju");
        winAuthorization.setUsername(username);
        winAuthorization.setPassword(password);
        boolean isUser = winAuthorization.getUser();
        assertEquals(isUser, true);
        assertNotNull(User.getCurUser());
    }
}
