package cofh.thermal.core.util.recipes.machine;

import cofh.thermal.lib.util.recipes.MachineRecipeSerializer;
import cofh.thermal.lib.util.recipes.ThermalRecipe;
import com.google.gson.JsonObject;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;

import static cofh.lib.util.recipes.RecipeJsonUtils.*;

public class InsolatorRecipeSerializer<T extends ThermalRecipe> extends MachineRecipeSerializer<T> {

    protected final int defaultWater;

    public InsolatorRecipeSerializer(IFactory<T> recipeFactory, int defaultEnergy, int defaultWater) {

        super(recipeFactory, defaultEnergy);
        this.defaultWater = defaultWater;
    }

    @Override
    public T read(ResourceLocation recipeId, JsonObject json) {

        int energy = defaultEnergy;
        int water = defaultWater;
        float experience = 0.0F;

        ArrayList<Ingredient> inputItems = new ArrayList<>();
        ArrayList<FluidStack> inputFluids = new ArrayList<>();
        ArrayList<ItemStack> outputItems = new ArrayList<>();
        ArrayList<Float> outputItemChances = new ArrayList<>();
        ArrayList<FluidStack> outputFluids = new ArrayList<>();

        /* INPUT */
        if (json.has(INGREDIENT)) {
            parseInputs(inputItems, inputFluids, json.get(INGREDIENT));
        } else if (json.has(INGREDIENTS)) {
            parseInputs(inputItems, inputFluids, json.get(INGREDIENTS));
        } else if (json.has(INPUT)) {
            parseInputs(inputItems, inputFluids, json.get(INPUT));
        } else if (json.has(INPUTS)) {
            parseInputs(inputItems, inputFluids, json.get(INPUTS));
        }

        /* OUTPUT */
        if (json.has(RESULT)) {
            parseOutputs(outputItems, outputItemChances, outputFluids, json.get(RESULT));
        } else if (json.has(RESULTS)) {
            parseOutputs(outputItems, outputItemChances, outputFluids, json.get(RESULTS));
        } else if (json.has(OUTPUT)) {
            parseOutputs(outputItems, outputItemChances, outputFluids, json.get(OUTPUT));
        } else if (json.has(OUTPUTS)) {
            parseOutputs(outputItems, outputItemChances, outputFluids, json.get(OUTPUTS));
        }

        /* ENERGY */
        if (json.has(ENERGY)) {
            energy = json.get(ENERGY).getAsInt();
        }
        if (json.has(ENERGY_MOD)) {
            energy *= json.get(ENERGY_MOD).getAsFloat();
        }
        /* EXPERIENCE */
        if (json.has(EXPERIENCE)) {
            experience = json.get(EXPERIENCE).getAsFloat();
        }
        /* WATER */
        if (json.has(WATER)) {
            water = json.get(WATER).getAsInt();
        }
        if (json.has(WATER_MOD)) {
            water *= json.get(WATER_MOD).getAsFloat();
        }
        if (inputFluids.isEmpty()) {
            inputFluids.add(new FluidStack(Fluids.WATER, water));
        }
        return factory.create(recipeId, energy, experience, inputItems, inputFluids, outputItems, outputItemChances, outputFluids);
    }

}
