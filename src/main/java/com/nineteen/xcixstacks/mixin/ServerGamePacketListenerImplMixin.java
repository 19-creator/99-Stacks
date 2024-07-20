package com.nineteen.xcixstacks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ServerboundSetCreativeModeSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

    @Shadow
    public ServerPlayer player;
    @Shadow
    private int dropSpamTickCount;


    /**
     * The purpose of this mixin is to allow stacks of up to 99 to be taken from the creative inventory, as Minecraft by default does not utilize its
     * own method meant for checking the maximum stack size of an item. Instead, it uses the static value 64 for this check. This mixin corrects that
     * flawed logic.
     */
    @Inject(method = "handleSetCreativeModeSlot(Lnet/minecraft/network/protocol/game/ServerboundSetCreativeModeSlotPacket;)V", at = @At(value = "HEAD"), cancellable = true)
    public void handleSetCreativeModeSlot(ServerboundSetCreativeModeSlotPacket pPacket, CallbackInfo ci) {
        ci.cancel();
        ServerGamePacketListenerImpl this_ = (ServerGamePacketListenerImpl)((Object)this);

        PacketUtils.ensureRunningOnSameThread(pPacket, this_, this.player.serverLevel());
        if (this.player.gameMode.isCreative()) {
            boolean flag = pPacket.getSlotNum() < 0;
            ItemStack itemstack = pPacket.getItem();
            if (!itemstack.isItemEnabled(this.player.level().enabledFeatures())) {
                return;
            }

            CompoundTag compoundtag = BlockItem.getBlockEntityData(itemstack);
            if (!itemstack.isEmpty() && compoundtag != null && compoundtag.contains("x") && compoundtag.contains("y") && compoundtag.contains("z")) {
                BlockPos blockpos = BlockEntity.getPosFromTag(compoundtag);
                if (this.player.level().isLoaded(blockpos)) {
                    BlockEntity blockentity = this.player.level().getBlockEntity(blockpos);
                    if (blockentity != null) {
                        blockentity.saveToItem(itemstack);
                    }
                }
            }

            boolean flag1 = pPacket.getSlotNum() >= 1 && pPacket.getSlotNum() <= 45;
            boolean flag2 = itemstack.isEmpty() || itemstack.getDamageValue() >= 0 && itemstack.getCount() <= itemstack.getMaxStackSize() && !itemstack.isEmpty(); //Only this line was changed
            if (flag1 && flag2) {
                this.player.inventoryMenu.getSlot(pPacket.getSlotNum()).setByPlayer(itemstack);
                this.player.inventoryMenu.broadcastChanges();
            } else if (flag && flag2 && this.dropSpamTickCount < 200) {
                this.dropSpamTickCount += 20;
                this.player.drop(itemstack, true);
            }
        }
    }


}
