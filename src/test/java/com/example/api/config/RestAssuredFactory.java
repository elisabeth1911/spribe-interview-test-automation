package com.example.api.config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class RestAssuredFactory {

    private RestAssuredFactory() {}

    public static RequestSpecification baseSpec(String baseUri) {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .addFilter(new AllureRestAssured())
                .build();
    }
}
