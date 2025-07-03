package xyz.iwolfking.createfiltersanywhere.mixin.create;


import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.content.logistics.filter.*;
import com.simibubi.create.content.logistics.item.filter.attribute.ItemAttribute;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

/*
This code is a port of the functionality from Vault Filters for Minecraft 1.18.2
The original code was done by the incredible rizek, most of the credit goes to them other than the adjustments needed for 1.21.1.
*/
@Mixin(value = AttributeFilterScreen.class, remap = false)
public abstract class MixinAttributeFilterScreen extends AbstractFilterScreen<AttributeFilterMenu> {
    protected MixinAttributeFilterScreen(AttributeFilterMenu menu, Inventory inv,
                                         Component title, AllGuiTextures background) {
        super(menu, inv, title, background);
    }

    // placing attr filter inside empty filter will take its attributes
    @Inject(at = @At(value = "HEAD"), method = "referenceItemChanged")
    public void copyCurrentAttrFilter(ItemStack stack, CallbackInfo ci) {
        if (((AttributeFilterMenuAccessor)this.menu).getSelectedAttributes().isEmpty()
                && FilterItemStack.of(stack) instanceof FilterItemStack.AttributeFilterItemStack attrFilter) {

            var currentTests = attrFilter.attributeTests;
            for (var test: currentTests){
                this.create_Filters_Anywhere$addAttr(test.getFirst(), test.getSecond());
            }
        }
    }

    // deletion logic below
    @Shadow private List<Component> selectedAttributes;
    @Shadow private Component selectedT;

    @Unique private int create_Filters_Anywhere$selectedAttrIndex = 0;
    @Unique private int create_Filters_Anywhere$deletionLastTick = 0;
    @Unique private int create_Filters_Anywhere$deletionProgressTick = 0;



    // deletion tooltip
    @Unique private static final Component create_Filters_Anywhere$delTooltipLine =  Component.literal("Hold [").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC)
            .append(Component.literal("DEL").withStyle(ChatFormatting.WHITE))
            .append(Component.literal("] to remove attribute"));

    @Inject(method = "init", at = @At("TAIL"), remap = true)
    private void addDelTooltipLine(CallbackInfo ci) {
        if (this.selectedAttributes.size() > 1) {
            this.selectedAttributes.add(create_Filters_Anywhere$delTooltipLine);
        }
    }
    @Inject(method = "handleAddedAttibute", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"))
    private void rmDelTooltipLine(boolean inverted, CallbackInfoReturnable<Boolean> cir) {
        this.selectedAttributes.remove(create_Filters_Anywhere$delTooltipLine);
    }

    @Inject(method = "handleAddedAttibute", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER))
    private void addDelTooltipLine(boolean inverted, CallbackInfoReturnable<Boolean> cir) {
        this.selectedAttributes.add(create_Filters_Anywhere$delTooltipLine);
    }
    @WrapOperation(method = "renderForeground", at = @At(value = "INVOKE", target = "Ljava/util/List;size()I"))
    private int renderForeground(List<Component> instance, Operation<Integer> original) {
        if (!instance.isEmpty() && instance.get(instance.size() - 1) == create_Filters_Anywhere$delTooltipLine) {
            return instance.size() - 1; // don't count the deletion tooltip line
        }
        return original.call(instance);
    }

    // handle hold to delete and highlighting
    @Inject(method = "containerTick", at = @At("TAIL"), remap = true)
    private void attributeDeletionTick(CallbackInfo ci) {
        if (create_Filters_Anywhere$pressDel()) {
            int idx = create_Filters_Anywhere$selectedAttrIndex;
            List<ItemAttribute.ItemAttributeEntry> selectedAttrs = ((AttributeFilterMenuAccessor) this.menu).getSelectedAttributes();
            if (idx >= 0 && idx < selectedAttrs.size()) {
                ItemAttribute.ItemAttributeEntry selectedAttr = selectedAttrs.get(idx);
                this.create_Filters_Anywhere$removeAttr(selectedAttr);
                create_Filters_Anywhere$selectedAttrIndex = Math.max(0, idx - 1);
                return;
            }
        }
        int idx = create_Filters_Anywhere$selectedAttrIndex;
        if (idx >= 0 && idx + 1 < this.selectedAttributes.size()) {
            float progress = Math.min(Math.max(0, create_Filters_Anywhere$deletionProgressTick), 20) / 20f;
            var selected = this.selectedAttributes.get(idx + 1);
            String text = selected.getString();
            int redChars = (int) (text.length() * progress);
            Component redPart = Component.literal(text.substring(0, redChars)).withStyle(ChatFormatting.RED);
            Component whitePart = Component.literal(text.substring(redChars)).withStyle(ChatFormatting.WHITE);
            this.selectedAttributes.set(idx + 1, Component.literal("").append(redPart).append(whitePart));
        }
    }

    // more hold to delete logic
    @Unique private boolean create_Filters_Anywhere$pressDel() {
        var pl = Minecraft.getInstance().player;
        if (pl == null) {
            return false;
        }
        var tc = pl.tickCount;
        if (tc < create_Filters_Anywhere$deletionLastTick || create_Filters_Anywhere$deletionProgressTick < 0) {
            create_Filters_Anywhere$deletionProgressTick = 0;
        }
        if (tc != create_Filters_Anywhere$deletionLastTick) {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_DELETE)) {
                create_Filters_Anywhere$deletionProgressTick++;
            } else {
                create_Filters_Anywhere$deletionProgressTick--;
            }
        }
        create_Filters_Anywhere$deletionLastTick = tc;

        if (create_Filters_Anywhere$deletionProgressTick > 20) {
            create_Filters_Anywhere$deletionProgressTick = 0;
            return true;
        }
        return false;
    }

    @Unique private void create_Filters_Anywhere$addAttr(ItemAttribute itemAttribute, boolean inverted) {
        CompoundTag tag = ItemAttribute.saveStatic(itemAttribute, Minecraft.getInstance().level.registryAccess());
        CatnipServices.NETWORK
                .sendToServer(new FilterScreenPacket(inverted ? FilterScreenPacket.Option.ADD_INVERTED_TAG : FilterScreenPacket.Option.ADD_TAG, tag));
        this.menu.appendSelectedAttribute(itemAttribute, inverted);
        if (((AttributeFilterMenuAccessor) this.menu).getSelectedAttributes().size() == 1) {
            this.selectedAttributes.set(0, this.selectedT.plainCopy().withStyle(ChatFormatting.YELLOW));
        }
        this.selectedAttributes.remove(create_Filters_Anywhere$delTooltipLine);
        this.selectedAttributes.add(Component.literal("- ").append(itemAttribute.format(inverted)).withStyle(ChatFormatting.GRAY));
        this.selectedAttributes.add(create_Filters_Anywhere$delTooltipLine);
    }

    // deletes all attrs and readds everything except the one that should be removed
    @Unique private void create_Filters_Anywhere$removeAttr(ItemAttribute.ItemAttributeEntry itemAttributeEntry) {;
        var currentAttributes = new ArrayList<>(((AttributeFilterMenuAccessor) this.menu).getSelectedAttributes());
        if (currentAttributes.remove(itemAttributeEntry)) {
            this.menu.clearContents();
            this.contentsCleared();
            this.menu.sendClearPacket();
            for (ItemAttribute.ItemAttributeEntry attr : currentAttributes) {
                this.create_Filters_Anywhere$addAttr(attr.attribute(), attr.inverted());
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        var res = super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        if (this.hoveredSlot != null && this.hoveredSlot.index == 37) {
            var idx = create_Filters_Anywhere$selectedAttrIndex;
            if (scrollY < 0 && idx + 1 < this.selectedAttributes.size() - 2) {
                create_Filters_Anywhere$selectedAttrIndex = idx + 1;
                var oldAt = this.selectedAttributes.get(idx + 1);
                this.selectedAttributes.set(idx + 1, Component.literal(oldAt.getString()).withStyle(ChatFormatting.GRAY));
                var newAt = this.selectedAttributes.get(idx + 2);
                this.selectedAttributes.set(idx + 2, Component.literal(newAt.getString()).withStyle(ChatFormatting.WHITE));
                create_Filters_Anywhere$deletionProgressTick = 0;
            } else if (scrollY > 0 && idx > 0) {
                create_Filters_Anywhere$selectedAttrIndex = idx - 1;
                if (this.selectedAttributes.size() > idx + 1) {
                    var oldAt = this.selectedAttributes.get(idx + 1);
                    this.selectedAttributes.set(idx + 1, Component.literal(oldAt.getString()).withStyle(ChatFormatting.GRAY));
                }
                var newAt = this.selectedAttributes.get(idx);
                this.selectedAttributes.set(idx, Component.literal(newAt.getString()).withStyle(ChatFormatting.WHITE));
                create_Filters_Anywhere$deletionProgressTick = 0;
            }
        }
        return res;
    }
}
