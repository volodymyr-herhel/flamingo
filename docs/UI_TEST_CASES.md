# Manual Test Cases - UI

Manual test case catalog for the Playwright UI automation (Part 2 of the Flamingo QA Automation
Assignment). Designed first (test design phase), then automated 1:1 — see
[UI_TRACEABILITY.md](UI_TRACEABILITY.md) for the mapping to automated test classes/methods.

Scope:
- DemoQA Practice Form (`https://demoqa.com/automation-practice-form`)
- DemoQA Web Tables (`https://demoqa.com/webtables`)

## Practice Form

### TC-UI-FORM-001: Submit a fully filled student registration form
- **Pre:** Practice form page is open
- **Steps:**
  1. Fill first/last name, email, gender, mobile number
  2. Select a date of birth from the date picker
  3. Add a subject via the subjects autocomplete
  4. Check a hobby
  5. Upload a picture
  6. Fill the current address
  7. Select a state and city from the dropdowns
  8. Submit
- **Expected:** The success modal appears and every field's value matches what was entered

### TC-UI-FORM-N01: Submit the form with no data filled in
- **Pre:** Practice form page is open
- **Steps:** Click Submit without filling any field
- **Expected:** No success modal appears (native required-field validation blocks submission)

## Web Tables

### TC-UI-TABLE-001: Add a new record
- **Pre:** Web Tables page is open
- **Steps:** Click Add, fill the registration form, submit
- **Expected:** The new record appears in the table

### TC-UI-TABLE-002: Edit an existing record
- **Pre:** A record exists
- **Steps:** Click its Edit icon, change a field, submit
- **Expected:** The table shows the updated value

### TC-UI-TABLE-003: Delete a record
- **Pre:** A record exists
- **Steps:** Click its Delete icon
- **Expected:** The record no longer appears in the table

### TC-UI-TABLE-004: Search filters the table
- **Pre:** A record with a known email exists
- **Steps:** Type the email into the search box
- **Expected:** Only the matching record remains visible

### TC-UI-TABLE-N01: Column header click does not reorder rows
- **Pre:** Default table data is loaded
- **Steps:** Click the "First Name" column header
- **Expected:** Row order is unchanged (this DemoQA build has no working column sort — verified
  manually; this test documents the current, known behavior rather than assuming the sort
  feature works)
