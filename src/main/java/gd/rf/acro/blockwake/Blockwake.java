package gd.rf.acro.blockwake;

import gd.rf.acro.blockwake.blocks.*;
import gd.rf.acro.blockwake.command.CommandInit;
import gd.rf.acro.blockwake.dimension.PirateOceanChunkGenerator;
import gd.rf.acro.blockwake.dimension.PirateOceanPlacementHandler;
import gd.rf.acro.blockwake.entities.PirateEntity;
import gd.rf.acro.blockwake.entities.SailingShipEntity;
import gd.rf.acro.blockwake.items.*;
import gd.rf.acro.blockwake.world.PortTownFeature;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.villager.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tag.Tag;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import gd.rf.acro.blockwake.config.ConfigLoader;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.poi.PointOfInterestType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class Blockwake implements ModInitializer {

	public static ConfigLoader config;
	public static Logger logger = LogManager.getLogger();
	private static final String[] ship_names = {"piratv_arcadia","lord_crawlmasks_clipper"};
	private static final String[] structure_names = {
		"port_town_bottom_floor",
		"port_town_farm",
		"port_town_tannery",
		"port_town_archery",
		"port_town_library",
		"port_town_storage",
		"port_town_shipwright"
	};

	public static final ItemGroup TAB = FabricItemGroupBuilder.build(
			new Identifier("blockwake", "blockwake_tab"),
			() -> new ItemStack(Blockwake.STATIC_CANNON_BLOCK));
	
	public static final Tag<Block> BOAT_MATERIAL = TagRegistry.block(new Identifier("blockwake","boat_material"));


	public static final RegistryKey<World> PIRATE_OCEAN_WORLD = RegistryKey.of(Registry.DIMENSION, new Identifier("blockwake", "pirate_ocean"));

	public static final EntityType<SailingShipEntity> SAILING_BOAT_ENTITY_ENTITY_TYPE =
			Registry.register(Registry.ENTITY_TYPE,new Identifier("blockwake","sail_boat")
					, FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT,SailingShipEntity::new).dimensions(EntityDimensions.fixed(10,10)).trackable(100,4).build());

	public static final EntityType<PirateEntity> PIRATE_ENTITY_ENTITY_TYPE =
			Registry.register(Registry.ENTITY_TYPE,new Identifier("blockwake","pirate")
					, FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT,PirateEntity::new).dimensions(EntityDimensions.fixed(1,2)).trackable(100,4).build());

	public static RegistryKey<World> dimensionRegistryKey;

	private static final Feature<DefaultFeatureConfig> PORT_TOWN = Registry.register(
			Registry.FEATURE,
			new Identifier("blockwake", "port_town"),
			new PortTownFeature(DefaultFeatureConfig.CODEC)
	);
	public static PointOfInterestType SHIPWRIGHT_POI;
	public static VillagerProfession SHIPWRIGHT;



	@Override
	public void onInitialize() {
		config = new ConfigLoader();
		// This code runs as soon as Minecraft is in a mod-load-ready state.e
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		registerBlocks();
		registerItems();
		registerBlockEntities();
		SHIPWRIGHT_POI = PointOfInterestHelper.register(new Identifier("blockwake","shipwright_poi"),1,50, Blockwake.SHIPWRIGHTS_TABLE);
		SHIPWRIGHT = VillagerProfessionBuilder.create().id(new Identifier("blockwake","shipwright")).workstation(SHIPWRIGHT_POI).build();
		CommandInit.INSTANCE.registerCommands();
		logger.info("Hello Fabric world!");
    
		FabricDefaultAttributeRegistry.register(SAILING_BOAT_ENTITY_ENTITY_TYPE, MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D));
		FabricDefaultAttributeRegistry.register(PIRATE_ENTITY_ENTITY_TYPE, MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).add(EntityAttributes.GENERIC_FOLLOW_RANGE,100).add(EntityAttributes.GENERIC_ATTACK_DAMAGE,2));


		Registry.register(Registry.CHUNK_GENERATOR, new Identifier("blockwake", "pirate_ocean"), PirateOceanChunkGenerator.CODEC);
		Biomes.BEACH.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES,PORT_TOWN.configure(new DefaultFeatureConfig()).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP.configure(new ChanceDecoratorConfig(100))));
		Registry.register(Registry.VILLAGER_PROFESSION,new Identifier("blockwake","shipwright_prof"),SHIPWRIGHT);

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
			@Override
			public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
				return CompletableFuture.runAsync(() -> {
					try {

						for (String name:ship_names)
						{
							FileUtils.writeLines(new File("./config/Blockwake/ships/"+ name+".blocks"),
									IOUtils.readLines(manager.getResource(new Identifier("blockwake","ships_default/"+name+".blocks")).getInputStream(),"utf-8"));
						}
						for (String name:structure_names)
						{
							FileUtils.writeLines(new File("./config/Blockwake/buildings/"+ name+".blocks"),
									IOUtils.readLines(manager.getResource(new Identifier("blockwake","structures_default/"+name+".blocks")).getInputStream(),"utf-8"));
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}).thenCompose(aVoid -> synchronizer.whenPrepared(null));
			}

			@Override
			public Identifier getFabricId() {
				return new Identifier("blockwake","data");
			}
		});

		dimensionRegistryKey = RegistryKey.of(Registry.DIMENSION, new Identifier("blockwake", "pirate_ocean"));

		FabricDimensions.registerDefaultPlacer(dimensionRegistryKey, PirateOceanPlacementHandler.INSTANCE.enter(new BlockPos(0, 64, 0)));

		Biomes.OCEAN.getEntitySpawnList(SpawnGroup.WATER_CREATURE).add(new Biome.SpawnEntry(SAILING_BOAT_ENTITY_ENTITY_TYPE, 5, 1, 1));


	}



	public static final ShipBuilderBlock SHIP_BUILDER_BLOCK = new ShipBuilderBlock(FabricBlockSettings.of(Material.METAL).build());
	public static final StructureBuilderBlock STRUCTURE_BUILDER_BLOCK = new StructureBuilderBlock(FabricBlockSettings.of(Material.METAL).build());
	public static final RandomDecayingBlock SHIP_BUILDER_MARKER = new RandomDecayingBlock(FabricBlockSettings.of(Material.METAL).strength(-1,3600000.0F).ticksRandomly().build());
	public static final StaticCannonBlock STATIC_CANNON_BLOCK = new StaticCannonBlock(FabricBlockSettings.of(Material.METAL).build());
	public static final StaticMobSpawner STATIC_MOB_SPAWNER = new StaticMobSpawner(FabricBlockSettings.of(Material.METAL).build(),EntityType.VILLAGER);
    public static final StaticMobSpawner STATIC_PIRATE_SPAWNER = new StaticMobSpawner(FabricBlockSettings.of(Material.METAL).build(),PIRATE_ENTITY_ENTITY_TYPE);
	public static final Block SHIPWRIGHTS_TABLE = new Block(FabricBlockSettings.of(Material.WOOD).build());
	public static final ShipWheelBlock SHIP_WHEEL_BLOCK = new ShipWheelBlock(FabricBlockSettings.of(Material.WOOD).build());
	private void registerBlocks()
	{
		Registry.register(Registry.BLOCK,new Identifier("blockwake","ship_builder"),SHIP_BUILDER_BLOCK);
		Registry.register(Registry.BLOCK,new Identifier("blockwake","ship_builder_marker"),SHIP_BUILDER_MARKER);
		Registry.register(Registry.BLOCK,new Identifier("blockwake","structure_builder"),STRUCTURE_BUILDER_BLOCK);
		Registry.register(Registry.BLOCK,new Identifier("blockwake","static_cannon"),STATIC_CANNON_BLOCK);
		Registry.register(Registry.BLOCK,new Identifier("blockwake","static_mob_spawner"),STATIC_MOB_SPAWNER);
        Registry.register(Registry.BLOCK,new Identifier("blockwake","static_pirate_spawner"),STATIC_PIRATE_SPAWNER);
		Registry.register(Registry.BLOCK,new Identifier("blockwake","shipwrights_table"),SHIPWRIGHTS_TABLE);
		Registry.register(Registry.BLOCK,new Identifier("blockwake","ship_wheel_block"),SHIP_WHEEL_BLOCK);
	}
	public static final MusketItem MUSKET_ITEM = new MusketItem(new Item.Settings().group(TAB),3,8);
	public static final MusketItem BLUNDERBUSS_ITEM = new MusketItem(new Item.Settings().group(TAB),1,6);
	public static final AstrolabeItem ASTROLABE_ITEM = new AstrolabeItem(new Item.Settings().group(TAB));
	public static final RecruitmentBookItem RECRUITMENT_BOOK_ITEM = new RecruitmentBookItem(new Item.Settings().group(TAB));
	public static final Item SHOT_ITEM = new Item(new Item.Settings().group(TAB));
	public static final Item CANNON_SHOT_ITEM = new Item(new Item.Settings().group(TAB));
	public static final BlocksLoaderItem BLOCKS_LOADER_ITEM = new BlocksLoaderItem(new Item.Settings().group(TAB));
	public static final CommissionItem COMMISSION_ITEM = new CommissionItem(new Item.Settings().group(TAB));
	public static final ShipGrapplerItem SHIP_GRAPPLER_ITEM = new ShipGrapplerItem(new Item.Settings().group(TAB));
	private void registerItems()
	{
		Registry.register(Registry.ITEM,new Identifier("blockwake","musket"),MUSKET_ITEM);
		Registry.register(Registry.ITEM,new Identifier("blockwake","commission_item"),COMMISSION_ITEM);
		Registry.register(Registry.ITEM,new Identifier("blockwake","blocks_loader"),BLOCKS_LOADER_ITEM);
		Registry.register(Registry.ITEM,new Identifier("blockwake","blunderbuss"),BLUNDERBUSS_ITEM);
		Registry.register(Registry.ITEM,new Identifier("blockwake","shot"),SHOT_ITEM);
		Registry.register(Registry.ITEM,new Identifier("blockwake","ship_grappler"),SHIP_GRAPPLER_ITEM);
		Registry.register(Registry.ITEM,new Identifier("blockwake","cannon_shot"),CANNON_SHOT_ITEM);
		Registry.register(Registry.ITEM,new Identifier("blockwake","astrolabe"),ASTROLABE_ITEM);
		Registry.register(Registry.ITEM,new Identifier("blockwake","recruitment_book"),RECRUITMENT_BOOK_ITEM);
		Registry.register(Registry.ITEM, new Identifier("blockwake", "static_cannon"), new BlockItem(STATIC_CANNON_BLOCK, new Item.Settings().group(TAB)));
		Registry.register(Registry.ITEM, new Identifier("blockwake", "shipwrights_table"), new BlockItem(SHIPWRIGHTS_TABLE, new Item.Settings().group(TAB)));
		Registry.register(Registry.ITEM, new Identifier("blockwake", "pirate_spawn_egg"), new SpawnEggItem(PIRATE_ENTITY_ENTITY_TYPE, 0xFF0000, 0x000000, new Item.Settings().group(TAB)));
	}
	public static BlockEntityType<ShipWheelBlockEntity> SHIPWHEEL_BLOCK_ENTITY;
	private void registerBlockEntities()
	{
		SHIPWHEEL_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE,"blockwake:shipwheel_entity",BlockEntityType.Builder.create(ShipWheelBlockEntity::new,SHIP_WHEEL_BLOCK).build(null));
	}


}
