package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 書籍詳細情報格納DTO
 *
 */
@Configuration
@Data
public class BookDetailsInfo {

    private int bookId;

    private String title = null;

    private String author = null;

    private String publisher = null;

    private String publishDate = null;

    private String thumbnailUrl;

    private String thumbnailName;

    private String description;

    private String isbn;

    public BookDetailsInfo() {

    }

    public BookDetailsInfo(int bookId, String title, String author, String publisher,
            String thumbnailUrl, String publishDate, String thumbnailName, String description, String isbn) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.thumbnailUrl = thumbnailUrl;
        this.thumbnailName = thumbnailName;
        this.description = description;
        this.isbn = isbn;

    }

}