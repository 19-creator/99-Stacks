package com.nineteen.xcixstacks.mixin;

import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin {

    @Shadow private int maxStackSize;

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "getMaxStackSize()I", at = @At(value = "HEAD"), cancellable = true)
    public final void getMaxStackSize(CallbackInfoReturnable<Integer> cir) {
        if(this.maxStackSize == 64) {
            cir.setReturnValue(99);
        }
    }


}
