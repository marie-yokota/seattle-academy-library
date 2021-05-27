package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.UsersLendingInfo;
import jp.co.seattle.library.rowMapper.UsersLendingInfoRowMapper;

@Service
public class UsersLendingHistoryService {
    final static Logger logger = LoggerFactory.getLogger(UsersLendingHistoryService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /**
     * usersIdに紐づく、貸出中の時のリストの取得
     * （出版社の降順）
     * @param usersLendingInfo 
     * @return
     */
    public List<UsersLendingInfo> UsersLendingBookList(int usersId) {
        String sql = "SELECT lending.id,lending.reg_date,lending.upd_date,lending.books_id,books.title,books.author,books.publisher,books.publish_date,books.thumbnail_name,books.thumbnail_url FROM lending JOIN books ON lending.books_id = books.id where lending.USERS_ID ="
                + usersId + " and lending_status = 1 ORDER BY lending.reg_date DESC;";
        List<UsersLendingInfo> getedLendingBookList = jdbcTemplate.query(sql, new UsersLendingInfoRowMapper());

        return getedLendingBookList;
    }

    /**
     * usersIdに紐づく、貸出可の時のリストの取得
     * @param usersLendingInfo
     * @return
     */
    public List<UsersLendingInfo> UsersHistoryBookList(int usersId) {
        String sql = "SELECT lending.id,lending.reg_date,lending.upd_date,lending.books_id,books.title,books.author,books.publisher,books.publish_date,books.thumbnail_name,books.thumbnail_url FROM lending JOIN books ON lending.books_id = books.id where lending.USERS_ID = "
                + usersId + " and lending_status = 0 ORDER BY lending.upd_date DESC;";
        List<UsersLendingInfo> getedHistoryBookList = jdbcTemplate.query(sql, new UsersLendingInfoRowMapper());
        return getedHistoryBookList;
    }
}
