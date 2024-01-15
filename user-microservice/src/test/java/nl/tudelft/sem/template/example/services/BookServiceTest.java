package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.BookRepository;
import nl.tudelft.sem.template.example.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookServiceTest {
    private BookRepository bookRepository;

    private BookService bookService;

    @BeforeEach
    public void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);
        bookService = new BookService(bookRepository);
    }

    private Book createSampleBook() {
        Book book = new Book();
        List<String> bookGenres = new ArrayList<>();

        book.setId(1L);
        book.setTitle("Existing Book");
        book.setDescription("Existing Description");
        book.setAuthor("Existing Author");
        book.setReads(2L);
        book.setSeries("Existing Series");
        bookGenres.add("Existing1");
        bookGenres.add("Existing2");
        book.setGenres(bookGenres);

        return book;
    }

    private Book createSampleBookManually(Long Id, String title, String description, String author, Long reads, String series, String genre1, String genre2) {
        Book book = new Book();
        List<String> bookGenres = new ArrayList<>();

        book.setId(Id);
        book.setTitle(title);
        book.setDescription(description);
        book.setAuthor(author);
        book.setReads(reads);
        book.setSeries(series);
        bookGenres.add(genre1);
        bookGenres.add(genre2);
        book.setGenres(bookGenres);

        return book;
    }

    @Test
    public void testUpdateBook() {
        Book existingBook = createSampleBook();

        Book updatedBook = new Book();
        List<String> updatedBookGenres = new ArrayList<>();

        updatedBook.setId(1L);
        updatedBook.setTitle("Updated Book");
        updatedBook.setDescription("Updated Description");
        updatedBook.setAuthor("Updated Author");
        updatedBook.setReads(3L);
        updatedBook.setSeries("Updated Series");
        updatedBookGenres.add("Updated1");
        updatedBookGenres.add("Updated2");
        updatedBook.setGenres(updatedBookGenres);

        Mockito.when(bookRepository.getOne(1L)).thenReturn(existingBook);

        bookService.updateBook(updatedBook, 1L);

        assertEquals(updatedBook.getAuthor(), existingBook.getAuthor());
        assertEquals(updatedBook.getTitle(), existingBook.getTitle());
        assertEquals(updatedBook.getDescription(), existingBook.getDescription());
        assertEquals(updatedBook.getReads(), existingBook.getReads());
        assertEquals(updatedBook.getSeries(), existingBook.getSeries());
        assertEquals(updatedBook.getGenres(), existingBook.getGenres());

        Mockito.verify(bookRepository, Mockito.times(1)).getOne(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).saveAndFlush(existingBook);
    }

    @Test
    public void testReadBook() {
        Book book = createSampleBook();

        Mockito.when(bookRepository.getOne(1L)).thenReturn(book);

        bookService.readBook(1L);

        assertEquals(book.getReads(), 2L + 1);

        Mockito.verify(bookRepository, Mockito.times(1)).getOne(1L);
        Mockito.verify(bookRepository, Mockito.times(1)).saveAndFlush(book);
    }

    @Test
    public void testGetBooksValid() {
        Book book1 = new Book();
        Book book2 = new Book();
        Book book3 = new Book();

        List<String> validIds = Arrays.asList("1", "2", "3");

        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.existsById(2L)).thenReturn(true);
        Mockito.when(bookRepository.existsById(3L)).thenReturn(true);

        Mockito.when(bookRepository.getOne(1L)).thenReturn(book1);
        Mockito.when(bookRepository.getOne(2L)).thenReturn(book2);
        Mockito.when(bookRepository.getOne(3L)).thenReturn(book3);

        List<Book> resultBooks = bookService.getBooks(validIds);

        assertEquals(3, resultBooks.size());
        assertTrue(resultBooks.contains(book1));
        assertTrue(resultBooks.contains(book2));
        assertTrue(resultBooks.contains(book3));

        Mockito.verify(bookRepository, Mockito.times(3)).existsById(Mockito.any());
        Mockito.verify(bookRepository, Mockito.times(3)).getOne(Mockito.any());
    }

    @Test
    public void testGetBooksInvalidWithException() {
        Book book1 = new Book();
//        Book book2 = new Book();
        Book book3 = new Book();

        List<String> validIds = Arrays.asList("1", "a", "3");

        Mockito.when(bookRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookRepository.existsById(3L)).thenReturn(true);

        Mockito.when(bookRepository.getOne(1L)).thenReturn(book1);
        Mockito.when(bookRepository.getOne(3L)).thenReturn(book3);

        List<Book> resultBooks = bookService.getBooks(validIds);

        assertEquals(2, resultBooks.size());
        assertTrue(resultBooks.contains(book1));
        assertTrue(resultBooks.contains(book3));

        Mockito.verify(bookRepository, Mockito.times(2)).existsById(Mockito.any());
        Mockito.verify(bookRepository, Mockito.times(2)).getOne(Mockito.any());
    }

    @Test
    public void testGetBooksInvalidWithoutException() {
        Book book1 = new Book();
        Book book2 = new Book();
        Book book3 = new Book();

        List<String> validIds = Arrays.asList("1", "2", "3");

        Mockito.when(bookRepository.existsById(1L)).thenReturn(false);
        Mockito.when(bookRepository.existsById(2L)).thenReturn(true);
        Mockito.when(bookRepository.existsById(3L)).thenReturn(false);

        Mockito.when(bookRepository.getOne(1L)).thenReturn(book1);
        Mockito.when(bookRepository.getOne(2L)).thenReturn(book2);
        Mockito.when(bookRepository.getOne(3L)).thenReturn(book3);

        List<Book> resultBooks = bookService.getBooks(validIds);

        assertEquals(1, resultBooks.size());
        assertTrue(resultBooks.contains(book2));

        Mockito.verify(bookRepository, Mockito.times(3)).existsById(Mockito.any());
        Mockito.verify(bookRepository, Mockito.times(1)).getOne(2L);
    }

    @Test
    public void testCreateBook() {
        Book bookToCreate = createSampleBook();

        Mockito.when(bookRepository.saveAndFlush(bookToCreate)).thenReturn(bookToCreate);

        Book createdBook = bookService.createBook(bookToCreate);

        assertEquals(bookToCreate, createdBook);

        Mockito.verify(bookRepository, Mockito.times(1)).saveAndFlush(bookToCreate);
    }

    @Test
    public void testGetBook() {
        Book book = createSampleBook();

        Mockito.when(bookRepository.getOne(book.getId())).thenReturn(book);

        Book returnedBook = bookService.getBook(book.getId());

        assertEquals(book, returnedBook);

        Mockito.verify(bookRepository, Mockito.times(1)).getOne(book.getId());
    }

    @Test
    public void testDeleteBook() {
        Book book = createSampleBook();

        bookService.deleteBook(book.getId());

        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(book.getId());
    }

    @Test
    public void testFindBookSortByReadCount() {
        Book exampleBook = new Book();
        exampleBook.setAuthor("author");
        exampleBook.setGenres(Collections.singletonList("genre"));
        exampleBook.setTitle("title");
        exampleBook.setDescription("description");
        exampleBook.setSeries("series");

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnoreNullValues();

        Example<Book> example = Example.of(exampleBook, matcher);

        Book book1 = createSampleBookManually(1L, "title1", "description1", "author1", 2L, "series1", "genre1a", "genre1b");
        Book book2 = createSampleBookManually(2L, "title2", "description2", "author2", 3L, "series2", "genre2a", "genre2b");
        Book book3 = createSampleBookManually(3L, "title3", "description3", "author3", 4L, "series3", "genre3a", "genre3b");

        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(book1);
        expectedBooks.add(book2);
        expectedBooks.add(book3);

        Mockito.when(bookRepository.findAll(example)).thenReturn(expectedBooks);

        List<Book> resultBooks = bookService.findBook("author", "genre", "title", "description", "series", "read_count");

        assertEquals(book3, resultBooks.get(0));
        assertEquals(book2, resultBooks.get(1));
        assertEquals(book1, resultBooks.get(2));

        Mockito.verify(bookRepository, Mockito.times(1)).findAll(example);
    }

    @Test
    public void testFindBookSortByAlphabetical() {
        Book exampleBook = new Book();
        exampleBook.setAuthor("author");
        exampleBook.setGenres(Collections.singletonList("genre"));
        exampleBook.setTitle("title");
        exampleBook.setDescription("description");
        exampleBook.setSeries("series");

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnoreNullValues();

        Example<Book> example = Example.of(exampleBook, matcher);

        Book book1 = createSampleBookManually(1L, "ctitle1", "description1", "author1", 2L, "series1", "genre1a", "genre1b");
        Book book2 = createSampleBookManually(2L, "atitle2", "description2", "author2", 3L, "series2", "genre2a", "genre2b");
        Book book3 = createSampleBookManually(3L, "btitle3", "description3", "author3", 4L, "series3", "genre3a", "genre3b");

        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(book1);
        expectedBooks.add(book2);
        expectedBooks.add(book3);

        Mockito.when(bookRepository.findAll(example)).thenReturn(expectedBooks);

        List<Book> resultBooks = bookService.findBook("author", "genre", "title", "description", "series", "alphabetical");

        assertEquals(book2, resultBooks.get(0));
        assertEquals(book3, resultBooks.get(1));
        assertEquals(book1, resultBooks.get(2));

        Mockito.verify(bookRepository, Mockito.times(1)).findAll(example);
    }

    @Test
    public void testFindBookSortByReverseReadCount() {
        Book exampleBook = new Book();
        exampleBook.setAuthor("author");
        exampleBook.setGenres(Collections.singletonList("genre"));
        exampleBook.setTitle("title");
        exampleBook.setDescription("description");
        exampleBook.setSeries("series");

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnoreNullValues();

        Example<Book> example = Example.of(exampleBook, matcher);

        Book book1 = createSampleBookManually(1L, "ctitle1", "description1", "author1", 2L, "series1", "genre1a", "genre1b");
        Book book2 = createSampleBookManually(2L, "atitle2", "description2", "author2", 3L, "series2", "genre2a", "genre2b");
        Book book3 = createSampleBookManually(3L, "btitle3", "description3", "author3", 4L, "series3", "genre3a", "genre3b");

        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(book1);
        expectedBooks.add(book2);
        expectedBooks.add(book3);

        Mockito.when(bookRepository.findAll(example)).thenReturn(expectedBooks);

        List<Book> resultBooks = bookService.findBook("author", "genre", "title", "description", "series", "read_count_reversed");

        assertEquals(book1, resultBooks.get(0));
        assertEquals(book2, resultBooks.get(1));
        assertEquals(book3, resultBooks.get(2));

        Mockito.verify(bookRepository, Mockito.times(1)).findAll(example);
    }

    @Test
    public void testFindBookSortByReversedAlphabetical() {
        Book exampleBook = new Book();
        exampleBook.setAuthor("author");
        exampleBook.setGenres(Collections.singletonList("genre"));
        exampleBook.setTitle("title");
        exampleBook.setDescription("description");
        exampleBook.setSeries("series");

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase()
                .withIgnoreNullValues();

        Example<Book> example = Example.of(exampleBook, matcher);

        Book book1 = createSampleBookManually(1L, "ctitle1", "description1", "author1", 2L, "series1", "genre1a", "genre1b");
        Book book2 = createSampleBookManually(2L, "atitle2", "description2", "author2", 3L, "series2", "genre2a", "genre2b");
        Book book3 = createSampleBookManually(3L, "btitle3", "description3", "author3", 4L, "series3", "genre3a", "genre3b");

        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(book1);
        expectedBooks.add(book2);
        expectedBooks.add(book3);

        Mockito.when(bookRepository.findAll(example)).thenReturn(expectedBooks);

        List<Book> resultBooks = bookService.findBook("author", "genre", "title", "description", "series", "alphabetical_reversed");

        assertEquals(book1, resultBooks.get(0));
        assertEquals(book3, resultBooks.get(1));
        assertEquals(book2, resultBooks.get(2));

        Mockito.verify(bookRepository, Mockito.times(1)).findAll(example);
    }
}