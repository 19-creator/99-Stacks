package com.nineteen.xcixstacks.mixin;

import org.spongepowered.asm.mixin.Mixin;

@SuppressWarnings({"unused", "MissingUnique", "AddedMixinMembersNamePattern"})
@Mixin(targets = "net.minecraft.world.Container")
public interface ContainerMixin {
    default int getMaxStackSize() {
        return 99;
    }
}
