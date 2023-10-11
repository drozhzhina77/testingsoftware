import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

/* Окно добавления слова */
public class WinAddNewWord extends JFrame {
    private JTextField txtCurCateg;
    private JTextField txtEngWord;
    private JTextField txtRuWord;
    private JTextField txtTranscWord;
    private static WinAddNewWord frame;
    private Border bord;
    private DefaultListModel<String> model;
    private JList<String> categList;
    private JScrollPane scrollPane;
    private JPanel listPanelCateg;

    public WinAddNewWord(DictionaryWords dictionary, String filename) {
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        setTitle("Добавление");
        getContentPane().setBackground(new Color(181, 234, 234));
        //формируем список имеющихся категорий
        ArrayList<Category> generalListCateg = new ArrayList<>(dictionary.getListCategory());

        JPanel panelChooseCateg = new JPanel();
        panelChooseCateg.add(new JLabel("<html>Выберите категорию или введите новую<br>" +
                "в поле для ввода и подтвердите:</html>") {{
            setPreferredSize(new Dimension(290, 37));
            setFont(new Font("Segoe Print", Font.BOLD, 13));
        }});
        panelChooseCateg.setBackground(new Color(237, 246, 229));
        add(panelChooseCateg);
        layout.putConstraint(SpringLayout.WEST, panelChooseCateg, 19,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelChooseCateg, 20,
                SpringLayout.NORTH, this);

        //формируем список имеющихся категорий
        model = new DefaultListModel();
        for (int i = 0; i < generalListCateg.size(); i++) {
            model.addElement(generalListCateg.get(i).getName() + " ");
        }
        categList = new JList(model);
        //создаем панель и прокрутку для списка всех категорий
        listPanelCateg = new JPanel();
        listPanelCateg.setLayout(new BoxLayout(listPanelCateg, BoxLayout.Y_AXIS));
        //wordList.setBackground(new Color(237, 246, 229));
        scrollPane = new JScrollPane(categList);
        scrollPane.setPreferredSize(new Dimension(200, 100));
        categList.setLayoutOrientation(JList.VERTICAL);
        categList.setVisibleRowCount(0);
        listPanelCateg.add(scrollPane);

        add(listPanelCateg);
        layout.putConstraint(SpringLayout.WEST, listPanelCateg, 19,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, listPanelCateg, 55,
                SpringLayout.NORTH, panelChooseCateg);
        JPanel panelCateg = new JPanel();
        panelCateg.setBackground(new Color(181, 234, 234));
        panelCateg.add(new JLabel("Категория:") {{
            setPreferredSize(new Dimension(95, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 13));
        }});
        //текстовое поле для вывода категории из списка или ввода новой категории
        txtCurCateg = new JTextField("", 20);
        txtCurCateg.setFont(new Font("TimesRoman", Font.BOLD, 13));
        txtCurCateg.addMouseListener(new MouseAdapter() { //подсказка по вводу
            @Override
            public void mouseEntered(MouseEvent mEvt) {
                txtCurCateg.setToolTipText("не содержит цифр");
            }
        });
        txtCurCateg.getDocument().addDocumentListener((txtFldDocumentListener) e -> {
            String str = txtCurCateg.getText();
            boolean isWordValid = Word.isWordValid(str);
            if (!isWordValid) {
                txtCurCateg.setBorder(BorderFactory.createLineBorder(Color.decode("#ff0000")));
            } else {
                txtCurCateg.setBorder(bord);
            }
        });
        categList.addListSelectionListener(event -> {
            int ind = categList.getSelectedIndex();
            if (ind != -1) {
                txtCurCateg.setText(model.get(ind));
            } else {
                txtCurCateg.setText("");
            }
        });
        panelCateg.add(txtCurCateg);
        JButton addButton = new JButton("+") {
            {
                setSize(new Dimension(20, 10));
                setMaximumSize(getSize());
                setBackground(new Color(237, 246, 229));
                setFont(new Font("TimesRoman", Font.BOLD, 12));
            }
        };
        addButton.addActionListener(event -> {
            String categ = txtCurCateg.getText().trim().toLowerCase().replaceAll("\\s+", " ");
            boolean isCategValid = Word.isWordValid(categ);
            if (categ.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Поле категории не заполнено!",
                        "Предупреждение", JOptionPane.WARNING_MESSAGE);
            } else if (!isCategValid) {
                JOptionPane.showMessageDialog(this, "Некорректный ввод категории!",
                        "Предупреждение", JOptionPane.WARNING_MESSAGE);
            } else {
                categ += " ";
                if (!model.contains(categ)) {
                    model.addElement(categ);
                    listPanelCateg.revalidate();
                    scrollPane.revalidate();
                }
                txtCurCateg.setText("");
            }
        });
        panelCateg.add(addButton);
        add(panelCateg);
        layout.putConstraint(SpringLayout.WEST, panelCateg, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelCateg, 120,
                SpringLayout.NORTH, listPanelCateg);

        JPanel panelWord = new JPanel();
        panelWord.add(new JLabel("Введите слово для добавления:") {
            {
                setBackground(new Color(237, 246, 229));
                setPreferredSize(new Dimension(200, 15));
                setFont(new Font("Segoe Print", Font.BOLD, 13));
            }
        });
        panelWord.setBackground(new Color(237, 246, 229));
        add(panelWord);
        layout.putConstraint(SpringLayout.WEST, panelWord, 19,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelWord, 45,
                SpringLayout.NORTH, panelCateg);

        JPanel panelEng = new JPanel();
        panelEng.setBackground(new Color(181, 234, 234));
        panelEng.add(new JLabel("Англ.слово:") {
            {
                setPreferredSize(new Dimension(95, 15));
                setFont(new Font("Segoe Print", Font.BOLD, 13));
            }
        });
        txtEngWord = new JTextField("", 20);
        txtEngWord.setFont(new Font("TimesRoman", Font.BOLD, 13));
        txtEngWord.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent mEvt) { //подсказка по вводу
                txtEngWord.setToolTipText("не содержит цифр");
            }
        });
        bord = txtEngWord.getBorder();

        txtEngWord.getDocument().addDocumentListener((txtFldDocumentListener) e -> {
            String str = txtEngWord.getText();
            boolean isWordValid = Word.isWordValid(str);
            if (!isWordValid)
                txtEngWord.setBorder(BorderFactory.createLineBorder(Color.decode("#ff0000")));
            else {
                txtEngWord.setBorder(bord);
            }
        });
        panelEng.add(txtEngWord);
        add(panelEng);
        layout.putConstraint(SpringLayout.WEST, panelEng, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelEng, 30,
                SpringLayout.NORTH, panelWord);

        JPanel panelTransc = new JPanel();
        panelTransc.setBackground(new Color(181, 234, 234));
        panelTransc.add(new JLabel("Транскрипция:") {
            {
                setPreferredSize(new Dimension(95, 15));
                setFont(new Font("Segoe Print", Font.BOLD, 13));
            }
        });
        txtTranscWord = new JTextField("", 20);
        txtTranscWord.setFont(new Font("TimesRoman", Font.BOLD, 13));
        txtTranscWord.addMouseListener(new MouseAdapter() { //подсказка по вводу
            @Override
            public void mouseEntered(MouseEvent mEvt) {
                txtTranscWord.setToolTipText("<html>не содержит цифр,<br>начинается и заканчивается" +
                        " квадратными скобками<br>скобки образуют правильную<br>последовательность</html>");
            }
        });
        txtTranscWord.getDocument().addDocumentListener((txtFldDocumentListener) e -> {
            String str = txtTranscWord.getText();
            boolean isWordValid = Word.isTranscValid(str);
            if (!isWordValid) {
                txtTranscWord.setBorder(BorderFactory.createLineBorder(Color.decode("#ff0000")));
            } else {
                txtTranscWord.setBorder(bord);
            }
        });
        panelTransc.add(txtTranscWord);
        add(panelTransc);
        layout.putConstraint(SpringLayout.WEST, panelTransc, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelTransc, 30,
                SpringLayout.NORTH, panelEng);

        JPanel panelRu = new JPanel();
        panelRu.setBackground(new Color(181, 234, 234));
        panelRu.add(new JLabel("Перевод:") {
            {
                setPreferredSize(new Dimension(95, 15));
                setFont(new Font("Segoe Print", Font.BOLD, 13));
            }
        });
        txtRuWord = new JTextField("", 20);
        txtRuWord.setFont(new Font("TimesRoman", Font.BOLD, 13));
        txtRuWord.addMouseListener(new MouseAdapter() { //подсказка по вводу
            @Override
            public void mouseEntered(MouseEvent mEvt) {
                txtRuWord.setToolTipText("не содержит цифр");
            }
        });
        txtRuWord.getDocument().addDocumentListener((txtFldDocumentListener) e -> {
            String str = txtRuWord.getText();
            boolean isWordValid = Word.isWordValid(str);
            if (!isWordValid) {
                txtRuWord.setBorder(BorderFactory.createLineBorder(Color.decode("#ff0000")));
            } else {
                txtRuWord.setBorder(bord);
            }
        });
        panelRu.add(txtRuWord);
        add(panelRu);
        layout.putConstraint(SpringLayout.WEST, panelRu, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelRu, 30,
                SpringLayout.NORTH, panelTransc);
        setWord();

        //создать кнопки Добавить и Назад
        JButton okButton = new JButton("Добавить");
        okButton.setBackground(new Color(237, 246, 229));
        okButton.setFont(new Font("Segoe Print", Font.BOLD, 12));
        okButton.addActionListener(event -> {
            try {
                getWord(dictionary, filename);
            } catch (MyFileException e) {
                e.printStackTrace();
            }
        });
        JButton cancelButton = new JButton("Назад");
        cancelButton.setBackground(new Color(237, 246, 229));
        cancelButton.setFont(new Font("Segoe Print", Font.BOLD, 12));
        cancelButton.addActionListener(event -> {
            try {
                if (WinDictionaryGeneral.getCallFromGenDict()) {
                    WinDictionaryGeneral.setFrame(new WinDictionaryGeneral());
                } else {
                    WinDictionaryUser.setFrame(new WinDictionaryUser());
                }
                dispose();
            } catch (MyFileException | IOException e) {
                e.printStackTrace();
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(181, 234, 234));
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel);
        layout.putConstraint(SpringLayout.WEST, buttonPanel, 120,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, buttonPanel, 40,
                SpringLayout.NORTH, panelRu);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                try {
                    if (WinDictionaryGeneral.getCallFromGenDict()) {
                        WinDictionaryGeneral.setFrame(new WinDictionaryGeneral());
                    } else {
                        WinDictionaryUser.setFrame(new WinDictionaryUser());
                    }
                    dispose();
                } catch (MyFileException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
        setPreferredSize(new Dimension(415, 500));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static void setFrame(WinAddNewWord frame) {
        WinAddNewWord.frame = frame;
    }

    public static WinAddNewWord getFrame() {
        return WinAddNewWord.frame;
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

    //устанавливает окно добавления в исходное состояние
    public void setWord() {
        txtEngWord.setText("");
        txtRuWord.setText("");
        txtTranscWord.setText("");
        txtCurCateg.setBorder(bord);
        txtEngWord.setBorder(bord);
        txtRuWord.setBorder(bord);
        txtTranscWord.setBorder(bord);
    }

    //получает данные, введенные в окне добавления
    void getWord(DictionaryWords dictionary, String filename) throws MyFileException {
        //берём значения из текстовых полей, удаляя пробелы в начале и в конце и повторяющиеся и переводя в нижний регистр
        String categ = txtCurCateg.getText().trim().toLowerCase().replaceAll("\\s+", " ");
        String eng = txtEngWord.getText().trim().toLowerCase().replaceAll("\\s+", " ");
        String transc = txtTranscWord.getText().trim().toLowerCase().replaceAll("\\s+", " ");
        String ru = txtRuWord.getText().trim().toLowerCase().replaceAll("\\s+", " ");
        boolean isCategValid = Word.isWordValid(categ);
        boolean isEngValid = Word.isWordValid(eng);
        boolean isTranscValid = Word.isTranscValid(transc);
        boolean isRuValid = Word.isWordValid(ru);
        Word newWord = new Word(eng, transc, ru, categ); //создаем новое слово
        Category curCateg = dictionary.getCategoryByName(categ);
        boolean isWordExist = false;
        if (curCateg != null) {
            if (DictionaryWords.seekWordInCategory(curCateg, newWord)) {
                isWordExist = true;
            }
        }
        if (categ.isEmpty() || eng.isEmpty() || transc.isEmpty() || ru.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Не все поля заполнены!",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
        } else if (!isCategValid) {
            JOptionPane.showMessageDialog(this, "Некорректный ввод категории!",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
        } else if (!isEngValid) {
            JOptionPane.showMessageDialog(this, "Некорректный ввод англ.слова!",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
        } else if (isWordExist) {
            JOptionPane.showMessageDialog(this, "Такое слово уже существует!",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
        } else if (!isTranscValid) {
            JOptionPane.showMessageDialog(this, "Некорректный ввод транскрипции!",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
        } else if (!isRuValid) {
            JOptionPane.showMessageDialog(this, "Некорректный ввод перевода!",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Слово добавлено в словарь!",
                    "Сообщение", JOptionPane.INFORMATION_MESSAGE);
            DictionaryWords.addWordIntoGeneralDictionary(dictionary, categ, newWord, filename); //добавляем слово
            dispose();
            WinAddNewWord.setFrame(new WinAddNewWord(dictionary, filename));
        }
    }
}