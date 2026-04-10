package com.example.fastcrystal;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class FastCrystalMod implements ModInitializer {

    @Override
    public void onInitialize() {}

    @Mixin(EndCrystalEntity.class)
    public static class CrystalMixin {
        @Inject(method = "tick", at = @At("HEAD"))
        private void tick(CallbackInfo ci) {
            EndCrystalEntity c = (EndCrystalEntity)(Object)this;
            World w = c.getWorld();

            if (!w.isClient && c.age > 1) {
                w.createExplosion(c, c.getX(), c.getY(), c.getZ(), 6f, World.ExplosionSourceType.TNT);
                c.discard();
            }
        }
    }

    @Mixin(PlayerEntity.class)
    public static class PlayerMixin {
        @Inject(method = "tick", at = @At("HEAD"))
        private void tick(CallbackInfo ci) {
            PlayerEntity p = (PlayerEntity)(Object)this;

            if (p.getMainHandStack().getItem() == Items.END_CRYSTAL) {
                p.getItemCooldownManager().remove(Items.END_CRYSTAL);
            }
        }
    }
}
