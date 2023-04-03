package pt.unl.fct.di.apdc.firstwebapp.resources;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.cloud.datastore.Entity;
import com.google.appengine.repackaged.org.apache.commons.codec.digest.DigestUtils;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Key;
import com.google.gson.Gson;

import pt.unl.fct.di.apdc.firstwebapp.util.LoginData;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class UserResource {
	private static Logger LOG = Logger.getLogger(LoginResource.class.getName());
	private final Gson g = new Gson();
	private final Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	public UserResource() {
	}

	@POST
	@Path("/v1")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response doRegistrationV1(LoginData data) {
		LOG.fine("Attempt to regist user : " + data.username);

		/*
		 * if(!data.validRegistration()) { return
		 * Response.status(Status.BAD_REQUEST).entity("Missing or wrong parameter").
		 * build(); }
		 */
		Key userKey = datastore.newKeyFactory().setKind("User").newKey(data.username);
		Entity user = Entity.newBuilder(userKey).set("user_pwd",DigestUtils.sha512Hex(data.password)).set("user_creation_time",Timestamp.now()).build();
		datastore.add(user);
		LOG.info("User registered" + data.username);
		
		return Response.ok("{}").build();
	}

}
