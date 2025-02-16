package de.schelle.backupsystem;

import de.schelle.backupsystem.commands.Backup;
import org.bukkit.plugin.java.JavaPlugin;

public final class BackupSystem extends JavaPlugin {

    @Override
    public void onEnable() {

        getCommand("backup").setExecutor(new Backup(this));



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
