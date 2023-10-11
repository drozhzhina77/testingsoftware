import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;

/* Окно общего словаря */
public class WinDictionaryGeneral extends JFrame {
    private static final int DEFAULT_WIDTH = 900;
    private static final int DEFAULT_HEIGHT = 800;
    private static WinDictionaryGeneral frame;
    private JTable table;
    private JCheckBoxMenuItem rowsltem;
    private JCheckBoxMenuItem columnsltem;
    private JCheckBoxMenuItem cellsltem;
    private static DictionaryWords genDict;
    private static boolean callFromGenDict;
    private JPopupMenu popup;

    public WinDictionaryGeneral() throws MyFileException {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("Общий словарь");
        WinDictionaryGeneral.setCallFromGenDict(true);
        Vector<String> columnNames = new Vector<String>();
        columnNames.add("№");
        columnNames.add("Категория");
        columnNames.add("Англ.слово");
        columnNames.add("Транскрипция");
        columnNames.add("Перевод");

        genDict = new DictionaryWords();
        genDict = DictionaryWords.makeGeneralDictionary(genDict, "dictionary.txt");
        WinChooseCategory.setGeneralDict(genDict);
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
            WinDictionaryGeneral.getFrame().dispose();
            WinMainMenu.setFrame(new WinMainMenu());
        });
        JButton dictButton = new JButton("Мой словарь");
        dictButton.setBackground(new Color(237, 246, 229));
        dictButton.setFont(new Font("Segoe Print", Font.BOLD, 12));
        dictButton.addActionListener(event -> {
            try {
                WinDictionaryUser.setFrame(new WinDictionaryUser());
                WinDictionaryGeneral.getFrame().dispose();
            } catch (MyFileException | IOException myFileException) {
                myFileException.printStackTrace();
            }
        });
        //расположить кнопки Меню и Мой словарь под таблицей
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
            //всплывающее меню
            popup = new JPopupMenu();
            JMenuItem popupDictUserItem = new JMenuItem("Добавить в мой словарь");
            popupDictUserItem.addActionListener(event -> {
                Vector<Vector<String>> rows = getInformFromSelectedRows(table);
                if (rows.size() > 0) {
                    try {
                        WinDictionaryUser.addRowToJTable(rows);
                    } catch (MyFileException myFileException) {
                        myFileException.printStackTrace();
                    }
                }
            });
            popupDictUserItem.setBackground(new Color(237, 246, 229));
            popup.add(popupDictUserItem);

            if ((User.getCurUser().getAcessLevel() == 1) &&
                    (WinChooseCategory.getGeneralDict().getListCategory().size() > 0)) {
                JMenu menuMake = new JMenu("Создать слово");
                JMenuItem menuMakeOne = new JMenuItem("По одному в конструкторе");
                JMenuItem menuMakeSev = new JMenuItem("Несколько из файла");
                menuMakeOne.addActionListener(event -> {
                    if (User.getCurUser() != null) {
                        dispose();
                        WinAddNewWord.setFrame(new WinAddNewWord(genDict, "dictionary.txt"));
                    } else {
                        JOptionPane.showMessageDialog(this, "Вход в учетную запись не осуществлен!",
                                "Предупреждение", JOptionPane.WARNING_MESSAGE);
                    }
                });
                menuMakeSev.addActionListener(event -> {
                    dispose();
                    WinAddNewWordsFromFile.setFrame(new WinAddNewWordsFromFile(genDict, "dictionary.txt"));
                });
                menuMake.add(menuMakeOne);
                menuMake.add(menuMakeSev);
                menuBar.add(menuMake);
                JMenuItem popupDictUserDelItem = new JMenuItem("Удалить из общего словаря");
                popupDictUserDelItem.addActionListener(event -> {
                    Vector<Vector<String>> rows = WinDictionaryGeneral.getInformFromSelectedRows(table);
                    if (rows.size() > 0) {
                        try {
                            WinDictionaryUser.delRowFromJTable(rows, table, true);
                        } catch (MyFileException myFileException) {
                            myFileException.printStackTrace();
                        }
                    }
                });
                popupDictUserDelItem.setBackground(new Color(237, 246, 229));
                popup.add(popupDictUserDelItem);
            }
            PopupListener pl = new PopupListener();
            addMouseListener(pl);
            table.setComponentPopupMenu(popup);
        }
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                try {
                    WinDictionaryGeneral.getFrame().dispose();
                    if (WinChooseCategory.getCallFromCateg()) {
                        WinChooseCategory.setFrame(new WinChooseCategory());
                    } else {
                        WinDictionaryUser.setFrame(new WinDictionaryUser());
                    }
                } catch (MyFileException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static boolean getCallFromGenDict() {
        return callFromGenDict;
    }

    public static void setCallFromGenDict(boolean callFromGenDict) {
        WinDictionaryGeneral.callFromGenDict = callFromGenDict;
    }

    public static WinDictionaryGeneral getFrame() {
        return frame;
    }

    public static void setFrame(WinDictionaryGeneral frame) {
        WinDictionaryGeneral.frame = frame;
    }

    private void updateCheckboxMenuItems() {
        rowsltem.setSelected(table.getRowSelectionAllowed());
        columnsltem.setSelected(table.getColumnSelectionAllowed());
        cellsltem.setSelected(table.getCellSelectionEnabled());
    }

    public static Vector<Vector<String>> getCells(Vector<Vector<String>> cells) throws MyFileException {
        ArrayList<Category> listUserCateg = genDict.getListCategory();
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

    public static String fromHTML(String str) {
        String res = str;
        int pos = str.indexOf("<p");
        if (pos != -1) {
            str = str.substring(pos + 1);
            pos = str.indexOf(">");
            if (pos != -1) {
                str = str.substring(pos + 1);
                pos = str.indexOf("<");
                if (pos != -1) {
                    str = str.substring(0, pos).trim();
                    return str;
                }
            }
        }
        return res;
    }

    public static Vector<Vector<String>> getInformFromSelectedRows(JTable entryTable) {
        Vector<Vector<String>> rows = new Vector<Vector<String>>();
        if (entryTable.getRowCount() > 0) {
            if (entryTable.getSelectedRowCount() > 0) {
                int selectedRow[] = entryTable.getSelectedRows();
                for (int i : selectedRow) {
                    Vector<String> curRow = new Vector<>();
                    curRow.add(entryTable.getValueAt(i, 0).toString());
                    curRow.add(fromHTML(entryTable.getValueAt(i, 1).toString()));
                    curRow.add(fromHTML(entryTable.getValueAt(i, 2).toString()));
                    curRow.add(fromHTML(entryTable.getValueAt(i, 3).toString()));
                    curRow.add(fromHTML(entryTable.getValueAt(i, 4).toString()));
                    rows.add(curRow);
                }
            }
        }
        return rows;
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

