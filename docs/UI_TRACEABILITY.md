# Traceability Matrix - UI

Maps every manual test case in [UI_TEST_CASES.md](UI_TEST_CASES.md) to its automated
implementation. All manual test cases below are automated (status `Automated`).

## Practice Form

- **TC-UI-FORM-001** → `com.flamingo.qa.ui.form.PracticeFormTests#submitFullyFilledFormShowsMatchingSummary` — Automated
- **TC-UI-FORM-N01** → `com.flamingo.qa.ui.form.PracticeFormTests#submitEmptyFormShowsNoSuccessModal` — Automated
- **TC-UI-FORM-N02** → `com.flamingo.qa.ui.form.PracticeFormTests#submitFormWithInvalidFieldShowsNoSuccessModal` (parameterized, "invalid email" case) — Automated
- **TC-UI-FORM-N03** → `com.flamingo.qa.ui.form.PracticeFormTests#submitFormWithInvalidFieldShowsNoSuccessModal` (parameterized, "invalid mobile number" case) — Automated

## Web Tables

- **TC-UI-TABLE-001** → `com.flamingo.qa.ui.webtables.WebTablesTests#addNewRecordShowsItInTable` — Automated
- **TC-UI-TABLE-002** → `com.flamingo.qa.ui.webtables.WebTablesTests#editRecordUpdatesItsValues` — Automated
- **TC-UI-TABLE-003** → `com.flamingo.qa.ui.webtables.WebTablesTests#deleteRecordRemovesItFromTable` — Automated
- **TC-UI-TABLE-004** → `com.flamingo.qa.ui.webtables.WebTablesTests#searchFiltersTableToMatchingRecords` — Automated
- **TC-UI-TABLE-N01** → `com.flamingo.qa.ui.webtables.WebTablesTests#clickingColumnHeaderDoesNotReorderRows` — Automated
- **TC-UI-TABLE-N02** → `com.flamingo.qa.ui.webtables.WebTablesTests#addRecordWithInvalidEmailIsRejected` — Automated
- **TC-UI-TABLE-N03** → `com.flamingo.qa.ui.webtables.WebTablesTests#addRecordWithMissingRequiredFieldIsRejected` — Automated

**Coverage summary:** 11 manual test cases, 11 automated (100%).
