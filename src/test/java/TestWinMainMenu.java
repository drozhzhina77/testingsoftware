import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

// тест окна главного меню
public class TestWinMainMenu {
    @Test
    /*
       тест нажатия на кнопку Начать,
       если в системе не задан пользователь, должно открыться окно Войти
       если в системе задан пользователь, должно открыться окно Выбор категории
    */
    public void buttonStartUserTest() {
        WinMainMenu mainMenu = new WinMainMenu();
        WinMainMenu.setFrame(mainMenu);
        JButton btnStart = new JButton("Начать");
        btnStart.addActionListener(new WinMainMenu.ButtonPress());
        btnStart.doClick();

        assertNotNull(WinAuthorization.getFrame());
        assertNull(WinMainMenu.getFrame());


        WinMainMenu mainMenu2 = new WinMainMenu();
        WinMainMenu.setFrame(mainMenu2);
        User.setCurUser(new User());
        btnStart.doClick();

        assertNotNull(WinChooseCategory.getFrame());
        assertNull(WinMainMenu.getFrame());
    }

    @Test
    // тест нажатия на кнопку Словарь, должно открыться окно Словарь (пользователя)
    public void buttonDictionaryTest() {
        WinMainMenu mainMenu = new WinMainMenu();
        WinMainMenu.setFrame(mainMenu);
        JButton btnDict = new JButton("Словарь");
        btnDict.addActionListener(new WinMainMenu.ButtonPress());
        btnDict.doClick();

        assertEquals(false, WinChooseCategory.getCallFromCateg());
        assertEquals(false, WinDictionaryGeneral.getCallFromGenDict());
        assertNull(WinMainMenu.getFrame());
        assertNotNull(WinDictionaryUser.getFrame());
    }

    @Test
    // тест нажатия на кнопку Результаты, должно открыться окно Результаты (по всем пользователям)
    public void buttonResultsTest() {
        WinMainMenu mainMenu = new WinMainMenu();
        WinMainMenu.setFrame(mainMenu);
        JButton btnRes = new JButton("Результаты");
        btnRes.addActionListener(new WinMainMenu.ButtonPress());
        btnRes.doClick();

        assertNotNull(WinResultsGeneral.getFrame());
        assertNull(WinMainMenu.getFrame());
    }
}
