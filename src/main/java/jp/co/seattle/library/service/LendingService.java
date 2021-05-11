package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LendingService {
    final static Logger logger = LoggerFactory.getLogger(LendingService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 貸出登録をする
     * 
     * @param bookId 書籍ID
     */
    public void LendingRegistration(int bookId) {
        String sql = "INSERT INTO lending(booksid,reg_date) VALUES(" + bookId + ",sysdate())";
        jdbcTemplate.update(sql);
    }

    /**
     * 貸出中か確認する
     * @param bookId
     * @return countLendingId 
     */
    public int LendingConfirmation(int bookId) {
        //mysqlの設定がID、BOOKSIDどちらも重複なしである。
        //countでデータの数を調べると1(対象がある)もしくは0(対象がない)となる。
        String sql = "SELECT count(id) FROM lending where booksid =" + bookId;
        int countLendingId = jdbcTemplate.queryForObject(sql, int.class);
        return countLendingId;
    }

    /**
     * 返却をする=貸出テーブルの情報を削除する
     * @param bookId
     */
    public void deleteLending(int bookId) {
        String sql = "DELETE FROM lending WHERE booksid = " + bookId + ";";
        jdbcTemplate.update(sql);
    }
}
