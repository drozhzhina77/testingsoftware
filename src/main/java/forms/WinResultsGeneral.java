package forms;

import model.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.*;
import javax.swing.text.TableView;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;
import java.util.Vector;

/* Окно общих результатов */
public class WinResultsGeneral extends JFrame {
    private static final int DEFAULT_WIDTH = 950;
    private static final int DEFAULT_HEIGHT = 800;
    private static WinResultsGeneral frame;
    private JTable table;
    private JCheckBoxMenuItem rowsltem;
    private JCheckBoxMenuItem columnsltem;
    private JCheckBoxMenuItem cellsltem;

    public WinResultsGeneral() throws MyFileException {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("Результаты");
        //getContentPane().setBackground(new Color(181, 234, 234));
        Vector<String> columnNames = new Vector<>();
        columnNames.add("№");
        columnNames.add("ФИО");
        columnNames.add("Логин");
        columnNames.add("Группа");
        columnNames.add("Курс");
        columnNames.add("Категории");
        columnNames.add("Оценка");
        Vector<Vector<String>> cells = new Vector<>();
        getCells(cells);
        TableModel model = new DefaultTableModel(cells, columnNames);
        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new TableRenderer());
        table.setRowHeight(100);

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
        table.getColumnModel().getColumn(0).setMinWidth(20);
        table.getColumnModel().getColumn(0).setMaxWidth(20);
        table.getColumnModel().getColumn(3).setMinWidth(32);
        table.getColumnModel().getColumn(4).setMinWidth(20);
        table.getColumnModel().getColumn(5).setMinWidth(80);
        table.getColumnModel().getColumn(1).setMinWidth(130);
        table.getColumnModel().getColumn(1).setMaxWidth(200);
        //updateRowHeights();
        //update2();
        table.repaint();

        TableRowSorter<TableModel> sorter = new TableRowSorter(model);
        table.setRowSorter(sorter);
        JPanel panel = new JPanel();
        panel.add(new JScrollPane(table));
        panel.setBackground(new Color(181, 234, 234));
        ColumnListener cl = new ColumnListener(){
            @Override
            public void columnMoved(int oldLocation, int newLocation) {
            }

            @Override
            public void columnResized(int column, int newWidth) {
                updateRowHeights(column, newWidth, table);
            }
        };
        table.getColumnModel().addColumnModelListener(cl);
        table.getTableHeader().addMouseListener(cl);
        TableColumn c = table.getColumnModel().getColumn(2);
        updateRowHeights(5, c.getWidth(), table);
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
            dispose();
            WinMainMenu.setFrame(new WinMainMenu());
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(181, 234, 234));
        buttonPanel.add(menuButton);
        add(panel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                WinMainMenu.setFrame(new WinMainMenu());
            }
        });
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static WinResultsGeneral getFrame() {
        return frame;
    }

    public static void setFrame(WinResultsGeneral frame) {
        WinResultsGeneral.frame = frame;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    private void updateCheckboxMenuItems() {
        rowsltem.setSelected(table.getRowSelectionAllowed());
        columnsltem.setSelected(table.getColumnSelectionAllowed());
        cellsltem.setSelected(table.getCellSelectionEnabled());
    }

    public static void updateRowHeights(int column, int width, JTable table){
        for (int row = 0; row < table.getRowCount(); row++) {
            int rowHeight = table.getRowHeight();
            Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
            Dimension d = comp.getPreferredSize();
            comp.setSize(new Dimension(width, d.height));
            d = comp.getPreferredSize();
            rowHeight = Math.max(rowHeight, d.height);
            table.setRowHeight(row, rowHeight);
        }
    }

    abstract static class ColumnListener extends MouseAdapter implements TableColumnModelListener {
        private int oldIndex = -1;
        private int newIndex = -1;
        private boolean dragging = false;

        private boolean resizing = false;
        private int resizingColumn = -1;
        private int oldWidth = -1;

        @Override
        public void mousePressed(MouseEvent e) {
            // capture start of resize
            if(e.getSource() instanceof JTableHeader) {
                JTableHeader header = (JTableHeader)e.getSource();
                TableColumn tc = header.getResizingColumn();
                if(tc != null) {
                    resizing = true;
                    JTable table = header.getTable();
                    resizingColumn = table.convertColumnIndexToView( tc.getModelIndex());
                    oldWidth = tc.getPreferredWidth();
                } else {
                    resizingColumn = -1;
                    oldWidth = -1;
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // column moved
            if(dragging && oldIndex != newIndex) {
                columnMoved(oldIndex, newIndex);
            }
            dragging = false;
            oldIndex = -1;
            newIndex = -1;

            // column resized
            if(resizing) {
                if(e.getSource() instanceof JTableHeader) {
                    JTableHeader header = (JTableHeader)e.getSource();
                    TableColumn tc = header.getColumnModel().getColumn(resizingColumn);
                    if(tc != null) {
                        int newWidth = tc.getPreferredWidth();
                        if(newWidth != oldWidth) {
                            columnResized(resizingColumn, newWidth);
                        }
                    }
                }
            }
            resizing = false;
            resizingColumn = -1;
            oldWidth = -1;
        }

        @Override
        public void columnAdded(TableColumnModelEvent e) {
        }

        @Override
        public void columnRemoved(TableColumnModelEvent e) {
        }

        @Override
        public void columnMoved(TableColumnModelEvent e) {
            dragging = true;
            if(oldIndex == -1){
                oldIndex = e.getFromIndex();
            }
            newIndex = e.getToIndex();
        }

        @Override
        public void columnMarginChanged(ChangeEvent e) {
        }

        @Override
        public void columnSelectionChanged(ListSelectionEvent e) {
        }

        public abstract void columnMoved(int oldLocation, int newLocation);
        public abstract void columnResized(int column, int newWidth);
    }

    public static String toHTML(String str, String align) {
        return  "<html>" +
                "<head>" +
                "<style>" +
                ".perenos-hyphens {" +
                "-moz-hyphens: auto;" +
                "-webkit-hyphens: auto;" +
                "-ms-hyphens: auto;" +
                "}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<p class=\"perenos-hyphens\" align=\""+ align +"\">" + str + "</p>" +
                "</body>" +
                "</html>";
    }

    public static Vector<Vector<String>> getCells(Vector<Vector<String>> cells) throws MyFileException {
        if (User.getListUsersResults().size() > 0) {
            for (int i = 0; i < User.getListUsersResults().size(); i++) {
                Results res = User.getListUsersResults().get(i);
                Vector<String> curCell = new Vector<>();
                curCell.add(Integer.toString(i + 1));
                Student stud = (Student) res.getUser();
                curCell.add(toHTML(stud.getName(), "center"));
                curCell.add(toHTML(stud.getUsername(), "center"));
                curCell.add(toHTML(stud.getGroup(), "center"));
                curCell.add(toHTML((String) stud.getCourse(), "center"));
                String str = "";
                for (int j = 0; j < res.getNamesCateg().size(); j++) {
                    str += res.getNamesCateg().get(j) + "<br>";
                }
                curCell.add(toHTML(str, "center"));
                curCell.add(toHTML(String.valueOf(res.getMark()),"center"));
                cells.add(curCell);
            }
        }
        return cells;
    }

    public class TableRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            //JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
            Component comp = super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

            JLabel c = (JLabel) comp;
            c.setFont(new Font("TimesRoman", Font.BOLD, 12));

            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
                //setBackground(new Color(181, 234, 234));
            }
            if (column == 0) {
                c.setBackground(new Color(237, 246, 229));
            }
            if (column == 0 || column == 3 || column == 4 || column == 6) {
                c.setHorizontalAlignment(SwingConstants.CENTER);
            }
            c.setVerticalAlignment(SwingConstants.CENTER);
            return c;
        }
    }

    public Dimension calcSize(String text) {
        TextArea l = new TextArea(text);
        return l.getPreferredSize();
    }

    private void updateRowHeights() {
        for (int row = 0; row < table.getRowCount(); row++) {
            int rowHeight = table.getRowHeight();
            for (int column = 0; column < table.getColumnCount(); column++) {
                Component comp = table.prepareRenderer(table.getCellRenderer(row, column), row, column);
                Dimension dim = new Dimension(calcSize(table.getValueAt(row, column).toString()));
                //rowHeight = Math.max(rowHeight, dim.height/*comp.getPreferredSize().height*/);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }
            table.setRowHeight(row, rowHeight);
        }
    }
}
