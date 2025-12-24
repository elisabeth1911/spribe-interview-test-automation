package com.example.api.tests.player;

import com.example.api.dto.PlayerGetByIdRequest;
import com.example.api.dto.PlayerGetByIdResponse;
import com.example.api.tests.BaseApiTest;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.Test;

import java.util.Collections;

public class PlayerGetTest extends BaseApiTest {

    @Test
    public void getExistingPlayer_supervisor_isReturned() {
        Response resp = playerClient.getById(new PlayerGetByIdRequest(1L));
        Assert.assertEquals(resp.statusCode(), 200);
        PlayerGetByIdResponse body = resp.as(PlayerGetByIdResponse.class);
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(body.getId().longValue(), 1L, "id should match seeded supervisor");
        softAssert.assertEquals(body.getLogin(), "supervisor", "login should match seeded supervisor");
        softAssert.assertEquals(body.getRole(), "supervisor", "role should match seeded supervisor");
        softAssert.assertAll();
    }

    @Test
    public void getNonExistingPlayer_returns404() {
        Response resp = playerClient.getById(new PlayerGetByIdRequest(9_999_999_999L));
        Assert.assertEquals(resp.statusCode(), 404);
    }

    @Test
    public void getWithoutBody_returns400() {
        Response resp = playerClient.getById(Method.POST, Collections.emptyMap());
        Assert.assertEquals(resp.statusCode(), 400);
    }

    @Test
    public void getWithWrongHttpMethod_returns405() {
        Response resp = playerClient.getById(Method.GET, null);
        Assert.assertEquals(resp.statusCode(), 405);
    }
}
