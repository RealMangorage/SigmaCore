package org.mangorage.sigmacoremixins;

public class Util {
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o) {
        return (T) o;
    }
}
