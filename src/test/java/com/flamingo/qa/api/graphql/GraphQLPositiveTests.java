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
@Feature("Video schema - positive queries")
class GraphQLPositiveTests {

    @Test
    @DisplayName("TC-GQL-001: Query movies returns a list with title and poster")
    void queryMovies_returnsListWithTitleAndPoster() {
        Response response = GraphQLClient.execute(GraphQLQueries.MOVIES_LIST);

        response.then().statusCode(200);
        List<Map<String, Object>> movies = response.jsonPath().getList("data.movies");
        assertThat(movies).isNotEmpty();
        assertThat(movies.get(0)).containsKeys("id", "title", "moviePoster");
    }

    @Test
    @DisplayName("TC-GQL-002: Query a single movie by id returns the matching movie")
    void queryMovieById_returnsMatchingMovie() {
        Response listResponse = GraphQLClient.execute(GraphQLQueries.MOVIES_LIST);
        String firstMovieId = listResponse.jsonPath().getString("data.movies[0].id");

        Response response = GraphQLClient.execute(GraphQLQueries.movieById(firstMovieId));

        response.then().statusCode(200);
        assertThat(response.jsonPath().getString("data.movie.id")).isEqualTo(firstMovieId);
    }

    @Test
    @DisplayName("TC-GQL-003: Query movies with pagination returns a limited number of results")
    void queryMoviesWithPagination_returnsLimitedResults() {
        Response response = GraphQLClient.execute(GraphQLQueries.MOVIES_WITH_PAGINATION);

        response.then().statusCode(200);
        List<Map<String, Object>> movies = response.jsonPath().getList("data.movies");
        assertThat(movies).hasSizeLessThanOrEqualTo(2);
    }

    @Test
    @DisplayName("TC-GQL-004: Query movies connection returns pagination info")
    void queryMoviesConnection_returnsPageInfo() {
        Response response = GraphQLClient.execute(GraphQLQueries.MOVIES_CONNECTION);

        response.then().statusCode(200);
        Boolean hasNextPage = response.jsonPath().get("data.moviesConnection.pageInfo.hasNextPage");
        assertThat(hasNextPage).isNotNull();
        assertThat(response.jsonPath().getList("data.moviesConnection.edges")).isNotEmpty();
    }

    @Test
    @DisplayName("TC-GQL-005: Query movies ordered by title returns results sorted ascending")
    void queryMoviesOrderedByTitle_returnsSortedResults() {
        Response response = GraphQLClient.execute(GraphQLQueries.MOVIES_ORDERED_BY_TITLE);

        response.then().statusCode(200);
        List<String> titles = response.jsonPath().getList("data.movies.title", String.class);
        assertThat(titles).isNotEmpty().isSorted();
    }
}
