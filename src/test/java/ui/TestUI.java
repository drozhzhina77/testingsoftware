package ui;

import forms.*;
import model.*;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class TestUI {
    @Test
    public void winMainMenuUITest() {
        WinMainMenu mainMenu = new WinMainMenu();
        FrameFixture frame = new FrameFixture(mainMenu);
        frame.button("start").click();
        frame.button("dictionary").click();
        frame.button("results").click();
        frame.button("exit").text();
        frame.cleanUp();
    }

    @Test
    public void winAuthorizationUITest() {
        WinAuthorization winAuthorization = new WinAuthorization();
        FrameFixture frame = new FrameFixture(winAuthorization);
        frame.label("labelUsername").requireText("Логин:");
        frame.textBox("username").requireEditable();
        frame.textBox("username").enterText("testusername");
        frame.textBox("username").deleteText();

        frame.label("labelPass").requireText("Пароль:");
        frame.textBox("password").requireEditable();
        frame.textBox("password").enterText("testpassword");
        frame.textBox("password").deleteText();

        frame.button("reg").click();
        frame.button("ok").click();
        frame.button("cancel").click();
        frame.cleanUp();
    }

    @Test
    public void winRegistrationUITest() {
        WinRegistration winRegistration = new WinRegistration();
        FrameFixture frame = new FrameFixture(winRegistration);
        frame.label("labelUsername").requireText("Логин:");
        frame.textBox("username").requireEditable();
        frame.textBox("username").enterText("testusername");
        frame.textBox("username").deleteText();

        frame.label("labelPass").requireText("Пароль:");
        frame.textBox("password").requireEditable();
        frame.textBox("password").enterText("testpassword");
        frame.textBox("password").deleteText();

        frame.label("labelFIO").requireText("ФИО:");
        frame.textBox("name").requireEditable();
        frame.textBox("name").enterText("testname");
        frame.textBox("name").deleteText();

        frame.label("labelGroup").requireText("Группа:");
        frame.textBox("group").requireEditable();
        frame.textBox("group").enterText("testgroup");
        frame.textBox("group").deleteText();

        frame.label("labelCourse").requireText("Курс:");
        frame.textBox("course").requireEditable();
        frame.textBox("course").enterText("testcourse");
        frame.textBox("course").deleteText();

        frame.button("menu").click();
        frame.button("ok").click();
        frame.button("cancel").click();
        frame.cleanUp();
    }

    @Test
    public void winResultsGeneralUITest() throws MyFileException {
        WinResultsGeneral winResultsGeneral = new WinResultsGeneral();
        FrameFixture frame = new FrameFixture(winResultsGeneral);
        frame.table("tableResGen").requireNoSelection();
        frame.button("menu").click();
        frame.cleanUp();
    }

    @Test
    public void winDictionaryGeneralUITest() throws MyFileException {
        WinDictionaryGeneral winDictionaryGeneral = new WinDictionaryGeneral();
        FrameFixture frame = new FrameFixture(winDictionaryGeneral);
        frame.table("tableDictGen").requireNoSelection();
        frame.button("menu").click();
        frame.button("myDictionary").click();
        frame.cleanUp();
    }

    @Test
    public void winDictionaryUserUITest() throws MyFileException, IOException {
        WinDictionaryUser winDictionaryUser = new WinDictionaryUser();
        FrameFixture frame = new FrameFixture(winDictionaryUser);
        frame.table("tableDictUser").requireNoSelection();
        frame.button("menu").click();
        frame.button("generalDictionary").click();
        frame.cleanUp();
    }

    @Test
    public void winChooseCategoryUITest() throws MyFileException {
        WinChooseCategory winChooseCategory = new WinChooseCategory();
        FrameFixture frame = new FrameFixture(winChooseCategory);
        frame.radioButton("Экзамен").click();
        frame.radioButton("Тренировка").click();
        frame.list("wordList").selectItems(0, 1, 2);
        frame.button("menu").click();
        frame.button("next").click();
        frame.cleanUp();
    }

    public ArrayList<Exercise> getListExercises(boolean isTraining) throws MyFileException {
        //создадим пользователя и зададим 2 категории: природа и животные
        User us = new User("petr_752", "pwAsfg+64", 0);
        User.setCurUser(us);
        WinChooseCategory winChooseCategory = new WinChooseCategory();
        DictionaryWords dict = new DictionaryWords();
        ArrayList<Category> genListCateg = winChooseCategory.getGeneralListCateg();
        ArrayList<Category> genSublistCateg = new ArrayList<>();
        genSublistCateg.add(genListCateg.get(0));
        genSublistCateg.add(genListCateg.get(1));
        dict.setListCategory(genSublistCateg);
        WinChooseCategory.setTestDict(dict);
        WinChooseCategory.setIsTraining(isTraining);
        //создадим для теста список с заданиями по этим категориям
        ArrayList<Exercise> listExerc = ExerciseWithVariants.makeExercises();
        WinExercise.setListExercises(listExerc);
        return listExerc;
    }

    public ExerciseWithVariants getExerciseWithVar() throws MyFileException {
        ArrayList<Exercise> listExerc = getListExercises(false);
        ExerciseWithVariants exercWithVar = null;
        for (int i = 0; i < listExerc.size(); i++) {
            Exercise exerc = listExerc.get(i);
            if (exerc instanceof ExerciseWithVariants) {
                exercWithVar = (ExerciseWithVariants) exerc;
                i = listExerc.size();
            }
        }
        return exercWithVar;
    }

    public Exercise getExerciseWithOpenAnswer() throws MyFileException {
        ArrayList<Exercise> listExerc = getListExercises(false);
        Exercise exercWithOpenAnswer = null;
        for (int i = 0; i < listExerc.size(); i++) {
            Exercise exerc = listExerc.get(i);
            if (!(exerc instanceof ExerciseWithVariants)) {
                exercWithOpenAnswer = exerc;
                i = listExerc.size();
            }
        }
        return exercWithOpenAnswer;
    }

    @Test
    public void winExerciseWithVariantsUITest() throws MyFileException {
        ExerciseWithVariants exercWithVar = getExerciseWithVar();
        WinExerciseWithVariants winExercWithVar = new WinExerciseWithVariants(exercWithVar);
        FrameFixture frame = new FrameFixture(winExercWithVar);
        frame.label("labelTask").requireText(exercWithVar.getTask());
        frame.label("labelWordTask").requireText(exercWithVar.getWordTask());
        frame.radioButton(exercWithVar.getFirstVar()).click();
        frame.radioButton(exercWithVar.getSecondVar()).click();
        frame.radioButton(exercWithVar.getThirdVar()).click();
        frame.radioButton(exercWithVar.getFourthVar()).click();
        frame.button("next").click();
        frame.cleanUp();
    }

    @Test
    public void winExerciseWithOpenAnswerUITest() throws MyFileException {
        Exercise exercWithOpenAnswer = getExerciseWithOpenAnswer();
        WinExerciseWithOpenAswer winExercWithOpenAswer = new WinExerciseWithOpenAswer(exercWithOpenAnswer);
        FrameFixture frame = new FrameFixture(winExercWithOpenAswer);
        frame.label("labelTask").requireText(exercWithOpenAnswer.getTask());
        frame.label("labelWordTask").requireText(exercWithOpenAnswer.getWordTask());
        frame.textBox("txtFldUsAnswer").requireEditable();
        frame.textBox("txtFldUsAnswer").enterText("testuseranswer");
        frame.button("next").click();
        frame.cleanUp();
    }

    @Test
    public void winResultsUserTrainUITest() throws MyFileException {
        ArrayList<Exercise> listExerc = getListExercises(true);
        ArrayList<Word> newWords = new ArrayList<>();
        ArrayList<Word> learnedWords = new ArrayList<>();
        //добавляем 2 слова в список новых слов и 1 в список выученных
        newWords.add(listExerc.get(0).getWord());
        newWords.add(listExerc.get(1).getWord());
        learnedWords.add(listExerc.get(2).getWord());

        WinResultsUserTrain winResultsUserTrain = new WinResultsUserTrain(newWords, learnedWords);
        FrameFixture frame = new FrameFixture(winResultsUserTrain);
        frame.list("learnedList").selectItem(0);
        frame.list("newList").selectItem(0);
        frame.button("menu").click();
        frame.button("newCheck").click();
        frame.cleanUp();
    }

    @Test
    public void winResultsUserExamUITest() throws MyFileException {
        ArrayList<Exercise> listExerc = getListExercises(false);
        //запишем в качестве ответов пользователя все правильные ответы, кроме ответа на первый вопрос
        for (int i = 0; i < listExerc.size(); i++) {
            Exercise curExerc = listExerc.get(i);
            if (i == 0) { //неверный ответ на первый вопрос
                curExerc.setUserAnswer(curExerc.getRightAnswer() + "test");
            } else { //верный ответ на любой другой вопрос
                curExerc.setUserAnswer(curExerc.getRightAnswer());
            }
        }
        WinExercise.setListExercises(listExerc);
        WinResultsUserExam winResultsUserExam = new WinResultsUserExam();
        FrameFixture frame = new FrameFixture(winResultsUserExam);
        frame.table("tableResults").selectRows(1);
        frame.button("menu").click();
        frame.cleanUp();
    }
}