package jp.co.seattle.library.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.UsersLendingInfo;

/**
 * lendingテーブルに関する処理を実装する
 *
 * 
 */
@Service
public class LendingService {
    final static Logger logger = LoggerFactory.getLogger(LendingService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 貸出登録をする (貸出状況)lendingStatus=1
     * @param usersLendingInfo
     */
    public void LendingRegistration(UsersLendingInfo usersLendingInfo) {
        String sql = "INSERT INTO lending(BOOKS_ID,USERS_ID,LENDING_STATUS,REG_DATE,UPD_DATE) VALUES("
                + usersLendingInfo.getBookId() + "," + usersLendingInfo.getUsersId() + ",1,sysdate(),sysdate())";
        jdbcTemplate.update(sql);
    }

    /**
     * 貸出中か確認する
     * @param bookId
     * @return countLendingId 
     */
    public int LendingConfirmation(int bookId) {
        //countでデータの数を調べると1(対象がある)もしくは0(対象がない)となる。
        String sql = "SELECT COUNT(LENDING_STATUS) FROM lending WHERE BOOKS_ID=" + bookId + " AND LENDING_STATUS = 1 ";
        int countLendingId = jdbcTemplate.queryForObject(sql, int.class);
        return countLendingId;
    }

    /**
     * 返却をする (貸出状況)lendingStatus=0
     * @param usersLendingInfo
     */
    public void deleteLending(UsersLendingInfo usersLendingInfo) {
        String sql = "UPDATE lending SET LENDING_STATUS = 0 , UPD_DATE = SYSDATE() WHERE BOOKS_ID = "
                + usersLendingInfo.getBookId() + " AND USERS_ID = " + usersLendingInfo.getUsersId() + ";";
        jdbcTemplate.update(sql);
    }
}
