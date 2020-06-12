package gd.rf.acro.pap;

import net.fabricmc.api.ModInitializer;

public class PiratesAndPlunderers implements ModInitializer {
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		registerBlocks();
		registerItems();
		System.out.println("Hello Fabric world!");
	}
	private void registerBlocks()
	{

	}
	private void registerItems()
	{

	}
}
