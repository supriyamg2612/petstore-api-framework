package api.test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import api.endpoints.UserEndPoints;
import api.payload.User;
import api.utilities.DataProviders;
import io.restassured.response.Response;

public class DataDrivenTest {
	
	
	
	
	@Test(priority = 1, dataProvider ="Data", dataProviderClass = DataProviders.class)
	public void testPostuser(String userID, String userName,String fname,String lname,String useremail,String pwd,String ph)
	{
		User userPayload=new User();
		
		userPayload.setId(Integer.parseInt(userID));
		userPayload.setUsername(userName);
		userPayload.setFirstName(fname);
		userPayload.setLastName(lname);
		userPayload.setEmail(useremail);
		userPayload.setPassword(pwd);
		userPayload.setPhone(ph);
		
		Response response=UserEndPoints.createUser(userPayload);
		Assert.assertEquals(response.getStatusCode(),200);
			
	}

	  
	@Test(priority = 2, dataProvider = "UserNames", dataProviderClass = DataProviders.class)
	public void testDeleteUserByName(String userName) {
	    // Check if user exists first
	    Response getResponse = UserEndPoints.readUser(userName);

	    if (getResponse.getStatusCode() == 200) {
	        // User exists → delete
	        Response deleteResponse = UserEndPoints.deleteUser(userName);
	        Assert.assertEquals(deleteResponse.getStatusCode(), 200, "Failed to delete user: " + userName);
	        System.out.println("Deleted user: " + userName);
	    } else {
	        // User does not exist → skip deletion
	        System.out.println("User " + userName + " does not exist. Skipping deletion.");
	    }
	}

	}
	