package cofh.core.enchantment;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public abstract class EnchantmentOverride extends EnchantmentCoFH {

    protected EnchantmentOverride(Rarity rarityIn, EnchantmentType typeIn, EquipmentSlotType[] slots) {

        super(rarityIn, typeIn, slots);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {

        return super.canApplyAtEnchantingTable(stack) || supportsEnchantment(stack);
    }

    @Override
    public boolean isAllowedOnBooks() {

        return allowOnBooks;
    }

}
