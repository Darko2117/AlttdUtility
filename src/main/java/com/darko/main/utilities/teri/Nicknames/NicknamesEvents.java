package com.darko.main.utilities.teri.Nicknames;

import com.Zrips.CMI.utils.Util;
import org.bukkit.event.EventHandler;
import java.sql.SQLException;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;

public class NicknamesEvents implements Listener
{
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) throws SQLException {
        final Player player = e.getPlayer();
        final String nickName = DatabaseQueries.getNicknameFromDatabase(player.getUniqueId());
        final String cmiNick = Util.CMIChatColor.deColorize(Nicknames.getInstance().getNick(player));
        if (cmiNick != null) {
            if (nickName == null) {
                Nicknames.getInstance().resetNick(player);
            }
            else if (!nickName.equals(cmiNick)) {
                Nicknames.getInstance().setNick(player, nickName);
            }
        }
        else if (nickName != null) {
            Nicknames.getInstance().setNick(player, nickName);
        }
    }
}
