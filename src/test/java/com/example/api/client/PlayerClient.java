package com.example.api.client;

import com.example.api.dto.PlayerCreateRequest;
import com.example.api.dto.PlayerDeleteRequest;
import com.example.api.dto.PlayerGetByIdRequest;
import com.example.api.dto.PlayerUpdateRequest;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public final class PlayerClient {

    private final RequestSpecification spec;

    public PlayerClient(RequestSpecification spec) {
        this.spec = spec;
    }

    /**
     * NOTE: this API uses GET for a state-changing operation (create).
     * TODO: When the API is fixed, migrate this operation to POST with a body DTO.
     */
    public Response create(String editor, PlayerCreateRequest request) {
        return create(Method.GET, editor, request.toMap());
    }

    public Response create(Method method, String editor, PlayerCreateRequest request) {
        return create(method, editor, request.toMap());
    }

    public Response create(Method method, String editor, Map<String, Object> request) {
        return request(method, "/player/create/{editor}", Map.of("editor", editor), request, null);
    }

    /**
     * NOTE: this API uses POST for a non state-changing operation (get).
     * TODO: When the API is fixed, migrate this operation to GET with one query parameter.
     */
    public Response getById(PlayerGetByIdRequest request) {
        return getById(Method.POST, request.toMap());
    }

    public Response getById(Method method, Map<String, Object> request) {
        return request(method, "/player/get", null, null, request);
    }

    public Response getAll() {
        return getAll(Method.GET);
    }

    public Response getAll(Method method) {
        return request(method, "/player/get/all", null, null, null);
    }

    public Response update(String editor, long id, PlayerUpdateRequest request) {
        return update(Method.PATCH, editor, id, request);
    }

    public Response update(Method method, String editor, long id, Object body) {
        return request(method, "/player/update/{editor}/{id}", Map.of("editor", editor, "id", id), null, body);
    }

    public Response delete(String editor, PlayerDeleteRequest request) {
        return delete(Method.DELETE, editor, request);
    }

    public Response delete(Method method, String editor, Object body) {
        return request(method, "/player/delete/{editor}", Map.of("editor", editor), null, body);
    }

    private Response request(Method method, String path, Map<String, Object> pathParams, Map<String, Object> queryParams, Object body) {
        RequestSpecification reqSpecification = RestAssured.with().spec(spec);
        if (pathParams != null) {
            reqSpecification.pathParams(pathParams);
        }
        if (queryParams != null) {
            reqSpecification.queryParams(queryParams);
        }
        if (body != null) {
            reqSpecification.body(body);
        }
        return reqSpecification.request(method, path).then().extract().response();
    }

}
