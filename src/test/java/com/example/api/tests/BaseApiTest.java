package com.example.api.tests;

import com.example.api.client.PlayerClient;
import com.example.api.config.RestAssuredFactory;
import com.example.api.domain.Editor;
import com.example.api.dto.PlayerCreateRequest;
import com.example.api.dto.PlayerDeleteRequest;
import io.qameta.allure.testng.AllureTestNg;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Listeners({AllureTestNg.class})
public abstract class BaseApiTest {
    protected static final String BASE_URI = System.getProperty("baseUri", "http://3.68.165.45");
    protected static final RequestSpecification SPEC = RestAssuredFactory.baseSpec(BASE_URI);
    protected static final PlayerClient playerClient = new PlayerClient(SPEC);

    private static final ThreadLocal<List<Long>> CREATED_PLAYERS = ThreadLocal.withInitial(ArrayList::new);

    @BeforeMethod(alwaysRun = true)
    public void initCleanup() {
        CREATED_PLAYERS.get().clear();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanupCreatedPlayers() {
         List<Long> ids = CREATED_PLAYERS.get();
         for (Long id : ids) {
             System.out.println("Cleanup player id=" + id);
             Response resp = playerClient.delete(Editor.SUPERVISOR.value(), new PlayerDeleteRequest(id));
             System.out.println("Cleanup player id=" + id + " status=" + resp.statusCode());
         }
         ids.clear();
    }

    protected static Response createPlayerAs(Editor editor, PlayerCreateRequest request) {
        Response resp = playerClient.create(editor.value(), request);
        trackCreatedPlayerIfPresent(resp);
        return resp;
    }

    protected static Response createPlayerAs(Editor editor, Map<String, Object> request) {
        Response resp = playerClient.create(Method.GET, editor.value(), request);
        trackCreatedPlayerIfPresent(resp);
        return resp;
    }

    private static void trackCreatedPlayerIfPresent(Response resp) {
        try {
            long id = resp.jsonPath().getLong("id");
            if (id != 0L) CREATED_PLAYERS.get().add(id);
        } catch (Exception ignored) {}
    }
}
