package xyz.iwolfking.createfiltersanywhere.attributes.impl.apotheosis;

import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttributeType;
import net.neoforged.fml.loading.LoadingModList;
import xyz.iwolfking.createfiltersanywhere.api.CreateAttributeRegistry;
import xyz.iwolfking.createfiltersanywhere.api.util.apotheosis.ApotheosisUtil;
import xyz.iwolfking.createfiltersanywhere.attributes.impl.sophisticatedbackpacks.BackpackHasUUIDAttribute;

public class ApotheosisAttributes {
    public static final ItemAttributeType APOTH_GEM_PURITY = new GemPurityAttribute.Type();
    public static final ItemAttributeType APOTH_GEM_BONUS_TYPE = new GemBonusTypeAttribute.Type();

}
