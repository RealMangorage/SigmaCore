package org.mangorage.sigmacoremixins.services;

import java.util.Map;

public interface IServiceProvider {
    Map<ServiceId<?>, ?> createServices(Object object);
}
