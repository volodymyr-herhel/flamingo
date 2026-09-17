# Manual Test Cases

Manual test case catalog for the Flamingo QA Automation Assignment. These test cases were
designed first (test design phase) and every one of them has since been automated — see
[TRACEABILITY.md](TRACEABILITY.md) for the mapping to the automated test classes/methods.

Scope:
- Restful Booker REST API (`https://restful-booker.herokuapp.com`)
- Hygraph GraphQL API — "Video Streaming" example schema

Legend: **Pre** = preconditions, **Steps** = execution steps, **Expected** = expected result.

## Authentication

| ID | Title | Pre | Steps | Expected |
|----|-------|-----|-------|----------|
| TC-AUTH-001 | Successful authentication returns a token | API is reachable | 1. `POST /auth` with `{"username":"admin","password":"password123"}` | Status `200`; response body contains a non-empty `token` |
| TC-AUTH-002 | Authentication with invalid credentials is rejected | API is reachable | 1. `POST /auth` with a wrong password | Status `200`; no `token` field; `reason` = `"Bad credentials"` |

## Health Check

| ID | Title | Pre | Steps | Expected |
|----|-------|-----|-------|----------|
| TC-PING-001 | Health check endpoint is reachable | API is reachable | 1. `GET /ping` | Status `201` |

## Booking CRUD (positive)

| ID | Title | Pre | Steps | Expected |
|----|-------|-----|-------|----------|
| TC-BOOK-001 | Create booking with valid data | None | 1. `POST /booking` with a fully populated booking payload | Status `200`; response contains a positive `bookingid` and an echoed `booking` object equal to the request |
| TC-BOOK-002 | Get booking by id | A booking exists | 1. `GET /booking/{id}` for an existing booking | Status `200`; returned fields match the created booking |
| TC-BOOK-003 | Update booking (PUT) with a valid token | A booking exists; auth token obtained | 1. `PUT /booking/{id}` with a full new payload and `Cookie: token=...` | Status `200`; all fields reflect the new data |
| TC-BOOK-004 | Partial update (PATCH) with a valid token | A booking exists; auth token obtained | 1. `PATCH /booking/{id}` with only `firstname` and a valid token | Status `200`; `firstname` changed; other fields unchanged |
| TC-BOOK-005 | Delete booking with a valid token | A booking exists; auth token obtained | 1. `DELETE /booking/{id}` with a valid token<br>2. `GET /booking/{id}` | Delete returns `201`; subsequent GET returns `404` |
| TC-BOOK-006 | List all booking ids | Bookings exist | 1. `GET /booking` (no filters) | Status `200`; response is a non-empty list of `{bookingid}` objects |
| TC-BOOK-007 | Filter booking ids by first/last name | A booking with a known name exists | 1. `GET /booking?firstname=X&lastname=Y` | Status `200`; result list contains the booking's id |

## Booking Negative Scenarios

| ID | Title | Pre | Steps | Expected |
|----|-------|-----|-------|----------|
| TC-BOOK-N01 | Get booking with a non-existent id | None | 1. `GET /booking/999999999` | Status `404` |
| TC-BOOK-N02 | Update booking without an auth token | A booking exists | 1. `PUT /booking/{id}` without `Cookie`/`Authorization` header | Status `403`; booking is not modified |
| TC-BOOK-N03 | Delete booking without an auth token | A booking exists | 1. `DELETE /booking/{id}` without `Cookie`/`Authorization` header | Status `403`; booking is not removed |
| TC-BOOK-N04 | Create booking with a malformed JSON body | None | 1. `POST /booking` with a syntactically invalid JSON body | Status is a client error (`4xx`) |

## GraphQL (positive)

| ID | Title | Pre | Steps | Expected |
|----|-------|-----|-------|----------|
| TC-GQL-001 | Query movies list | None | 1. Query `movies(first: 5) { id title moviePoster { url } }` | Status `200`; non-empty list; each item has `id`, `title`, `moviePoster` |
| TC-GQL-002 | Query a single movie by id | A movie id is known | 1. Query `movie(where: { id })` | Status `200`; returned `movie.id` matches requested id |
| TC-GQL-003 | Query movies with pagination | None | 1. Query `movies(first: 2)` | Status `200`; result has at most 2 items |
| TC-GQL-004 | Query movies connection (pageInfo) | None | 1. Query `moviesConnection(first: 3) { pageInfo { hasNextPage } edges { node { id title } } }` | Status `200`; `pageInfo.hasNextPage` present; edges non-empty |
| TC-GQL-005 | Query movies ordered by title | None | 1. Query `movies(orderBy: title_ASC)` | Status `200`; returned titles are sorted ascending |

## GraphQL (negative)

| ID | Title | Pre | Steps | Expected |
|----|-------|-----|-------|----------|
| TC-GQL-N01 | Query an unknown field | None | 1. Query `movies { thisFieldDoesNotExist }` | Response contains a non-empty `errors` array |
| TC-GQL-N02 | Query with malformed GraphQL syntax | None | 1. Send a query missing a closing brace | Response contains a non-empty `errors` array describing a syntax error |
| TC-GQL-N03 | Query a movie with a non-existent id | None | 1. Query `movie(where: { id: "does-not-exist-000000" })` | Status `200`; `data.movie` is `null`; no errors |
