package xyz.iwolfking.createfiltersanywhere.api.integration.ftbquests;

import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import dev.ftb.mods.ftbquests.api.ItemFilterAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class CreateItemFilterAdapter implements ItemFilterAdapter {

    @Override
    public String getName() {
        return "Create";
    }

    @Override
    public boolean isFilterStack(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof FilterItem;
    }

    @Override
    public boolean doesItemMatch(ItemStack filterStack, ItemStack toCheck, HolderLookup.Provider provider) {
        if (!isFilterStack(filterStack) || toCheck.isEmpty()) {
            return false;
        }

        Level level = resolveLevel();
        FilterItemStack wrapper = FilterItemStack.of(filterStack);

        return wrapper.test(level, toCheck);
    }

    @Override
    public Matcher getMatcher(ItemStack itemStack, HolderLookup.Provider provider) {
        if (!isFilterStack(itemStack)) {
            return NO_MATCH;
        }

        FilterItemStack wrapper = FilterItemStack.of(itemStack.copy());

        return stackToTest -> {
            if (stackToTest.isEmpty()) {
                return false;
            }
            Level level = resolveLevel();
            return wrapper.test(level, stackToTest);
        };
    }

    @Override
    public boolean hasItemTagFilter() {
        return false;
    }

    @Override
    public ItemStack makeTagFilterStack(TagKey<Item> tag) {
        return ItemStack.EMPTY;
    }

    private Level resolveLevel() {
        if (FMLLoader.getDist().isClient()) {
            Level clientLevel = Minecraft.getInstance().level;
            if (clientLevel != null) {
                return clientLevel;
            }
        }

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            return server.getLevel(Level.OVERWORLD);
        }

        return null;
    }
}