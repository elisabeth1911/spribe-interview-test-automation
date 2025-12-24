package com.example.api.tests.player;

import com.example.api.dto.PlayerGetAllResponse;
import com.example.api.dto.PlayerItem;
import com.example.api.tests.BaseApiTest;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.Test;

import java.util.List;

public class PlayerGetAllTest extends BaseApiTest {

    @Test
    public void getAll_returnsPlayersArray() {
        Response resp = playerClient.getAll();
        Assert.assertEquals(resp.statusCode(), 200);

        PlayerGetAllResponse body = resp.as(PlayerGetAllResponse.class);
        List<PlayerItem> players = body.getPlayers();

        SoftAssert softAssert = new SoftAssert();

        boolean hasSupervisor = false;
        boolean hasAdmin = false;
        if (players != null) {
            softAssert.assertTrue(players.size() >= 2, "At least 2 seeded players should exist");
            int index = 0;
            for (PlayerItem p : players) {
                softAssert.assertNotNull(p.getId(), "player[" + index + "].id should be returned");
                softAssert.assertNotNull(p.getScreenName(), "player[" + index + "].screenName should be returned");
                softAssert.assertNotNull(p.getGender(), "player[" + index + "].gender should be returned");
                softAssert.assertNotNull(p.getAge(), "player[" + index + "].age should be returned");

                if (p.getId() == 1L) {
                    hasSupervisor = true;
                }
                if (p.getId() == 2L) {
                    hasAdmin = true;
                }

                index++;
            }
        }

        softAssert.assertTrue(hasSupervisor, "List should contain seeded supervisor (id=1)");
        softAssert.assertTrue(hasAdmin, "List should contain seeded admin (id=2)");
        softAssert.assertAll();
    }

    @Test
    public void getAll_shouldNotExposeLoginOrPassword() {
        Response resp = playerClient.getAll();
        Assert.assertEquals(resp.statusCode(), 200);

        String raw = resp.getBody().asString();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertFalse(raw.contains("\"password\""));
        softAssert.assertFalse(raw.contains("\"login\""));
        softAssert.assertAll();
    }

    @Test
    public void getAllWithWrongHttpMethod_returns405() {
        Response resp = playerClient.getAll(Method.POST);
        Assert.assertEquals(resp.statusCode(), 405);
    }
}
