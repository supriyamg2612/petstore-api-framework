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
    public void testPostUser(String userID, String userName, String fname, String lname,
                             String useremail, String pwd, String ph) throws InterruptedException {

        ExtentTest methodTest = ExtentReportManager.createTest("Create User: " + userName);

        User userPayload = new User();
        userPayload.setId(Integer.parseInt(userID));
        userPayload.setUsername(userName);
        userPayload.setFirstName(fname);
        userPayload.setLastName(lname);
        userPayload.setEmail(useremail);
        userPayload.setPassword(pwd);
        userPayload.setPhone(ph);

        Response getResponse = UserEndPoints.readUser(userName);

        if (getResponse.getStatusCode() != 200) {
            Response response = UserEndPoints.createUser(userPayload);
            methodTest.log(Status.INFO, "POST response code: " + response.getStatusCode());
            methodTest.log(Status.INFO, "POST response body: " + response.getBody().asString());
            Assert.assertEquals(response.getStatusCode(), 200, "Failed to create user: " + userName);
            methodTest.log(Status.PASS, "User " + userName + " created successfully.");
        } else {
            methodTest.log(Status.INFO, "User " + userName + " already exists. Skipping creation.");
        }

        Thread.sleep(300);
    }

    @Test(priority = 2, dataProvider = "UserNames", dataProviderClass = DataProviders.class,
          dependsOnGroups = {"createUser"})
    public void testDeleteUserByName(String userName) {

        ExtentTest methodTest = ExtentReportManager.createTest("Delete User: " + userName);

        // Ensure user exists before deletion
        Response getResponse = UserEndPoints.readUser(userName);

        if (getResponse.getStatusCode() != 200) {
            // User does not exist, create it first
            User userPayload = new User();
            userPayload.setId((int)(Math.random() * 1000000)); // random ID
            userPayload.setUsername(userName);
            userPayload.setFirstName("Test");
            userPayload.setLastName("User");
            userPayload.setEmail(userName + "@example.com");
            userPayload.setPassword("password123");
            userPayload.setPhone("1234567890");

            Response createResponse = UserEndPoints.createUser(userPayload);
            Assert.assertEquals(createResponse.getStatusCode(), 200,
                    "Failed to create user for deletion: " + userName);
            methodTest.log(Status.INFO, "User " + userName + " created for deletion.");
        }

        // Now delete the user
        Response deleteResponse = UserEndPoints.deleteUser(userName);
        methodTest.log(Status.INFO, "DELETE response code: " + deleteResponse.getStatusCode());
        methodTest.log(Status.INFO, "DELETE response body: " + deleteResponse.getBody().asString());
        Assert.assertEquals(deleteResponse.getStatusCode(), 200,
                "Failed to delete user: " + userName);
        methodTest.log(Status.PASS, "User " + userName + " deleted successfully.");
    }
}