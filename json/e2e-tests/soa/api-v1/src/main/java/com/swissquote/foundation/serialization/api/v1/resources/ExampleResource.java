package com.swissquote.foundation.serialization.api.v1.resources;

import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.swissquote.foundation.serialization.api.v1.entities.ComplexData;
import com.swissquote.foundation.serialization.api.v1.entities.ComplexValue;

@Path("example")
public interface ExampleResource {

	String ROLE = "EXAMPLE";

	@GET
	@Path("string")
	@RolesAllowed(ROLE)
	@Produces(MediaType.APPLICATION_JSON)
	String getString();

	@GET
	@Path("complex-value")
	@RolesAllowed(ROLE)
	@Produces(MediaType.APPLICATION_JSON)
	ComplexValue getComplexValue();

	@GET
	@Path("complex-data")
	@RolesAllowed(ROLE)
	@Produces(MediaType.APPLICATION_JSON)
	ComplexData getComplexData();

	@GET
	@Path("map-complex-value-value")
	@RolesAllowed(ROLE)
	@Produces(MediaType.APPLICATION_JSON)
	Map<String, ComplexValue> getMapComplexValueAsValue();

	@GET
	@Path("map-complex-value-key")
	@RolesAllowed(ROLE)
	@Produces(MediaType.APPLICATION_JSON)
	Map<ComplexValue, String> getMapComplexValueAsKey();

	@GET
	@Path("map-complex-data-value")
	@RolesAllowed(ROLE)
	@Produces(MediaType.APPLICATION_JSON)
	Map<String, ComplexData> getMapComplexDataAsValue();

	@GET
	@Path("map-complex-data-key")
	@RolesAllowed(ROLE)
	@Produces(MediaType.APPLICATION_JSON)
	Map<ComplexData, String> getMapComplexDataAsKey();

}
