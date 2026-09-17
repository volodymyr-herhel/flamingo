package com.flamingo.qa.api.graphql;

import com.flamingo.qa.graphql.GraphQLClient;
import com.flamingo.qa.graphql.GraphQLQueries;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
@Epic("Hygraph GraphQL API")
@Feature("Video schema - negative queries")
class GraphQLNegativeTests {

    @Test
    @DisplayName("TC-GQL-N01: Query with an unknown field returns a GraphQL error")
    void queryWithUnknownField() {
        Response response = GraphQLClient.execute(GraphQLQueries.UNKNOWN_FIELD_QUERY);

        response.then().statusCode(400);
        List<Map<String, Object>> errors = response.jsonPath().getList("errors");
        assertThat(errors).isNotEmpty();
    }

    @Test
    @DisplayName("TC-GQL-N02: Query with malformed syntax returns a GraphQL syntax error")
    void queryWithMalformedSyntax() {
        Response response = GraphQLClient.execute(GraphQLQueries.MALFORMED_SYNTAX_QUERY);

        response.then().statusCode(400);
        List<Map<String, Object>> errors = response.jsonPath().getList("errors");
        assertThat(errors).isNotEmpty();
    }

    @Test
    @DisplayName("TC-GQL-N03: Query a movie with a non-existent id returns null data")
    void queryMovieWithNonExistentId() {
        Response response = GraphQLClient.execute(GraphQLQueries.MOVIE_BY_ID,
                GraphQLQueries.movieByIdVariables(GraphQLQueries.NON_EXISTENT_MOVIE_ID));

        response.then().statusCode(200);
        Object movie = response.jsonPath().get("data.movie");
        assertThat(movie).isNull();
    }
}
