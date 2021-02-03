package fr.nivcoo.goldencooldown.events;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import fr.nivcoo.goldencooldown.GoldenCooldown;
import fr.nivcoo.goldencooldown.utils.Config;

public class PlayerItemConsume implements Listener {

	private HashMap<String, Long> wait = new HashMap<>();
	private GoldenCooldown gc = GoldenCooldown.get();
	private Config config = gc.getConfiguration();

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent e) {

		Player p = e.getPlayer();
		ItemStack item = e.getItem();
		if (p.hasPermission("goldencooldown.bypass") || item.getType() != Material.GOLDEN_APPLE)
			return;
		String timeBeforeEatString = config.getString("time_before_eat");
		int multipleAmount = 1;
		if (timeBeforeEatString.contains("m")) {
			multipleAmount = 60;
			timeBeforeEatString = timeBeforeEatString.substring(0, timeBeforeEatString.length() - 1);
		}
		int timeBeforeEat = Integer.parseInt(timeBeforeEatString);

		Long currentMillis = System.currentTimeMillis();
		String UUID = p.getUniqueId().toString();
		Long time = wait.get(UUID);

		if (time == null)
			wait.put(UUID, currentMillis);
		else {
			long timeInSecondBeforeEat = timeBeforeEat * multipleAmount;
			if ((currentMillis - time) / 1000 > timeInSecondBeforeEat) {
				wait.put(UUID, currentMillis);
				return;
			}
			sendCancelMessage(p, time, timeInSecondBeforeEat);
			e.setCancelled(true);
		}
	}

	private void sendCancelMessage(Player p, long timeBeforeEat, long timeInSecondBeforeEat) {
		Long currentMillis = System.currentTimeMillis();
		timeBeforeEat = ((timeBeforeEat + (timeInSecondBeforeEat * 1000)) - currentMillis) / 1000;

		int minutes = (int) (timeBeforeEat / 60);

		timeBeforeEat -= (minutes * 60);
		int seconds = (int) timeBeforeEat;

		String globalMessage = "";
		String type = config.getString("messages.minute");
		if (minutes > 0) {
			if (minutes > 1) {
				type = config.getString("messages.minutes");
			}
			globalMessage = minutes + " " + type + ", ";
		}
		type = config.getString("messages.second");
		if (seconds > 1) {
			type = config.getString("messages.seconds");
		}
		globalMessage += seconds + " " + type;

		p.sendMessage(config.getString("messages.prefix")
				+ config.getString("messages.cancel_message").replace("{0}", globalMessage));
	}

}
