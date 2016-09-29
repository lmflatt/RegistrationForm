import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Created by lee on 9/29/16.
 */
public class MainTest {
    // GIVEN
    public Connection startConnection() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:test");
        Main.createTables(conn);
        return conn;
    }

    @Test
    public void testUserInsertAndSelect() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "address", "Alicemail");
        Main.insertUser(conn, "Bob", "something", "someemail");
        Main.insertUser(conn, "Charlie", "where", "what");
        Main.insertUser(conn, "David", "blah", "balls");
        ArrayList<User> users = Main.selectUsers(conn);
        conn.close();

        assertTrue(users.size() == 4);
    }

    @Test
    public void testUpdateUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "address", "Alicemail");
        Main.updateUser(conn, 1, "Bob", "something", "someemail");
        ArrayList<User> users = Main.selectUsers(conn);

        assertTrue(users.size() == 1);

        User user = users.get(0);
        assertTrue(user.username.equalsIgnoreCase("Bob"));
    }

    @Test
    public void testDeleteUser() throws SQLException {
        Connection conn = startConnection();
        Main.insertUser(conn, "Alice", "address", "Alicemail");
        Main.insertUser(conn, "Bob", "something", "someemail");
        ArrayList<User> users = Main.selectUsers(conn);

        assertTrue(users.size() == 2);

        Main.deleteUser(conn, 2);
        users = Main.selectUsers(conn);

        assertTrue(users.size() == 1);
    }
}
