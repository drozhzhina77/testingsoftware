package forms;

import model.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

/* Окно выбора вида проверки знаний и категорий */
public class WinChooseCategory extends JFrame {
    private static DictionaryWords generalDict;
    private static DictionaryWords testDict;
    private ArrayList<Category> generalListCateg;
    private ButtonGroup groupRadio;
    private JPanel panelRadioGroup;
    private DefaultListModel<String> model;
    private JList<String> wordList;
    private JScrollPane scrollPane;
    private JPanel listPanelCateg;
    private String prefix = "Выбраны: ";
    private DefaultListModel<String> modelCurCateg;
    private JScrollPane scrollPaneCurCateg;
    private JPanel listPanelCurCateg;
    private static boolean isTraining;
    private static boolean callFromCateg;
    private static WinChooseCategory frame;
    private JPopupMenu popup;

    public WinChooseCategory() throws MyFileException {
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        setTitle("Выбор категории");

        //формируем список имеющихся категорий
        generalDict = new DictionaryWords();
        generalDict = DictionaryWords.makeGeneralDictionary(generalDict, "dictionary.txt");
        generalListCateg = new ArrayList<>(generalDict.getListCategory());
        testDict = new DictionaryWords();

        JLabel labelFormCheck = new JLabel("Выберите форму проведения опроса:") {{
            setPreferredSize(new Dimension(270, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 12));
            setBackground(new Color(237, 246, 229));
        }};
        groupRadio = new ButtonGroup();
        panelRadioGroup = new JPanel();
        panelRadioGroup.setBackground(new Color(237, 246, 229));
        addRadioButton("Тренировка", true);
        addRadioButton("Экзамен", false);
        isTraining = true;

        add(labelFormCheck);
        add(panelRadioGroup);
        layout.putConstraint(SpringLayout.WEST, labelFormCheck, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, labelFormCheck, 20,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, panelRadioGroup, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelRadioGroup, 20,
                SpringLayout.NORTH, labelFormCheck);

        //формируем список всех категорий
        model = new DefaultListModel();
        for (int i = 0; i < generalListCateg.size(); i++) {
            model.addElement(generalListCateg.get(i).getName() + " ");
        }
        model.addElement("мой словарь ");
        wordList = new JList(model);

        JLabel labelCurCateg = new JLabel(prefix) {{
            setPreferredSize(new Dimension(80, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 12));
            setBackground(new Color(237, 246, 229));
        }};

        modelCurCateg = new DefaultListModel();
        JList<String> curCategList = new JList(modelCurCateg);
        //если выбрана категория, она отображается в списке выбранных категорий
        wordList.addListSelectionListener(event -> {
            modelCurCateg.removeAllElements();
            for (String value : wordList.getSelectedValuesList()) {
                modelCurCateg.addElement(value);
                listPanelCurCateg.revalidate();
                scrollPaneCurCateg.revalidate();
            }
        });

        //создаем панель и прокрутку для списка всех категорий
        listPanelCateg = new JPanel();
        listPanelCateg.setLayout(new BoxLayout(listPanelCateg, BoxLayout.Y_AXIS));
        //wordList.setBackground(new Color(237, 246, 229));
        scrollPane = new JScrollPane(wordList);
        scrollPane.setPreferredSize(new Dimension(300, 100));
        wordList.setLayoutOrientation(JList.VERTICAL_WRAP);
        wordList.setVisibleRowCount(0);
        listPanelCateg.add(scrollPane);

        //создаем панель и прокрутку для списка выбранных категорий
        listPanelCurCateg = new JPanel();
        listPanelCurCateg.setLayout(new BoxLayout(listPanelCurCateg, BoxLayout.Y_AXIS));
        //curCategList.setBackground(new Color(237, 246, 229));
        scrollPaneCurCateg = new JScrollPane(curCategList);
        scrollPaneCurCateg.setPreferredSize(new Dimension(300, 100));
        curCategList.setLayoutOrientation(JList.VERTICAL_WRAP);
        curCategList.setVisibleRowCount(0);
        listPanelCurCateg.add(scrollPaneCurCateg);

        JLabel labelChooseCategory = new JLabel("Выберите категорию/категории(зажав Ctrl):") {{
            setPreferredSize(new Dimension(300, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 12));
            setBackground(new Color(237, 246, 229));
        }};
        add(labelChooseCategory);
        add(listPanelCateg);

        add(labelCurCateg);
        add(listPanelCurCateg);

        //размещаем labelChooseCategory относительно panelRadioGroup и левого края
        layout.putConstraint(SpringLayout.WEST, labelChooseCategory, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, labelChooseCategory, 50,
                SpringLayout.NORTH, panelRadioGroup);

        //размещаем listPanelCateg относительно labelChooseCategory и левого края
        layout.putConstraint(SpringLayout.WEST, listPanelCateg, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, listPanelCateg, 20,
                SpringLayout.NORTH, labelChooseCategory);

        //размещаем labelCurCateg относительно listPanelCateg и левого края
        layout.putConstraint(SpringLayout.WEST, labelCurCateg, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, labelCurCateg, 120,
                SpringLayout.NORTH, listPanelCateg);

        //размещаем listPanelCurCateg относительно labelCurCateg и левого края
        layout.putConstraint(SpringLayout.WEST, listPanelCurCateg, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, listPanelCurCateg, 20,
                SpringLayout.NORTH, labelCurCateg);

        JButton buttonMenu = new JButton("Меню") {
            {
                setSize(new Dimension(120, 20));
                setMaximumSize(getSize());
                setBackground(new Color(237, 246, 229));
                setFont(new Font("Segoe Print", Font.BOLD, 16));
            }
        };
        buttonMenu.addActionListener(event -> {
            WinMainMenu.setFrame(new WinMainMenu());
            dispose();
        });

        JButton buttonOk = new JButton("Далее") {
            {
                setSize(new Dimension(120, 20));
                setMaximumSize(getSize());
                setBackground(new Color(237, 246, 229));
                setFont(new Font("Segoe Print", Font.BOLD, 16));
            }
        };

        buttonOk.addActionListener(event -> {
            boolean findCateg = getCategories();
            if (findCateg) {
                WinExercise winExerc = new WinExercise();
                boolean findExerc = winExerc.init();
                if (!findExerc) {
                    JOptionPane.showMessageDialog(this, "Недостаточно слов для тренировки!",
                            "Сообщение", JOptionPane.WARNING_MESSAGE);
                } else {
                    try {
                        winExerc.run();
                        dispose();
                    } catch (MyFileException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        add(buttonMenu);
        add(buttonOk);
        //размещаем buttonMenu относительно нижнего левого угла
        layout.putConstraint(SpringLayout.WEST, buttonMenu, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.SOUTH, buttonMenu, -85,
                SpringLayout.SOUTH, this);
        //размещаем buttonOk относительно нижнего правого угла
        layout.putConstraint(SpringLayout.EAST, buttonOk, -30,
                SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.SOUTH, buttonOk, -85,
                SpringLayout.SOUTH, this);

        //сконструировать меню File
        JMenuBar mbar = new JMenuBar();
        setJMenuBar(mbar);
        JMenu fileMenu = new JMenu("Файл");
        mbar.add(fileMenu);
        mbar.setBackground(new Color(241, 243, 243));

        //ввести в меню пункты Словарь, Выход из учетной записи и Выход из системы
        JMenu dictItem = new JMenu("Словарь");
        JMenuItem dictUser = new JMenuItem("Мой словарь");
        JMenuItem dictGeneral = new JMenuItem("Общий словарь");
        dictUser.addActionListener(event -> {
            try {
                WinDictionaryUser.setFrame(new WinDictionaryUser());
                dispose();
            } catch (MyFileException | IOException e) {
                e.printStackTrace();
            }
        });
        dictGeneral.addActionListener(event -> {
            try {
                WinDictionaryGeneral.setFrame(new WinDictionaryGeneral());
                dispose();
            } catch (MyFileException e) {
                e.printStackTrace();
            }
        });
        dictItem.add(dictUser);
        dictItem.add(dictGeneral);
        fileMenu.add(dictItem);

        //при выборе пункта меню exitFromAccItem происходит выход из аккаунта
        JMenuItem exitFromAccItem = new JMenuItem("Выйти из аккаунта");
        exitFromAccItem.addActionListener(event -> {
            dispose();
            User.setCurUser(null);
            WinMainMenu.setFrame(new WinMainMenu());
        });
        fileMenu.add(exitFromAccItem);

        //при выборе пункта меню exitItem происходит выход из программы
        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(event -> System.exit(0));
        fileMenu.add(exitItem);
        //всплывающее меню
        popup = new JPopupMenu();
        JMenuItem popupDictUserItem = new JMenuItem("Мой словарь");
        JMenuItem popupDictGenerItem = new JMenuItem("Общий словарь");
        popupDictUserItem.addActionListener(event -> {
            try {
                setCallFromCateg(true);
                WinDictionaryUser.setFrame(new WinDictionaryUser());
                WinChooseCategory.getFrame().dispose();
            } catch (MyFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        popupDictGenerItem.addActionListener(event -> {
            try {
                setCallFromCateg(true);
                WinDictionaryGeneral.setFrame(new WinDictionaryGeneral());
                WinChooseCategory.getFrame().dispose();
            } catch (MyFileException e) {
                e.printStackTrace();
            }
        });
        popupDictUserItem.setBackground(new Color(237, 246, 229));
        popupDictGenerItem.setBackground(new Color(237, 246, 229));
        popup.add(popupDictUserItem);
        popup.add(popupDictGenerItem);
        PopupListener pl = new PopupListener();
        addMouseListener(pl);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                WinMainMenu.setFrame(new WinMainMenu());
                dispose();
            }
        });
        getContentPane().setBackground(new Color(181, 234, 234));
        setPreferredSize(new Dimension(400, 500));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static boolean getIsTraining() {
        return isTraining;
    }

    public static void setIsTraining(boolean training) {
        isTraining = training;
    }

    public static DictionaryWords getGeneralDict() {
        return WinChooseCategory.generalDict;
    }

    public static void setGeneralDict(DictionaryWords generalDict) {
        WinChooseCategory.generalDict = generalDict;
    }

    public static DictionaryWords getTestDict() {
        return testDict;
    }

    public static void setTestDict(DictionaryWords testDict) {
        WinChooseCategory.testDict = testDict;
    }

    public static boolean getCallFromCateg() {
        return callFromCateg;
    }

    public static void setCallFromCateg(boolean callFromCateg) {
        WinChooseCategory.callFromCateg = callFromCateg;
    }

    public static void setFrame(WinChooseCategory frame) {
        WinChooseCategory.frame = frame;
    }

    public static WinChooseCategory getFrame() {
        return WinChooseCategory.frame;
    }

    class PopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger())
                popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public void addRadioButton(String name, boolean isSelected) {
        JRadioButton button = new JRadioButton(name, isSelected);
        button.setBackground(new Color(237, 246, 229));
        groupRadio.add(button);
        panelRadioGroup.add(button);
        ActionListener listener = e -> {
            if (((JRadioButton) e.getSource()).getText().equals("Тренировка")) {
                if (!model.contains("мой словарь ")) {
                    model.addElement("мой словарь ");
                    listPanelCateg.revalidate();
                    scrollPane.revalidate();
                }
                setIsTraining(true);
            } else if (((JRadioButton) e.getSource()).getText().equals("Экзамен")) {
                if (model.contains("мой словарь ")) {
                    model.removeElement("мой словарь ");
                    listPanelCateg.revalidate();
                    scrollPane.revalidate();
                }
                setIsTraining(false);
            }
        };
        button.addActionListener(listener);
    }

    //по выбранным категориям в списке формируем словарь с этими категориями для заданий
    public boolean getCategories() {
        if (wordList.getSelectedValuesList().size() == 0) {
            JOptionPane.showMessageDialog(this, "Выберите категорию!", "Сообщение",
                    JOptionPane.WARNING_MESSAGE);
            return false;
        } else {
            DictionaryWords dictTest = new DictionaryWords();
            for (String value : wordList.getSelectedValuesList()) {
                for (int i = 0; i < generalListCateg.size(); i++) {
                    if (generalListCateg.get(i).getName().trim().equals(value.trim())) {
                        dictTest.addCategory(generalListCateg.get(i));
                    }
                }
            }
            //добавляем к имеющимся для тренировки категориям слова из словаря пользователя, если выбран "мой словарь"
            if (wordList.getSelectedValuesList().contains("мой словарь ")) {
                DictionaryWords dictUser = User.getCurUser().getDictionary();
                ArrayList<Category> listUserCateg = dictUser.getListCategory();
                ArrayList<Category> listTestCateg = dictTest.getListCategory();
                for (int i = 0; i < listUserCateg.size(); i++) {
                    boolean isCategFind = false;
                    Category userCateg = listUserCateg.get(i);
                    for (int j = 0; j < listTestCateg.size(); j++) {
                        Category testCateg = listTestCateg.get(j);
                        if (userCateg.getName().equals(testCateg.getName())) {
                            isCategFind = true;
                            ArrayList<Word> listUserWords = userCateg.getListWord();
                            ArrayList<Word> listTestWords = testCateg.getListWord();
                            for (int g = 0; g < listUserWords.size(); g++) {
                                boolean isWordFind = false;
                                Word userWord = listUserWords.get(g);
                                for (int k = 0; k < listTestWords.size(); k++) {
                                    Word testWord = listTestWords.get(k);
                                    if (userWord.getEngValue().equals(testWord.getEngValue().trim())) {
                                        isWordFind = true;
                                        break;
                                    }
                                }
                                if (!isWordFind) {
                                    testCateg.addWordIntoCategory(userWord);
                                }
                            }
                        }
                    }
                    if (!isCategFind) {
                        dictTest.addCategory(dictUser.getCategoryByName(userCateg.getName()));
                    }
                }
            }
            setTestDict(dictTest);
            return true;
        }
    }

}