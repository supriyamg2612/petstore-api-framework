package api.test;


import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import groovyjarjarantlr4.v4.codegen.model.chunk.ThisRulePropertyRef_ctx;
import io.restassured.response.Response;

public class UserTests {
	
	Faker faker;    // Faker → Used to generate random test data.
	User userPayload;
	
	@BeforeClass
	public void setupData() {
		
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5, 10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
	}
		
		@Test(priority = 1)
		public void testPostUser(){
			
			Response reponse = UserEndPoints.createUser(userPayload);
			reponse.then().log().all();
			Assert.assertEquals(reponse.getStatusCode(), 200);
			
			}
		
		
		@Test(priority = 2)

		public void testgetUserByName(){
			
			Response reponse = UserEndPoints.readUser(this.userPayload.getUsername());
			reponse.then().log().all();
			Assert.assertEquals(reponse.getStatusCode(), 200);

			

		}
		
		
		@Test(priority = 3)
		public void testUpdateUserByName(){
			//update data using payload
			
			userPayload.setFirstName(faker.name().firstName());
			userPayload.setLastName(faker.name().lastName());
			userPayload.setEmail(faker.internet().safeEmailAddress());
		
			Response reponse = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);
			reponse.then().log().all();
			Assert.assertEquals(reponse.getStatusCode(), 200);
			
			// check data after updation
			
			Response reponseAfterUpdate = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);
			Assert.assertEquals(reponse.getStatusCode(), 200);
			

		
		}
		@Test(priority = 4)
		public void testDeleteUserByName(){
			
			Response reponse = UserEndPoints.deleteUser(this.userPayload.getUsername());
			Assert.assertEquals(reponse.getStatusCode(), 200);

		}
		
		
	}


