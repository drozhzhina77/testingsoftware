package forms;

import model.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

/* Окно результатов пользователя после экзамена */
public class WinResultsUserExam extends JFrame {
    private static final int DEFAULT_WIDTH = 920;
    private static final int DEFAULT_HEIGHT = 800;
    private static WinResultsUserExam frame;
    private JTable table;
    private JCheckBoxMenuItem rowsltem;
    private JCheckBoxMenuItem columnsltem;
    private JCheckBoxMenuItem cellsltem;

    public WinResultsUserExam() {
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setTitle("Результаты экзамена " + User.getCurUser().getUsername());
        Vector<String> columnNames = new Vector<>();
        columnNames.add("№");
        columnNames.add("Условие");
        columnNames.add("Задание");
        columnNames.add("Правильный");
        columnNames.add("Ваш ответ");
        Vector<Vector<String>> cells = new Vector<>();
        getCells(cells);
        TableModel model = new DefaultTableModel(cells, columnNames);
        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new TableRenderer());

        table.setRowHeight(85);

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
        table.getColumnModel().getColumn(1).setMinWidth(150);
        table.getColumnModel().getColumn(1).setMaxWidth(200);
        //table.getColumnModel().getColumn(4).setMinWidth(150);
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

        int rightAnsw = User.getCurUser().calcMark(WinExercise.getListExercises(), 0);
        double mark = ExerciseWithVariants.getMaxCountExerc();
        mark = User.getCurUser().convertBallsToMark(rightAnsw / mark);

        JPanel panelMark = new JPanel();
        panelMark.add(new JLabel("<html>Вы выполнили верно " + rightAnsw + " из " + WinExercise.getListExercises().size()
                + "<br>и получаете оценку " + (int)mark + "</html>") {{
            setPreferredSize(new Dimension(230, 40));
            setFont(new Font("Segoe Print", Font.BOLD, 12));
        }});
        panelMark.setBackground(new Color(181, 234, 234));
        JButton menuButton = new JButton("Меню");
        menuButton.setBackground(new Color(237, 246, 229));
        menuButton.setFont(new Font("Segoe Print", Font.BOLD, 12));
        menuButton.addActionListener(event -> {
            dispose();
            WinMainMenu.setFrame(new WinMainMenu());
        });

        //расположить кнопку Меню и надпись с оценкой под таблицей
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(181, 234, 234));
        buttonPanel.add(panelMark);
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

    public static WinResultsUserExam getFrame() {
        return frame;
    }

    public static void setFrame(WinResultsUserExam frame) {
        WinResultsUserExam.frame = frame;
    }

    private void updateCheckboxMenuItems() {
        rowsltem.setSelected(table.getRowSelectionAllowed());
        columnsltem.setSelected(table.getColumnSelectionAllowed());
        cellsltem.setSelected(table.getCellSelectionEnabled());
    }

    public static Vector<Vector<String>> getCells(Vector<Vector<String>> cells) {
        for (int i = 0; i < WinExercise.getListExercises().size(); i++) {
            Exercise nextExerc = WinExercise.getListExercises().get(i);
            Vector<String> curCell = new Vector<>();
            curCell.add(Integer.toString(i + 1));
            curCell.add(nextExerc.getTask());
            curCell.add(WinResultsGeneral.toHTML(nextExerc.getWordTask(), "center"));
            curCell.add(WinResultsGeneral.toHTML(nextExerc.getRightAnswer(), "center"));
            curCell.add(WinResultsGeneral.toHTML(nextExerc.getUserAnswer(), "center"));
            cells.add(curCell);
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
                //setBackground(new Color(181, 234, 234));
            }
            if (column == 0) {
                c.setBackground(new Color(237, 246, 229));
            }
            if (column == 4) {
                for (int i = 0; i < WinExercise.getListExercises().size(); i++) {
                    if (row == i) {
                        if (!WinExercise.getListExercises().get(i).getUserAnswer().
                                equals(WinExercise.getListExercises().get(i).getRightAnswer())) {
                            c.setBackground(new Color(255, 43, 43));
                            c.setForeground(table.getForeground());
                        }
                    }
                }
            }
            return c;
        }
    }
}
