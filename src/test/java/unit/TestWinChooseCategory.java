package unit;

import org.junit.jupiter.api.Test;

import forms.*;
import model.*;

import javax.swing.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * тест окна Выбор категории:
 * проверка корректности отображения имеющихся в общем словаре категорий,
 * переноса названий этих категорий в список выбранных категорий по нажатию на них,
 * а также создания словаря для заданий по выбранным категориям
 */
public class TestWinChooseCategory {
    private User us;
    private WinChooseCategory winChooseCateg;

    //инициализация окна Выбор категории и текущего пользователя
    public void initWindowAndUser() throws MyFileException {
        us = new User("matvey12", "fBh23k&ju", 0);
        User.setCurUser(us);
        winChooseCateg = new WinChooseCategory();
        WinChooseCategory.setFrame(winChooseCateg);
    }

    //подсчет одинаковых категорий: в словаре и в списке на экране
    public int calcEqualCategories(JList<String> wordList, DictionaryWords genDict) {
        int k = 0;
        for (int i = 0; i < genDict.getListCategory().size(); i++) {
            String modelCateg = wordList.getModel().getElementAt(i);
            String dictCateg = genDict.getListCategory().get(i).getName();
            if (modelCateg.trim().equals(dictCateg)) {
                k++;
            }
        }
        return k;
    }

    @Test
    //тест на корректность отображения имеющихся в общем словаре категорий в списке для выбора
    public void printCategoryInListTest() throws MyFileException {
        initWindowAndUser();
        JList<String> wordList = winChooseCateg.getWordList();
        DictionaryWords genDict = WinChooseCategory.getGeneralDict();
        int k = calcEqualCategories(wordList, genDict);
        if (wordList.getModel().getElementAt(k).trim().equals("мой словарь")) {
            k++;
        }
        int expectedSize = genDict.getListCategory().size() + 1;
        assertEquals(expectedSize, k);
        //если пользователь не будет задан, категории "мой словарь" не должно быть
        User.setCurUser(null);
        winChooseCateg = new WinChooseCategory();
        wordList = winChooseCateg.getWordList();
        expectedSize = genDict.getListCategory().size();
        k = calcEqualCategories(wordList, genDict);
        assertEquals(expectedSize, k);
    }

    @Test
    //тест на перенос названий категорий в список выбранных категорий по нажатию на них
    public void moveCategoryFromListToListTest() throws MyFileException {
        initWindowAndUser();
        JList<String> wordList = winChooseCateg.getWordList();
        wordList.setSelectedIndices(new int[]{0, 1}); //выбираем "природа" и "животные"
        winChooseCateg.setWordList(wordList);
        JList<String> curCategList = winChooseCateg.getCurCategList();
        assertEquals(2, curCategList.getModel().getSize());
        assertEquals("природа", curCategList.getModel().getElementAt(0).trim());
        assertEquals("животные", curCategList.getModel().getElementAt(1).trim());
    }

    @Test
    /* тест для формирования словаря по выбранным категориям, сейчас в файле есть как минимум:
     * природа
     * sea [siː] море
     *
     * животные
     * ox [ɒks] бык
     */
    public void getCategoriesTest() throws MyFileException {
        initWindowAndUser();
        JList<String> wordList = winChooseCateg.getWordList();
        wordList.setSelectedIndices(new int[]{0, 1}); //выбираем "природа" и "животные"
        winChooseCateg.setWordList(wordList);
        winChooseCateg.getCategories();
        DictionaryWords testDict = winChooseCateg.getTestDict();
        assertNotNull(testDict);
        List<Category> listCateg = testDict.getListCategory();
        assertEquals(2, listCateg.size());
        Category categ = listCateg.get(0);
        assertEquals("природа", categ.getName());
        List<Word> listWords = categ.getListWord();
        assertEquals("sea", listWords.get(0).getEngValue());
        assertEquals("[siː]", listWords.get(0).getRightTransc());
        assertEquals("море", listWords.get(0).getRuValue());
        //проверка 2ой считанной категории и ее слова
        categ = listCateg.get(1);
        assertEquals("животные", categ.getName());
        listWords = categ.getListWord();
        assertEquals("ox", listWords.get(0).getEngValue());
        assertEquals("[ɒks]", listWords.get(0).getRightTransc());
        assertEquals("бык", listWords.get(0).getRuValue());
    }
}
