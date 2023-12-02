package forms;

import model.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

class WinHelp extends JFrame {
    private static WinHelp frame;

    public WinHelp() {
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        setTitle("Помощь");
        getContentPane().setBackground(new Color(181, 234, 234));

        JPanel panelEnter = new JPanel();
        panelEnter.add(new JLabel("<html>Здесь Вы можете ознакомиться с разделами тренажера:</html>") {{
            setPreferredSize(new Dimension(300, 40));
            setFont(new Font("Segoe Print", Font.BOLD, 13));
        }});
        panelEnter.setBackground(new Color(237, 246, 229));
        add(panelEnter);
        layout.putConstraint(SpringLayout.WEST, panelEnter, 19,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelEnter, 20,
                SpringLayout.NORTH, this);

        JEditorPane helpEditor = new JEditorPane();
        helpEditor.setMaximumSize(new Dimension(430, 320));
        helpEditor.setPreferredSize(new Dimension(430, 320));
        HTMLEditorKit editorKit = new HTMLEditorKit();
        helpEditor.setEditorKit(editorKit);
        helpEditor.setText("<html>" +
                "<font face=\"Times New Roman\" size=\"8px\">" +
                "<p style=\"text-indent: 20px;\" align=\"justify\">По нажатию на <b><a href=\"Начать\">«Начать»</a></b>" +
                " – если вход не осуществлен, " +
                "предлагается окно <b><a href=\"Войти\">«Войти»</a></b> (также можно зарегистрироваться, " +
                "если пользователя еще нет в системе); если осуществлен, происходит " +
                "переход к окну <b><a href=\"Выбор категории\" align=\"justify\">«Выбор категории»</a></b>, " +
                "где пользователь выбирает нужный " +
                "вид опроса: тренировку или экзамен, необходимые категории (темы, " +
                "по которым проводится опрос) и переходит к выполнению заданий.</p>" +
                "<p style=\"text-indent: 20px;\" align=\"justify\">В режиме тренировки доступна категория «мой словарь» " +
                "(для опроса слова могут выбираться и из словаря пользователя). Далее будет " +
                "предложен опрос. В режиме тренировки – задания с выбором варианта " +
                "ответа и с перетаскиванием варианта ответа, неверные слова " +
                "записываются в словарь, а верные удаляются после 3-х правильных " +
                "ответов, в конце вывод выученных и новых слов.</p>" +
                "<p style=\"text-indent: 20px;\" align=\"justify\">В режиме экзамена – задания с выбором " +
                "варианта ответа, с перетаскиванием варианта " +
                "ответа, с открытым ответом, неверные слова записываются в словарь, " +
                "в конце вывод таблицы с заданиями, правильными ответами, ответами " +
                "пользователя (неверные подсвечиваются красным), и вывод оценки.</p>" +
                "<p style=\"text-indent: 20px;\" align=\"justify\"><b><a href=\"Словарь\">«Словарь»</a></b>" +
                " – окно со словарем пользователя (доступно, если " +
                "вход осуществлен), далее можно перейти в общий словарь, в котором " +
                "представлены все слова системы, из него можно добавлять слова в " +
                "свой словарь. Возможно создание слов через меню: " +
                "«Создать слово» => <b><a href=\"По одному в конструкторе\">«По одному в конструкторе»</a></b> или " +
                "<b><a href=\"Несколько из файла\">«Несколько из файла»</a></b> " +
                "(можно перетащить файл или ввести в редактор несколько слов); а также " +
                "– удаление (необходимо выделить нужные строки) и нажать <b>«Удалить из словаря»</b>.</p>" +
                "<p style=\"text-indent: 20px;\" align=\"justify\"><b><a href=\"«Результаты»\">Результаты</a></b> –" +
                " окно с результатами всех пользователей за всё время.</p>" +
                "</font>" +
                "</html>");
        helpEditor.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() != HyperlinkEvent.EventType.ACTIVATED)
                    return;
                try {
                    if (e.getDescription().equals("Начать")) {
                        dispose();
                        WinMainMenu.setFrame(new WinMainMenu());
                    } else if (e.getDescription().equals("Войти")) {
                        if (User.getCurUser() == null) {
                            dispose();
                            WinAuthorization.setFrame(new WinAuthorization());
                        } else {
                            JOptionPane.showMessageDialog(WinHelp.getFrame(), "Невозможен переход,\nвход уже осуществлен!",
                                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
                        }
                    } else if (e.getDescription().equals("Выбор категории")) {
                        if (User.getCurUser() != null) {
                            dispose();
                            WinChooseCategory.setFrame(new WinChooseCategory());
                        } else {
                            JOptionPane.showMessageDialog(WinHelp.getFrame(), "Невозможен переход,\nвход не осуществлен!",
                                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
                        }
                    } else if (e.getDescription().equals("Словарь")) {
                        dispose();
                        WinDictionaryUser.setFrame(new WinDictionaryUser());
                    } else if (e.getDescription().equals("По одному в конструкторе")) {
                        if (User.getCurUser() != null) {
                            dispose();
                            WinAddNewWord.setFrame(new WinAddNewWord(User.getCurUser().getDictionary(),
                                    User.getCurUser().getUsername()+".txt"));
                        } else {
                            JOptionPane.showMessageDialog(WinHelp.getFrame(), "Невозможен переход,\nвход не осуществлен!",
                                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
                        }
                    } else if (e.getDescription().equals("Несколько из файла")) {
                        if (User.getCurUser() != null) {
                            dispose();
                            WinAddNewWordsFromFile.setFrame(new WinAddNewWordsFromFile(User.getCurUser().getDictionary(),
                                    User.getCurUser().getUsername()+".txt"));
                        } else {
                            JOptionPane.showMessageDialog(WinHelp.getFrame(), "Невозможен переход,\nвход не осуществлен!",
                                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
                        }
                    } else if (e.getDescription().equals("Результаты")) {
                        dispose();
                        WinResultsGeneral.setFrame(new WinResultsGeneral());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(WinHelp.getFrame(), "Невозможен переход!",
                            "Предупреждение", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        helpEditor.setEditable(false);
        helpEditor.setOpaque(true);
        JScrollPane scrollPane = new JScrollPane(helpEditor);
        JPanel panel = new JPanel();
        panel.add(scrollPane);
        panel.setBackground(new Color(181, 234, 234));
        add(panel);

        layout.putConstraint(SpringLayout.WEST, panel, 19,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panel, 65,
                SpringLayout.NORTH, panelEnter);

        JButton cancelButton = new JButton("Назад");
        cancelButton.setBackground(new Color(237, 246, 229));
        cancelButton.setFont(new Font("Segoe Print", Font.BOLD, 12));
        cancelButton.addActionListener(event -> {
            dispose();
            WinMainMenu.setFrame(new WinMainMenu());
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(181, 234, 234));
        buttonPanel.add(cancelButton);

        add(buttonPanel);
        layout.putConstraint(SpringLayout.WEST, buttonPanel, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, buttonPanel, 15,
                SpringLayout.SOUTH, panel);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                WinMainMenu.setFrame(new WinMainMenu());
                dispose();
            }
        });
        setPreferredSize(new Dimension(500, 540));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static WinHelp getFrame() {
        return frame;
    }

    public static void setFrame(WinHelp frame) {
        WinHelp.frame = frame;
    }


}