import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLEditorKit;

class WinAddNewWordsFromFile extends JFrame {
    private JEditorPane editor;
    private static WinAddNewWordsFromFile frame;

    public WinAddNewWordsFromFile(DictionaryWords dictionary, String filename) {
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        setTitle("Добавление слов");
        getContentPane().setBackground(new Color(181, 234, 234));
        JPanel panelEnter = new JPanel();
        panelEnter.add(new JLabel("<html>Перетащите файл со словами или введите<br>вручную в редактор:</html>") {{
            setPreferredSize(new Dimension(300, 40));
            setFont(new Font("Segoe Print", Font.BOLD, 13));
        }});
        panelEnter.setBackground(new Color(237, 246, 229));
        add(panelEnter);
        layout.putConstraint(SpringLayout.WEST, panelEnter, 19,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelEnter, 20,
                SpringLayout.NORTH, this);

        editor = new AutoScrollingEditorPane();
        EditorDropTarget target = new EditorDropTarget(editor);
        JScrollPane scrlPaneEditor = new JScrollPane(editor);
        editor.setPreferredSize(new Dimension(305, 300));
        layout.putConstraint(SpringLayout.WEST, scrlPaneEditor, 19,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, scrlPaneEditor, 65,
                SpringLayout.NORTH, panelEnter);

        JEditorPane helpEditor = new JEditorPane();
        HTMLEditorKit editorKit = new HTMLEditorKit();
        helpEditor.setEditorKit(editorKit);
        helpEditor.setText("<html><font face=\"Times New Roman\" size=\"8px\" align=\"center\" >" +
                "<b>Формат ввода:</b><br>" +
                "название категории + Enter<br>" +
                "англ.слово [транскрипция] перевод + Enter<br> " +
                "англ.слово [транскрипция] перевод + Enter<br> " +
                "англ.слово [транскрипция] перевод + Enter<br> + Enter<br>" +
                "следующая категория...<br><br>" +
                "<b>Пример:</b><br>" +
                "животные<br>" +
                "squirrel [ˈskwɪrəl] белка<br>" +
                "jellyfish [ˈʤɛlɪfɪʃ] медуза<br><br>" +
                "дом<br>" +
                "brick [brɪk] кирпич<br>" +
                "stove [stəʊv] печь<br>" +
                "bench [bɛnʧ] скамья</font></html>");
        helpEditor.setEditable(false);
        helpEditor.setOpaque(true);

        add(helpEditor);

        layout.putConstraint(SpringLayout.WEST, helpEditor, 19,
                SpringLayout.EAST, scrlPaneEditor);
        layout.putConstraint(SpringLayout.NORTH, helpEditor, 65,
                SpringLayout.NORTH, panelEnter);

        JPanel panelCheck = new JPanel();
        JCheckBox editable = new JCheckBox("Редактируемый") {{
            setFont(new Font("Segoe Print", Font.BOLD, 12));
            setBackground(new Color(237, 246, 229));
        }};
        editable.setSelected(true);
        panelCheck.add(editable);
        panelCheck.setBackground(new Color(237, 246, 229));
        editable.addActionListener(evt -> editor.setEditable(editable.isSelected()));
        JCheckBox enabled = new JCheckBox("Доступный") {{
            setFont(new Font("Segoe Print", Font.BOLD, 12));
            setBackground(new Color(237, 246, 229));
        }};
        enabled.setSelected(true);
        panelCheck.add(enabled);
        enabled.addActionListener(evt -> editor.setEnabled(enabled.isSelected()));
        add(scrlPaneEditor);
        add(panelCheck);
        layout.putConstraint(SpringLayout.WEST, panelCheck, 19,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelCheck, 15,
                SpringLayout.SOUTH, scrlPaneEditor);

        //создать кнопки Добавить и Назад
        JButton addButton = new JButton("Добавить");
        addButton.setBackground(new Color(237, 246, 229));
        addButton.setFont(new Font("Segoe Print", Font.BOLD, 12));
        addButton.addActionListener(event -> {
            try {
                getText(dictionary, filename);
            } catch (BadLocationException | MyFileException e) {
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
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel);
        layout.putConstraint(SpringLayout.WEST, buttonPanel, 19,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, buttonPanel, 15,
                SpringLayout.SOUTH, panelCheck);
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
        setPreferredSize(new Dimension(650, 550));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static WinAddNewWordsFromFile getFrame() {
        return frame;
    }

    public static void setFrame(WinAddNewWordsFromFile frame) {
        WinAddNewWordsFromFile.frame = frame;
    }

    public void setEditor() {
        editor.setText("");
        editor.setBackground(Color.WHITE);
    }

    public static boolean hasLatin(String str) {
        for (int i = 0; i < str.length(); i++) {
            String character = str.valueOf(str.charAt(i));
            if (character.matches("[a-zA-Z]")) {
                return true;
            }
        }
        return false;
    }

    //получает данные, введенные в окне добавления
    //void getWord(DictionaryWords dictionary) throws MyFileException {
    void getText(DictionaryWords dictionary, String filename) throws BadLocationException, MyFileException {
        String text = editor.getText();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Редактор не заполнен!",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
        } else if (!Word.isWordValid(text)) {
            JOptionPane.showMessageDialog(this, "Слова не должны содержать цифр!",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
        } else if ((!text.contains("\n")) || (!text.contains(" "))) {
            JOptionPane.showMessageDialog(this, "Ввод не соответствует формату!",
                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
        } else {
            String[] strings;
            if (text.contains("\r\n")) {
                strings = text.split("\r\n");
            } else {
                strings = text.split("\n");
            }
            DictionaryWords dict = new DictionaryWords();
            Category categ = null;
            for (int i = 0; i < strings.length; i++) {
                String curStr = strings[i].trim().toLowerCase().replaceAll("\\s+", " ");
                boolean cyrilExist = curStr.chars().mapToObj(Character.UnicodeBlock::of).
                        anyMatch(b -> b.equals(Character.UnicodeBlock.CYRILLIC));
                boolean latinExist = hasLatin(curStr);
                if (cyrilExist && !latinExist) { //если только русские - это категория
                    if (categ != null) {
                        dict.addCategory(categ);
                    }
                    categ = new Category(curStr);

                } else if (cyrilExist && latinExist) { //если русские и английские - это слово
                    if (i == 0) { //если первая строка не категория
                        JOptionPane.showMessageDialog(this, "Ввод не соответствует формату!",
                                "Предупреждение", JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                    int pos = curStr.indexOf("[");
                    if (pos != -1) { //если есть транскрипция
                        String eng = curStr.substring(0, pos).trim().toLowerCase().
                                replaceAll("\\s+", " ");
                        boolean cyril = eng.chars().mapToObj(Character.UnicodeBlock::of).
                                anyMatch(b -> b.equals(Character.UnicodeBlock.CYRILLIC));
                        if (cyril) { //если в англ.слове содержатся русские буквы
                            JOptionPane.showMessageDialog(this, "Ввод не соответствует формату!",
                                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
                            break;
                        }
                        Word word = new Word(eng, "", "", categ.getName());
                        curStr = curStr.substring(pos); //удалили из строки англ.слово
                        pos = curStr.indexOf("]");
                        int pos2 = curStr.indexOf("[");
                        if (((pos != -1) && (pos2 == -1)) || ((pos == -1) && (pos2 != -1))) { //нет равенства скобок
                            JOptionPane.showMessageDialog(this, "Ввод не соответствует формату!",
                                    "Предупреждение", JOptionPane.WARNING_MESSAGE);
                            break;
                        }
                        if ((pos != -1) && (pos2 != -1)) {
                            //считываем первую часть транскрипции
                            String buf = curStr.substring(0, pos + 1).trim().toLowerCase().
                                    replaceAll("\\s+", " ");
                            //удалили из строки первую часть транскрипци
                            curStr = curStr.substring(pos + 1).trim().toLowerCase().
                                    replaceAll("\\s+", " ");
                            pos2 = curStr.indexOf("[");
                            pos = curStr.indexOf("]");
                            if (((pos != -1) && (pos2 == -1)) || ((pos == -1) && (pos2 != -1))) { //нет равенства скобок
                                JOptionPane.showMessageDialog(this, "Ввод не соответствует формату!",
                                        "Предупреждение", JOptionPane.WARNING_MESSAGE);
                                break;
                            }
                            if ((pos != -1) && (pos2 != -1)) {
                                buf += curStr.substring(0, pos + 1).trim().toLowerCase().
                                        replaceAll("\\s+", " ");
                                //удалили из строки вторую часть транскрипции
                                curStr = curStr.substring(pos + 1).trim().toLowerCase().
                                        replaceAll("\\s+", " ");
                            }
                            cyril = buf.chars().mapToObj(Character.UnicodeBlock::of).
                                    anyMatch(b -> b.equals(Character.UnicodeBlock.CYRILLIC));
                            if (cyril) { //если в транскрипции содержатся русские буквы
                                JOptionPane.showMessageDialog(this, "Ввод не соответствует формату!",
                                        "Предупреждение", JOptionPane.WARNING_MESSAGE);
                                break;
                            }
                            word.setRightTransc(buf);
                            boolean latin = hasLatin(curStr);

                            //curStr.valueOf(curStr.charAt(0)).equals("[") || curStr.valueOf(curStr.charAt(0)).equals("]")

                            //если в переводе содержатся латинские буквы и первый символ не является русской буквой
                            if ((latin) || !(Character.UnicodeBlock.of(curStr.charAt(0)).equals(Character.UnicodeBlock.CYRILLIC))) {
                                JOptionPane.showMessageDialog(this, "Ввод не соответствует формату!",
                                        "Предупреждение", JOptionPane.WARNING_MESSAGE);
                                break;
                            }
                            word.setRuValue(curStr);
                        }
                        categ.addWordIntoCategory(word); //добавляем слово в категорию
                    } else {
                        JOptionPane.showMessageDialog(this, "Ввод не соответствует формату!",
                                "Предупреждение", JOptionPane.WARNING_MESSAGE);
                        break;
                    }
                }
            }
            if (categ != null) {
                dict.addCategory(categ); //добавили последнюю категорию
                ArrayList<Category> listCateg = dict.getListCategory();
                for (int i = 0; i < listCateg.size(); i++) {
                    Category category = listCateg.get(i);
                    ArrayList<Word> listWords = category.getListWord();
                    for (int j = 0; j < listWords.size(); j++) {
                        DictionaryWords.addWordIntoGeneralDictionary(dictionary, category.getName(),
                                listWords.get(j), filename); //добавляем слово
                    }
                }
                setEditor();
            }
        }
    }
}