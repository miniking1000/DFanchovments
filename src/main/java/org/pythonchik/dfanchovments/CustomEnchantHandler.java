package org.pythonchik.dfanchovments;

import net.minecraft.core.registries.BuiltInRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public class CustomEnchantHandler {

    public static void unfreezeRegistry() {
        setFieldValue(BuiltInRegistries.ENCHANTMENT_PROVIDER_TYPE, "l", false); // MappedRegistry#frozen
        setFieldValue(BuiltInRegistries.ENCHANTMENT_PROVIDER_TYPE, "m", new IdentityHashMap<>()); // MappedRegistry#unregisteredIntrusiveHolders
    }

    public static void freezeRegistry() {
        BuiltInRegistries.ENCHANTMENT_PROVIDER_TYPE.freeze();
    }


    public static Field getField(@NotNull Class<?> clazz, @NotNull String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        }
        catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            return superClass == null ? null : getField(superClass, fieldName);
        }
    }

    public static boolean setFieldValue(@NotNull Object of, @NotNull String fieldName, @Nullable Object value) {
        try {
            boolean isStatic = of instanceof Class;
            Class<?> clazz = isStatic ? (Class<?>) of : of.getClass();

            Field field = getField(clazz, fieldName);
            if (field == null) return false;

            field.setAccessible(true);
            field.set(isStatic ? null : of, value);
            return true;
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }
}