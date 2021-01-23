package com.darko.main.utilities.teri.Nicknames;

import com.darko.main.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseQueries
{
    public static void setNicknameInDatabase(final UUID uuid, final String nickName) {
        final String sql = "INSERT INTO nicknames (uuid, nickname) VALUES (?, ?) ON DUPLICATE KEY UPDATE nickname = ?";
        try (final PreparedStatement statement = Database.connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, nickName);
            statement.setString(3, nickName);
            statement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static String getNicknameFromDatabase(final UUID uuid) throws SQLException {
        final ResultSet nickName = getStringResult("SELECT nickname FROM nicknames WHERE uuid = ?", uuid.toString());
        if (nickName.next()) {
            return nickName.getString("nickname");
        }
        return null;
    }
    
    public static void removePlayerFromDataBase(final UUID uuid) throws SQLException {
        final PreparedStatement insert = Database.connection.prepareStatement("DELETE FROM nicknames WHERE uuid = ?");
        insert.setString(1, uuid.toString());
        insert.executeUpdate();
        insert.close();
    }
    
    private static ResultSet getStringResult(final String query, final String... parameters) throws SQLException {
        final PreparedStatement statement = Database.connection.prepareStatement(query);
        for (int i = 1; i < parameters.length + 1; ++i) {
            statement.setString(i, parameters[i - 1]);
        }
        return statement.executeQuery();
    }
}
