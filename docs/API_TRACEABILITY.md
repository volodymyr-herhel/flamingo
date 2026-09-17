# Traceability Matrix

Maps every manual test case in [API_TEST_CASES.md](API_TEST_CASES.md) to its automated implementation.
All manual test cases below are automated (status `Automated`).

## Authentication

- **TC-AUTH-001** → `com.flamingo.qa.api.auth.AuthTests#createTokenWithValidCredentials` — Automated
- **TC-AUTH-002** → `com.flamingo.qa.api.auth.AuthTests#createTokenWithInvalidCredentials` — Automated

## Health Check

- **TC-PING-001** → `com.flamingo.qa.api.ping.PingTests#pingHealthCheck` — Automated

## Booking CRUD (positive)

- **TC-BOOK-001** → `com.flamingo.qa.api.booking.BookingCrudTests#createBookingWithValidData` — Automated
- **TC-BOOK-002** → `com.flamingo.qa.api.booking.BookingCrudTests#getBookingWithValidId` — Automated
- **TC-BOOK-003** → `com.flamingo.qa.api.booking.BookingCrudTests#updateBookingWithValidTokenAndData` — Automated
- **TC-BOOK-004** → `com.flamingo.qa.api.booking.BookingCrudTests#partialUpdateBookingWithValidToken` — Automated
- **TC-BOOK-005** → `com.flamingo.qa.api.booking.BookingCrudTests#deleteBookingWithValidToken` — Automated
- **TC-BOOK-006** → `com.flamingo.qa.api.booking.BookingCrudTests#getBookingIdsWithoutFilters` — Automated
- **TC-BOOK-007** → `com.flamingo.qa.api.booking.BookingCrudTests#getBookingIdsFilteredByFirstNameAndLastName` — Automated
- **TC-BOOK-008** → `com.flamingo.qa.api.booking.BookingCrudTests#getBookingIdsFilteredByCheckinAndCheckout` — Automated
- **TC-BOOK-009** → `com.flamingo.qa.api.booking.BookingCrudTests#updateBookingWithBasicAuth` — Automated

## Booking Negative Scenarios

- **TC-BOOK-N01** → `com.flamingo.qa.api.booking.BookingNegativeTests#getBookingWithNonExistentId` — Automated
- **TC-BOOK-N02** → `com.flamingo.qa.api.booking.BookingNegativeTests#updateBookingWithoutAuthToken` — Automated
- **TC-BOOK-N03** → `com.flamingo.qa.api.booking.BookingNegativeTests#deleteBookingWithoutAuthToken` — Automated
- **TC-BOOK-N04** → `com.flamingo.qa.api.booking.BookingNegativeTests#createBookingWithMalformedJsonBody` — Automated

## GraphQL (positive)

- **TC-GQL-001** → `com.flamingo.qa.api.graphql.GraphQLPositiveTests#queryMovies` — Automated
- **TC-GQL-002** → `com.flamingo.qa.api.graphql.GraphQLPositiveTests#queryMovieById` — Automated
- **TC-GQL-003** → `com.flamingo.qa.api.graphql.GraphQLPositiveTests#queryMoviesWithPagination` — Automated
- **TC-GQL-004** → `com.flamingo.qa.api.graphql.GraphQLPositiveTests#queryMoviesConnection` — Automated
- **TC-GQL-005** → `com.flamingo.qa.api.graphql.GraphQLPositiveTests#queryMoviesOrderedByTitle` — Automated

## GraphQL (negative)

- **TC-GQL-N01** → `com.flamingo.qa.api.graphql.GraphQLNegativeTests#queryWithUnknownField` — Automated
- **TC-GQL-N02** → `com.flamingo.qa.api.graphql.GraphQLNegativeTests#queryWithMalformedSyntax` — Automated
- **TC-GQL-N03** → `com.flamingo.qa.api.graphql.GraphQLNegativeTests#queryMovieWithNonExistentId` — Automated

**Coverage summary:** 24 manual test cases, 24 automated (100%). Booking CRUD (9) exceeds the
minimum of 3, GraphQL (8) exceeds the minimum of 5.

