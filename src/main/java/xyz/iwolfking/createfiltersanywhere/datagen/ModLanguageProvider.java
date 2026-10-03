package xyz.iwolfking.createfiltersanywhere.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider; // Or net.minecraftforge.common.data.LanguageProvider on Forge
import xyz.iwolfking.createfiltersanywhere.CreateFiltersAnywhere;

public class ModLanguageProvider extends LanguageProvider {

    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, CreateFiltersAnywhere.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        addAttribute("has_backpack_uuid", "is a backpack with a UUID of '%1$s'", "isn't a backpack with a UUID of '%1$s'");
        addAttribute("has_backpack_upgrade", "is a backpack with a '%1$s'", "isn't a backpack with a '%1$s'");

        addAttribute("apoth_gem_purity", "has a '%1$s' purity", "doesn't have a '%1$s' purity");
        addAttribute("apoth_gem_unique", "is a unique Apotheosis gem", "isn't a unique Apotheosis gem");
        addAttribute("apoth_gem_bonus_type", "is an Apotheosis gem with a '%1$s' bonus", "isn't an Apotheosis gem with a '%1$s' bonus");
        addAttribute("apoth_gem_id", "is a '%1$s'", "isn't a '%1$s'");
        addAttribute("apoth_loot_rarity", "has a '%1$s' rarity", "doesn't have a '%1$s' rarity");
        addAttribute("apoth_has_affix", "has '%1$s' affix", "doesn't have '%1$s' affix");
        addAttribute("apoth_has_rarity", "has an Apotheosis rarity", "doesn't have an Apotheosis rarity");
        addAttribute("apoth_socket_count", "has at least '%1$s' sockets", "doesn't have at least '%1$s' sockets");
        addAttribute("apoth_socket_count_empty", "has at least '%1$s' empty sockets", "doesn't have at least '%1$s' empty sockets");

        addAttribute("is_uncharged", "is uncharged", "is charged");
        addAttribute("can_be_charged", "can be charged", "cannot be charged");
        addAttribute("is_fully_charged", "is fully charged", "isn't fully charged");
        addAttribute("has_data_component", "has a '%1$s' data component", "doesn't have a '%1$s' data component");
        addAttribute("is_item", "is a %1$s", "isn't a %1$s");
    }

    /**
     * Helper to register both the regular and inverted Create attribute translation keys.
     *
     * @param attributeId The string ID used in your ItemAttribute getTranslationKey()
     * @param positive    The text when condition matches (e.g., "is a backpack")
     * @param inverted    The text when inverted (e.g., "isn't a backpack")
     */
    private void addAttribute(String attributeId, String positive, String inverted) {
        add("create.item_attributes." + attributeId, positive);
        add("create.item_attributes." + attributeId + ".inverted", inverted);
    }
}