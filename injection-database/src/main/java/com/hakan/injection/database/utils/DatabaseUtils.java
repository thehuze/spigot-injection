package com.hakan.injection.database.utils;

import com.hakan.injection.database.annotations.Table;
import com.hakan.injection.database.connection.result.DbResult;
import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.function.BiFunction;

/**
 * DatabaseUtils is the utility class
 * for database processes.
 */
public class DatabaseUtils {

    /**
     * Creates a proxy instance from
     * the given interface.
     *
     * @param clazz    interface
     * @param function function
     * @return proxy instance
     */
    public static @Nonnull Object createProxy(@Nonnull Class<?> clazz,
                                              @Nonnull ProxyFunction function) {
        return Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[]{clazz},
                (proxy, method, args) -> function.apply(method, args)
        );
    }

    /**
     * Creates an instance from the given
     * {@link DbResult} and {@link Class}.
     *
     * @param dbResult result
     * @param clazz    class
     * @param <T>      type
     * @return instance
     */
    @SneakyThrows
    public static @Nonnull <T> T createInstance(@Nonnull DbResult dbResult,
                                                @Nonnull Class<T> clazz) {
        T instance = clazz.newInstance();
        String table = clazz.getAnnotation(Table.class).value();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);

            if (field.getType().isPrimitive() || field.getType().equals(String.class)) {
                field.set(instance, dbResult.getObject(table + "." + field.getName()));
                continue;
            }

            field.set(instance, createInstance(dbResult, field.getType()));
        }

        return instance;
    }



    /**
     * ProxyFunction is the callback function
     * for {@link Proxy#newProxyInstance(ClassLoader, Class[], InvocationHandler)}.
     */
    public interface ProxyFunction extends BiFunction<Method, Object[], Object> {

    }
}
