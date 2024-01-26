package forms;

import model.*;
import javax.swing.*;
import java.awt.*;

/* Окно задания с открытым ответом */
public class WinExerciseWithOpenAswer extends JFrame {
    private JTextField txtFldUsAnswer;
    private static WinExerciseWithOpenAswer frame;
    private JLabel labelTask;
    private JLabel labelWordTask;
    private JButton buttonOk;
    private String message;

    public WinExerciseWithOpenAswer(Exercise exerc) {
        SpringLayout layout = new SpringLayout();
        setLayout(layout);
        setTitle("Опрос");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        getContentPane().setBackground(new Color(181, 234, 234));

        JLabel labelNum = new JLabel("Задание " + Integer.toString(WinExercise.getICurExerc() + 1)) {{
            setPreferredSize(new Dimension(90, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 14));
        }};
        JPanel panelTask = new JPanel();
        labelTask = new JLabel(exerc.getTask()) {{
            setName("labelTask");
            setPreferredSize(new Dimension(245, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 14));
        }};
        panelTask.add(labelTask);
        panelTask.setBackground(new Color(237, 246, 229));
        labelWordTask = new JLabel(exerc.getWordTask()) {{
            setName("labelWordTask");
            setPreferredSize(new Dimension(350, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 14));
        }};

        add(labelNum);
        add(panelTask);
        add(labelWordTask);

        layout.putConstraint(SpringLayout.NORTH, labelNum, 40,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, labelNum, 18,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelTask, 30,
                SpringLayout.NORTH, labelNum);
        layout.putConstraint(SpringLayout.WEST, panelTask, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, labelWordTask, 50,
                SpringLayout.NORTH, panelTask);
        layout.putConstraint(SpringLayout.WEST, labelWordTask, 18,
                SpringLayout.WEST, this);

        //поле для ответа
        JPanel panelAnsw = new JPanel();
        panelAnsw.add(new JLabel("Ответ:") {
            {
                setPreferredSize(new Dimension(60, 15));
                setFont(new Font("Segoe Print", Font.BOLD, 14));
            }
        });
        txtFldUsAnswer = new JTextField("", 20);
        txtFldUsAnswer.setName("txtFldUsAnswer");
        txtFldUsAnswer.setFont(new Font("TimesRoman", Font.BOLD, 13));
        panelAnsw.add(txtFldUsAnswer);
        panelAnsw.setBackground(new Color(237, 246, 229));

        add(panelAnsw);
        layout.putConstraint(SpringLayout.NORTH, panelAnsw, 35,
                SpringLayout.NORTH, labelWordTask);
        layout.putConstraint(SpringLayout.WEST, panelAnsw, 15,
                SpringLayout.WEST, this);

        buttonOk = new JButton("Далее") {
            {
                setName("next");
                setSize(new Dimension(120, 25));
                setMaximumSize(getSize());
                setBackground(new Color(237, 246, 229));
                setFont(new Font("Segoe Print", Font.BOLD, 13));
            }
        };
        buttonOk.addActionListener(event -> {
            message = "";
            exerc.setUserAnswer(txtFldUsAnswer.getText().trim().toLowerCase().replaceAll("\\s+", " "));
            if (!exerc.getUserAnswer().equals(exerc.getRightAnswer())) {
                message = "Неверный ответ!";
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

    public static WinExerciseWithOpenAswer getFrame() {
        return frame;
    }

    public static void setFrame(WinExerciseWithOpenAswer frame) {
        WinExerciseWithOpenAswer.frame = frame;
    }

    public JLabel getLabelTask() {
        return labelTask;
    }

    public JLabel getLabelWordTask() {
        return labelWordTask;
    }

    public JTextField getTxtFldUsAnswer() {
        return txtFldUsAnswer;
    }

    public void setTxtFldUsAnswer(JTextField txtFldUsAnswer) {
        this.txtFldUsAnswer = txtFldUsAnswer;
    }

    public JButton getButtonOk() {
        return buttonOk;
    }

    public String getMessage() {
        return message;
    }
}
