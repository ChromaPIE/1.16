package cofh.thermal.core.tileentity.device;

import cofh.lib.util.Utils;
import cofh.lib.util.helpers.MathHelper;
import cofh.lib.xp.XpStorage;
import cofh.thermal.core.inventory.container.device.DeviceFisherContainer;
import cofh.thermal.lib.tileentity.DeviceTileBase;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.BiomeDictionary;

import javax.annotation.Nullable;

import static cofh.lib.util.StorageGroup.OUTPUT;
import static cofh.thermal.core.init.TCoreReferences.DEVICE_FISHER_TILE;
import static cofh.thermal.lib.common.ThermalConfig.deviceAugments;

public class DeviceFisherTile extends DeviceTileBase implements ITickableTileEntity {

    private static final int TIME_CONSTANT = 7200;

    protected boolean cached;
    protected boolean valid;

    protected int process = TIME_CONSTANT / 2;

    public DeviceFisherTile() {

        super(DEVICE_FISHER_TILE);

        inventory.addSlots(OUTPUT, 15, item -> filter.valid(item));

        xpStorage = new XpStorage(getBaseXpStorage());

        addAugmentSlots(deviceAugments);
        initHandlers();
    }

    @Override
    protected void updateValidity() {

        if (world == null || !world.isAreaLoaded(pos, 1) || Utils.isClientWorld(world)) {
            return;
        }
        int adjWaterSource = 0;
        valid = false;

        BlockPos[] cardinals = new BlockPos[]{
                pos.down(),
                pos.north(),
                pos.south(),
                pos.west(),
                pos.east(),
        };
        for (BlockPos adj : cardinals) {
            FluidState state = world.getFluidState(adj);
            if (state.getFluid().equals(Fluids.WATER)) {
                ++adjWaterSource;
            }
        }
        valid = adjWaterSource > 1;
        cached = true;
    }

    @Override
    protected void updateActiveState() {

        if (!cached) {
            updateValidity();
        }
        super.updateActiveState();
    }

    @Override
    protected boolean isValid() {

        return valid;
    }

    @Override
    public void tick() {

        updateActiveState();

        if (!isActive) {
            return;
        }
        --process;
        if (process > 0) {
            return;
        }
        process = getTimeConstant();
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory inventory, PlayerEntity player) {

        return new DeviceFisherContainer(i, world, pos, inventory, player);
    }

    // region HELPERS
    protected int getTimeConstant() {

        if (world == null) {
            return TIME_CONSTANT;
        }
        int constant = TIME_CONSTANT;
        Iterable<BlockPos> area = BlockPos.getAllInBoxMutable(pos.add(-2, -1, -2), pos.add(2, 0, 2));
        for (BlockPos scan : area) {
            FluidState state = world.getFluidState(scan);
            if (state.getFluid().equals(Fluids.WATER)) {
                constant -= 20;
            }
        }
        boolean isOcean = Utils.hasBiomeType(world, pos, BiomeDictionary.Type.OCEAN);
        boolean isRiver = Utils.hasBiomeType(world, pos, BiomeDictionary.Type.RIVER);
        boolean isRaining = world.isRainingAt(pos);

        if (isOcean) {
            constant /= 3;
        }
        if (isRiver) {
            constant /= 2;
        }
        if (isRaining) {
            constant /= 2;
        }
        return MathHelper.clamp(constant, TIME_CONSTANT / 12, TIME_CONSTANT);
    }
    // endregion

    // region AUGMENTS

    // endregion
}
