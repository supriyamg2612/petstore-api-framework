package api.test;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndpointsTwo;
import api.payload.User;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserTestTwo {

    Faker faker;
    User userPayload;
    public Logger logger;

    @BeforeClass
    public void setup() {
        faker = new Faker();
        userPayload = new User();

        userPayload.setId(faker.idNumber().hashCode());
        userPayload.setUsername(faker.name().username() + System.currentTimeMillis()); // unique
        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setEmail(faker.internet().safeEmailAddress());
        userPayload.setPassword(faker.internet().password(5, 10));
        userPayload.setPhone(faker.phoneNumber().cellPhone());

        logger = LogManager.getLogger(this.getClass());
        logger.debug("debugging.....");
    }

    @Test(priority = 1)
    public void testPostUser() {
        logger.info("********** Creating user ***************");
        Response response = UserEndpointsTwo.createUser(userPayload);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("********** User is created ***************");
    }

    @Test(priority = 2)
    public void testGetUserByName() throws InterruptedException {
        logger.info("********** Reading User Info ***************");

        Response response = retryReadUser(userPayload.getUsername(), 5);
        response.then().log().all();
        Assert.assertEquals(response.getStatusCode(), 200);

        logger.info("********** User info is displayed ***************");
    }

    @Test(priority = 3)
    public void testUpdateUserByName() throws InterruptedException {
        logger.info("********** Updating User ***************");

        userPayload.setFirstName(faker.name().firstName());
        userPayload.setLastName(faker.name().lastName());
        userPayload.setEmail(faker.internet().safeEmailAddress());

        Response response = UserEndpointsTwo.updateUser(this.userPayload.getUsername(), userPayload);
        response.then().log().body();
        Assert.assertEquals(response.getStatusCode(), 200);

        // Verify update
        Response responseAfterUpdate = retryReadUser(this.userPayload.getUsername(), 5);
        Assert.assertEquals(responseAfterUpdate.getStatusCode(), 200);

        logger.info("********** User updated ***************");
    }

    @Test(priority = 4)
    public void testDeleteUserByName() throws InterruptedException {
        logger.info("********** Deleting User ***************");

        Response response = UserEndpointsTwo.deleteUser(this.userPayload.getUsername());
        Assert.assertEquals(response.getStatusCode(), 200);

        // Verify deletion
        Response getResponse = retryReadUser(this.userPayload.getUsername(), 5);
        Assert.assertEquals(getResponse.getStatusCode(), 404);

        logger.info("********** User deleted ***************");
    }

    // 🔑 Retry helper for GET
    private Response retryReadUser(String username, int retries) throws InterruptedException {
        Response resp = null;
        while (retries-- > 0) {
            resp = UserEndpointsTwo.readUser(username);
            if (resp.getStatusCode() == 200 || resp.getStatusCode() == 404) {
                break;
            }
            Thread.sleep(500); // wait half a second
        }
        return resp;
    }
}
