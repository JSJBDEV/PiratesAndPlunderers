package gd.rf.acro.pap;

import net.fabricmc.api.ModInitializer;
import gd.rf.acro.pap.config.ConfigLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PiratesAndPlunderers implements ModInitializer {

	public static ConfigLoader config;
	public static Logger logger = LogManager.getLogger();

	@Override
	public void onInitialize() {
		config = new ConfigLoader();
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		registerBlocks();
		registerItems();
		logger.info("Hello Fabric world!");


	}
	private void registerBlocks()
	{

	}
	private void registerItems()
	{

	}
}
