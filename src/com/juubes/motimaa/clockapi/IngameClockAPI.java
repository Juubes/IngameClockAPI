package com.juubes.motimaa.clockapi;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * API for an ingame clock and daycycle. This API is supposed to be used as a
 * helper for scheduling in-game daily tasks with Bukkit Scheduler.
 */
public class IngameClockAPI extends JavaPlugin implements CommandExecutor {

	/**
	 * Marks a gameday in ticks.
	 */
	public static final int INGAME_DAY = 2880 * 20;

	/**
	 * Marks a gamehour in ticks.
	 */
	public static final int INGAME_HOUR = INGAME_DAY / 24;

	/**
	 * Marks a gameminute in ticks.
	 */
	public static final int INGAME_MINUTE = INGAME_HOUR / 60;

	/**
	 * Milliseconds since the server was started.
	 */
	public static final long START_TIME_MILLIS = System.currentTimeMillis();

	/*
	 * Set null to disable.
	 */
	public static String OPTION_PERMISSION_CLOCK_COMMAND;

	@Override
	public void onEnable() {
		this.getCommand("clock").setExecutor(this);

		saveDefaultConfig();

		FileConfiguration conf = getConfig();
		OPTION_PERMISSION_CLOCK_COMMAND = conf.getString("permission-clock-command");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;

		if (!sender.hasPermission("ingameclock.clock")) {
			sender.sendMessage("§cNo permission.");
			return true;
		}

		long hours = getTimeHours();
		long minutes = getTimeMinutes();
		long seconds = getTimeSeconds();

		sender.sendMessage(hours + ":" + minutes + ":" + seconds);
		return true;
	}

	/**
	 * Returns the milliseconds since the server was started.
	 */
	public static long getUptimeMillis() {
		return System.currentTimeMillis() - START_TIME_MILLIS;
	}

	/**
	 * Returns the ticks since the server was started.
	 */
	public static long getUptimeTicks() {
		return getUptimeMillis() / 50;
	}

	/**
	 * Returns the in-game seconds the server was started.
	 */
	public static long getUptimeSeconds() {
		return getUptimeMillis() / INGAME_MINUTE / 60;
	}

	/**
	 * Returns the in-game minutes the server was started.
	 */
	public static long getUptimeMinutes() {
		return getUptimeMillis() / INGAME_MINUTE;
	}

	/**
	 * Returns the in-game hours since the server was started.
	 */
	public static long getUptimeHours() {
		return getUptimeMillis() / INGAME_HOUR;
	}

	/**
	 * Returns the in-game days since the server was started.
	 */
	public static long getUptimeDays() {
		return getUptimeMillis() / INGAME_DAY;
	}

	/**
	 * Returns the time in ticks since the day started.
	 */
	public static long getTimeTicks() {
		return getUptimeTicks() % INGAME_DAY;
	}

	/**
	 * Returns the time in seconds since the day started.
	 */
	public static long getTimeSeconds() {
		return getTimeTicks() / 20;
	}

	/**
	 * Returns the time in minutes since the day started.
	 */
	public static long getTimeMinutes() {
		return getTimeTicks() / 20 / 60;
	}

	/**
	 * Returns the time in hours since the day started.
	 */
	public static long getTimeHours() {
		return getTimeTicks() / 20 / 60 / 60;
	}
}
