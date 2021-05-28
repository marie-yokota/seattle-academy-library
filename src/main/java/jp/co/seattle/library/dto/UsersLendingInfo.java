package jp.co.seattle.library.dto;

import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class UsersLendingInfo {
    
    private int lendingId;

    private int bookId;

    private int usersId;

    private int lendingStatus;

    private String lendingRegDate;

    private String lendingUpdDate;

    private String title;

    private String author;

    private String publisher;

    private String publishDate;

    private String thumbnail;

    public UsersLendingInfo() {

    }

    public UsersLendingInfo(int lendingId, int bookId, int usersId, int lendingStatus, String lendingRegDate,
            String lendingUpdDate, String title, String author, String publisher,
            String thumbnailUrl, String publishDate, String thumbnail) {
        this.lendingId = lendingId;
        this.usersId = usersId;
        this.lendingStatus = lendingStatus;
        this.lendingRegDate = lendingRegDate;
        this.lendingUpdDate = lendingUpdDate;
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.thumbnail = thumbnail;

    }

}
