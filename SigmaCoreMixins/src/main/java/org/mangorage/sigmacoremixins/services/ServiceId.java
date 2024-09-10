package org.mangorage.sigmacoremixins.services;

public record ServiceId<T>(Class<T> clazz, String id) {
    public static <T> ServiceId<T> create(Class<T> tClass, String id) {
        return new ServiceId<>(tClass, id);
    }

    public ServiceId<T> extend(String id) {
        return new ServiceId<>(clazz, id);
    }
}
