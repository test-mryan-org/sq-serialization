package com.swissquote.foundation.serialization.rest.v1.resources;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.swissquote.foundation.serialization.api.v1.entities.ComplexData;
import com.swissquote.foundation.serialization.api.v1.entities.ComplexValue;
import com.swissquote.foundation.serialization.api.v1.resources.ExampleResource;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
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

	@GET
	@Path("map-complex-value-value")
	@RolesAllowed(ROLE)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, ComplexValue> getMapComplexValueAsValue() {
		Map<String, ComplexValue> map = new HashMap<>();
		map.put("default", new ComplexValue("value"));
		return map;
	}

	@GET
	@Path("map-complex-value-key")
	@RolesAllowed(ROLE)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<ComplexValue, String> getMapComplexValueAsKey() {
		Map<ComplexValue, String> map = new HashMap<>();
		map.put(new ComplexValue("value"), "default");
		return map;
	}

	@GET
	@Path("map-complex-data-value")
	@RolesAllowed(ROLE)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, ComplexData> getMapComplexDataAsValue() {
		Map<String, ComplexData> map = new HashMap<>();
		map.put("default", new ComplexData("data"));
		return map;
	}

	@GET
	@Path("map-complex-data-key")
	@RolesAllowed(ROLE)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<ComplexData, String> getMapComplexDataAsKey() {
		Map<ComplexData, String> map = new HashMap<>();
		map.put(new ComplexData("data"), "default");
		return map;
	}
}