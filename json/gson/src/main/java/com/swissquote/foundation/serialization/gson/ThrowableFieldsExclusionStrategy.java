/*
 * (c) Swissquote Mar 14, 2013
 */

package com.swissquote.foundation.serialization.gson;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * This instructs GSON to skip the fields "stackTrace" and "suppressedExceptions" (java-7)
 * when serializing objects to JSON.
 * We don't need this because exceptions that we deliver on client side are not supposed
 * to have the full stack-trace (and thus classes) from the server side.
 *
 * @author SWAT
 */
public class ThrowableFieldsExclusionStrategy implements ExclusionStrategy {

	private static final Logger logger = LoggerFactory.getLogger(ThrowableFieldsExclusionStrategy.class);

	private final Set<FieldAttributesWithEquals> excludedFields;

	public ThrowableFieldsExclusionStrategy() {
		excludedFields = new HashSet<FieldAttributesWithEquals>();
		addThrField("stackTrace");
		addThrField("suppressedExceptions");
	}

	private void addThrField(final String name) {
		try {
			Field f = Throwable.class.getDeclaredField(name);
			excludedFields.add(new FieldAttributesWithEquals(new FieldAttributes(f)));
		}
		catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("Didn't add for exclusion the field: " + name, e);
			}
		}
	}

	@Override
	public boolean shouldSkipField(final FieldAttributes f) {
		return excludedFields.contains(new FieldAttributesWithEquals(f));
	}

	@Override
	public boolean shouldSkipClass(final Class<?> clazz) {
		return false;
	}

	private static class FieldAttributesWithEquals {
		private final FieldAttributes fa;

		public FieldAttributesWithEquals(FieldAttributes fa) {
			this.fa = fa;
			if (fa == null) {
				throw new IllegalArgumentException("Given FieldAttributes cannot be null");
			}
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			Class<?> dc = fa.getDeclaringClass();
			result = prime * result + ((dc == null) ? 0 : dc.hashCode());
			String n = fa.getName();
			result = prime * result + ((n == null) ? 0 : n.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			FieldAttributesWithEquals other = (FieldAttributesWithEquals) obj;

			Class<?> dc = fa.getDeclaringClass();
			Class<?> odc = other.fa.getDeclaringClass();
			if (dc == null) {
				if (odc != null) {
					return false;
				}
			} else if (!dc.equals(odc)) {
				return false;
			}

			String n = fa.getName();
			String on = other.fa.getName();
			if (n == null) {
				if (on != null) {
					return false;
				}
			} else if (!n.equals(on)) {
				return false;
			}

			return true;
		}

	}

}
