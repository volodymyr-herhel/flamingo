# Manual Test Cases

Manual test case catalog for the Flamingo QA Automation Assignment. These test cases were
designed first (test design phase) and every one of them has since been automated — see
[API_TRACEABILITY.md](API_TRACEABILITY.md) for the mapping to the automated test classes/methods.

Scope:
- Restful Booker REST API (`https://restful-booker.herokuapp.com`)
- Hygraph GraphQL API — "Video Streaming" example schema

## Authentication

### TC-AUTH-001: Successful authentication returns a token
- **Pre:** API is reachable
- **Steps:** `POST /auth` with `{"username":"admin","password":"password123"}`
- **Expected:** Status `200`; response body contains a non-empty `token`

### TC-AUTH-002: Authentication with invalid credentials is rejected
- **Pre:** API is reachable
- **Steps:** `POST /auth` with a wrong password
- **Expected:** Status `200`; no `token` field; `reason` = `"Bad credentials"`

## Health Check

### TC-PING-001: Health check endpoint is reachable
- **Pre:** API is reachable
- **Steps:** `GET /ping`
- **Expected:** Status `201`

## Booking CRUD (positive)

### TC-BOOK-001: Create booking with valid data
- **Pre:** None
- **Steps:** `POST /booking` with a fully populated booking payload
- **Expected:** Status `200`; response contains a positive `bookingid` and an echoed `booking` object equal to the request

### TC-BOOK-002: Get booking by id
- **Pre:** A booking exists
- **Steps:** `GET /booking/{id}` for an existing booking
- **Expected:** Status `200`; returned fields match the created booking

### TC-BOOK-003: Update booking (PUT) with a valid token
- **Pre:** A booking exists; auth token obtained
- **Steps:** `PUT /booking/{id}` with a full new payload and `Cookie: token=...`
- **Expected:** Status `200`; all fields reflect the new data

### TC-BOOK-004: Partial update (PATCH) with a valid token
- **Pre:** A booking exists; auth token obtained
- **Steps:** `PATCH /booking/{id}` with only `firstname` and a valid token
- **Expected:** Status `200`; `firstname` changed; other fields unchanged

### TC-BOOK-005: Delete booking with a valid token
- **Pre:** A booking exists; auth token obtained
- **Steps:**
  1. `DELETE /booking/{id}` with a valid token
  2. `GET /booking/{id}`
- **Expected:** Delete returns `201`; subsequent GET returns `404`

### TC-BOOK-006: List all booking ids
- **Pre:** Bookings exist
- **Steps:** `GET /booking` (no filters)
- **Expected:** Status `200`; response is a non-empty list of `{bookingid}` objects

### TC-BOOK-007: Filter booking ids by first/last name
- **Pre:** A booking with a known name exists
- **Steps:** `GET /booking?firstname=X&lastname=Y`
- **Expected:** Status `200`; result list contains the booking's id

### TC-BOOK-008: Filter booking ids by checkin/checkout date
- **Pre:** A booking with known checkin/checkout dates exists
- **Steps:** `GET /booking?checkin={checkin}&checkout={checkout}` using the booking's own dates
- **Expected:** Status `200`; response is a well-formed list of `{bookingid}` objects (Restful
  Booker's date filter is known to be unreliable on the shared demo instance, so this doesn't
  assert the specific booking is present)

### TC-BOOK-009: Update booking (PUT) with Basic auth
- **Pre:** A booking exists
- **Steps:** `PUT /booking/{id}` with a full new payload and `Authorization: Basic <base64(admin:password123)>`
- **Expected:** Status `200`; all fields reflect the new data

## Booking Negative Scenarios

### TC-BOOK-N01: Get booking with a non-existent id
- **Pre:** None
- **Steps:** `GET /booking/999999999`
- **Expected:** Status `404`

### TC-BOOK-N02: Update booking without an auth token
- **Pre:** A booking exists
- **Steps:** `PUT /booking/{id}` without `Cookie`/`Authorization` header
- **Expected:** Status `403`; booking is not modified

### TC-BOOK-N03: Delete booking without an auth token
- **Pre:** A booking exists
- **Steps:** `DELETE /booking/{id}` without `Cookie`/`Authorization` header
- **Expected:** Status `403`; booking is not removed

### TC-BOOK-N04: Create booking with a malformed JSON body
- **Pre:** None
- **Steps:** `POST /booking` with a syntactically invalid JSON body
- **Expected:** Status is a client error (`4xx`)

## GraphQL (positive)

### TC-GQL-001: Query movies list
- **Pre:** None
- **Steps:** Query `movies(first: 5) { id title moviePoster { url } }`
- **Expected:** Status `200`; non-empty list; each item has `id`, `title`, `moviePoster`

### TC-GQL-002: Query a single movie by id
- **Pre:** A movie id is known
- **Steps:** Query `movie(where: { id })`
- **Expected:** Status `200`; returned `movie.id` matches requested id

### TC-GQL-003: Query movies with pagination
- **Pre:** None
- **Steps:** Query `movies(first: 2)`
- **Expected:** Status `200`; result has at most 2 items

### TC-GQL-004: Query movies connection (pageInfo)
- **Pre:** None
- **Steps:** Query `moviesConnection(first: 3) { pageInfo { hasNextPage } edges { node { id title } } }`
- **Expected:** Status `200`; `pageInfo.hasNextPage` present; edges non-empty

### TC-GQL-005: Query movies ordered by title
- **Pre:** None
- **Steps:** Query `movies(orderBy: title_ASC)`
- **Expected:** Status `200`; returned titles are sorted ascending

## GraphQL (negative)

### TC-GQL-N01: Query an unknown field
- **Pre:** None
- **Steps:** Query `movies { thisFieldDoesNotExist }`
- **Expected:** Response contains a non-empty `errors` array

### TC-GQL-N02: Query with malformed GraphQL syntax
- **Pre:** None
- **Steps:** Send a query missing a closing brace
- **Expected:** Response contains a non-empty `errors` array describing a syntax error

### TC-GQL-N03: Query a movie with a non-existent id
- **Pre:** None
- **Steps:** Query `movie(where: { id: "does-not-exist-000000" })`
- **Expected:** Status `200`; `data.movie` is `null`; no errors

