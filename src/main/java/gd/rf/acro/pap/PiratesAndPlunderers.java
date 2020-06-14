package gd.rf.acro.pap;

import gd.rf.acro.pap.blocks.RandomDecayingBlock;
import gd.rf.acro.pap.blocks.ShipBuilderBlock;
import gd.rf.acro.pap.command.CommandInit;
import gd.rf.acro.pap.dimension.PirateOceanChunkGenerator;
import gd.rf.acro.pap.entities.SailingShipEntity;
import gd.rf.acro.pap.items.MusketItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.tag.FabricTagBuilder;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tag.GlobalTagAccessor;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import gd.rf.acro.pap.config.ConfigLoader;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.chunk.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PiratesAndPlunderers implements ModInitializer {

	public static ConfigLoader config;
	public static Logger logger = LogManager.getLogger();

	public static final Tag<Block> BOAT_MATERIAL = TagRegistry.block(new Identifier("pap","boat_material"));

	public static final RegistryKey<World> PIRATE_OCEAN_WORLD = RegistryKey.of(Registry.DIMENSION, new Identifier("pap", "pirate_ocean"));

	public static final EntityType<SailingShipEntity> SAILING_BOAT_ENTITY_ENTITY_TYPE =
			Registry.register(Registry.ENTITY_TYPE,new Identifier("pap","sail_boat")
					, FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT,SailingShipEntity::new).dimensions(EntityDimensions.fixed(10,10)).trackable(100,4).build());
	@Override
	public void onInitialize() {
		config = new ConfigLoader();
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		registerBlocks();
		registerItems();
		CommandInit.INSTANCE.registerCommands();
		logger.info("Hello Fabric world!");
    
		FabricDefaultAttributeRegistry.register(SAILING_BOAT_ENTITY_ENTITY_TYPE, MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D));


		Registry.register(Registry.CHUNK_GENERATOR, new Identifier("pap", "pirate_ocean"), PirateOceanChunkGenerator.CODEC);
	}
	public static final ShipBuilderBlock SHIP_BUILDER_BLOCK = new ShipBuilderBlock(FabricBlockSettings.of(Material.METAL).build());
	public static final RandomDecayingBlock SHIP_BUILDER_MARKER = new RandomDecayingBlock(FabricBlockSettings.of(Material.METAL).strength(-1,3600000.0F).ticksRandomly().build());
	private void registerBlocks()
	{
		Registry.register(Registry.BLOCK,new Identifier("pap","ship_builder"),SHIP_BUILDER_BLOCK);
		Registry.register(Registry.BLOCK,new Identifier("pap","ship_builder_marker"),SHIP_BUILDER_MARKER);
	}
	public static final MusketItem MUSKET_ITEM = new MusketItem(new Item.Settings().group(ItemGroup.MISC));
	public static final MusketItem BLUNDERBUSS_ITEM = new MusketItem(new Item.Settings().group(ItemGroup.MISC));
	private void registerItems()
	{
		Registry.register(Registry.ITEM,new Identifier("pap","musket"),MUSKET_ITEM);
		Registry.register(Registry.ITEM,new Identifier("pap","blunderbuss"),BLUNDERBUSS_ITEM);
	}
}
