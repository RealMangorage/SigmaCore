package org.mangorage.sigmacoremixins.services;

import java.util.Optional;

public interface IServiceHolder {
    static IServiceHolder get(Object o) {
        return (IServiceHolder) o;
    }

    <T> T getService(ServiceId<T> serviceId);

    default <T> Optional<T> getServiceOptional(ServiceId<T> serviceId) {
        var r = getService(serviceId);
        return r == null ? Optional.empty() : Optional.of(r);
    }
}
