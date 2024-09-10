package org.mangorage.sigmacoremixins.services;

import org.mangorage.sigmacoremixins.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ServiceProviderBuilder {

    public static ServiceProviderBuilder of() {
        return new ServiceProviderBuilder();
    }

    private record Provider<T>(ServiceId<T> serviceId, Predicate<Object> predicate, Function<Object, T> supplier) {};

    private final List<Provider<?>> list = new ArrayList<>();

    private ServiceProviderBuilder() {}

    public <T> ServiceProviderBuilder put(ServiceId<T> serviceId, Predicate<Object> predicate, Function<Object, T> supplier) {
        list.add(new Provider<T>(serviceId, predicate, supplier));
        return this;
    }

    public IServiceProvider build() {
        return new BuiltProvider(list);
    }

    private record BuiltProvider(List<Provider<?>> list) implements IServiceProvider {
        @Override
        public Map<ServiceId<?>, ?> createServices(Object object) {
            Map<ServiceId<?>, ?> services = new HashMap<>();

            list.forEach(p -> {
                if (p.predicate().test(object)) {
                    services.put(
                            p.serviceId(),
                            Util.cast(p.supplier().apply(object))
                    );
                }
            });

            return services;
        }
    }
}
