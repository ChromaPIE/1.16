package cofh.thermal.core.util.recipes.dynamo;

import cofh.thermal.core.init.TCoreRecipeTypes;
import cofh.thermal.lib.util.recipes.ThermalFuel;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static cofh.thermal.core.ThermalCore.RECIPE_SERIALIZERS;

public class StirlingFuel extends ThermalFuel {

    public StirlingFuel(ResourceLocation recipeId, int energy, @Nullable List<Ingredient> inputItems, @Nullable List<FluidStack> inputFluids) {

        super(recipeId, energy, inputItems, inputFluids);
    }

    @Nonnull
    @Override
    public IRecipeSerializer<?> getSerializer() {

        return RECIPE_SERIALIZERS.get(TCoreRecipeTypes.ID_FUEL_STIRLING);
    }

    @Nonnull
    @Override
    public IRecipeType<?> getType() {

        return TCoreRecipeTypes.FUEL_STIRLING;
    }

    //    @Nonnull
    //    @Override
    //    public String getGroup() {
    //
    //        return DYNAMO_STIRLING_BLOCK.getTranslationKey();
    //    }
    //
    //    @Nonnull
    //    @Override
    //    public ItemStack getIcon() {
    //
    //        return new ItemStack(DYNAMO_STIRLING_BLOCK);
    //    }

}
