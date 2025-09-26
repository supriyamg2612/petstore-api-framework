package api.endpoints;
import  org.testng.annotations.Test;

import api.payload.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

public class UserEndPoints { 
	
	// Perfom CRUD operation - create, read,update and delete
	//UserEndPoints → Contains API methods (create, read, update, delete).
	
	public static Response createUser(User payload){ //create user
		
		Response response =given()					// start building the request
		.contentType(ContentType.JSON)				// tell server we are sending JSON
		.accept(ContentType.JSON)					// tell server we expect JSON in response
		.body(payload)								// attach request body (User object → JSON)
		
		.when()
		.post(Routes.base_url); 					 // make POST request to the API (base URL)
		
		return response;                        // return the response back
		
		
		
	}
	
	public static Response readUser(String username){  //read user
		
		Response response = given()    							 // start building the request 
								.pathParam("username", username) // replace {username} in URL with actual value
		.get(Routes.get_url);                                    // send GET request to the "get user" endpoint
		
		return response; 										 // return the response
		
		
		
	}
	
	
	public static Response updateUser(String username, User payload){ //update user
		
		Response response =given()						// start building the request
				.contentType(ContentType.JSON)				// tell server we are sending JSON
				.accept(ContentType.JSON)					// tell server we expect JSON in response
				.body(payload)								// attach request body (User object → JSON)
				.pathParam("username", username)           // replace {username} in URL with actual value
				.when()
				.put(Routes.update_url); 					 // make PUT requestto the "UPDATE user" endpoint
				
				return response;                        // return the response back
		
		
	}
	
	
public static Response deleteUser( String username){ //delete user 
		
		Response response =given()					// start building the request
		.contentType(ContentType.JSON)				// tell server we are sending JSON
		.accept(ContentType.JSON)					// tell server we expect JSON in response
		.pathParam("username", username)           // replace {username} in URL with actual value
		
		.when()
		.delete(Routes.delete_url); 					// make DELETE requestto the "Delete user" endpoint
		
		return response;                        // return the response back
		
		
		
	}
	
	

	

}
