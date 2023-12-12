package integration;

import model.*;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тест общего словаря и личного словаря:
 * проверка отображения всех слов, содержащихся в файле общего словаря,
 * а также добавления новых слов и удаления имеющихся.
 */
public class TestDictionaryWords {
    /**
     * тест создания словаря (имеющихся категорий и слов), сейчас в файле есть как минимум:
     * природа
     * sea [siː] море
     *
     * животные
     * ox [ɒks] бык
     */
    @Test
    public void makeGeneralDictionaryTest() throws MyFileException {
        DictionaryWords dict = new DictionaryWords();
        String fileName = "dictionary.txt";
        //формирование словаря
        dict = DictionaryWords.makeGeneralDictionary(dict, fileName);
        List<Category> listCateg = dict.getListCategory();
        assertNotEquals(0, listCateg.size());
        //проверка 1ой считанной категории и ее слова
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

    //создание тестовой категории с двумя словами для тестов
    public static Category makeTestCategWithWords() {
        Word testWord1 = new Word("test1", "[test1]", "тест1", "тестовая");
        Word testWord2 = new Word("test2", "[test2]", "тест2", "тестовая");
        Category testCateg = new Category("тестовая");
        testCateg.addWordIntoCategory(testWord1);
        testCateg.addWordIntoCategory(testWord2);
        return testCateg;
    }

    @Test
    //тест обновления файла словаря
    public static void apdateFileDictionaryTest() throws MyFileException {
        DictionaryWords dict = new DictionaryWords();
        String fileName = "dictionary.txt";
        dict = DictionaryWords.makeGeneralDictionary(dict, fileName);
        //до добавления категории не было в словаре
        Category foundCateg = dict.getCategoryByName("тестовая");
        assertNull(foundCateg);
        Category testCateg = makeTestCategWithWords();
        List<Word> testWords = foundCateg.getListWord();
        dict.addCategory(testCateg);

        //обновление словаря
        DictionaryWords.apdateFileDictionary(dict, fileName);
        //новая категория появилась в словаре
        foundCateg = dict.getCategoryByName("тестовая");
        assertNotNull(foundCateg);
        assertEquals(testCateg.getName(), foundCateg.getName());
        //проверка добавления тестовых слов
        List<Word> foundWords = foundCateg.getListWord();
        assertEquals(testCateg.getListWord().size(), foundWords.size());
        assertNotNull(dict.getWordInDict(testWords.get(0)));
        assertNotNull(dict.getWordInDict(testWords.get(0)));

        //удаление тестовой категории из словаря
        dict.getListCategory().remove(foundCateg);
        DictionaryWords.apdateFileDictionary(dict, fileName);
    }

    @Test
    //тест добавления в общий словарь слова и обновления файла словаря
    public static void addWordIntoGeneralDictionaryTest() throws MyFileException {
        DictionaryWords dict = new DictionaryWords();
        String fileName = "dictionary.txt";
        dict = DictionaryWords.makeGeneralDictionary(dict, fileName);
        Category testCateg = makeTestCategWithWords();
        Word testWord = testCateg.getListWord().get(0);
        //проверка, что нового слова еще нет в словаре
        Word findWord = dict.getWordInDict(testWord);
        assertNull(findWord);

        //добавление слова в словарь
        DictionaryWords.addWordIntoGeneralDictionary(dict, testWord.getNameCategory(), testWord, fileName);
        //чтение словаря из файла заново
        dict = DictionaryWords.makeGeneralDictionary(dict, fileName);

        Category foundCateg = dict.getCategoryByName(testCateg.getName());
        assertNotNull(foundCateg);
        //проверка добавления тестового слова
        assertNotNull(dict.getWordInDict(testWord));

        //удаление тестовой категории из словаря
        dict.getListCategory().remove(foundCateg);
        DictionaryWords.apdateFileDictionary(dict, fileName);
    }

    @Test
    //тест добавления в словарь пользователя слова и обновления файла словаря
    public void addWordIntoUserDictionaryTest() throws MyFileException {
        User us = new User("matvey12", "fBh23k&ju", 0);
        us.setDictionary();
        User.setCurUser(us);
        DictionaryWords dict = us.getDictionary();
        Category testCateg = makeTestCategWithWords();
        Word testWord = testCateg.getListWord().get(0);
        //проверка, что нового слова еще нет в словаре
        Word findWord = dict.getWordInDict(testWord);
        assertNull(findWord);

        //добавление слова в словарь
        dict.addWordIntoUserDictionary(testWord);
        //чтение словаря из файла заново
        us.setDictionary();
        dict = us.getDictionary();

        Category foundCateg = dict.getCategoryByName(testCateg.getName());
        assertNotNull(foundCateg);
        //проверка добавления тестового слова
        assertNotNull(dict.getWordInDict(testWord));

        //удаление тестовой категории из словаря
        String fileName = us.getUsername();
        fileName += ".txt";
        dict.getListCategory().remove(foundCateg);
        DictionaryWords.apdateFileDictionary(dict, fileName);
    }

    @Test
    //тест удаления слова из словаря пользователя
    public void delWordFromUserDictionaryTest() throws MyFileException {
        User us = new User("matvey12", "fBh23k&ju", 0);
        us.setDictionary();
        User.setCurUser(us);
        DictionaryWords dict = us.getDictionary();
        Category testCateg = makeTestCategWithWords();
        Word testWord = testCateg.getListWord().get(0);
        //добавление слова в словарь
        dict.addWordIntoUserDictionary(testWord);

        //чтение словаря из файла заново
        us.setDictionary();
        dict = us.getDictionary();
        Word foundWord = dict.getWordInDict(testWord);
        assertNotNull(foundWord);

        //удаление слова из словаря
        dict.delWordFromUserDictionary(testWord);
        foundWord = dict.getWordInDict(testWord);
        assertNull(foundWord);
    }

    @Test
    //тест удаления слова из общего словаря
    public void delWordFromGeneralDictionaryTest() throws MyFileException {
        DictionaryWords dict = new DictionaryWords();
        String fileName = "dictionary.txt";
        dict = DictionaryWords.makeGeneralDictionary(dict, fileName);
        Category testCateg = makeTestCategWithWords();
        Word testWord = testCateg.getListWord().get(0);

        //добавление слова в словарь
        DictionaryWords.addWordIntoGeneralDictionary(dict, testWord.getNameCategory(), testWord, fileName);
        //чтение словаря из файла заново
        DictionaryWords dict2 = new DictionaryWords();
        dict2 = DictionaryWords.makeGeneralDictionary(dict2, fileName);
        Word foundWord = dict2.getWordInDict(testWord);
        assertNotNull(foundWord);

        //удаление слова из словаря
        dict2.delWordFromGeneralDictionary(testWord, fileName);
        foundWord = dict2.getWordInDict(testWord);
        assertNull(foundWord);
    }
}
