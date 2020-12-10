package com.juubes.motimaa.clockapi;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * API for an ingame clock and daycycle. This API is supposed to be used as a
 * helper for scheduling in-game daily tasks with Bukkit Scheduler.
 */
public class IngameClockAPI extends JavaPlugin implements CommandExecutor {

	/**
	 * Marks a gameday in ticks. IRL 30 minutes = 36000 ticks.
	 */
	public static final float INGAME_DAY = 36000f;

	/**
	 * 20 minutes / 24 hours
	 */
	public static final float MINECRAFT_TIME_MULTIPLIER = 72;

	/**
	 * Marks the relative speed of the in-game day. One Minecraft day is 24000
	 * ticks.
	 */
	public static final float TIME_RATIO = 24000f / INGAME_DAY * MINECRAFT_TIME_MULTIPLIER;

	/**
	 * Marks a gamehour in ticks.
	 */
	public static final float INGAME_HOUR = INGAME_DAY / 24f;

	/**
	 * Marks a gameminute in ticks.
	 */
	public static final float INGAME_MINUTE = INGAME_HOUR / 60f;

	/**
	 * Timestamp of server start.
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

		System.out.println("Time ratio: " + TIME_RATIO);
	}

	// TODO: Implement /uptime XX:YY:ZZ:SS command
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player))
			return true;

		if (OPTION_PERMISSION_CLOCK_COMMAND != null && !sender.hasPermission(OPTION_PERMISSION_CLOCK_COMMAND)) {
			sender.sendMessage("§cNo permission.");
			return true;
		}

		sender.sendMessage(getTimeFormatted());
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
	public static float getUptimeTicks() {
		return getUptimeMillis() / 50;
	}

	//
	// /**
	// * Returns the in-game minutes the server was started.
	// */
	// public static float getUptimeMinutes() {
	// return getUptimeMillis() / INGAME_MINUTE;
	// }
	//
	// /**
	// * Returns the in-game hours since the server was started.
	// */
	// public static float getUptimeHours() {
	// return getUptimeMillis() / INGAME_HOUR;
	// }

	// /**
	// * Returns the in-game days since the server was started.
	// */
	// public static float getUptimeDays() {
	// return getUptimeMillis() / INGAME_DAY;
	// }
	//
	/**
	 * Returns the time in absolute ticks since the in-game day started.
	 */
	public static float getTimeTicksAbsolute() {
		return getUptimeTicks() % INGAME_DAY;
	}

	/**
	 * Returns the time in seconds since the day started.
	 */
	public static float getTimeSeconds() {
		return (getUptimeTicks() / 20f * TIME_RATIO);
	}

	/**
	 * Returns the time in minutes since the day started.
	 */
	public static float getTimeMinutes() {
		return (getUptimeTicks() / 20f / 60f * TIME_RATIO);
	}

	/**
	 * Returns the time in hours since the day started.
	 */
	public static float getTimeHours() {
		return (getUptimeTicks() / 20f / 60f / 60f * TIME_RATIO);
	}

	/**
	 * Returns the time formatted as HH:MM:SS.
	 * 
	 * @param d
	 * @param minutes
	 * @param hours
	 */
	public static String getTimeFormatted(boolean includeHours, boolean includeMinutes, boolean includeSeconds) {
		int hours = (int) getTimeHours() % 24;
		int minutes = (int) getTimeMinutes() % 60;
		int seconds = (int) getTimeSeconds() % 60;

		String hoursStr = String.valueOf(hours);
		String minutesStr = String.valueOf(minutes);
		String secondsStr = String.valueOf(seconds);

		if (hours < 10)
			hoursStr = "0" + hours;
		if (minutes < 10)
			minutesStr = "0" + minutes;
		if (seconds < 10)
			secondsStr = "0" + seconds;

		List<String> numbers = new ArrayList<>();
		if (includeHours)
			numbers.add(hoursStr);
		if (includeMinutes)
			numbers.add(minutesStr);
		if (includeSeconds)
			numbers.add(secondsStr);

		return String.join(":", numbers);
	}

	/**
	 * {@link IngameClockAPI#getTimeFormatted(boolean, boolean, boolean)}
	 */
	public static String getTimeFormatted() {
		return getTimeFormatted(true, true, true);
	}
}
