package api.test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import api.endpoints.UserEndPoints;
import api.payload.User;
import api.utilities.DataProviders;
import api.utilities.ExtentReportManager;
import io.restassured.response.Response;


	
	
	
	@Listeners(api.utilities.ExtentReportManager.class)
	public class DataDrivenTest {

		@Test(priority = 1, dataProvider = "Data", dataProviderClass = DataProviders.class, groups = {"createUser"})
	    public void testPostuser(String userID, String userName, String fname, String lname,
	                             String useremail, String pwd, String ph) throws InterruptedException {

	        // Create ExtentTest for this method
	        ExtentTest methodTest = ExtentReportManager.createTest("Create User: " + userName);

	        User userPayload = new User();
	        userPayload.setId(Integer.parseInt(userID));
	        userPayload.setUsername(userName);
	        userPayload.setFirstName(fname);
	        userPayload.setLastName(lname);
	        userPayload.setEmail(useremail);
	        userPayload.setPassword(pwd);
	        userPayload.setPhone(ph);

	        Response response = UserEndPoints.createUser(userPayload);

	        methodTest.log(Status.INFO, "POST response code: " + response.getStatusCode());
	        methodTest.log(Status.INFO, "POST response body: " + response.getBody().asString());

	        Assert.assertEquals(response.getStatusCode(), 200);
	        Thread.sleep(300);

	        methodTest.log(Status.PASS, "User " + userName + " created successfully.");
	    }

	    @Test(priority = 2, dataProvider = "UserNames", dataProviderClass = DataProviders.class,
	          dependsOnGroups = {"createUser"})
	    public void testDeleteUserByName(String userName) {

	        ExtentTest methodTest = ExtentReportManager.createTest("Delete User: " + userName);

	        // Check if user exists
	        Response getResponse = UserEndPoints.readUser(userName);

	        if (getResponse.getStatusCode() == 200) {
	            Response deleteResponse = UserEndPoints.deleteUser(userName);

	            methodTest.log(Status.INFO, "DELETE response code: " + deleteResponse.getStatusCode());
	            methodTest.log(Status.INFO, "DELETE response body: " + deleteResponse.getBody().asString());

	            Assert.assertEquals(deleteResponse.getStatusCode(), 200,
	                    "Failed to delete user: " + userName);
	            methodTest.log(Status.PASS, "User " + userName + " deleted successfully.");

	        } else {
	            methodTest.log(Status.SKIP,
	                    "User " + userName + " does not exist. Skipping deletion.");
	            System.out.println("User " + userName + " does not exist. Skipping deletion.");
	        }
	    }
	}
	