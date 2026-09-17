package com.flamingo.qa.graphql;

import java.util.Map;

/** Reusable GraphQL query strings for the Hygraph "Video Streaming" example schema. */
public final class GraphQLQueries {

    private GraphQLQueries() {
    }

    public static final String MOVIES_LIST = """
            query {
              movies(first: 5) {
                id
                title
                moviePoster { url }
              }
            }
            """;

    public static final String MOVIES_WITH_PAGINATION = """
            query {
              movies(first: 2) {
                id
                title
              }
            }
            """;

    public static final String MOVIES_CONNECTION = """
            query {
              moviesConnection(first: 3) {
                pageInfo {
                  hasNextPage
                  hasPreviousPage
                }
                edges {
                  node { id title }
                }
              }
            }
            """;

    public static final String MOVIES_ORDERED_BY_TITLE = """
            query {
              movies(orderBy: title_ASC, first: 10) {
                title
              }
            }
            """;

    /** Uses a declared GraphQL variable (${@code $id}) rather than string interpolation. */
    public static final String MOVIE_BY_ID = """
            query MovieById($id: ID!) {
              movie(where: { id: $id }) {
                id
                title
              }
            }
            """;

    public static Map<String, Object> movieByIdVariables(String id) {
        return Map.of("id", id);
    }

    public static final String UNKNOWN_FIELD_QUERY = """
            query {
              movies(first: 1) {
                thisFieldDoesNotExist
              }
            }
            """;

    /** Deliberately missing a closing brace to trigger a GraphQL syntax error. */
    public static final String MALFORMED_SYNTAX_QUERY = """
            query {
              movies(first: 1) {
                title
            """;

    public static final String NON_EXISTENT_MOVIE_ID = "does-not-exist-000000";
}
