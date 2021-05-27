package jp.co.seattle.library.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import jp.co.seattle.library.dto.UsersLendingInfo;

@Configuration
public class UsersLendingInfoRowMapper implements RowMapper<UsersLendingInfo> {
    @Override
    public UsersLendingInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        UsersLendingInfo usersLendingInfo = new UsersLendingInfo();

        usersLendingInfo.setLendingId(rs.getInt("id"));
        usersLendingInfo.setBookId(rs.getInt("books_id"));
        usersLendingInfo.setLendingRegDate(rs.getString("reg_date"));
        usersLendingInfo.setLendingUpdDate(rs.getString("upd_date"));
        usersLendingInfo.setTitle(rs.getString("title"));
        usersLendingInfo.setAuthor(rs.getString("author"));
        usersLendingInfo.setPublisher(rs.getString("publisher"));
        usersLendingInfo.setPublishDate(rs.getString("publish_date"));
        usersLendingInfo.setThumbnail(rs.getString("thumbnail_url"));

        return usersLendingInfo;
    }
}
