package gd.rf.acro.pap;

import gd.rf.acro.pap.blocks.RandomDecayingBlock;
import gd.rf.acro.pap.blocks.ShipBuilderBlock;
import gd.rf.acro.pap.blocks.StaticCannonBlock;
import gd.rf.acro.pap.blocks.StructureBuilderBlock;
import gd.rf.acro.pap.entities.SailingShipEntity;
import gd.rf.acro.pap.items.AstrolabeItem;
import gd.rf.acro.pap.items.MusketItem;
import gd.rf.acro.pap.world.PortTownFeature;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.tag.FabricTagBuilder;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.tag.GlobalTagAccessor;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.Registry;
import gd.rf.acro.pap.config.ConfigLoader;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.ChanceDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


public class PiratesAndPlunderers implements ModInitializer {

	public static ConfigLoader config;
	public static Logger logger = LogManager.getLogger();
	public static List<Resource> buildings = new ArrayList<>();
	public static List<Resource> ships = new ArrayList<>();
	private static final String[] ship_names = {"ship1"};
	private static final String[] structure_names = {"port_town_bottom_floor","port_town_farm","port_town_tannery","port_town_archery","port_town_library"};

	public static final Tag<Block> BOAT_MATERIAL = TagRegistry.block(new Identifier("pap","boat_material"));

	public static final EntityType<SailingShipEntity> SAILING_BOAT_ENTITY_ENTITY_TYPE =
			Registry.register(Registry.ENTITY_TYPE,new Identifier("pap","sail_boat")
					, FabricEntityTypeBuilder.create(SpawnGroup.AMBIENT,SailingShipEntity::new).dimensions(EntityDimensions.fixed(10,10)).trackable(100,4).build());

	private static final Feature<DefaultFeatureConfig> PORT_TOWN = Registry.register(
			Registry.FEATURE,
			new Identifier("pap", "port_town"),
			new PortTownFeature(DefaultFeatureConfig.CODEC)
	);

	@Override
	public void onInitialize() {
		config = new ConfigLoader();
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		registerBlocks();
		registerItems();
		logger.info("Hello Fabric world!");
    
		FabricDefaultAttributeRegistry.register(SAILING_BOAT_ENTITY_ENTITY_TYPE, MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D));

		Biomes.BEACH.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES,PORT_TOWN.configure(new DefaultFeatureConfig()).createDecoratedFeature(Decorator.CHANCE_HEIGHTMAP.configure(new ChanceDecoratorConfig(100))));

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new IdentifiableResourceReloadListener() {
			@Override
			public CompletableFuture<Void> reload(Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
				return CompletableFuture.runAsync(() -> {
					try {

						for (String name:ship_names)
						{
							FileUtils.writeLines(new File("./config/PiratesAndPlunderers/ships/"+ name+".blocks"),
									IOUtils.readLines(manager.getResource(new Identifier("pap","ships_default/"+name+".blocks")).getInputStream(),"utf-8"));
						}
						for (String name:structure_names)
						{
							FileUtils.writeLines(new File("./config/PiratesAndPlunderers/buildings/"+ name+".blocks"),
									IOUtils.readLines(manager.getResource(new Identifier("pap","structures_default/"+name+".blocks")).getInputStream(),"utf-8"));
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}).thenCompose(aVoid -> synchronizer.whenPrepared(null));
			}

			@Override
			public Identifier getFabricId() {
				return new Identifier("pap","data");
			}
		});

	}

	private static void copyDefaultFiles()
	{

	}


	public static final ShipBuilderBlock SHIP_BUILDER_BLOCK = new ShipBuilderBlock(FabricBlockSettings.of(Material.METAL).build());
	public static final StructureBuilderBlock STRUCTURE_BUILDER_BLOCK = new StructureBuilderBlock(FabricBlockSettings.of(Material.METAL).build());
	public static final RandomDecayingBlock SHIP_BUILDER_MARKER = new RandomDecayingBlock(FabricBlockSettings.of(Material.METAL).strength(-1,3600000.0F).ticksRandomly().build());
	public static final StaticCannonBlock STATIC_CANNON_BLOCK = new StaticCannonBlock(FabricBlockSettings.of(Material.METAL).build());
	private void registerBlocks()
	{
		Registry.register(Registry.BLOCK,new Identifier("pap","ship_builder"),SHIP_BUILDER_BLOCK);
		Registry.register(Registry.BLOCK,new Identifier("pap","ship_builder_marker"),SHIP_BUILDER_MARKER);
		Registry.register(Registry.BLOCK,new Identifier("pap","structure_builder"),STRUCTURE_BUILDER_BLOCK);
		Registry.register(Registry.BLOCK,new Identifier("pap","static_cannon"),STATIC_CANNON_BLOCK);
	}
	public static final MusketItem MUSKET_ITEM = new MusketItem(new Item.Settings().group(ItemGroup.MISC));
	public static final MusketItem BLUNDERBUSS_ITEM = new MusketItem(new Item.Settings().group(ItemGroup.MISC));
	public static final AstrolabeItem ASTROLABE_ITEM = new AstrolabeItem(new Item.Settings().group(ItemGroup.MISC));
	private void registerItems()
	{
		Registry.register(Registry.ITEM,new Identifier("pap","musket"),MUSKET_ITEM);
		Registry.register(Registry.ITEM,new Identifier("pap","blunderbuss"),BLUNDERBUSS_ITEM);
		Registry.register(Registry.ITEM,new Identifier("pap","astrolabe"),ASTROLABE_ITEM);
	}
}
