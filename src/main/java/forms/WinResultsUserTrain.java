package forms;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/* Окно результатов пользователя после тренировки */
public class WinResultsUserTrain extends JFrame {
    private static WinResultsUserTrain frame;
    private JList<String> learnedList;
    private JList<String> newList;

    public WinResultsUserTrain(ArrayList<Word> newWords, ArrayList<Word> learnedWords) {
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        setTitle("Результаты тренировки " + User.getCurUser().getUsername());
        JLabel labelLearned = new JLabel("Выученные слова:") {{
            setPreferredSize(new Dimension(270, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 12));
            setBackground(new Color(237, 246, 229));
        }};
        //формируем список выученных слов
        DefaultListModel<String> model = new DefaultListModel();
        for (int i = 0; i < learnedWords.size(); i++) {
            model.addElement(learnedWords.get(i).getEngValue() + " ");
        }
        if (learnedWords.size() < 1) {
            model.addElement("отсутствуют");
        }
        learnedList = new JList(model);
        learnedList.setName("learnedList");
        //создаем панель и прокрутку для списка выученных слов
        JPanel listPanelLearned = new JPanel();
        listPanelLearned.setLayout(new BoxLayout(listPanelLearned, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(learnedList);
        scrollPane.setPreferredSize(new Dimension(300, 100));
        learnedList.setLayoutOrientation(JList.VERTICAL_WRAP);
        learnedList.setVisibleRowCount(0);
        listPanelLearned.add(scrollPane);
        JLabel labelNew = new JLabel("Добавленные в словарь слова:") {{
            setPreferredSize(new Dimension(300, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 12));
            setBackground(new Color(237, 246, 229));
        }};

        //формируем список новых (добавленных) слов
        DefaultListModel<String> modelNew = new DefaultListModel();
        for (int i = 0; i < newWords.size(); i++) {
            modelNew.addElement(newWords.get(i).getEngValue() + " ");
        }
        if (newWords.size() < 1) {
            modelNew.addElement("отсутствуют");
        }
        newList = new JList(modelNew);
        newList.setName("newList");

        //создаем панель и прокрутку для списка добавленных слов
        JPanel listPanelNew = new JPanel();
        listPanelNew.setLayout(new BoxLayout(listPanelNew, BoxLayout.Y_AXIS));
        JScrollPane scrollPaneNew = new JScrollPane(newList);
        scrollPaneNew.setPreferredSize(new Dimension(300, 100));
        newList.setLayoutOrientation(JList.VERTICAL_WRAP);
        newList.setVisibleRowCount(0);
        listPanelNew.add(scrollPaneNew);
        add(labelLearned);
        add(listPanelLearned);
        add(labelNew);
        add(listPanelNew);
        layout.putConstraint(SpringLayout.WEST, labelLearned, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, labelLearned, 20,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, listPanelLearned, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, listPanelLearned, 20,
                SpringLayout.NORTH, labelLearned);
        layout.putConstraint(SpringLayout.WEST, labelNew, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, labelNew, 120,
                SpringLayout.NORTH, listPanelLearned);
        layout.putConstraint(SpringLayout.WEST, listPanelNew, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, listPanelNew, 20,
                SpringLayout.NORTH, labelNew);

        JButton buttonMenu = new JButton("Меню") {
            {
                setName("menu");
                setSize(new Dimension(120, 20));
                setMaximumSize(getSize());
                setBackground(new Color(237, 246, 229));
                setFont(new Font("Segoe Print", Font.BOLD, 12));
            }
        };
        buttonMenu.addActionListener(event -> {
            WinMainMenu.setFrame(new WinMainMenu());
            dispose();
        });

        JButton buttonCateg = new JButton("Новый опрос") {
            {
                setName("newCheck");
                setSize(new Dimension(120, 20));
                setMaximumSize(getSize());
                setBackground(new Color(237, 246, 229));
                setFont(new Font("Segoe Print", Font.BOLD, 12));
            }
        };

        buttonCateg.addActionListener(event -> {
            try {
                WinChooseCategory.setFrame(new WinChooseCategory());
                dispose();
            } catch (MyFileException e) {
                e.printStackTrace();
            }
        });
        add(buttonMenu);
        add(buttonCateg);
        //размещаем buttonMenu относительно нижнего левого угла
        layout.putConstraint(SpringLayout.WEST, buttonMenu, 20,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.SOUTH, buttonMenu, -55,
                SpringLayout.SOUTH, this);
        //размещаем buttonCateg относительно нижнего правого угла
        layout.putConstraint(SpringLayout.EAST, buttonCateg, -35,
                SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.SOUTH, buttonCateg, -55,
                SpringLayout.SOUTH, this);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                WinMainMenu.setFrame(new WinMainMenu());
                dispose();
            }
        });
        getContentPane().setBackground(new Color(181, 234, 234));
        setPreferredSize(new Dimension(400, 450));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void setFrame(WinResultsUserTrain frame) {
        WinResultsUserTrain.frame = frame;
    }

    public static WinResultsUserTrain getFrame() {
        return WinResultsUserTrain.frame;
    }

    public JList<String> getLearnedList() {
        return learnedList;
    }

    public JList<String> getNewList() {
        return newList;
    }
}