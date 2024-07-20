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
     * This changes the default value a container uses for its max stack size. This shouldn't
     * override the override a container may have
     */
    @Overwrite
    default int getMaxStackSize() {
        return 99;
    }
}
