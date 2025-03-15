package xyz.iwolfking.createfiltersanywhere.api.attributes.abstracts.test;

import net.minecraft.world.item.*;
import xyz.iwolfking.createfiltersanywhere.api.attributes.abstracts.StringAttribute;

public class TestStringAttribute extends StringAttribute {
    public TestStringAttribute(String value) {
        super(value);
    }

    @Override
    public String getValue(ItemStack stack) {
        Item item = stack.getItem();
        return switch (item) {
            case SwordItem swordItem -> "Sword";
            case AxeItem axeItem -> "Axe";
            case ShearsItem shearsItem -> "Shears";
            case ShovelItem shovelItem -> "Shovel";
            case PickaxeItem pickaxeItem -> "Pickaxe";
            default -> null;
        };
    }

    @Override
    public String getTranslationKey() {
        return "test_string_attribute";
    }
}
