package de.schelle.backupsystem.commands;

import de.schelle.backupsystem.BackupManager;
import de.schelle.backupsystem.BackupSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;




public class Backup implements CommandExecutor {
    private final BackupSystem plugin;

    public Backup(BackupSystem plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.hasPermission("backup")) {
            sender.sendMessage("Backup wurde erstellt!");
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                BackupManager.backup(Bukkit.getWorld("world"));
            });
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Du darfst das nicht tun!");
        return false;
    }
}
