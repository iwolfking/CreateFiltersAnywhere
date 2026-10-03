package xyz.iwolfking.createfiltersanywhere.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import xyz.iwolfking.createfiltersanywhere.CreateFiltersAnywhere;

@EventBusSubscriber(modid = CreateFiltersAnywhere.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();

        generator.addProvider(
            event.includeClient(),
            new ModLanguageProvider(output, "en_us")
        );
    }
}