package com.flamingo.qa.graphql;

import com.flamingo.qa.client.AllureReportingSupport;
import com.flamingo.qa.config.Config;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

/** Minimal GraphQL client: sends {query, variables} payloads to the Hygraph endpoint. */
public final class GraphQLClient {

    static {
        AllureReportingSupport.ensureRegistered();
    }

    private GraphQLClient() {
    }

    @Step("POST GraphQL query")
    public static Response execute(String query) {
        return execute(query, null);
    }

    @Step("POST GraphQL query with variables")
    public static Response execute(String query, Map<String, Object> variables) {
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        if (variables != null) {
            body.put("variables", variables);
        }
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(body)
                .when().post(Config.HYGRAPH_VIDEO_ENDPOINT);
    }
}
