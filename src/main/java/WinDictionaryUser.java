import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import static java.nio.file.StandardWatchEventKinds.*;

/* Окно словаря пользователя */
public class WinDictionaryUser extends JFrame {
    private static final int DEFAULT_WIDTH = 900;
    private static final int DEFAULT_HEIGHT = 800;
    private static WinDictionaryUser frame;
    private static JTable table;
    private JCheckBoxMenuItem rowsltem;
    private JCheckBoxMenuItem columnsltem;
    private JCheckBoxMenuItem cellsltem;
    private JPopupMenu popup;

    public WinDictionaryUser() throws MyFileException, IOException {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLayout(new BorderLayout());
        WinDictionaryGeneral.setCallFromGenDict(false);
        if (User.getCurUser() != null) {
            setTitle("Словарь " + User.getCurUser().getUsername());
            User.getCurUser().setDictionary();
        } else {
            setTitle("Словарь");
        }
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("№");
        columnNames.add("Категория");
        columnNames.add("Англ.слово");
        columnNames.add("Транскрипция");
        columnNames.add("Перевод");

        Vector<Vector<String>> cells = new Vector<Vector<String>>();
        getCells(cells);
        TableModel model = new DefaultTableModel(cells, columnNames);

        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new TableRenderer());
        table.setRowHeight(50);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(237, 246, 229));
        header.setFont(new Font("Segoe Print", Font.BOLD, 12));

        // Определение минимального и максимального размеров столбцов
        Enumeration<TableColumn> e = table.getColumnModel().getColumns();
        while (e.hasMoreElements()) {
            TableColumn column = (TableColumn) e.nextElement();
            column.setMinWidth(50);
            column.setMaxWidth(200);
        }
        table.getColumnModel().getColumn(0).setMinWidth(30);
        table.getColumnModel().getColumn(0).setMaxWidth(30);
        table.getColumnModel().getColumn(4).setMinWidth(130);
        TableRowSorter<TableModel> sorter = new TableRowSorter(model);
        table.setRowSorter(sorter);

        //sorter.setSortable(IMAGE_COLUMN, false);
        JPanel panel = new JPanel();
        panel.add(new JScrollPane(table));
        panel.setBackground(new Color(181, 234, 234));

        // создать меню
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(241, 243, 243));
        setJMenuBar(menuBar);
        JMenu selectionMenu = new JMenu("Выделение");
        menuBar.add(selectionMenu);
        rowsltem = new JCheckBoxMenuItem("Строки");
        columnsltem = new JCheckBoxMenuItem("Столбцы");
        cellsltem = new JCheckBoxMenuItem("Ячейки");
        rowsltem.setSelected(table.getRowSelectionAllowed());
        columnsltem.setSelected(table.getColumnSelectionAllowed());
        cellsltem.setSelected(table.getCellSelectionEnabled());
        rowsltem.addActionListener(event -> {
            table.clearSelection();
            table.setRowSelectionAllowed(rowsltem.isSelected());
            updateCheckboxMenuItems();
        });
        selectionMenu.add(rowsltem);
        columnsltem.addActionListener(event -> {
            table.clearSelection();
            table.setColumnSelectionAllowed(columnsltem.isSelected());
            updateCheckboxMenuItems();
        });
        selectionMenu.add(columnsltem);
        cellsltem.addActionListener(event -> {
            table.clearSelection();
            table.setCellSelectionEnabled(cellsltem.isSelected());
            updateCheckboxMenuItems();
        });
        selectionMenu.add(cellsltem);

        JButton menuButton = new JButton("Меню");
        menuButton.setBackground(new Color(237, 246, 229));
        menuButton.setFont(new Font("Segoe Print", Font.BOLD, 12));
        menuButton.addActionListener(event -> {
            WinDictionaryUser.getFrame().dispose();
            WinMainMenu.setFrame(new WinMainMenu());
        });
        JButton dictButton = new JButton("Общий словарь");
        dictButton.setBackground(new Color(237, 246, 229));
        dictButton.setFont(new Font("Segoe Print", Font.BOLD, 12));
        dictButton.addActionListener(event -> {
            try {
                WinDictionaryUser.getFrame().dispose();
                WinDictionaryGeneral.setFrame(new WinDictionaryGeneral());
            } catch (MyFileException myFileException) {
                myFileException.printStackTrace();
            }
        });
        //расположить кнопки Меню и Общий словарь под таблицей
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(181, 234, 234));
        if (WinChooseCategory.getCallFromCateg()) {
            add(panel, BorderLayout.NORTH);
        } else {
            buttonPanel.add(menuButton);
            buttonPanel.add(dictButton);
            add(panel, BorderLayout.NORTH);
            add(buttonPanel, BorderLayout.SOUTH);
        }
        if (User.getCurUser() != null) {
            JMenu menuMake = new JMenu("Создать слово");
            JMenuItem menuMakeOne = new JMenuItem("По одному в конструкторе");
            JMenuItem menuMakeSev = new JMenuItem("Несколько из файла");
            User us = User.getCurUser();
            menuMakeOne.addActionListener(event -> {
                dispose();
                WinAddNewWord.setFrame(new WinAddNewWord(us.getDictionary(), us.getUsername() + ".txt"));
            });
            menuMakeSev.addActionListener(event -> {
                dispose();
                WinAddNewWordsFromFile.setFrame(new WinAddNewWordsFromFile(us.getDictionary(), us.getUsername() + ".txt"));
            });
            menuMake.add(menuMakeOne);
            menuMake.add(menuMakeSev);
            menuBar.add(menuMake);
            if (User.getCurUser().getDictionary().getListCategory().size() > 0) {
                //всплывающее меню
                popup = new JPopupMenu();
                popup.setBackground(new Color(237, 246, 229));
                JMenuItem popupDictUserItem = new JMenuItem("Удалить из словаря");
                popupDictUserItem.addActionListener(event -> {
                    Vector<Vector<String>> rows = WinDictionaryGeneral.getInformFromSelectedRows(table);
                    if (rows.size() > 0) {
                        try {
                            WinDictionaryUser.delRowFromJTable(rows, table, false);
                        } catch (MyFileException myFileException) {
                            myFileException.printStackTrace();
                        }
                    }
                });
                popupDictUserItem.setBackground(new Color(237, 246, 229));
                popup.add(popupDictUserItem);
                PopupListener pl = new PopupListener();
                addMouseListener(pl);
                table.setComponentPopupMenu(popup);
            }
        }
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                WinDictionaryUser.getFrame().dispose();
                if (WinChooseCategory.getCallFromCateg()) {
                    try {
                        WinChooseCategory.setFrame(new WinChooseCategory());
                    } catch (MyFileException e) {
                        e.printStackTrace();
                    }
                } else {
                    WinMainMenu.setFrame(new WinMainMenu());
                }
            }
        });
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static WinDictionaryUser getFrame() {
        return frame;
    }

    public static void setFrame(WinDictionaryUser frame) {
        WinDictionaryUser.frame = frame;
    }

    private void updateCheckboxMenuItems() {
        rowsltem.setSelected(table.getRowSelectionAllowed());
        columnsltem.setSelected(table.getColumnSelectionAllowed());
        cellsltem.setSelected(table.getCellSelectionEnabled());
    }

    public static Vector<Vector<String>> getCells(Vector<Vector<String>> cells) throws MyFileException {
        if (User.getCurUser() != null) {
            //User.getCurUser().setDictionary();
            DictionaryWords dictUser = User.getCurUser().getDictionary();
            ArrayList<Category> listUserCateg = dictUser.getListCategory();
            int count = 1;
            for (int k = 0; k < listUserCateg.size(); k++) {
                ArrayList<Word> listUserWords = listUserCateg.get(k).getListWord();
                for (int i = 0; i < listUserWords.size(); i++) {
                    Vector<String> curCell = new Vector<>();
                    curCell.add(Integer.toString(count));
                    curCell.add(WinResultsGeneral.toHTML(listUserWords.get(i).getNameCategory(), "center"));
                    curCell.add(WinResultsGeneral.toHTML(listUserWords.get(i).getEngValue(), "center"));
                    curCell.add(WinResultsGeneral.toHTML(listUserWords.get(i).getRightTransc(), "center"));
                    curCell.add(WinResultsGeneral.toHTML(listUserWords.get(i).getRuValue(), "center"));
                    cells.add(curCell);
                    count++;
                }
            }
        }
        return cells;
    }

    public class TableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
            c.setFont(new Font("TimesRoman", Font.BOLD, 12));
            if (column == 0 || column == 2 || column == 3 || column == 4) {
                c.setHorizontalAlignment(SwingConstants.CENTER);
            }
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            if (column == 1) {
                c.setBackground(new Color(237, 246, 229));
            }
            return c;
        }
    }

    public static void addRowToJTable(Vector<Vector<String>> dataRows) throws MyFileException {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (Vector<String> dataRow : dataRows) {
            DictionaryWords dict = User.getCurUser().getDictionary();
            Word rowWord = new Word(); //создаем слово из выбранной строки таблицы общего словаря
            rowWord.setNameCategory(dataRow.get(1).trim().toLowerCase().replaceAll("\\s+", " "));
            rowWord.setEngValue(dataRow.get(2).trim().toLowerCase().replaceAll("\\s+", " "));
            rowWord.setRightTransc(dataRow.get(3).trim().toLowerCase().replaceAll("\\s+", " "));
            rowWord.setRuValue(dataRow.get(4).trim().toLowerCase().replaceAll("\\s+", " "));
            Word word = dict.getWordInDict(rowWord); //ищем это слово в словаре пользователя
            //если его еще нет, добавляем новую строку в таблице словаря пользователя и сохраняем в файл пользователя
            if (word == null) {
                int ind = model.getRowCount() + 1;
                dataRow.set(0, Integer.toString(ind));
                model.addRow(dataRow);
                dict.addWordIntoUserDictionary(rowWord);
            }
        }
    }

    public static void delRowFromJTable(Vector<Vector<String>> dataRows, JTable table, boolean isGeneral) throws MyFileException {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < dataRows.size(); i++) {
            Vector<String> dataRow = dataRows.get(i);
            DictionaryWords dict;
            if (isGeneral) {
                dict = WinChooseCategory.getGeneralDict();
            } else {
                dict = User.getCurUser().getDictionary();
            }
            Word rowWord = new Word(); //создаем слово из выбранной строки таблицы словаря
            rowWord.setNameCategory(dataRow.get(1).trim().toLowerCase().replaceAll("\\s+", " "));
            rowWord.setEngValue(dataRow.get(2).trim().toLowerCase().replaceAll("\\s+", " "));
            rowWord.setRightTransc(dataRow.get(3).trim().toLowerCase().replaceAll("\\s+", " "));
            rowWord.setRuValue(dataRow.get(4).trim().toLowerCase().replaceAll("\\s+", " "));
            Word word = dict.getWordInDict(rowWord); //ищем это слово в словаре пользователя
            if (word != null) {
                for (int j = 0; j < model.getRowCount(); j++) { //удаление слова из модели
                    String eng = WinDictionaryGeneral.fromHTML(model.getValueAt(j, 2).toString());
                    eng.trim().toLowerCase().replaceAll("\\s+", " ");
                    String transc = WinDictionaryGeneral.fromHTML(model.getValueAt(j, 3).toString());
                    transc.trim().toLowerCase().replaceAll("\\s+", " ");
                    String ru = WinDictionaryGeneral.fromHTML(model.getValueAt(j, 4).toString());
                    ru.trim().toLowerCase().replaceAll("\\s+", " ");
                    if (rowWord.getEngValue().equals(eng) && (rowWord.getRightTransc().equals(transc)) &&
                            (rowWord.getRuValue().equals(ru))) {
                        model.removeRow(j);
                    }
                }
                dataRows.remove(i);
                if (isGeneral) {
                    dict.delWordFromGeneralDictionary(rowWord, "dictionary.txt");
                    WinChooseCategory.setGeneralDict(dict);
                } else {
                    dict.delWordFromUserDictionary(rowWord);
                }
                i--;
                for (int j = 0; j < model.getRowCount(); j++) { //пересчет индексов
                    model.setValueAt(Integer.toString(j + 1), j, 0);
                }
            }
        }
    }

    class PopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popup.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }
}

