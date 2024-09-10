package org.mangorage.sigmacoremixins.services;

public interface IServiceHolder {
    <T> T getService(ServiceId<T> serviceId);
}
