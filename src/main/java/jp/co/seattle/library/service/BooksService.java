package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍リストを取得する
     *
     * @return 書籍リスト
     */
    public List<BookInfo> getBookList() {

        // TODO 取得したい情報を取得するようにSQLを修正
        //取得したい情報は、書籍ID、書籍名と著者名、出版社名、出版日、サムネイル画像
        //書籍IDはデータ上にIDで管理しているから必要になる
        List<BookInfo> getedBookList = jdbcTemplate.query(
                "SELECT id ,title ,author ,publisher, publish_date, thumbnail_url FROM books ORDER BY title ASC",
                new BookInfoRowMapper());

        return getedBookList;
    }

    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return bookDetailsInfo 書籍詳細情報
     */
    public BookDetailsInfo getBookInfo(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "SELECT * FROM books where id ="
                + bookId;

        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return bookDetailsInfo;
    }

    /**
     * 書籍IDを取得する
     * 
     * 
     * @return bookId 書籍ID
     */
    public int getBookId() {
        String sql = "SELECT id FROM books ORDER BY id DESC LIMIT 1;";
        int bookId = jdbcTemplate.queryForObject(sql, int.class);
        return bookId;
    }

    /**
     * 書籍を登録する
     *
     * @param bookInfo 書籍情報
     */
    public void registBook(BookDetailsInfo bookInfo) {

        String sql = "INSERT INTO books (title, author,publisher,publish_date,thumbnail_name,thumbnail_url,description,isbn,reg_date,upd_date) VALUES ('"
                + bookInfo.getTitle() + "','" + bookInfo.getAuthor() + "','" + bookInfo.getPublisher() + "','"
                + bookInfo.getPublishDate() + "','"
                + bookInfo.getThumbnailName() + "','"
                + bookInfo.getThumbnailUrl() + "','"
                + bookInfo.getDescription() + "','"
                + bookInfo.getIsbn() + "',"
                + "sysdate(),"
                + "sysdate())";

        jdbcTemplate.update(sql);

    }

    /**
     * 書籍を削除する
     * @param bookId 書籍ID
     */
    public void deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE id = " + bookId + ";";
        jdbcTemplate.update(sql);
    }

    /**
     * 書籍情報を更新する
     * 
     * @param bookInfo 書籍情報
     */
    public void editBook(BookDetailsInfo bookInfo) {
        String sql = "UPDATE books SET title = '" + bookInfo.getTitle() +
                "',author = '" + bookInfo.getAuthor() +
                "',publisher = '" + bookInfo.getPublisher() +
                "', publish_date = '" + bookInfo.getPublishDate() +
                "', thumbnail_name = '" + bookInfo.getThumbnailName() +
                "', thumbnail_url = '" + bookInfo.getThumbnailUrl() +
                "', description = '" + bookInfo.getDescription() +
                "', isbn = '" + bookInfo.getIsbn() +
                "', upd_date = sysdate() WHERE id = " + bookInfo.getBookId();

        jdbcTemplate.update(sql);

    }
    
    


}
