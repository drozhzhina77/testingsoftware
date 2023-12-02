import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/* Окно регистрации */
public class WinRegistration extends JFrame {
    private JTextField username;
    private JTextField password;
    private JTextField name;
    private JTextField group;
    private JTextField course;
    private static WinRegistration frame;
    private Border bord;
    private String messageJOptionPane;

    public WinRegistration() {
        setLayout(new BorderLayout());
        setTitle("Зарегистрироваться");

        //панель с полями для ввода имени пользователя и пароля
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());

        JPanel panelLog = new JPanel();
        panelLog.setBackground(new Color(181, 234, 234));
        panelLog.add(new JLabel("Логин:") {{
            setPreferredSize(new Dimension(60, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 13));
        }});
        username = new JTextField("", 25);
        username.setFont(new Font("TimesRoman", Font.BOLD, 13));
        username.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent mEvt) {
                username.setToolTipText("хотя бы одна цифра и строчная лат.буква, " +
                        "без пробелов, 8..10 символов");
            }
        });
        bord = username.getBorder();

        username.getDocument().addDocumentListener((txtFldDocumentListener) e -> {
            String usnameStud = username.getText();
            boolean isUsnameValid = User.isUsernameValid(usnameStud);
            if ((!isUsnameValid) && (!usnameStud.isEmpty()))
                username.setBorder(BorderFactory.createLineBorder(Color.decode("#ff0000")));
            else {
                username.setBorder(bord);
            }
        });
        panelLog.add(username);

        JPanel panelPass = new JPanel();
        panelPass.setBackground(new Color(181, 234, 234));
        panelPass.add(new JLabel("Пароль:") {{
            setPreferredSize(new Dimension(60, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 13));
        }});
        password = new JPasswordField("", 25);
        password.setFont(new Font("TimesRoman", Font.BOLD, 13));
        password.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent mEvt) {
                password.setToolTipText("хотя бы одна цифра и строчная и прописная лат.буква, " +
                        "спец.символ, без пробелов, 8..10 символов");
            }
        });
        password.getDocument().addDocumentListener((txtFldDocumentListener) e -> {
            String passwdStud = password.getText();
            boolean isPasswdValid = User.isPasswordValid(passwdStud);
            if ((!isPasswdValid) && (!passwdStud.isEmpty())) {
                password.setBorder(BorderFactory.createLineBorder(Color.decode("#ff0000")));
            } else {
                password.setBorder(bord);
            }
        });
        panelPass.add(password);

        JPanel panelFio = new JPanel();
        panelFio.setBackground(new Color(181, 234, 234));
        panelFio.add(new JLabel("ФИО:") {{
            setPreferredSize(new Dimension(60, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 13));
        }});
        name = new JTextField("", 25);
        name.setFont(new Font("TimesRoman", Font.BOLD, 13));
        name.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent mEvt) {
                name.setToolTipText("не содержит цифр");
            }
        });
        name.getDocument().addDocumentListener((txtFldDocumentListener) e -> {
            String nameStud = name.getText();
            boolean isNameValid = Student.isNameValid(nameStud);
            if (!isNameValid) {
                name.setBorder(BorderFactory.createLineBorder(Color.decode("#ff0000")));
            } else {
                name.setBorder(bord);
            }
        });
        panelFio.add(name);

        JPanel panelGroup = new JPanel();
        panelGroup.setBackground(new Color(181, 234, 234));
        panelGroup.add(new JLabel("Группа:") {{
            setPreferredSize(new Dimension(60, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 13));
        }});
        group = new JTextField("", 25);
        group.setFont(new Font("TimesRoman", Font.BOLD, 13));
        panelGroup.add(group);

        JPanel panelCourse = new JPanel();
        panelCourse.setBackground(new Color(181, 234, 234));
        panelCourse.add(new JLabel("Курс:") {{
            setPreferredSize(new Dimension(60, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 13));
        }});
        course = new JTextField("", 25);
        course.setFont(new Font("TimesRoman", Font.BOLD, 13));
        panelCourse.add(course);

        setStudent();

        //создать кнопки ОК и Отмена для закрытия окна
        JButton okButton = new JButton("ОК");
        okButton.setBackground(new Color(237, 246, 229));
        okButton.setFont(new Font("Segoe Print", Font.BOLD, 12));
        okButton.addActionListener(event -> {
            try {
                getStudent();
            } catch (MyFileException e) {
                e.printStackTrace();
            }
        });
        JButton cancelButton = new JButton("Отмена");
        cancelButton.setBackground(new Color(237, 246, 229));
        cancelButton.setFont(new Font("Segoe Print", Font.BOLD, 12));
        cancelButton.addActionListener(event -> {
            dispose();
            WinAuthorization.setFrame(new WinAuthorization());
        });

        JButton menuButton = new JButton("Меню");
        menuButton.setBackground(new Color(237, 246, 229));
        menuButton.setFont(new Font("Segoe Print", Font.BOLD, 12));
        menuButton.addActionListener(event -> {
            dispose();
            WinMainMenu.setFrame(new WinMainMenu());
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(181, 234, 234));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        JPanel leftPanel = WinAuthorization.createCenterVerticalPanel(5,
                panelLog, panelPass, panelFio, panelGroup, panelCourse, buttonPanel, menuButton);
        leftPanel.setBackground(new Color(181, 234, 234));
        add(leftPanel);

        addWindowListener(new WindowAdapter() {
            public void windowClosing (WindowEvent event) {
                WinAuthorization.setFrame(new WinAuthorization());
                dispose();
            }
        });
        setSize(450, 450);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void setFrame(WinRegistration frame) {
        WinRegistration.frame = frame;
    }

    public static WinRegistration getFrame() {
        return WinRegistration.frame;
    }

    public void setUsername(JTextField username) {
        this.username = username;
    }

    public void setPassword(JTextField password) {
        this.password = password;
    }

    public void setName(JTextField name) {
        this.name = name;
    }

    public void setGroup(JTextField group) {
        this.group = group;
    }

    public void setCourse(JTextField course) {
        this.course = course;
    }

    public void setBord(Border bord) {
        this.bord = bord;
    }

    public String getMessageJOptionPane() {
        return messageJOptionPane;
    }

    @FunctionalInterface
    public interface txtFldDocumentListener extends DocumentListener {
        void checkData(DocumentEvent e);

        @Override
        default void insertUpdate(DocumentEvent e) {
            checkData(e);
        }

        @Override
        default void removeUpdate(DocumentEvent e) {
            checkData(e);
        }

        @Override
        default void changedUpdate(DocumentEvent e) {
            checkData(e);
        }
    }

    //устанавливает окно регистрации в исходное состояние
    public void setStudent() {
        username.setText("");
        password.setText("");
        name.setText("");
        group.setText("");
        course.setText("");
        username.setBorder(bord);
        password.setBorder(bord);
        name.setBorder(bord);
    }

    //получает данные, введенные в окне регистрации
    void getStudent() throws MyFileException {
        ArrayList<User> listUsers = User.getListUsers(); //получить все аккаунты
        String usnameStud = username.getText().trim().replaceAll("\\s+", " ");
        String passwdStud = password.getText().trim().replaceAll("\\s+", " ");
        String nameStud = name.getText().trim().replaceAll("\\s+", " ");
        String groupStud = group.getText().trim().replaceAll("\\s+", " ");
        String courseStud = course.getText().trim().replaceAll("\\s+", " ");
        boolean isNameValid = Student.isNameValid(nameStud);
        boolean isUsnameValid = User.isUsernameValid(usnameStud);
        boolean isPasswdValid = User.isPasswordValid(passwdStud);
        User us = User.seekUserInSystem(usnameStud, listUsers);
        if (usnameStud.isEmpty() || passwdStud.isEmpty() || nameStud.isEmpty() || groupStud.isEmpty() || courseStud.isEmpty()) {
            messageJOptionPane = "Не все поля заполнены!";
            JOptionPane.showMessageDialog(this, messageJOptionPane, "Предупреждение",
                    JOptionPane.WARNING_MESSAGE);
        } else if (!isNameValid) {
            messageJOptionPane = "Некорректный ввод ФИО!";
            JOptionPane.showMessageDialog(this, messageJOptionPane, "Предупреждение",
                    JOptionPane.WARNING_MESSAGE);
        } else if (us != null) {
            messageJOptionPane = "Такой логин уже существует!";
            JOptionPane.showMessageDialog(this, messageJOptionPane, "Предупреждение",
                    JOptionPane.WARNING_MESSAGE);
        } else if (!isUsnameValid) {
            messageJOptionPane = "Некорректный ввод логина!";
            JOptionPane.showMessageDialog(this, messageJOptionPane, "Предупреждение",
                    JOptionPane.WARNING_MESSAGE);
        } else if (!isPasswdValid) {
            messageJOptionPane = "Некорректный ввод пароля!";
            JOptionPane.showMessageDialog(this, messageJOptionPane, "Предупреждение",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            messageJOptionPane = "Регистрация прошла успешно!";
            JOptionPane.showMessageDialog(this, messageJOptionPane, "Сообщение",
                    JOptionPane.INFORMATION_MESSAGE);
            try (FileWriter writer = new FileWriter("accounts.txt", true)) {
                writer.append(usnameStud + "~" + passwdStud + "~" + 0 + "\n" +
                        nameStud + "~" + groupStud + "~" + courseStud + "\n");
                writer.flush();
            } catch (IOException e) {
                throw new MyFileException();
            }
            dispose();
            WinAuthorization.setFrame(new WinAuthorization());
        }
    }
}