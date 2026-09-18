package com.flamingo.qa.api.graphql;

import com.flamingo.qa.graphql.GraphQLClient;
import com.flamingo.qa.graphql.GraphQLQueries;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
@Epic("Hygraph GraphQL API")
@Feature("Video schema - negative queries")
class GraphQLNegativeTests {

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidQueries")
    void invalidQueryReturnsGraphQLError(String scenario, String query) {
        Response response = GraphQLClient.execute(query);

        response.then().statusCode(400);
        List<Map<String, Object>> errors = response.jsonPath().getList("errors");
        assertThat(errors).isNotEmpty();
    }

    private static Stream<Arguments> invalidQueries() {
        return Stream.of(
                Arguments.of("TC-GQL-N01: Query with an unknown field returns a GraphQL error",
                        GraphQLQueries.UNKNOWN_FIELD_QUERY),
                Arguments.of("TC-GQL-N02: Query with malformed syntax returns a GraphQL syntax error",
                        GraphQLQueries.MALFORMED_SYNTAX_QUERY)
        );
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
