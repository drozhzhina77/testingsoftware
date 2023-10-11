import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/* Окно задания с перетаскивающимися вариантами ответа */
public class WinExerciseWithVariantsDnD extends JFrame {
    private JTextField txtFldUsAnswer;
    private JPanel panelLabels;
    private static WinExerciseWithVariantsDnD frame;

    public WinExerciseWithVariantsDnD(ExerciseWithVariants exercWithVar) {
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        setTitle("Опрос");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        getContentPane().setBackground(new Color(181, 234, 234));

        JLabel labelNum = new JLabel("Задание " + Integer.toString(WinExercise.getICurExerc() + 1)){{
            setPreferredSize(new Dimension(90, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 14));
        }};
        JPanel panelTask = new JPanel();
        panelTask.add(new JLabel(exercWithVar.getTask()) {{
            setPreferredSize(new Dimension(260, 45));
            setFont(new Font("Segoe Print", Font.BOLD, 14));
        }});
        panelTask.setBackground(new Color(237, 246, 229));
        JLabel labelWordTask = new JLabel(exercWithVar.getWordTask()) {{
            setPreferredSize(new Dimension(350, 18));
            setFont(new Font("Segoe Print", Font.BOLD, 14));
        }};

        //варианты ответа
        MouseListener listener = new WinExerciseWithVariantsDnD.DragMouseAdapter();
        panelLabels = new JPanel();
        panelLabels.setLayout(new GridLayout(0, 1));
        //panelLabels.setBackground(new Color(181, 234, 234));
        addDnDLabel(exercWithVar.getFirstVar(), listener);
        addDnDLabel(exercWithVar.getSecondVar(), listener);
        addDnDLabel(exercWithVar.getThirdVar(), listener);
        addDnDLabel(exercWithVar.getFourthVar(), listener);

        add(labelNum);
        add(panelTask);
        add(labelWordTask);
        add(panelLabels);

        layout.putConstraint(SpringLayout.NORTH, labelNum, 40,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, labelNum, 18,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelTask, 28,
                SpringLayout.NORTH, labelNum);
        layout.putConstraint(SpringLayout.WEST, panelTask, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, labelWordTask, 65,
                SpringLayout.NORTH, panelTask);
        layout.putConstraint(SpringLayout.WEST, labelWordTask, 18,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelLabels, 35,
                SpringLayout.NORTH, labelWordTask);
        layout.putConstraint(SpringLayout.WEST, panelLabels, 15,
                SpringLayout.WEST, this);

        //поле для ответа
        JPanel panelAnsw = new JPanel();
        panelAnsw.add(new JLabel("Ответ:") {{
            setPreferredSize(new Dimension(60, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 14));
        }});
        txtFldUsAnswer = new JTextField("", 20);
        txtFldUsAnswer.setFont(new Font("TimesRoman", Font.BOLD, 13));
        txtFldUsAnswer.setDragEnabled(true);
        panelAnsw.add(txtFldUsAnswer);
        panelAnsw.setBackground(new Color(237, 246, 229));

        add(panelAnsw);
        layout.putConstraint(SpringLayout.NORTH, panelAnsw, 125,
                SpringLayout.NORTH, panelLabels);
        layout.putConstraint(SpringLayout.WEST, panelAnsw, 15,
                SpringLayout.WEST, this);

        JButton buttonOk = new JButton("Далее") {
            {
                setSize(new Dimension(120, 25));
                setMaximumSize(getSize());
                setBackground(new Color(237, 246, 229));
                setFont(new Font("Segoe Print", Font.BOLD, 13));
            }
        };
        buttonOk.addActionListener(event -> {
            exercWithVar.setUserAnswer(txtFldUsAnswer.getText().trim().toLowerCase().replaceAll("\\s+", " "));
            if (!exercWithVar.getUserAnswer().equals(exercWithVar.getRightAnswer())) {
                JOptionPane.showMessageDialog(this, "Неверный ответ!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
            WinExercise win = new WinExercise();
            try {
                win.run();
            } catch (MyFileException e) {
                e.printStackTrace();
            }
            setVisible(false);
            dispose();
        });
        add(buttonOk);
        //размещаем buttonOk относительно нижнего правого угла
        layout.putConstraint(SpringLayout.EAST, buttonOk, -35,
                SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.SOUTH, buttonOk, -55,
                SpringLayout.SOUTH, this);
        setPreferredSize(new Dimension(430, 430));
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    public static WinExerciseWithVariantsDnD getFrame() {
        return frame;
    }

    public static void setFrame(WinExerciseWithVariantsDnD frame) {
        WinExerciseWithVariantsDnD.frame = frame;
    }

    private class DragMouseAdapter extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            TransferHandler handler = c.getTransferHandler();
            handler.exportAsDrag(c, e, TransferHandler.COPY);
        }
    }

    public void addDnDLabel(String text, MouseListener listener) {
        JPanel panel = new JPanel();
        panel.add(new JLabel(text) {{
            setPreferredSize(new Dimension(130, 18));
            setFont(new Font("Segoe Print", Font.BOLD, 13));
            setTransferHandler(new TransferHandler("text"));
            addMouseListener(listener);
        }});
        panel.setBackground(new Color(237, 246, 229));
        panelLabels.add(panel);
    }
}
