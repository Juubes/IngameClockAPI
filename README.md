# IngameClockAPI
API for an ingame clock and daycycle. This API is supposed to be used as a helper for scheduling in-game daily tasks with Bukkit Scheduler.

# Build
with command 'mvn' or 'mvn package'.

# Using as a dependency
1. Please run 'mvn install' if you want to find the API from your maven repositories. 
2. Add the jar to your buildpath or the maven dependency to your pom.xml.


# Example
```
import static com.juubes.motimaa.clockapi.IngameClockAPI.*;

public class Ruokaplugini extends JavaPlugin {

	/*
	* Runs task every in-game day at 16:00. 
	*/
	@Override
	public void onEnable() {
		// Executes the runnable every 16th hour of the ingame day.
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			// Run stuff
		}, 16 * INGAME_HOUR, INGAME_DAY);
	}

}
```
The above example ran at plugin load, but if you need to initialize scheduled events at another time, just add the dayoffset with `IngameClockAPI.getTimeInTicks()`.

