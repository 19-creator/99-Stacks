package com.nineteen.xcixstacks.mixin;

import net.minecraft.world.Container;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@SuppressWarnings({"unused", "MissingUnique", "AddedMixinMembersNamePattern"})
@Mixin(Container.class)
public interface ContainerMixin {
    /**
     * @author Nineteen
     * @reason Can't inject into interface method.
     */
    @Overwrite
    default int getMaxStackSize() {
        return 99;
    }
}
