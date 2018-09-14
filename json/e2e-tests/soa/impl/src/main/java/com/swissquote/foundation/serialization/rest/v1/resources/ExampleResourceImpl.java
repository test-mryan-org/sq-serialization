package com.swissquote.foundation.serialization.rest.v1.resources;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.swissquote.foundation.serialization.api.v1.resources.ExampleResource;

@Path("example")
public class ExampleResourceImpl implements ExampleResource {

	@GET
	@Path("string")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed("EXAMPLE")
	@Override
	public String getString() {
		return "string";
	}
}