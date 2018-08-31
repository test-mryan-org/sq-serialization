package com.swissquote.foundation.serialization.jackson;

import java.io.IOException;
import java.util.Properties;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.Versioned;
import com.fasterxml.jackson.core.util.VersionUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * Automatically generated from PackageVersion.java.in during
 * packageVersion-generate execution of maven-replacer-plugin in
 * pom.xml.
 */
@Slf4j
public final class PackageVersion implements Versioned {

	public static Version buildVersion() {
		Properties props = new Properties();
		try {
			props.load(PackageVersion.class.getClassLoader().getResourceAsStream("version.properties"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return VersionUtil.parseVersion(
				props.getProperty("version", "0.0.0"),
				props.getProperty("groupId", "na"),
				props.getProperty("artifactId", "na"));
	}

	@Override
	public Version version() {
		return buildVersion();
	}
}
