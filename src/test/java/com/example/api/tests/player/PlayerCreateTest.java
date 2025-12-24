package com.example.api.tests.player;

import com.example.api.data.PlayerRequestFactory;
import com.example.api.dataprovider.PlayerDataProviders;
import com.example.api.domain.Editor;
import com.example.api.dto.PlayerCreateRequest;
import com.example.api.dto.PlayerCreateResponse;
import com.example.api.dto.PlayerGetByIdRequest;
import com.example.api.dto.PlayerGetByIdResponse;
import com.example.api.mapper.PlayerMapper;
import com.example.api.tests.BaseApiTest;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PlayerCreateTest extends BaseApiTest {
    private static final PlayerMapper MAPPER = new PlayerMapper();

    @Test
    public void createByAdmin_thenGetById_returnsSameData() {
        PlayerCreateRequest request = PlayerRequestFactory.validUserCreate();
        Response createResp = createPlayerAs(Editor.ADMIN, request);
        Assert.assertEquals(createResp.statusCode(), 200, "Create player status");
        PlayerCreateResponse created = createResp.as(PlayerCreateResponse.class);
        Assert.assertNotNull(created.getId(), "Create should return id");

        Response getResp = playerClient.getById(new PlayerGetByIdRequest(created.getId()));
        Assert.assertEquals(getResp.statusCode(), 200);
        PlayerGetByIdResponse body = getResp.as(PlayerGetByIdResponse.class);

        PlayerGetByIdResponse expected = MAPPER.expectedGetById(created.getId(), request);
        Assert.assertEquals(body, expected);
    }

    @Test
    public void createBySupervisor_thenGetById_returnsSameData() {
        PlayerCreateRequest request = PlayerRequestFactory.validUserCreate();
        Response createResp = createPlayerAs(Editor.SUPERVISOR, request);
        Assert.assertEquals(createResp.statusCode(), 200, "Create player status");
        PlayerCreateResponse created = createResp.as(PlayerCreateResponse.class);
        Assert.assertNotNull(created.getId(), "Create should return id");

        Response getResp = playerClient.getById(new PlayerGetByIdRequest(created.getId()));
        Assert.assertEquals(getResp.statusCode(), 200);
        PlayerGetByIdResponse body = getResp.as(PlayerGetByIdResponse.class);

        PlayerGetByIdResponse expected = MAPPER.expectedGetById(created.getId(), request);
        Assert.assertEquals(body, expected);
    }

    @Test
    public void createWithUnknownEditor_returns403() {
        PlayerCreateRequest request = PlayerRequestFactory.validUserCreate();
        Response resp = createPlayerAs(Editor.UNKNOWN, request);
        Assert.assertEquals(resp.statusCode(), 403);
    }

    @Test(dataProvider = "validationEditors", dataProviderClass = PlayerDataProviders.class)
    public void privilegedEditorCanCreateAdminRolePlayer(Editor editor) {
        PlayerCreateRequest request = PlayerRequestFactory.validUserCreate(r -> r.setRole("admin"));

        Response resp = createPlayerAs(editor, request);
        Assert.assertEquals(resp.statusCode(), 200, "Privileged editor should be able to create admin users");
    }

    @Test
    public void createByUser_returns403() {
        PlayerCreateRequest editorUser = PlayerRequestFactory.validUserCreate();
        Response editorResp = createPlayerAs(Editor.SUPERVISOR, editorUser);
        Assert.assertEquals(editorResp.statusCode(), 200, "Create player status");
        PlayerCreateResponse created = editorResp.as(PlayerCreateResponse.class);
        Assert.assertNotNull(created.getId(), "Create should return id");

        PlayerCreateRequest request = PlayerRequestFactory.validUserCreate();
        Response resp = playerClient.create(editorUser.getLogin(), request);
        Assert.assertEquals(resp.statusCode(), 403, "User role should not create players");
    }

    @Test
    public void createResponse_shouldReturnCreatedFields() {
        PlayerCreateRequest request = PlayerRequestFactory.validUserCreate();

        Response createResp = createPlayerAs(Editor.ADMIN, request);
        Assert.assertEquals(createResp.statusCode(), 200, "Create player status");
        PlayerCreateResponse body = createResp.as(PlayerCreateResponse.class);
        Assert.assertNotNull(body.getId(), "Create should return id");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(body.getLogin(), request.getLogin(), "login should match");
        softAssert.assertNotNull(body.getScreenName(), "screenName should be returned");
        softAssert.assertNotNull(body.getGender(), "gender should be returned");
        softAssert.assertNotNull(body.getAge(), "age should be returned");
        softAssert.assertNotNull(body.getRole(), "role should be returned");
        softAssert.assertAll();
    }

    @Test(dataProvider = "validationEditors", dataProviderClass = PlayerDataProviders.class)
    public void createDuplicateLogin_shouldReturn409(Editor editor) {
        PlayerCreateRequest request = PlayerRequestFactory.validUserCreate();

        Response first = createPlayerAs(editor, request);
        Assert.assertEquals(first.statusCode(), 200, "Create player status");
        Response second = createPlayerAs(editor, request);
        Assert.assertEquals(second.statusCode(), 409, "Duplicate login should be rejected with 409 Conflict");
    }

    @Test(dataProvider = "validationEditors", dataProviderClass = PlayerDataProviders.class)
    public void createDuplicateScreenName_shouldReturn409(Editor editor) {
        PlayerCreateRequest firstRequest = PlayerRequestFactory.validUserCreate();
        Response first = createPlayerAs(editor, firstRequest);
        Assert.assertEquals(first.statusCode(), 200, "Create player status");

        PlayerCreateRequest secondRequest = PlayerRequestFactory.validUserCreate(r -> r.setScreenName(firstRequest.getScreenName()));
        Response second = createPlayerAs(editor, secondRequest);
        Assert.assertEquals(second.statusCode(), 409, "Duplicate screenName should be rejected with 409 Conflict");
    }

    @Test(dataProvider = "invalidCreateQueryParams", dataProviderClass = PlayerDataProviders.class)
    public void createWithInvalidOrMissingParams_returns400(
            Editor editor,
            String scenario,
            Consumer<Map<String, Object>> mutate
    ) {
        PlayerCreateRequest request = PlayerRequestFactory.validUserCreate();
        Map<String, Object> params = new HashMap<>(request.toMap());

        mutate.accept(params);

        Response resp = createPlayerAs(editor, params);
        Assert.assertEquals(resp.statusCode(), 400, "Expected 400 for scenario: " + scenario);
    }

    @Test(dataProvider = "invalidPasswords", dataProviderClass = PlayerDataProviders.class)
    public void createWithInvalidPassword_returns400(Editor editor, String scenario, String password) {
        PlayerCreateRequest request = PlayerRequestFactory.validUserCreate(r -> r.setPassword(password));
        Response resp = createPlayerAs(editor, request);
        Assert.assertEquals(resp.statusCode(), 400, "Expected 400 for scenario: " + scenario);
    }

    @Test
    public void createWithWrongHttpMethod_returns405() {
        PlayerCreateRequest request = PlayerRequestFactory.validUserCreate();

        Response resp = playerClient.create(Method.POST, Editor.ADMIN.value(), request);
        Assert.assertEquals(resp.statusCode(), 405);
    }
}
