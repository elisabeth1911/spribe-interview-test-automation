package com.example.api.tests.player;

import com.example.api.data.PlayerRequestFactory;
import com.example.api.domain.Editor;
import com.example.api.dto.PlayerCreateRequest;
import com.example.api.dto.PlayerCreateResponse;
import com.example.api.dto.PlayerDeleteRequest;
import com.example.api.dto.PlayerGetByIdRequest;
import com.example.api.tests.BaseApiTest;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PlayerDeleteTest extends BaseApiTest {

    @Test
    public void deleteCreatedPlayer_returns204() {
        Response createResp = createPlayerAs(Editor.SUPERVISOR, PlayerRequestFactory.validUserCreate());
        Assert.assertEquals(createResp.statusCode(), 200, "Create player status");
        PlayerCreateResponse created = createResp.as(PlayerCreateResponse.class);
        Assert.assertNotNull(created.getId(), "Create should return id");
        long id = created.getId();

        Response deleteResp = playerClient.delete(Editor.ADMIN.value(), new PlayerDeleteRequest(id));
        Assert.assertEquals(deleteResp.statusCode(), 204);

        Response getResp = playerClient.getById(new PlayerGetByIdRequest(id));
        Assert.assertEquals(getResp.statusCode(), 404);
    }

    @Test
    public void deleteNonExistingPlayer_returns204() {
        Response deleteResp = playerClient.delete(Editor.ADMIN.value(), new PlayerDeleteRequest(9_999_999_999L));
        Assert.assertEquals(deleteResp.statusCode(), 204);
    }

    @Test
    public void userCannotDeleteSelf_returns403() {
        PlayerCreateRequest createRequest = PlayerRequestFactory.validUserCreate();
        Response createResp = createPlayerAs(Editor.SUPERVISOR, createRequest);
        Assert.assertEquals(createResp.statusCode(), 200, "Create player status");
        PlayerCreateResponse created = createResp.as(PlayerCreateResponse.class);
        Assert.assertNotNull(created.getId(), "Create should return id");
        long id = created.getId();

        Response deleteResp = playerClient.delete(createRequest.getLogin(), new PlayerDeleteRequest(id));
        Assert.assertEquals(deleteResp.statusCode(), 403);
    }

    @Test
    public void adminCannotDeleteOtherAdmin_returns403() {
        PlayerCreateRequest createRequest = PlayerRequestFactory.validUserCreate(r -> r.setRole("admin"));
        Response createResp = createPlayerAs(Editor.SUPERVISOR, createRequest);
        Assert.assertEquals(createResp.statusCode(), 200, "Create player status");
        PlayerCreateResponse created = createResp.as(PlayerCreateResponse.class);
        Assert.assertNotNull(created.getId(), "Create should return id");
        long id = created.getId();

        Response deleteResp = playerClient.delete(Editor.ADMIN.value(), new PlayerDeleteRequest(id));
        Assert.assertEquals(deleteResp.statusCode(), 403);
    }

    @Test
    public void supervisorCanDeleteUser_returns204() {
        Response createResp = createPlayerAs(Editor.SUPERVISOR, PlayerRequestFactory.validUserCreate());
        Assert.assertEquals(createResp.statusCode(), 200, "Create player status");
        PlayerCreateResponse created = createResp.as(PlayerCreateResponse.class);
        Assert.assertNotNull(created.getId(), "Create should return id");
        long id = created.getId();

        Response deleteResp = playerClient.delete(Editor.SUPERVISOR.value(), new PlayerDeleteRequest(id));
        Assert.assertEquals(deleteResp.statusCode(), 204);
    }

    @Test
    public void deleteWithUnknownEditor_returns403() {
        Response resp = playerClient.delete(Editor.UNKNOWN.value(), new PlayerDeleteRequest(1L));
        Assert.assertEquals(resp.statusCode(), 403);
    }

    @Test
    public void deleteWithoutBody_returns400() {
        Response resp = playerClient.delete(Method.DELETE, Editor.ADMIN.value(), null);
        Assert.assertEquals(resp.statusCode(), 400);
    }

    @Test
    public void deleteWithWrongHttpMethod_returns405() {
        Response resp = playerClient.delete(Method.POST, Editor.ADMIN.value(), new PlayerDeleteRequest(1L));
        Assert.assertEquals(resp.statusCode(), 405);
    }
}
