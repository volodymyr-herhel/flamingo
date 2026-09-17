# Traceability Matrix

Maps every manual test case in [API_TEST_CASES.md](API_TEST_CASES.md) to its automated implementation.
All manual test cases below are automated (status `Automated`).

| TC ID | Automated Test Class | Automated Method | Status |
|-------|-----------------------|-------------------|--------|
| TC-AUTH-001 | `com.flamingo.qa.api.auth.AuthTests` | `createToken_withValidCredentials_returnsToken` | Automated |
| TC-AUTH-002 | `com.flamingo.qa.api.auth.AuthTests` | `createToken_withInvalidCredentials_returnsReasonBadCredentials` | Automated |
| TC-PING-001 | `com.flamingo.qa.api.ping.PingTests` | `ping_healthCheck_returns201` | Automated |
| TC-BOOK-001 | `com.flamingo.qa.api.booking.BookingCrudTests` | `createBooking_withValidData_returnsCreatedBooking` | Automated |
| TC-BOOK-002 | `com.flamingo.qa.api.booking.BookingCrudTests` | `getBooking_withValidId_returnsMatchingBookingData` | Automated |
| TC-BOOK-003 | `com.flamingo.qa.api.booking.BookingCrudTests` | `updateBooking_withValidTokenAndData_updatesAllFields` | Automated |
| TC-BOOK-004 | `com.flamingo.qa.api.booking.BookingCrudTests` | `partialUpdateBooking_withValidToken_updatesOnlyProvidedFields` | Automated |
| TC-BOOK-005 | `com.flamingo.qa.api.booking.BookingCrudTests` | `deleteBooking_withValidToken_removesBooking` | Automated |
| TC-BOOK-006 | `com.flamingo.qa.api.booking.BookingCrudTests` | `getBookingIds_withoutFilters_returnsNonEmptyIdList` | Automated |
| TC-BOOK-007 | `com.flamingo.qa.api.booking.BookingCrudTests` | `getBookingIds_filteredByFirstNameAndLastName_returnsCreatedBookingId` | Automated |
| TC-BOOK-N01 | `com.flamingo.qa.api.booking.BookingNegativeTests` | `getBooking_withNonExistentId_returns404` | Automated |
| TC-BOOK-N02 | `com.flamingo.qa.api.booking.BookingNegativeTests` | `updateBooking_withoutAuthToken_returns403` | Automated |
| TC-BOOK-N03 | `com.flamingo.qa.api.booking.BookingNegativeTests` | `deleteBooking_withoutAuthToken_returns403` | Automated |
| TC-BOOK-N04 | `com.flamingo.qa.api.booking.BookingNegativeTests` | `createBooking_withMalformedJsonBody_returnsClientError` | Automated |
| TC-GQL-001 | `com.flamingo.qa.api.graphql.GraphQLPositiveTests` | `queryMovies_returnsListWithTitleAndPoster` | Automated |
| TC-GQL-002 | `com.flamingo.qa.api.graphql.GraphQLPositiveTests` | `queryMovieById_returnsMatchingMovie` | Automated |
| TC-GQL-003 | `com.flamingo.qa.api.graphql.GraphQLPositiveTests` | `queryMoviesWithPagination_returnsLimitedResults` | Automated |
| TC-GQL-004 | `com.flamingo.qa.api.graphql.GraphQLPositiveTests` | `queryMoviesConnection_returnsPageInfo` | Automated |
| TC-GQL-005 | `com.flamingo.qa.api.graphql.GraphQLPositiveTests` | `queryMoviesOrderedByTitle_returnsSortedResults` | Automated |
| TC-GQL-N01 | `com.flamingo.qa.api.graphql.GraphQLNegativeTests` | `queryWithUnknownField_returnsGraphQLError` | Automated |
| TC-GQL-N02 | `com.flamingo.qa.api.graphql.GraphQLNegativeTests` | `queryWithMalformedSyntax_returnsSyntaxError` | Automated |
| TC-GQL-N03 | `com.flamingo.qa.api.graphql.GraphQLNegativeTests` | `queryMovieWithNonExistentId_returnsNullData` | Automated |

**Coverage summary:** 22 manual test cases, 22 automated (100%). Booking CRUD (7) exceeds the
minimum of 3, GraphQL (8) exceeds the minimum of 5.
