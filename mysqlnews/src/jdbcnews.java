import java.sql.*;

public class jdbcnews {

    // JDBC URL, username and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/mysqlnews";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "B@i13821820539";

    // JDBC variables for opening and managing connection
    private static Connection connection;

    public static void main(String[] args) {
        try {
            // Open a connection
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            System.out.println("Connection successful");

            // Insert a user
            insertUser();

            // Insert a news article
            insertNews();

            // Insert a comment
            insertComment();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void insertUser() throws SQLException {
        String sql = "INSERT INTO Users (email, phone, avatar_url, nickname, favorites, news_preferences, browsing_history, comment_history, last_login) " +
                "VALUES ('test@jdbcexample.com', '1234567890', 'http://jdbcexample.com/avatar.jpg', 'testuser', '[]', '[]', '[]', '[]', NOW())";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected (Insert User): " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertNews() throws SQLException {
        String sql = "INSERT INTO News (title, cover_image_url, publish_datetime, author, content, view_count, favorite_count, share_count, is_promoted) " +
                "VALUES ('JDBCExample News', 'http://jdbcexample.com/cover.jpg', NOW(), 'Author Name', 'This is the content of the news.', 0, 0, 0, FALSE)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected (Insert News): " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertComment() throws SQLException {
        String sql = "INSERT INTO Comments (user_id, news_id, content, comment_datetime, referenced_comment_id, like_count, dislike_count) " +
                "VALUES (1, 1, 'This is a comment.', NOW(), NULL, 0, 0)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected (Insert Comment): " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
