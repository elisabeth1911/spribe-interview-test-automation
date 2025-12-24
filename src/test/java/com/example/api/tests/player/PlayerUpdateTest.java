package com.example.api.tests.player;

import com.example.api.data.PlayerRequestFactory;
import com.example.api.dataprovider.PlayerDataProviders;
import com.example.api.domain.Editor;
import com.example.api.dto.PlayerCreateRequest;
import com.example.api.dto.PlayerCreateResponse;
import com.example.api.dto.PlayerGetByIdRequest;
import com.example.api.dto.PlayerGetByIdResponse;
import com.example.api.dto.PlayerUpdateRequest;
import com.example.api.dto.PlayerUpdateResponse;
import com.example.api.mapper.PlayerMapper;
import com.example.api.tests.BaseApiTest;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.Test;

public class PlayerUpdateTest extends BaseApiTest {
    private static final PlayerMapper MAPPER = new PlayerMapper();

    @Test
    public void updateUser_persistsChanges() {
        PlayerCreateRequest createRequest = PlayerRequestFactory.validUserCreate();
        Response createResp = createPlayerAs(Editor.SUPERVISOR, createRequest);
        Assert.assertEquals(createResp.statusCode(), 200, "Create player status");
        PlayerCreateResponse created = createResp.as(PlayerCreateResponse.class);
        Assert.assertNotNull(created.getId(), "Create should return id");

        int updatedAge = createRequest.getAge() == 59 ? 58 : createRequest.getAge() + 1;
        String updatedScreenName = "updated_" + createRequest.getScreenName();
        PlayerUpdateRequest updateRequest = PlayerRequestFactory.updateFromCreate(createRequest, u -> {
            u.setAge(updatedAge);
            u.setScreenName(updatedScreenName);
        });

        Response updateResp = playerClient.update(Editor.SUPERVISOR.value(), created.getId(), updateRequest);
        Assert.assertEquals(updateResp.statusCode(), 200);
        PlayerUpdateResponse update = updateResp.as(PlayerUpdateResponse.class);

        Response getResp = playerClient.getById(new PlayerGetByIdRequest(created.getId()));
        Assert.assertEquals(getResp.statusCode(), 200);
        PlayerGetByIdResponse after = getResp.as(PlayerGetByIdResponse.class);

        PlayerCreateRequest expectedRequest = createRequest.copy();
        expectedRequest.setAge(updatedAge);
        expectedRequest.setScreenName(updatedScreenName);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(update, MAPPER.expectedUpdate(created.getId(), updateRequest), "Update response should match");
        softAssert.assertEquals(after, MAPPER.expectedGetById(created.getId(), expectedRequest), "Get after update should match");
        softAssert.assertAll();
    }

    @Test
    public void updateWithUnknownEditor_returns403() {
        PlayerCreateRequest create = PlayerRequestFactory.validUserCreate();
        PlayerUpdateRequest body = PlayerRequestFactory.updateFromCreate(create);

        Response resp = playerClient.update("unknownEditor", 1L, body);
        Assert.assertEquals(resp.statusCode(), 403);
    }

    @Test
    public void updateNonExistingId_returns204() {
        PlayerCreateRequest create = PlayerRequestFactory.validUserCreate();
        PlayerUpdateRequest body = PlayerRequestFactory.updateFromCreate(create);

        Response resp = playerClient.update(Editor.ADMIN.value(), 9_999_999_999L, body);
        Assert.assertEquals(resp.statusCode(), 204);
    }

    @Test(dataProvider = "invalidUpdateAges", dataProviderClass = PlayerDataProviders.class)
    public void updateWithInvalidAge_returns400(Editor editor, String scenario, int age) {
        PlayerCreateRequest createRequest = PlayerRequestFactory.validUserCreate();
        Response createResp = createPlayerAs(Editor.SUPERVISOR, createRequest);
        Assert.assertEquals(createResp.statusCode(), 200, "Create player status");
        PlayerCreateResponse created = createResp.as(PlayerCreateResponse.class);
        Assert.assertNotNull(created.getId(), "Create should return id");

        PlayerUpdateRequest updateRequest = PlayerRequestFactory.updateFromCreate(createRequest, u -> u.setAge(age));
        Response resp = playerClient.update(editor.value(), created.getId(), updateRequest);
        Assert.assertEquals(resp.statusCode(), 400, "Expected 400 for scenario: " + scenario);
    }

    @Test(dataProvider = "invalidPasswords", dataProviderClass = PlayerDataProviders.class)
    public void updateWithInvalidPassword_returns400(Editor editor, String scenario, String password) {
        PlayerCreateRequest createRequest = PlayerRequestFactory.validUserCreate();
        Response createResp = createPlayerAs(Editor.SUPERVISOR, createRequest);
        Assert.assertEquals(createResp.statusCode(), 200, "Create player status");
        PlayerCreateResponse created = createResp.as(PlayerCreateResponse.class);
        Assert.assertNotNull(created.getId(), "Create should return id");

        PlayerUpdateRequest updateRequest = PlayerRequestFactory.updateFromCreate(createRequest, u -> u.setPassword(password));
        Response resp = playerClient.update(editor.value(), created.getId(), updateRequest);
        Assert.assertEquals(resp.statusCode(), 400, "Expected 400 for scenario: " + scenario);
    }

    @Test(dataProvider = "validationEditors", dataProviderClass = PlayerDataProviders.class)
    public void updateWithInvalidGender_returns400(Editor editor) {
        PlayerCreateRequest createRequest = PlayerRequestFactory.validUserCreate();
        Response createResp = createPlayerAs(Editor.SUPERVISOR, createRequest);
        Assert.assertEquals(createResp.statusCode(), 200, "Create player status");
        PlayerCreateResponse created = createResp.as(PlayerCreateResponse.class);
        Assert.assertNotNull(created.getId(), "Create should return id");

        PlayerUpdateRequest updateRequest = PlayerRequestFactory.updateFromCreate(createRequest, u -> u.setGender("unknown"));
        Response resp = playerClient.update(editor.value(), created.getId(), updateRequest);
        Assert.assertEquals(resp.statusCode(), 400);
    }

    @Test
    public void userCanUpdateSelf_persistsChanges() {
        PlayerCreateRequest createRequest = PlayerRequestFactory.validUserCreate();
        Response createResp = createPlayerAs(Editor.SUPERVISOR, createRequest);
        Assert.assertEquals(createResp.statusCode(), 200, "Create player status");
        PlayerCreateResponse created = createResp.as(PlayerCreateResponse.class);
        Assert.assertNotNull(created.getId(), "Create should return id");

        String updatedScreenName = "self_" + createRequest.getScreenName();
        PlayerUpdateRequest updateRequest = PlayerRequestFactory.updateFromCreate(createRequest, u -> u.setScreenName(updatedScreenName));
        Response updateResp = playerClient.update(createRequest.getLogin(), created.getId(), updateRequest);
        Assert.assertEquals(updateResp.statusCode(), 200);
        PlayerUpdateResponse update = updateResp.as(PlayerUpdateResponse.class);

        Response getResp = playerClient.getById(new PlayerGetByIdRequest(created.getId()));
        Assert.assertEquals(getResp.statusCode(), 200);
        PlayerGetByIdResponse after = getResp.as(PlayerGetByIdResponse.class);

        PlayerCreateRequest expectedRequest = createRequest.copy();
        expectedRequest.setScreenName(updatedScreenName);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(update, MAPPER.expectedUpdate(created.getId(), updateRequest), "Update response should match");
        softAssert.assertEquals(after, MAPPER.expectedGetById(created.getId(), expectedRequest), "Get after update should match");
        softAssert.assertAll();
    }

    @Test
    public void userCannotUpdateOtherUser_returns403() {
        PlayerCreateRequest userA = PlayerRequestFactory.validUserCreate();
        Response createA = createPlayerAs(Editor.SUPERVISOR, userA);
        Assert.assertEquals(createA.statusCode(), 200, "Create player status");
        PlayerCreateResponse createdA = createA.as(PlayerCreateResponse.class);
        Assert.assertNotNull(createdA.getId(), "Create should return id");

        PlayerCreateRequest userB = PlayerRequestFactory.validUserCreate();
        Response createB = createPlayerAs(Editor.SUPERVISOR, userB);
        Assert.assertEquals(createB.statusCode(), 200, "Create player status");

        PlayerUpdateRequest updateRequest = PlayerRequestFactory.updateFromCreate(userA);
        Response resp = playerClient.update(userB.getLogin(), createdA.getId(), updateRequest);
        Assert.assertEquals(resp.statusCode(), 403);
    }

    @Test
    public void adminCannotUpdateOtherAdmin_returns403() {
        PlayerCreateRequest adminRequest = PlayerRequestFactory.validUserCreate(r -> r.setRole("admin"));
        Response createResp = createPlayerAs(Editor.SUPERVISOR, adminRequest);
        Assert.assertEquals(createResp.statusCode(), 200, "Create player status");
        PlayerCreateResponse created = createResp.as(PlayerCreateResponse.class);
        Assert.assertNotNull(created.getId(), "Create should return id");

        PlayerUpdateRequest updateRequest = PlayerRequestFactory.updateFromCreate(adminRequest);
        Response resp = playerClient.update(Editor.ADMIN.value(), created.getId(), updateRequest);
        Assert.assertEquals(resp.statusCode(), 403);
    }

    @Test
    public void updateWithMissingBody_returns400() {
        Response resp = playerClient.update(Method.PATCH, Editor.ADMIN.value(), 1L, null);
        Assert.assertEquals(resp.statusCode(), 400);
    }

    @Test
    public void updateWithWrongHttpMethod_returns405() {
        PlayerUpdateRequest body = PlayerRequestFactory.updateFromCreate(PlayerRequestFactory.validUserCreate());
        Response resp = playerClient.update(Method.POST, Editor.ADMIN.value(), 1L, body);
        Assert.assertEquals(resp.statusCode(), 405);
    }
}
