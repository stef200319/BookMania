//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

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

@Entity
public class Book {
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
    public Book() {
    }

    public Book id(Long id) {
        this.id = id;
        return this;
    }

    @Schema(
            name = "id",
            example = "10",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("id")
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book title(String title) {
        this.title = title;
        return this;
    }

    @Schema(
            name = "title",
            example = "The Best Book",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("title")
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Book description(String description) {
        this.description = description;
        return this;
    }

    @Schema(
            name = "description",
            example = "The greatest book ever",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Book author(String author) {
        this.author = author;
        return this;
    }

    @Schema(
            name = "author",
            example = "Bob Bobson",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("author")
    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Book reads(Long reads) {
        this.reads = reads;
        return this;
    }

    @Schema(
            name = "reads",
            example = "0",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("reads")
    public Long getReads() {
        return this.reads;
    }

    public void setReads(Long reads) {
        this.reads = reads;
    }

    public Book series(String series) {
        this.series = series;
        return this;
    }

    @Schema(
            name = "series",
            example = "The Big Trilogy",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("series")
    public String getSeries() {
        return this.series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Book genres(List<String> genres) {
        this.genres = genres;
        return this;
    }

    public Book addGenresItem(String genresItem) {
        if (this.genres == null) {
            this.genres = new ArrayList();
        }

        this.genres.add(genresItem);
        return this;
    }

    @Schema(
            name = "genres",
            example = "[\"Horror\",\"Sci-fi\",\"Thriller\"]",
            requiredMode = RequiredMode.NOT_REQUIRED
    )
    @JsonProperty("genres")
    public List<String> getGenres() {
        return this.genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Book book = (Book)o;
            return Objects.equals(this.id, book.id) && Objects.equals(this.title, book.title) && Objects.equals(this.description, book.description) && Objects.equals(this.author, book.author) && Objects.equals(this.reads, book.reads) && Objects.equals(this.series, book.series) && Objects.equals(this.genres, book.genres);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id, this.title, this.description, this.author, this.reads, this.series, this.genres});
    }

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

    private String toIndentedString(Object o) {
        return o == null ? "null" : o.toString().replace("\n", "\n    ");
    }
}
