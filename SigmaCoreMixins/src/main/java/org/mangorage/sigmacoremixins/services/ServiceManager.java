package org.mangorage.sigmacoremixins.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceManager {
    private static final ServiceManager INSTANCE = new ServiceManager();

    public static ServiceManager getInstance() {
        return INSTANCE;
    }

    private final List<IServiceProvider> serviceProviders = new ArrayList<>();

    public <T> void registerProvider(IServiceProvider serviceProvider) {
        serviceProviders.add(serviceProvider);
    }

    private ServiceManager() {}

    protected Map<ServiceId<?>, ?> createServices(Object o) {
        Map<ServiceId<?>, Object> services = new HashMap<>();
        serviceProviders.forEach(p -> {
            services.putAll(p.createServices(o));
        });
        return services;
    }
}
