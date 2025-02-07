package cofh.ensorcellation;

import cofh.core.init.CoreItems;
import cofh.ensorcellation.init.EnsorcConfig;
import cofh.ensorcellation.init.EnsorcEnchantments;
import cofh.lib.util.DeferredRegisterCoFH;
import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cofh.lib.util.constants.Constants.ID_ENSORCELLATION;

@Mod(ID_ENSORCELLATION)
public class Ensorcellation {

    public static final Logger LOG = LogManager.getLogger(ID_ENSORCELLATION);

    public static final DeferredRegisterCoFH<Enchantment> ENCHANTMENTS = DeferredRegisterCoFH.create(ForgeRegistries.ENCHANTMENTS, ID_ENSORCELLATION);

    public Ensorcellation() {

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        ENCHANTMENTS.register(modEventBus);

        EnsorcEnchantments.register();

        CoreItems.registerHorseArmorOverrides();
        CoreItems.registerShieldOverride();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

        EnsorcConfig.register();
    }

}
