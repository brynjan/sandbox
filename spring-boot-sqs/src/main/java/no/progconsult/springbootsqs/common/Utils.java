package no.progconsult.springbootsqs.common;

import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class Utils {

    public static <T> Optional<T> getFirstOptional(List<T> distributionPoints) {
        return distributionPoints.stream().findFirst();
    }

    public static <U, V> Map<U, V> nullToEmptyMap(Map<U, V> map) {
        return map == null ? new HashMap<>() : map;
    }

    public static <T> List<T> nullToEmptyList(List<T> list) {
        return list == null ? new ArrayList<T>() : list;
    }


    /**
     * Parse string format "key1:value1,key2:value2" into map.
     */
    public static Map<String, String> parsePropertyToMap(String mappedProperty) {
        if (StringUtils.isBlank(mappedProperty)) {
            return Collections.emptyMap();
        }

        return Arrays.stream(mappedProperty.split(", *"))
                .collect(Collectors.toMap(m -> m.split(":")[0], m -> m.split(":")[1]));
    }

    /**
     * Safe mapping from String to enum. Returns Optional.
     *
     * @param enumClass
     * @param stringValue
     * @param <T>
     * @return the enum value or empty if not found
     */
    public static <T extends Enum<T>> Optional<T> valueOf(final Class<T> enumClass, final String stringValue) {
        if (enumClass == null) {
            return empty();
        }
        for (final T value : enumClass.getEnumConstants()) {
            if (value.name().equals(stringValue) || value.toString().equals(stringValue)) {
                return of(value);
            }
        }
        return empty();
    }

    /**
     * Map between enum by enum name.
     *
     * @param enumType destination enum type.
     * @param value    source enum value
     * @param <T>      source type
     * @param <E>      destination type
     * @return the destination enum value.
     */
    public static <T extends Enum<T>, E extends Enum<E>> Optional<T> valueOf(final Class<T> enumType, final E value) {
        if (Objects.isNull(value)) {
            return empty();
        }
        return valueOf(enumType, value.name());
    }
}
