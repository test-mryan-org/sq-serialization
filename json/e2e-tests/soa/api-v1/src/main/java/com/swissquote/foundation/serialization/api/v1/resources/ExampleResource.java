package com.swissquote.foundation.serialization.api.v1.resources;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("example")
public interface ExampleResource {

	@GET
	@Path("string")
	@RolesAllowed("EXAMPLE")
	@Produces(MediaType.APPLICATION_JSON)
	String getString();
}
