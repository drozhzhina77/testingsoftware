package unit;

import model.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static integration.TestDictionaryWords.makeTestCategWithWords;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест общего словаря и личного словаря:
 * проверка поиска категории по наименованию в словаре,
 * поиска слова в словаре, поиска слова в категории
 */
public class TestDictionaryWords {
    @Test
    //тест поиска категории по наименованию в словаре
    public void getCategoryByNameTest() throws MyFileException {
        //для теста считываются категории из общего словаря
        DictionaryWords dict = new DictionaryWords();
        dict = DictionaryWords.makeGeneralDictionary(dict, "dictionary.txt");
        //до добавления категории не было в словаре
        Category findCateg = dict.getCategoryByName("тестовая");
        assertNull(findCateg);
        //создание новой категории
        Category testCateg = new Category("тестовая");
        dict.addCategory(testCateg);
        //новая категория появилась в словаре
        findCateg = dict.getCategoryByName("тестовая");
        assertNotNull(findCateg);
    }

    @Test
    //тест поиска слова в словаре
    public void getWordInDictTest() throws MyFileException {
        //для теста считываются категории из общего словаря
        DictionaryWords dict = new DictionaryWords();
        dict = DictionaryWords.makeGeneralDictionary(dict, "dictionary.txt");
        Category testCateg = makeTestCategWithWords();
        List<Word> testWords = testCateg.getListWord();
        //до добавления категории слова не было в словаре
        Word foundWord = dict.getWordInDict(testWords.get(0));
        assertNull(foundWord);
        dict.addCategory(testCateg);
        //новое слово появилось в словаре
        foundWord = dict.getWordInDict(testWords.get(0));
        assertNotNull(foundWord);
    }

    @Test
    //тест поиска нового слова в категории
    public void seekWordInCategoryTest() {
        Category testCateg = makeTestCategWithWords();
        Word testWord = testCateg.getListWord().get(0);
        //поиск существующего слова в категории
        assertTrue(DictionaryWords.seekWordInCategory(testCateg, testWord));
        //поиск несуществующего слова
        Word testWord2 = new Word("test50", "[test50]", "тест50", testCateg.getName());
        assertFalse(DictionaryWords.seekWordInCategory(testCateg, testWord2));
    }
}
