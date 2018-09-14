package com.swissquote.treasury.fx.compensate;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendNotificationTest extends AbstractTest {


	@Test
	public void soa_getString() {

		String string = exampleResource().getString();

		Assert.assertThat(string, Matchers.is("string"));

	}

}
