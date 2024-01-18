package nl.tudelft.sem.template.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.Valid;
import lombok.AllArgsConstructor;

@Entity
@AllArgsConstructor
public class    Book {
    @Id
    private Long id;
    private String title;
    private String description;
    private String author;
    private Long reads;
    private String series;

    @ElementCollection
    @Schema(
            name = "genres",
            example = "[\"Horror\",\"Sci-fi\",\"Thriller\"]",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("genres")
    private @Valid List<String> genres;

    /**
     * Create a book with null values.
     */
    public Book() {
    }

    /**
     * Get the id of the book.
     *
     * @return The id of the book.
     */
    @Schema(
            name = "id",
            example = "10",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("id")
    public Long getId() {
        return this.id;
    }

    /**
     * Set the id of the book.
     *
     * @param id The id of the book.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the title of the book.
     *
     * @return The title of the book.
     */
    @Schema(
            name = "title",
            example = "The Best Book",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("title")
    public String getTitle() {
        return this.title;
    }

    /**
     * Set the title of the book.
     *
     * @param title The title of the book.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the description of the book.
     *
     * @return The description of the book.
     */
    @Schema(
            name = "description",
            example = "The greatest book ever",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("description")
    public String getDescription() {
        return this.description;
    }

    /**
     * Set the description of the book.
     *
     * @param description The description of the book.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the author of the book.
     *
     * @return The author of the book.
     */
    @Schema(
            name = "author",
            example = "Bob Bobson",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("author")
    public String getAuthor() {
        return this.author;
    }

    /**
     * Set the author of the book.
     *
     * @param author The author of the book.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get the number of reads of the book.
     *
     * @return The number of reads of the book.
     */
    @Schema(
            name = "reads",
            example = "0",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("reads")
    public Long getReads() {
        return this.reads;
    }

    /**
     * Set the reads for the book.
     *
     * @param reads Number of reads.
     */
    public void setReads(Long reads) {
        this.reads = reads;
    }

    /**
     * Get the series the book is in.
     *
     * @return The series the book is in.
     */
    @Schema(
            name = "series",
            example = "The Big Trilogy",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("series")
    public String getSeries() {
        return this.series;
    }

    /**
     * Set the series of the book.
     *
     * @param series The series.
     */
    public void setSeries(String series) {
        this.series = series;
    }

    /**
     * Add a genre to the list of genres.
     *
     * @param genresItem The genre to add.
     */
    public void addGenresItem(String genresItem) {
        if (this.genres == null) {
            this.genres = new ArrayList();
        }

        this.genres.add(genresItem);
    }

    /**
     * Get the list of genres.
     *
     * @return The list of genres.
     */
    @Schema(
            name = "genres",
            example = "[\"Horror\",\"Sci-fi\",\"Thriller\"]",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("genres")
    public List<String> getGenres() {
        return this.genres;
    }

    /**
     * Set the list of genres.
     *
     * @param genres The list of genres.
     */
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    /**
     * Checks the equality of two books.
     *
     * @param o The other object to compare with.
     * @return Whether the books are equal.
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Book book = (Book) o;
            return Objects.equals(this.id, book.id) && Objects.equals(this.title, book.title)
                    && Objects.equals(this.description, book.description)
                    && Objects.equals(this.author, book.author) && Objects.equals(this.reads, book.reads)
                    && Objects.equals(this.series, book.series) && Objects.equals(this.genres, book.genres);
        } else {
            return false;
        }
    }

    /**
     * Compute the hash of the book.
     *
     * @return The hash code of the book.
     */
    public int hashCode() {
        return Objects.hash(this.id, this.title, this.description, this.author, this.reads, this.series, this.genres);
    }

    /**
     * Get the book's string representation.
     *
     * @return The book as a string.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Book {\n");
        sb.append("    id: ").append(this.toIndentedString(this.id)).append("\n");
        sb.append("    title: ").append(this.toIndentedString(this.title)).append("\n");
        sb.append("    description: ").append(this.toIndentedString(this.description)).append("\n");
        sb.append("    author: ").append(this.toIndentedString(this.author)).append("\n");
        sb.append("    reads: ").append(this.toIndentedString(this.reads)).append("\n");
        sb.append("    series: ").append(this.toIndentedString(this.series)).append("\n");
        sb.append("    genres: ").append(this.toIndentedString(this.genres)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Return the book as an indented string.
     *
     * @param o The book.
     * @return The book as an indented string.
     */
    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
