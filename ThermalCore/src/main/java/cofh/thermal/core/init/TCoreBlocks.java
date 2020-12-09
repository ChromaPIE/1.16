package cofh.thermal.core.init;

import cofh.core.block.*;
import cofh.core.block.storage.MetalStorageBlock;
import cofh.core.item.BlockItemCoFH;
import cofh.core.util.ProxyUtils;
import cofh.thermal.core.common.ThermalConfig;
import cofh.thermal.core.entity.item.*;
import cofh.thermal.core.inventory.container.device.DeviceHiveExtractorContainer;
import cofh.thermal.core.inventory.container.device.DeviceTreeExtractorContainer;
import cofh.thermal.core.inventory.container.device.DeviceWaterGenContainer;
import cofh.thermal.core.inventory.container.workbench.TinkerBenchContainer;
import cofh.thermal.core.tileentity.device.DeviceHiveExtractorTile;
import cofh.thermal.core.tileentity.device.DeviceTreeExtractorTile;
import cofh.thermal.core.tileentity.device.DeviceWaterGenTile;
import cofh.thermal.core.tileentity.workbench.TinkerBenchTile;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.extensions.IForgeContainerType;

import java.util.function.IntSupplier;
import java.util.function.Predicate;

import static cofh.core.util.helpers.BlockHelper.lightValue;
import static cofh.thermal.core.ThermalCore.*;
import static cofh.thermal.core.common.ThermalAugmentRules.DEVICE_VALIDATOR;
import static cofh.thermal.core.common.ThermalFlags.*;
import static cofh.thermal.core.common.ThermalItemGroups.THERMAL_BLOCKS;
import static cofh.thermal.core.init.TCoreIDs.*;
import static cofh.thermal.core.init.TCoreReferences.*;
import static cofh.thermal.core.util.RegistrationHelper.*;
import static net.minecraft.block.AbstractBlock.Properties.create;

public class TCoreBlocks {

    private TCoreBlocks() {

    }

    public static void register() {

        registerVanilla();
        registerResources();
        registerStorage();
        registerBuildingBlocks();
        registerMisc();

        registerTileBlocks();
        registerTileContainers();
        registerTileEntities();
    }

    public static void setup() {

        FireBlock fire = (FireBlock) Blocks.FIRE;
        fire.setFireInfo(BLOCKS.get(ID_CHARCOAL_BLOCK), 5, 5);
        fire.setFireInfo(BLOCKS.get(ID_GUNPOWDER_BLOCK), 15, 100);
        fire.setFireInfo(BLOCKS.get(ID_SUGAR_CANE_BLOCK), 60, 20);
        fire.setFireInfo(BLOCKS.get(ID_BAMBOO_BLOCK), 60, 20);

        fire.setFireInfo(BLOCKS.get(ID_COAL_COKE_BLOCK), 5, 5);
        fire.setFireInfo(BLOCKS.get(ID_SAWDUST_BLOCK), 10, 10);
        fire.setFireInfo(BLOCKS.get(ID_ROSIN_BLOCK), 5, 5);

        DispenserBlock.registerDispenseBehavior(BLOCKS.get(ID_PHYTO_TNT), TNTBlockCoFH.DISPENSER_BEHAVIOR);
    }

    // region HELPERS
    private static void registerVanilla() {

        registerBlockAndItem(ID_CHARCOAL_BLOCK, () -> new Block(create(Material.WOOD, MaterialColor.BLACK).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).setRequiresTool()),
                () -> new BlockItemCoFH(BLOCKS.get(ID_CHARCOAL_BLOCK), new Item.Properties().group(THERMAL_BLOCKS)).setBurnTime(16000).setShowInGroups(getFlag(FLAG_VANILLA_BLOCKS)));
        registerBlock(ID_GUNPOWDER_BLOCK, () -> new GunpowderBlock(create(Material.TNT, MaterialColor.GRAY).hardnessAndResistance(0.5F).sound(SoundType.SAND).harvestTool(ToolType.SHOVEL)), getFlag(FLAG_VANILLA_BLOCKS));
        registerBlock(ID_SUGAR_CANE_BLOCK, () -> new RotatedPillarBlock(create(Material.ORGANIC, MaterialColor.FOLIAGE).hardnessAndResistance(1.0F).sound(SoundType.CROP).harvestTool(ToolType.HOE)) {

            @Override
            public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {

                entityIn.onLivingFall(fallDistance, 0.6F);
            }
        }, getFlag(FLAG_VANILLA_BLOCKS));
        registerBlock(ID_BAMBOO_BLOCK, () -> new RotatedPillarBlock(create(Material.ORGANIC, MaterialColor.FOLIAGE).hardnessAndResistance(1.0F).sound(SoundType.WOOD).harvestTool(ToolType.AXE)) {

            @Override
            public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {

                entityIn.onLivingFall(fallDistance, 0.8F);
            }
        }, getFlag(FLAG_VANILLA_BLOCKS));

        registerBlock(ID_APPLE_BLOCK, () -> new Block(create(Material.WOOD, MaterialColor.RED).hardnessAndResistance(1.5F).sound(SoundType.WOOD).harvestTool(ToolType.AXE)), getFlag(FLAG_VANILLA_BLOCKS));
        registerBlock(ID_CARROT_BLOCK, () -> new Block(create(Material.WOOD, MaterialColor.ORANGE_TERRACOTTA).hardnessAndResistance(1.5F).sound(SoundType.WOOD).harvestTool(ToolType.AXE)), getFlag(FLAG_VANILLA_BLOCKS));
        registerBlock(ID_POTATO_BLOCK, () -> new Block(create(Material.WOOD, MaterialColor.BROWN_TERRACOTTA).hardnessAndResistance(1.5F).sound(SoundType.WOOD).harvestTool(ToolType.AXE)), getFlag(FLAG_VANILLA_BLOCKS));
        registerBlock(ID_BEETROOT_BLOCK, () -> new Block(create(Material.WOOD, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(1.5F).sound(SoundType.WOOD).harvestTool(ToolType.AXE)), getFlag(FLAG_VANILLA_BLOCKS));
    }

    private static void registerResources() {

        registerBlock(ID_APATITE_ORE, () -> new OreBlockCoFH(1).xp(0, 2), getFlag(FLAG_RESOURCE_APATITE));
        registerBlock(ID_CINNABAR_ORE, () -> new OreBlockCoFH(1).xp(1, 3), getFlag(FLAG_RESOURCE_CINNABAR));
        registerBlock(ID_NITER_ORE, () -> new OreBlockCoFH(1).xp(0, 2), getFlag(FLAG_RESOURCE_NITER));
        registerBlock(ID_SULFUR_ORE, () -> new OreBlockCoFH(1).xp(0, 2), getFlag(FLAG_RESOURCE_SULFUR));

        registerBlock(ID_COPPER_ORE, () -> new OreBlockCoFH(1), getFlag(FLAG_RESOURCE_COPPER));
        registerBlock(ID_TIN_ORE, () -> new OreBlockCoFH(1), getFlag(FLAG_RESOURCE_TIN));
        registerBlock(ID_LEAD_ORE, () -> new OreBlockCoFH(2), getFlag(FLAG_RESOURCE_LEAD));
        registerBlock(ID_SILVER_ORE, () -> new OreBlockCoFH(2), getFlag(FLAG_RESOURCE_SILVER));
        registerBlock(ID_NICKEL_ORE, () -> new OreBlockCoFH(2), getFlag(FLAG_RESOURCE_NICKEL));

        registerBlock(ID_RUBY_ORE, () -> new OreBlockCoFH(2).xp(3, 7), getFlag(FLAG_RESOURCE_RUBY));
        registerBlock(ID_SAPPHIRE_ORE, () -> new OreBlockCoFH(2).xp(3, 7), getFlag(FLAG_RESOURCE_SAPPHIRE));
    }

    private static void registerStorage() {

        registerBlock(ID_APATITE_BLOCK, () -> new Block(create(Material.ROCK, MaterialColor.LIGHT_BLUE_TERRACOTTA).hardnessAndResistance(3.0F, 3.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).setRequiresTool()));
        registerBlock(ID_CINNABAR_BLOCK, () -> new Block(create(Material.ROCK, MaterialColor.RED_TERRACOTTA).hardnessAndResistance(3.0F, 3.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).setRequiresTool()));
        registerBlock(ID_NITER_BLOCK, () -> new Block(create(Material.ROCK, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(3.0F, 3.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).setRequiresTool()));
        registerBlock(ID_SULFUR_BLOCK, () -> new Block(create(Material.ROCK, MaterialColor.YELLOW_TERRACOTTA).hardnessAndResistance(3.0F, 3.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).setRequiresTool()) {

            @Override
            public boolean isFireSource(BlockState state, IWorldReader world, BlockPos pos, Direction side) {

                return side == Direction.UP;
            }
        });

        registerBlock(ID_COPPER_BLOCK, () -> new MetalStorageBlock(1), getFlag(FLAG_RESOURCE_COPPER));
        registerBlock(ID_TIN_BLOCK, () -> new MetalStorageBlock(1), getFlag(FLAG_RESOURCE_TIN));
        registerBlock(ID_LEAD_BLOCK, () -> new MetalStorageBlock(1), getFlag(FLAG_RESOURCE_LEAD));
        registerBlock(ID_SILVER_BLOCK, () -> new MetalStorageBlock(1), getFlag(FLAG_RESOURCE_SILVER));
        registerBlock(ID_NICKEL_BLOCK, () -> new MetalStorageBlock(1), getFlag(FLAG_RESOURCE_NICKEL));

        registerBlock(ID_BRONZE_BLOCK, () -> new MetalStorageBlock(1), getFlag(FLAG_RESOURCE_BRONZE));
        registerBlock(ID_ELECTRUM_BLOCK, () -> new MetalStorageBlock(1), getFlag(FLAG_RESOURCE_ELECTRUM));
        registerBlock(ID_INVAR_BLOCK, () -> new MetalStorageBlock(1), getFlag(FLAG_RESOURCE_INVAR));
        registerBlock(ID_CONSTANTAN_BLOCK, () -> new MetalStorageBlock(1), getFlag(FLAG_RESOURCE_CONSTANTAN));

        registerBlock(ID_SIGNALUM_BLOCK, () -> new MetalStorageBlock(create(Material.IRON, MaterialColor.RED).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).harvestLevel(1).harvestTool(ToolType.PICKAXE).setRequiresTool().setLightLevel(lightValue(7)).notSolid()) {

            @Override
            public boolean canProvidePower(BlockState state) {

                return true;
            }

            @Override
            public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {

                return 15;
            }
        }, Rarity.UNCOMMON);
        registerBlock(ID_LUMIUM_BLOCK, () -> new MetalStorageBlock(create(Material.IRON, MaterialColor.YELLOW).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.METAL).harvestLevel(1).harvestTool(ToolType.PICKAXE).setRequiresTool().setLightLevel(lightValue(15)).notSolid()), Rarity.UNCOMMON);
        registerBlock(ID_ENDERIUM_BLOCK, () -> new MetalStorageBlock(create(Material.IRON, MaterialColor.CYAN).hardnessAndResistance(25.0F, 30.0F).sound(SoundType.METAL).harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool().setLightLevel(lightValue(3)).notSolid()), Rarity.UNCOMMON);

        registerBlock(ID_RUBY_BLOCK, () -> new MetalStorageBlock(MaterialColor.RED, 1), getFlag(FLAG_RESOURCE_RUBY));
        registerBlock(ID_SAPPHIRE_BLOCK, () -> new MetalStorageBlock(MaterialColor.BLUE, 1), getFlag(FLAG_RESOURCE_SAPPHIRE));

        registerBlockAndItem(ID_COAL_COKE_BLOCK, () -> new Block(create(Material.WOOD, MaterialColor.BLACK).hardnessAndResistance(5.0F, 6.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).setRequiresTool()),
                () -> new BlockItemCoFH(BLOCKS.get(ID_COAL_COKE_BLOCK), new Item.Properties().group(THERMAL_BLOCKS)).setBurnTime(32000));
        registerBlockAndItem(ID_SAWDUST_BLOCK, () -> new FallingBlock(create(Material.WOOD).hardnessAndResistance(1.0F, 1.0F).sound(SoundType.SAND).harvestTool(ToolType.SHOVEL)) {

            @OnlyIn(Dist.CLIENT)
            public int getDustColor(BlockState state) {

                return 11507581;
            }
        }, () -> new BlockItemCoFH(BLOCKS.get(ID_SAWDUST_BLOCK), new Item.Properties().group(THERMAL_BLOCKS)).setBurnTime(2400));

        registerBlockAndItem(ID_ROSIN_BLOCK, () -> new Block(create(Material.CLAY, MaterialColor.ADOBE).hardnessAndResistance(2.0F, 4.0F).speedFactor(0.8F).jumpFactor(0.8F).sound(SoundType.HONEY).harvestTool(ToolType.SHOVEL)) {

            @Override
            public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {

                entityIn.onLivingFall(fallDistance, 0.8F);
            }
        }, () -> new BlockItemCoFH(BLOCKS.get(ID_ROSIN_BLOCK), new Item.Properties().group(THERMAL_BLOCKS)).setBurnTime(8000));

        registerBlock(ID_RUBBER_BLOCK, () -> new RubberBlock(create(Material.CLAY, MaterialColor.WHITE_TERRACOTTA).hardnessAndResistance(3.0F, 3.0F).jumpFactor(1.25F).sound(SoundType.GROUND)));
        registerBlock(ID_CURED_RUBBER_BLOCK, () -> new RubberBlock(create(Material.CLAY, MaterialColor.BLACK_TERRACOTTA).hardnessAndResistance(3.0F, 3.0F).jumpFactor(1.25F).sound(SoundType.GROUND)));
        registerBlock(ID_SLAG_BLOCK, () -> new Block(create(Material.ROCK, MaterialColor.BLACK_TERRACOTTA).hardnessAndResistance(3.0F, 3.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).setRequiresTool()));
        registerBlock(ID_RICH_SLAG_BLOCK, () -> new Block(create(Material.ROCK, MaterialColor.BLACK_TERRACOTTA).hardnessAndResistance(3.0F, 3.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).setRequiresTool()));
    }

    private static void registerBuildingBlocks() {

        registerBlock(ID_MACHINE_FRAME, () -> new Block(create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0F).notSolid().harvestTool(ToolType.PICKAXE)), getFlag(ID_MACHINE_FRAME));

        registerBlock(ID_OBSIDIAN_GLASS, () -> new HardenedGlassBlock(create(Material.GLASS, MaterialColor.OBSIDIAN).hardnessAndResistance(5.0F, 1000.0F).sound(SoundType.GLASS).notSolid()));
        registerBlock(ID_SIGNALUM_GLASS, () -> new HardenedGlassBlock(create(Material.GLASS, MaterialColor.RED).hardnessAndResistance(5.0F, 1000.0F).sound(SoundType.GLASS).setLightLevel(lightValue(7)).notSolid()) {

            @Override
            public boolean canProvidePower(BlockState state) {

                return true;
            }

            @Override
            public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {

                return 15;
            }
        }, Rarity.UNCOMMON);
        registerBlock(ID_LUMIUM_GLASS, () -> new HardenedGlassBlock(create(Material.GLASS, MaterialColor.YELLOW).hardnessAndResistance(5.0F, 1000.0F).sound(SoundType.GLASS).setLightLevel(lightValue(15)).notSolid()), Rarity.UNCOMMON);
        registerBlock(ID_ENDERIUM_GLASS, () -> new HardenedGlassBlock(create(Material.GLASS, MaterialColor.CYAN).hardnessAndResistance(5.0F, 1000.0F).sound(SoundType.GLASS).setLightLevel(lightValue(3)).notSolid()), Rarity.UNCOMMON);

        registerBlock(ID_WHITE_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.SNOW).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_ORANGE_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.ADOBE).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_MAGENTA_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.MAGENTA).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_LIGHT_BLUE_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.LIGHT_BLUE).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_YELLOW_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.YELLOW).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_LIME_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.LIME).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_PINK_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.PINK).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_GRAY_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.GRAY).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_LIGHT_GRAY_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.LIGHT_GRAY).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_CYAN_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.CYAN).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_PURPLE_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.PURPLE).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_BLUE_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.BLUE).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_BROWN_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.BROWN).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_GREEN_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.GREEN).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_RED_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.RED).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
        registerBlock(ID_BLACK_ROCKWOOL, () -> new Block(create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(2.0F, 6.0F).sound(SoundType.CLOTH)), getFlag(FLAG_ROCKWOOL));
    }

    private static void registerMisc() {

        registerBlock(ID_PHYTO_TNT, () -> new TNTBlockCoFH(PhytoTNTEntity::new, AbstractBlock.Properties.create(Material.TNT, MaterialColor.GREEN).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), getFlag(FLAG_PHYTOGRO_EXPLOSIVES));

        registerBlock(ID_FIRE_TNT, () -> new TNTBlockCoFH(FireTNTEntity::new, AbstractBlock.Properties.create(Material.TNT, MaterialColor.RED).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), getFlag(FLAG_ELEMENTAL_EXPLOSIVES));
        registerBlock(ID_EARTH_TNT, () -> new TNTBlockCoFH(EarthTNTEntity::new, AbstractBlock.Properties.create(Material.TNT, MaterialColor.OBSIDIAN).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), getFlag(FLAG_ELEMENTAL_EXPLOSIVES));
        registerBlock(ID_ICE_TNT, () -> new TNTBlockCoFH(IceTNTEntity::new, AbstractBlock.Properties.create(Material.TNT, MaterialColor.ICE).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), getFlag(FLAG_ELEMENTAL_EXPLOSIVES));
        registerBlock(ID_LIGHTNING_TNT, () -> new TNTBlockCoFH(LightningTNTEntity::new, AbstractBlock.Properties.create(Material.TNT, MaterialColor.YELLOW).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), getFlag(FLAG_ELEMENTAL_EXPLOSIVES));

        registerBlock(ID_NUKE_TNT, () -> new TNTBlockCoFH(NukeTNTEntity::new, AbstractBlock.Properties.create(Material.TNT, MaterialColor.LIME_TERRACOTTA).hardnessAndResistance(0.0F).sound(SoundType.PLANT)), Rarity.UNCOMMON, getFlag(FLAG_NUCLEAR_EXPLOSIVES));
    }

    private static void registerTileBlocks() {

        IntSupplier deviceAugs = () -> ThermalConfig.deviceAugments;

        registerAugBlock(ID_DEVICE_HIVE_EXTRACTOR, () -> new TileBlock4Way(create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.5F).harvestTool(ToolType.AXE), DeviceHiveExtractorTile::new), deviceAugs, DEVICE_VALIDATOR, getFlag(ID_DEVICE_HIVE_EXTRACTOR));
        registerAugBlock(ID_DEVICE_TREE_EXTRACTOR, () -> new TileBlock4Way(create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.5F).harvestTool(ToolType.AXE), DeviceTreeExtractorTile::new), deviceAugs, DEVICE_VALIDATOR, getFlag(ID_DEVICE_TREE_EXTRACTOR));
        registerAugBlock(ID_DEVICE_WATER_GEN, () -> new TileBlock4Way(create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0F).harvestTool(ToolType.PICKAXE), DeviceWaterGenTile::new), deviceAugs, DEVICE_VALIDATOR, getFlag(ID_DEVICE_WATER_GEN));
        // registerAugBlock(ID_DEVICE_ROCK_GEN, () -> new TileBlock4Way(create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(2.0F).harvestTool(ToolType.PICKAXE), DeviceRockGenTile::new), deviceAugs, DEVICE_VALIDATOR, getFlag(ID_DEVICE_ROCK_GEN));

        IntSupplier workbenchAugs = () -> ThermalConfig.storageAugments;
        Predicate<ItemStack> workbenchValidator = (e) -> true;

        registerAugBlock(ID_TINKER_BENCH, () -> new TileBlock4Way(AbstractBlock.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.5F).harvestTool(ToolType.AXE), TinkerBenchTile::new), workbenchAugs, workbenchValidator, getFlag(ID_TINKER_BENCH));
    }

    private static void registerTileContainers() {

        CONTAINERS.register(ID_DEVICE_HIVE_EXTRACTOR, () -> IForgeContainerType.create((windowId, inv, data) -> new DeviceHiveExtractorContainer(windowId, ProxyUtils.getClientWorld(), data.readBlockPos(), inv, ProxyUtils.getClientPlayer())));
        CONTAINERS.register(ID_DEVICE_TREE_EXTRACTOR, () -> IForgeContainerType.create((windowId, inv, data) -> new DeviceTreeExtractorContainer(windowId, ProxyUtils.getClientWorld(), data.readBlockPos(), inv, ProxyUtils.getClientPlayer())));
        CONTAINERS.register(ID_DEVICE_WATER_GEN, () -> IForgeContainerType.create((windowId, inv, data) -> new DeviceWaterGenContainer(windowId, ProxyUtils.getClientWorld(), data.readBlockPos(), inv, ProxyUtils.getClientPlayer())));
        // CONTAINERS.register(ID_DEVICE_ROCK_GEN, () -> IForgeContainerType.create((windowId, inv, data) -> new DeviceRockGenContainer(windowId, ProxyUtils.getClientWorld(), data.readBlockPos(), inv, ProxyUtils.getClientPlayer())));

        CONTAINERS.register(ID_TINKER_BENCH, () -> IForgeContainerType.create((windowId, inv, data) -> new TinkerBenchContainer(windowId, ProxyUtils.getClientWorld(), data.readBlockPos(), inv, ProxyUtils.getClientPlayer())));
    }

    private static void registerTileEntities() {

        TILE_ENTITIES.register(ID_DEVICE_HIVE_EXTRACTOR, () -> TileEntityType.Builder.create(DeviceHiveExtractorTile::new, DEVICE_HIVE_EXTRACTOR_BLOCK).build(null));
        TILE_ENTITIES.register(ID_DEVICE_TREE_EXTRACTOR, () -> TileEntityType.Builder.create(DeviceTreeExtractorTile::new, DEVICE_TREE_EXTRACTOR_BLOCK).build(null));
        TILE_ENTITIES.register(ID_DEVICE_WATER_GEN, () -> TileEntityType.Builder.create(DeviceWaterGenTile::new, DEVICE_WATER_GEN_BLOCK).build(null));
        // TILE_ENTITIES.register(ID_DEVICE_ROCK_GEN, () -> TileEntityType.Builder.create(DeviceRockGenTile::new, DEVICE_ROCK_GEN_BLOCK).build(null));

        TILE_ENTITIES.register(ID_TINKER_BENCH, () -> TileEntityType.Builder.create(TinkerBenchTile::new, TINKER_BENCH_BLOCK).build(null));
    }
    // endregion
}
