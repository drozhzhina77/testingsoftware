package forms;

import model.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

/* Окно входа */
public class WinAuthorization extends JFrame {
    private JTextField username;
    private JTextField password;
    private static WinAuthorization frame;

    public WinAuthorization() {
        setLayout(new BorderLayout());
        setTitle("Войти");
        //панель с полями для ввода имени пользователя и пароля
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());

        JPanel panelLog = new JPanel();
        panelLog.add(new JLabel("Логин:") {
            {
                setPreferredSize(new Dimension(60, 15));
                setFont(new Font("Segoe Print", Font.BOLD, 14));
            }
        });
        panelLog.setBackground(new Color(181, 234, 234));
        username = new JTextField("", 20);
        username.setFont(new Font("TimesRoman", Font.BOLD, 13));
        panelLog.add(username);

        JPanel panelPass = new JPanel();
        panelPass.add(new JLabel("Пароль:") {
            {
                setPreferredSize(new Dimension(60, 15));
                setFont(new Font("Segoe Print", Font.BOLD, 14));
            }
        });
        panelPass.setBackground(new Color(181, 234, 234));
        password = new JPasswordField("", 20);
        password.setFont(new Font("TimesRoman", Font.BOLD, 13));
        panelPass.add(password);

        setUser();

        //создать кнопки ОК и Отмена для закрытия диалогового окна
        JButton okButton = new JButton("ОК");
        okButton.setBackground(new Color(237, 246, 229));
        okButton.setFont(new Font("Segoe Print", Font.BOLD, 12));
        okButton.addActionListener(event -> {
            try {
                getUser();
            } catch (MyFileException e) {
                e.printStackTrace();
            }
        });
        JButton cancelButton = new JButton("Отмена");
        cancelButton.setBackground(new Color(237, 246, 229));
        cancelButton.setFont(new Font("Segoe Print", Font.BOLD, 12));
        cancelButton.addActionListener(event -> {
            dispose();
            WinMainMenu.setFrame(new WinMainMenu());
        });
        JButton regButton = new JButton("Зарегистрироваться");
        regButton.setBackground(new Color(237, 246, 229));
        regButton.setFont(new Font("Segoe Print", Font.BOLD, 12));
        regButton.addActionListener(event -> {
            dispose();
            WinRegistration.setFrame(new WinRegistration());
        });

        // ввести кнопки в нижней части окна у южной его границы
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(181, 234, 234));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        JPanel leftPanel = createCenterVerticalPanel(5, panelLog, panelPass, buttonPanel, regButton);
        leftPanel.setBackground(new Color(181, 234, 234));
        add(leftPanel);

        addWindowListener(new WindowAdapter() {
            public void windowClosing (WindowEvent event) {
                WinMainMenu.setFrame(new WinMainMenu());
                dispose();
            }
        });
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static JPanel createCenterVerticalPanel(int spaceBetweenComponents, JComponent... components) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Arrays.stream(components).forEach(component -> {
            component.setAlignmentX(JPanel.CENTER_ALIGNMENT);
            panel.add(component);
            panel.add(Box.createRigidArea(new Dimension(0, spaceBetweenComponents)));
        });
        return panel;
    }

    public static void setFrame(WinAuthorization frame) {
        WinAuthorization.frame = frame;
    }

    public static WinAuthorization getFrame() {
        return WinAuthorization.frame;
    }

    public JTextField getUsername() {
        return username;
    }

    public void setUsername(JTextField username) {
        this.username = username;
    }

    public JTextField getPassword() {
        return password;
    }

    public void setPassword(JTextField password) {
        this.password = password;
    }

    //Устанавливает диалоговое окно в исходное состояние
    public void setUser() {
        username.setText("");
        password.setText("");
    }

    //Получает данные, введенные в диалоговом окне, ищет пользователя и, если находит, то авторизует
    public boolean getUser() throws MyFileException {
        ArrayList<User> listUsers = User.getListUsers(); //получить все аккаунты
        String usname = username.getText().trim().replaceAll("\\s+", " ");
        String passwd = password.getText().trim().replaceAll("\\s+", " ");
        if (usname.isEmpty() || passwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Не все поля заполнены!", "Предупреждение",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            User us = User.seekUserInSystem(usname, listUsers);
            if (us != null && us.getPassword().equals(passwd)) {
                JOptionPane.showMessageDialog(this, "Вы успешно вошли в систему!", "Сообщение",
                        JOptionPane.INFORMATION_MESSAGE);
                User.setCurUser(us);
                us.setDictionary();
                dispose();
                WinMainMenu.setFrame(new WinMainMenu());
                return true;
            } else if (us == null) {
                JOptionPane.showMessageDialog(this, "Неверный ввод логина!", "Предупреждение",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            } else {
                JOptionPane.showMessageDialog(this, "Неверный ввод пароля!", "Предупреждение",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
    }
}