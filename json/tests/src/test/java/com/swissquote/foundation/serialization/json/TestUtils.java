package com.swissquote.foundation.serialization.json;

import static java.time.ZoneId.systemDefault;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class TestUtils {

	private static final String DATE_FORMAT_PATTERN = "yyyy'-'MM'-'dd'T'HH':'mm':'ssZ";

	private static Calendar givenCalendar;

	static {
		givenCalendar = Calendar.getInstance();
		givenCalendar.set(Calendar.YEAR, 2018);
		givenCalendar.set(Calendar.MONTH, 8);
		givenCalendar.set(Calendar.DAY_OF_MONTH, 10);
		givenCalendar.set(Calendar.HOUR_OF_DAY, 14);
		givenCalendar.set(Calendar.MINUTE, 30);
		givenCalendar.set(Calendar.SECOND, 15);
		givenCalendar.set(Calendar.MILLISECOND, 0);
	}

	static Date getJavaUtilDate() {
		return givenCalendar.getTime();
	}

	static LocalDate getLocalDate() {
		return getJavaUtilDate().toInstant().atZone(systemDefault()).toLocalDate();
	}

	static LocalDateTime getLocalDateTime() {
		return getJavaUtilDate().toInstant().atZone(systemDefault()).toLocalDateTime();
	}

	static LocalTime getLocalTime() {
		return getJavaUtilDate().toInstant().atZone(systemDefault()).toLocalTime();
	}

	static ZonedDateTime getZonedDateTime() {
		return getJavaUtilDate().toInstant().atZone(systemDefault());
	}

	static String getJavaUtilDateString() {
		return new SimpleDateFormat(DATE_FORMAT_PATTERN).format(givenCalendar.getTime());
	}

	static String getInstantString() {
		return DateTimeFormatter.ISO_INSTANT.format(givenCalendar.toInstant());
	}

	static String getLocalDateString() {
		return getLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	static String getLocalDateTimeString() {
		return getLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	static String getLocalTimeString() {
		return DateTimeFormatter.ISO_LOCAL_TIME.format(getLocalTime());
	}

	static String getZonedDateTimeString() {
		return getZonedDateTime().format(DateTimeFormatter.ISO_DATE_TIME);
	}

	static <K, V> Map<K, V> mapOf(K k1, V v1, K k2, V v2) {
		Map<K, V> map = new HashMap<>();
		map.put(k1, v1);
		map.put(k2, v2);
		return map;
	}

}
