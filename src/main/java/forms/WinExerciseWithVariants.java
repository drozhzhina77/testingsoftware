package forms;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/* Окно задания с вариантами ответа */
public class WinExerciseWithVariants extends JFrame {
    private ButtonGroup groupRadio;
    private JPanel panelRadioGroup;
    private static WinExerciseWithVariants frame;
    private JLabel labelTask;
    private JLabel labelWordTask;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JRadioButton radioButton3;
    private JRadioButton radioButton4;
    private JButton buttonOk;
    private String message;

    public WinExerciseWithVariants(ExerciseWithVariants exercWithVar) {
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
        labelTask = new JLabel(exercWithVar.getTask()) {{
            if (exercWithVar.getType() == 3 || exercWithVar.getType() == 4) {
                setPreferredSize(new Dimension(280, 40));
                setFont(new Font("Segoe Print", Font.BOLD, 13));
            } else {
                setPreferredSize(new Dimension(250, 15));
                setFont(new Font("Segoe Print", Font.BOLD, 14));
            }
        }};
        panelTask.add(labelTask);
        panelTask.setBackground(new Color(237, 246, 229));
        labelWordTask = new JLabel(exercWithVar.getWordTask()) {{
            setPreferredSize(new Dimension(350, 15));
            setFont(new Font("Segoe Print", Font.BOLD, 14));
        }};

        //варианты ответа
        groupRadio = new ButtonGroup();
        panelRadioGroup = new JPanel();
        panelRadioGroup.setLayout(new GridLayout(0, 1));
        radioButton1 = addRadioButton(exercWithVar.getFirstVar(), false, exercWithVar);
        radioButton2 = addRadioButton(exercWithVar.getSecondVar(), false, exercWithVar);
        radioButton3 = addRadioButton(exercWithVar.getThirdVar(), false, exercWithVar);
        radioButton4 = addRadioButton(exercWithVar.getFourthVar(), false, exercWithVar);
        add(labelNum);
        add(panelTask);
        add(labelWordTask);
        add(panelRadioGroup);

        layout.putConstraint(SpringLayout.NORTH, labelNum, 40,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, labelNum, 18,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelTask, 28,
                SpringLayout.NORTH, labelNum);
        layout.putConstraint(SpringLayout.WEST, panelTask, 15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, labelWordTask, 55,
                SpringLayout.NORTH, panelTask);
        layout.putConstraint(SpringLayout.WEST, labelWordTask, 18,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelRadioGroup, 35,
                SpringLayout.NORTH, labelWordTask);
        layout.putConstraint(SpringLayout.WEST, panelRadioGroup, 15,
                SpringLayout.WEST, this);

        buttonOk = new JButton("Далее") {
            {
                setSize(new Dimension(120, 25));
                setMaximumSize(getSize());
                setBackground(new Color(237, 246, 229));
                setFont(new Font("Segoe Print", Font.BOLD, 13));
            }
        };
        buttonOk.addActionListener(event -> {
            message = "";
            if (!exercWithVar.getUserAnswer().equals(exercWithVar.getRightAnswer())) {
                message = "Неверный ответ!";
                JOptionPane.showMessageDialog(this, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
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

    public static WinExerciseWithVariants getFrame() {
        return frame;
    }

    public static void setFrame(WinExerciseWithVariants frame) {
        WinExerciseWithVariants.frame = frame;
    }

    public JLabel getLabelTask() {
        return labelTask;
    }

    public JLabel getLabelWordTask() {
        return labelWordTask;
    }

    public JRadioButton getRadioButton1() {
        return radioButton1;
    }

    public JRadioButton getRadioButton2() {
        return radioButton2;
    }

    public JRadioButton getRadioButton3() {
        return radioButton3;
    }

    public JRadioButton getRadioButton4() {
        return radioButton4;
    }

    public JButton getButtonOk() {
        return buttonOk;
    }

    public String getMessage() {
        return message;
    }

    public JRadioButton addRadioButton(String name, boolean isSelected, ExerciseWithVariants exercWithVar) {
        JRadioButton button = new JRadioButton(name, isSelected);
        button.setBackground(new Color(237, 246, 229));
        if (exercWithVar.getType() == 3) {
            button.setFont(new Font("TimesRoman", Font.BOLD, 13));
        } else {
            button.setFont(new Font("Segoe Print", Font.BOLD, 13));
        }
        groupRadio.add(button);
        panelRadioGroup.add(button);
        ActionListener listener = e -> {
            exercWithVar.setUserAnswer(((JRadioButton) e.getSource()).getText().trim().toLowerCase().replaceAll("\\s+", " "));
        };
        button.addActionListener(listener);
        return button;
    }
}
