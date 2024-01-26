package forms;

import model.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/* Главное меню */
public class WinMainMenu extends JFrame {
    private static WinMainMenu mainMenu;

    public WinMainMenu() {
        setTitle("Тренажёр по английскому языку");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        String text = "Пользователь: ";
        if (User.getCurUser() != null) {
            text += User.getCurUser().getUsername();
        } else {
            text += "вход не осуществлен";
        }
        PanelCurUser panelCurUser = new PanelCurUser(text);
        add(panelCurUser);
        Thread thread = new Thread(panelCurUser, "ThreadCurUser");
        thread.start();

        //выравниваем panelCurUser относительно левого верхнего угла
        layout.putConstraint(SpringLayout.WEST, panelCurUser, 10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelCurUser, 10,
                SpringLayout.NORTH, this);

        //кнопки меню
        Font font = new Font("Segoe Print", Font.BOLD, 16);
        JButton btnStart = new JButton("Начать") {
            {
                setName("start");
                setSize(new Dimension(150, 30));
                setMaximumSize(getSize());
                setPreferredSize(new Dimension(150, 30));
                setBackground(new Color(237, 246, 229));
                setFont(font);
            }
        };
        JButton btnDictionary = new JButton("Словарь") {
            {
                setName("dictionary");
                setSize(new Dimension(150, 30));
                setMaximumSize(getSize());
                setPreferredSize(new Dimension(150, 30));
                setBackground(new Color(237, 246, 229));
                setFont(font);
            }
        };
        JButton btnResults = new JButton("Результаты") {
            {
                setName("results");
                setSize(new Dimension(150, 30));
                setMaximumSize(getSize());
                setPreferredSize(new Dimension(150, 30));
                setBackground(new Color(237, 246, 229));
                setFont(font);
            }
        };
        JButton btnExit = new JButton("Выйти") {
            {
                setName("exit");
                setSize(new Dimension(150, 30));
                setMaximumSize(getSize());
                setPreferredSize(new Dimension(150, 30));
                setBackground(new Color(237, 246, 229));
                setFont(font);
            }
        };

        //панель, на которой размещаются кнопки и им добавляются слушатели
        JPanel centerPanel = createCenterVerticalPanel(5, btnStart, btnDictionary, btnResults, btnExit);
        add(centerPanel);
        //выравниваем centerPanel с кнопками относительно левого верхнего угла
        layout.putConstraint(SpringLayout.NORTH, centerPanel, 120,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, centerPanel, 25,
                SpringLayout.EAST, this);
        //сконструировать меню File
        JMenuBar mbar = new JMenuBar();
        setJMenuBar(mbar);
        JMenu fileMenu = new JMenu("Файл");
        mbar.add(fileMenu);
        mbar.setBackground(new Color(241, 243, 243));
        //ввести в меню пункты Вход, Выход из учетной записи и Выход из системы
        JMenuItem authItem = new JMenuItem("Войти в аккаунт");
        authItem.addActionListener(event -> {
            WinAuthorization.setFrame(new WinAuthorization());
            dispose();
        });
        fileMenu.add(authItem);

        //при выборе пункта меню exitFromAccItem происходит выход из аккаунта
        JMenuItem exitFromAccItem = new JMenuItem("Выйти из аккаунта");
        exitFromAccItem.addActionListener(event -> {
            User.setCurUser(null);
            String text2 = "Пользователь: ";
            if (User.getCurUser() != null) {
                text2 += User.getCurUser().getUsername();
            } else {
                text2 += "вход не осуществлен";
            }
            panelCurUser.setText(text2);
        });
        fileMenu.add(exitFromAccItem);
        //при выборе пункта меню exitItem происходит выход из программы
        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(event -> System.exit(0));
        fileMenu.add(exitItem);

        JButton helpMenu = new JButton("Помощь"){{
            setBackground(new Color(241, 243, 243));
            setBorder(fileMenu.getBorder());
            setFocusPainted(false);
        }};
        helpMenu.addActionListener(event -> {
            WinHelp.setFrame(new WinHelp());
            dispose();
        });
        mbar.add(helpMenu);

        getContentPane().setBackground(new Color(181, 234, 234)); //задаем цвет
        pack();
        setSize(450, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static WinMainMenu getFrame() {
        return mainMenu;
    }

    public static void setFrame(WinMainMenu mainMenu) {
        WinMainMenu.mainMenu = mainMenu;
    }

    //центральное выравнивание кнопок и добавление им слушателей
    private JPanel createCenterVerticalPanel(int spaceBetweenComponents, JComponent... components) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(181, 234, 234));
        Arrays.stream(components).forEach(component -> {
            if (component instanceof JButton) {
                JButton b = (JButton) component;
                b.addActionListener(new ButtonPress());
            }
            component.setAlignmentX(JPanel.CENTER_ALIGNMENT);
            panel.add(component);
            panel.add(Box.createRigidArea(new Dimension(0, spaceBetweenComponents)));
        });
        return panel;
    }

    public static class ButtonPress implements ActionListener {
        public void actionPerformed(ActionEvent ae) {
            if (ae.getActionCommand().equals("Начать") && User.getCurUser() == null) {
                WinAuthorization.setFrame(new WinAuthorization());
                try {
                    WinMainMenu.getFrame().dispose();
                    WinMainMenu.setFrame(null);
                } catch (NullPointerException npe) {}
            } else if (ae.getActionCommand().equals("Начать") && User.getCurUser() != null) {
                try {
                    try {
                        WinChooseCategory.setFrame(new WinChooseCategory());
                        WinMainMenu.getFrame().dispose();
                        WinMainMenu.setFrame(null);
                    } catch (NullPointerException npe) {}
                } catch (MyFileException e) {
                    e.printStackTrace();
                }
            } else if (ae.getActionCommand().equals("Словарь")) {
                try {
                    try {
                        WinChooseCategory.setCallFromCateg(false);
                        WinDictionaryGeneral.setCallFromGenDict(false);
                        WinMainMenu.getFrame().dispose();
                        WinMainMenu.setFrame(null);
                        WinDictionaryUser.setFrame(new WinDictionaryUser());
                    } catch (NullPointerException npe) {}
                } catch (MyFileException | IOException e) {
                    e.printStackTrace();
                }
            } else if (ae.getActionCommand().equals("Результаты")) {
                try {
                    try {
                        WinMainMenu.getFrame().dispose();
                        WinMainMenu.setFrame(null);
                        WinResultsGeneral.setFrame(new WinResultsGeneral());
                    } catch (NullPointerException npe) {}
                } catch (MyFileException e) {
                    e.printStackTrace();
                }
            } else if (ae.getActionCommand().equals("Выйти")) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        // создать рамку окна в потоке диспетчеризации событий
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainMenu = new WinMainMenu();
            }
        });
    }
}