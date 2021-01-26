package com.darko.main.utilities.teri.Nicknames;

import com.darko.main.AlttdUtility;
import com.darko.main.database.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class DatabaseQueries
{
    public static void setNicknameInDatabase(final UUID uuid, final String nickName) {
        final String sql = "INSERT INTO nicknames (uuid, nickname, date_changed) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE nickname = ?";
        try (final PreparedStatement statement = Database.connection.prepareStatement(sql)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, nickName);
            statement.setLong(3, new Date().getTime());
            statement.setString(4, nickName);
            statement.execute();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void removePlayerFromDataBase(final UUID uuid) throws SQLException {
        final PreparedStatement insert = Database.connection.prepareStatement("DELETE FROM nicknames WHERE uuid = ?");
        insert.setString(1, uuid.toString());
        insert.executeUpdate();
        insert.close();
    }

    public static ArrayList<Nick> getNicknamesList() {
        ArrayList<Nick> nickList = new ArrayList<>();
        String queryNicknames = "SELECT uuid, nickname, date_changed FROM nicknames ";
        String queryRequests = "SELECT `requested_nicknames`.`nickname` AS new_nick, `requested_nicknames`.`date_requested`, " +
                "`nicknames`.`nickname` AS old_nick, `nicknames`.`date_changed`, `requested_nicknames`.`uuid` " +
                "FROM `requested_nicknames`" +
                "LEFT JOIN `nicknames` ON `requested_nicknames`.`uuid` = `nicknames`.`uuid` ";

        try {
            ResultSet resultSetNicknames = getStringResult(queryNicknames);
            while (resultSetNicknames.next()){
                nickList.add(new Nick(
                        UUID.fromString(resultSetNicknames.getString("uuid")),
                        resultSetNicknames.getString("nickname"),
                        resultSetNicknames.getLong("date_changed")));
            }

            ResultSet resultSetRequests = getStringResult(queryRequests);
            while (resultSetRequests.next()){
                nickList.add(new Nick(
                        UUID.fromString(resultSetRequests.getString("uuid")),
                        resultSetRequests.getString("old_nick"),
                        resultSetRequests.getLong("date_changed"),
                        resultSetRequests.getString("new_nick"),
                        resultSetRequests.getLong("date_requested")));
            }
        } catch (SQLException e) {
            AlttdUtility.getInstance().getLogger().warning("Failed to get nicknames list\n" + Arrays.toString(e.getStackTrace())
                    .replace(",", "\n"));
        }
        return nickList;
    }

    public static Nick getNick(UUID uniqueId) {
        String getNick = "SELECT nickname, date_changed, uuid FROM nicknames WHERE uuid = ?";
        String getRequest = "SELECT nickname, date_requested, uuid FROM requested_nicknames WHERE uuid = ?";

        try {
            ResultSet resultSetNick = getStringResult(getNick, uniqueId.toString());
            ResultSet resultSetRequest = getStringResult(getRequest, uniqueId.toString());
            String uuid = null;
            String currentNick = null;
            long dateChanged = 0;
            String requestedNick = null;
            long dateRequested = 0;

            if (resultSetNick.next()) {
                uuid = resultSetNick.getString("uuid");
                currentNick = resultSetNick.getString("nickname");
                dateChanged = resultSetNick.getLong("date_changed");
            }
            if (resultSetRequest.next()) {
                uuid = resultSetRequest.getString("uuid");
                requestedNick = resultSetRequest.getString("nickname");
                dateRequested = resultSetRequest.getLong("date_requested");
            }
            if (uuid != null) {
                return new Nick(UUID.fromString(uuid), currentNick, dateChanged, requestedNick, dateRequested);
            }
        } catch (SQLException e){
            AlttdUtility.getInstance().getLogger().warning("Failed to get nicknames for "
                    + uniqueId.toString() + "\n" + Arrays.toString(e.getStackTrace())
                    .replace(",", "\n"));

        }
        return null;
    }

    public static void denyNewNickname(UUID uniqueId) {
        String query = "DELETE FROM requested_nicknames WHERE uuid = ?";
        try {
            PreparedStatement statement = Database.connection.prepareStatement(query);
            statement.setString(1, uniqueId.toString());
            statement.execute();
        } catch (SQLException e) {
            AlttdUtility.getInstance().getLogger().warning("Failed to delete requested nickname for "
                    + uniqueId.toString() + "\n" + Arrays.toString(e.getStackTrace())
                    .replace(",", "\n"));
        }
    }

    public static void acceptNewNickname(UUID uniqueId, String newNick){
        String delete = "DELETE FROM requested_nicknames WHERE uuid = ?";
        String update = "INSERT INTO nicknames (uuid, nickname, date_changed) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE nickname = ?, date_changed = ?";
        long time = new Date().getTime();

        try {
            PreparedStatement deleteStatement = Database.connection.prepareStatement(delete);
            deleteStatement.setString(1, uniqueId.toString());

            deleteStatement.execute();

            PreparedStatement updateStatement = Database.connection.prepareStatement(update);
            updateStatement.setString(1, uniqueId.toString());
            updateStatement.setString(2, newNick);
            updateStatement.setLong(3, time);
            updateStatement.setString(4, newNick);
            updateStatement.setLong(5, time);

            updateStatement.execute();
        } catch (SQLException e) {
            AlttdUtility.getInstance().getLogger().warning("Failed to accept requested nickname for "
                    + uniqueId.toString() + "\n" + Arrays.toString(e.getStackTrace())
                    .replace(",", "\n"));
        }
    }

    public static void newNicknameRequest(UUID uniqueId, String nickName) {
        String requestQuery = "INSERT INTO requested_nicknames (uuid, nickname, date_requested) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE nickname = ?, date_requested = ?";
        String nickQuery = "INSERT INTO nicknames (uuid, nickname, date_changed) VALUES (?, ?, 0) " +
                "ON DUPLICATE KEY UPDATE uuid = uuid";
        long time = new Date().getTime();

        try {
            PreparedStatement requestPreparedStatement = Database.connection.prepareStatement(requestQuery);
            requestPreparedStatement.setString(1, uniqueId.toString());
            requestPreparedStatement.setString(2, nickName);
            requestPreparedStatement.setLong(3, time);
            requestPreparedStatement.setString(4, nickName);
            requestPreparedStatement.setLong(5, time);

            requestPreparedStatement.execute();

            PreparedStatement nickPreparedStatement = Database.connection.prepareStatement(nickQuery);
            nickPreparedStatement.setString(1, uniqueId.toString());
            nickPreparedStatement.setString(2, null);

            nickPreparedStatement.execute();

        } catch (SQLException e) {
            AlttdUtility.getInstance().getLogger().warning("Failed to store requested nickname for "
                    + uniqueId.toString() + "\n" + Arrays.toString(e.getStackTrace())
                    .replace(",", "\n"));
        }
    }

    private static ResultSet getStringResult(final String query, final String... parameters) throws SQLException {
        final PreparedStatement statement = Database.connection.prepareStatement(query);
        for (int i = 1; i < parameters.length + 1; ++i) {
            statement.setString(i, parameters[i - 1]);
        }
        return statement.executeQuery();
    }
}
