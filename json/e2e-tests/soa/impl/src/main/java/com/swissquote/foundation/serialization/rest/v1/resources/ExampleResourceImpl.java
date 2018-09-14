package com.swissquote.foundation.serialization.rest.v1.resources;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.swissquote.foundation.serialization.api.v1.entities.ComplexData;
import com.swissquote.foundation.serialization.api.v1.entities.ComplexValue;
import com.swissquote.foundation.serialization.api.v1.resources.ExampleResource;

@Path("example")
public class ExampleResourceImpl implements ExampleResource {

	@GET
	@Path("string")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed(ROLE)
	@Override
	public String getString() {
		return "string";
	}


	@GET
	@Path("complex-value")
	@RolesAllowed(ROLE)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public ComplexValue getComplexValue() {
		return new ComplexValue("value");
	}

	@GET
	@Path("complex-data")
	@RolesAllowed(ROLE)
	@Produces(MediaType.APPLICATION_JSON)
	@Override
	public ComplexData getComplexData() {
		return new ComplexData("data");
	}
}