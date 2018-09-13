package com.swissquote.foundation.serialization.jackson;

import java.util.Map;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;

public class TypeUtils {

	public static boolean isKeySimpleType(JavaType mapJavaType) {
		// this is better not to use the first key found in the map to check the typing because
		// the value could be null
		//		Map.Entry<?, ?> entry = value.entrySet().iterator().next();
		//		JavaType keyType = provider.getConfig().constructType(key.getClass());
		JavaType keyType = mapJavaType.getKeyType();
		Class<?> rawClass = keyType.getRawClass();
		return keyType.isPrimitive()
				|| keyType.isEnumType()
				|| String.class.isAssignableFrom(rawClass)
				|| Boolean.class.isAssignableFrom(rawClass)
				|| Number.class.isAssignableFrom(rawClass);
	}

	public static boolean isSimpleType(JavaType javaType) {
		Class<?> rawClass = javaType.getRawClass();
		return javaType.isPrimitive()
				|| javaType.isEnumType()
				|| String.class.isAssignableFrom(rawClass)
				|| Boolean.class.isAssignableFrom(rawClass)
				|| Number.class.isAssignableFrom(rawClass);
	}

	public static boolean isKeySimpleType(Map<?, ?> value, MapperConfig<?> config) {
		if (value.isEmpty()) {
			return isKeySimpleType(config.constructType(value.getClass()));
		}
		Object firstKey = findFirstNonNullKey(value);
		if (firstKey == null) {
			return isKeySimpleType(config.constructType(value.getClass()));
		}
		return isSimpleType(config.constructType(firstKey.getClass()));
	}

	private static Object findFirstNonNullKey(Map<?, ?> value) {
		for (Object key : value.keySet()) {
			if (key != null) {
				return key;
			}
		}
		return null;
	}

}
