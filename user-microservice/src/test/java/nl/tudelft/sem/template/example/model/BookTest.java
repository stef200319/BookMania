package nl.tudelft.sem.template.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class BookTest {

    @Test
    void testEquals() {
        Book book1 = new Book(1L, "Title", "Description", "Author", 100L, "Series", new ArrayList<>());
        Book book2 = new Book(1L, "Title", "Description", "Author", 100L, "Series", new ArrayList<>());
        Book book3 = new Book(2L, "Title", "Description", "Author", 100L, "Series", new ArrayList<>());

        assertEquals(book1, book2);
        assertNotEquals(book1, book3);
    }

    @Test
    void testHashCode() {
        Book book1 = new Book(1L, "Title", "Description", "Author", 100L, "Series", new ArrayList<>());
        Book book2 = new Book(1L, "Title", "Description", "Author", 100L, "Series", new ArrayList<>());
        Book book3 = new Book(2L, "Title", "Description", "Author", 100L, "Series", new ArrayList<>());

        assertEquals(book1.hashCode(), book2.hashCode());
        assertNotEquals(book1.hashCode(), book3.hashCode());
    }

    @Test
    void testToString() {
        Book book = new Book(1L, "Title", "Description", "Author", 100L, "Series", new ArrayList<>());

        String expected = "class Book {\n" +
                "    id: 1\n" +
                "    title: Title\n" +
                "    description: Description\n" +
                "    author: Author\n" +
                "    reads: 100\n" +
                "    series: Series\n" +
                "    genres: []\n" +
                "}";
        assertEquals(expected, book.toString());
    }

    @Test
    void testGettersAndSetters() {
        Book book = new Book();

        book.setId(1L);
        assertEquals(1L, book.getId());

        book.setTitle("Title");
        assertEquals("Title", book.getTitle());

        book.setDescription("Description");
        assertEquals("Description", book.getDescription());

        book.setAuthor("Author");
        assertEquals("Author", book.getAuthor());

        book.setReads(100L);
        assertEquals(100L, book.getReads());

        book.setSeries("Series");
        assertEquals("Series", book.getSeries());

        List<String> genres = new ArrayList<>();
        genres.add("Horror");
        genres.add("Sci-fi");
        book.setGenres(genres);
        assertEquals(genres, book.getGenres());
    }

    @Test
    void testAddGenresItem() {
        Book book = new Book();

        book.addGenresItem("Horror");
        book.addGenresItem("Sci-fi");

        List<String> expectedGenres = new ArrayList<>();
        expectedGenres.add("Horror");
        expectedGenres.add("Sci-fi");

        assertEquals(expectedGenres, book.getGenres());
    }

    @Test
    void testAddGenresItemEmpty() {
        Book book = new Book();

        book.addGenresItem("Horror");
        book.addGenresItem("Sci-fi");

        List<String> expectedGenres = new ArrayList<>();
        expectedGenres.add("Horror");
        expectedGenres.add("Sci-fi");

        assertEquals(expectedGenres, book.getGenres());
    }
}