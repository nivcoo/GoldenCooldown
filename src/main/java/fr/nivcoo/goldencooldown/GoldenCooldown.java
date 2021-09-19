/**
 *
 */
package fr.nivcoo.goldencooldown;

import fr.nivcoo.goldencooldown.events.PlayerItemConsume;
import fr.nivcoo.goldencooldown.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class GoldenCooldown extends JavaPlugin {
    private static GoldenCooldown INSTANCE;
    private Config config;

    @Override
    public void onEnable() {
        INSTANCE = this;
        saveDefaultConfig();
        config = new Config(new File("plugins" + File.separator + "GoldenCooldown" + File.separator + "config.yml"));
        getCommand("gc").setExecutor(new GoldenCooldownCommand());
        Bukkit.getPluginManager().registerEvents(new PlayerItemConsume(), this);
        Bukkit.getConsoleSender().sendMessage("§c==============§b===============");
        Bukkit.getConsoleSender().sendMessage("§7GoldenCooldown §av" + this.getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§aPlugin Enabled !");
        Bukkit.getConsoleSender().sendMessage("§c==============§b===============");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§c==============§b===============");
        Bukkit.getConsoleSender().sendMessage("§7GoldenCooldown §cv" + this.getDescription().getVersion());
        Bukkit.getConsoleSender().sendMessage("§cPlugin Disabled !");
        Bukkit.getConsoleSender().sendMessage("§c==============§b===============");
    }


    public void reload() {
        config.loadConfig();
    }


    public static GoldenCooldown get() {
        return INSTANCE;
    }

    public Config getConfiguration() {
        return config;
    }
}
