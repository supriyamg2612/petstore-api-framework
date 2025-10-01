package api.test;



import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserTests {
	
	Faker faker;    // Faker → Used to generate random test data.
	User userPayload;
	public Logger logger;
	
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
		
		
		// logs
			logger= LogManager.getLogger(this.getClass());
		
		logger.debug("debugging.....");
		
	}	
				
		
		@Test(priority = 1)
		public void testPostUser(){
			logger.info("********** Creating user  ***************");
			Response reponse = UserEndPoints.createUser(userPayload);
			reponse.then().log().all();
			Assert.assertEquals(reponse.getStatusCode(), 200);
			logger.info("**********User is creatged  ***************");

			}
		
		
		@Test(priority = 2)

		public void testgetUserByName() throws InterruptedException{
			
			logger.info("********** Reading User Info ***************");

			
			Response reponse = UserEndPoints.readUser(this.userPayload.getUsername());
			reponse.then().log().all();
			Assert.assertEquals(reponse.getStatusCode(), 200);
			Thread.sleep(300);

			logger.info("**********User info  is displayed ***************");


		}
		
		
		@Test(priority = 3)
		public void testUpdateUserByName(){
			
			logger.info("********** Updating User ***************");

			//update data using payload
			
			userPayload.setFirstName(faker.name().firstName());
			userPayload.setLastName(faker.name().lastName());
			userPayload.setEmail(faker.internet().safeEmailAddress());
		
			Response reponse = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);
			reponse.then().log().all();
			Assert.assertEquals(reponse.getStatusCode(), 200);
			
			logger.info("********** User updated ***************");

			
			// check data after updation
			
			Response reponseAfterUpdate = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);
			Assert.assertEquals(reponse.getStatusCode(), 200);
			

		
		}
		@Test(priority = 4)
		public void testDeleteUserByName(){
			
			logger.info("**********   Deleting User  ***************");

			
			Response reponse = UserEndPoints.deleteUser(this.userPayload.getUsername());
			Assert.assertEquals(reponse.getStatusCode(), 200);
			
			logger.info("********** User deleted ***************");


		}
		
		
		
	}


