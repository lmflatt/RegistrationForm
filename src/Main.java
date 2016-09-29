import jodd.json.JsonParser;
import jodd.json.JsonSerializer;
import org.h2.tools.Server;
import spark.Spark;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by lee on 9/29/16.
 */
public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, name VARCHAR, address VARCHAR, email VARCHAR)");
    }

    public static void insertUser(Connection conn, String name, String address, String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (null, ?, ?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, address);
        stmt.setString(3, email);
        stmt.execute();
    }

    public static ArrayList<User> selectUsers(Connection conn) throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String address = rs.getString("address");
            String email = rs.getString("email");
            users.add(new User(id, name, address, email));
        }

        return users;
    }

    public static void updateUser(Connection conn, Integer id, String name, String address, String email) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE users SET name = ?, address = ?, email = ? WHERE id = ?");
        stmt.setString(1, name);
        stmt.setString(2, address);
        stmt.setString(3, email);
        stmt.setInt(4, id);
        stmt.execute();
    }

    public static void deleteUser(Connection conn, Integer id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }


    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        createTables(conn);

        Spark.externalStaticFileLocation("public");
        Spark.init();

        Spark.get("/user",
                (request, response) -> {
                    ArrayList<User> messages = selectUsers(conn);
                    JsonSerializer s = new JsonSerializer();
                    return s.serialize(messages);
                }
        );

        Spark.post("/user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);

                    insertUser(conn, user.username, user.address, user.email);
                    response.status(200);
                    return "";
                }
        );

        Spark.put("/user",
                (request, response) -> {
                    String body = request.body();
                    JsonParser p = new JsonParser();
                    User user = p.parse(body, User.class);

                    updateUser(conn, user.id, user.username, user.address, user.email);
                    return "";
                }
        );

        Spark.delete("/user/:id",
                (request, response) -> {
                    String idString = request.params(":id");
                    Integer id = Integer.parseInt(idString);

                    deleteUser(conn, id);
                    return "";
                }
        );
    }
}