package org.mangorage.sigmacoremixins.mixins.core;

import org.bukkit.craftbukkit.entity.CraftEntity;
import org.mangorage.sigmacoremixins.Util;
import org.mangorage.sigmacoremixins.services.IServiceHolder;
import org.mangorage.sigmacoremixins.services.ServiceHolder;
import org.mangorage.sigmacoremixins.services.ServiceId;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CraftEntity.class)
public class CraftEntityMixin implements IServiceHolder {

    @Unique
    private final ServiceHolder<CraftEntityMixin> HOLDER = new ServiceHolder<>(Util.cast(this));

    @Override
    public final <T> T getService(ServiceId<T> serviceId) {
        return HOLDER.getService(serviceId);
    }
}
