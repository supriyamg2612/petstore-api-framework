package api.endpoints;
import  org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class UserEndPoints { 
	
	// Perfom CRUD operation - create, read,update and delete
	
	public static Response createUser(User payload){ //create user
		
		Response response =given()
		.contentType(ContentType.JSON)
		.accept(ContentType.JSON)
		.body(payload)
		
		.when()
		.post(Routes.base_url);
		
		return response;
		
		
		
	}
	

}
