package org.mangorage.sigmacoremixins.services;

import org.mangorage.sigmacoremixins.Util;

import java.util.HashMap;
import java.util.Map;

public final class ServiceHolder<O> {
    private final Map<ServiceId<?>, ?> services = new HashMap<>();
    private final O object;

    public ServiceHolder(O object) {
        this.object = object;
        ServiceManager.getInstance().createServices(object).forEach((i, s) -> {
            services.put(i, Util.cast(s));
        });
    }

    @SuppressWarnings("unchecked")
    public <T> T getService(ServiceId<T> serviceId) {
        return (T) services.get(serviceId);
    }

}
