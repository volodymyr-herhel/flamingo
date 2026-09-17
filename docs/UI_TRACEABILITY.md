# Traceability Matrix - UI

Maps every manual test case in [UI_TEST_CASES.md](UI_TEST_CASES.md) to its automated
implementation. All manual test cases below are automated (status `Automated`).

## Practice Form

- **TC-UI-FORM-001** → `com.flamingo.qa.ui.form.PracticeFormTests#submitFullyFilledFormShowsMatchingSummary` — Automated
- **TC-UI-FORM-N01** → `com.flamingo.qa.ui.form.PracticeFormTests#submitEmptyFormShowsNoSuccessModal` — Automated

## Web Tables

- **TC-UI-TABLE-001** → `com.flamingo.qa.ui.webtables.WebTablesTests#addNewRecordShowsItInTable` — Automated
- **TC-UI-TABLE-002** → `com.flamingo.qa.ui.webtables.WebTablesTests#editRecordUpdatesItsValues` — Automated
- **TC-UI-TABLE-003** → `com.flamingo.qa.ui.webtables.WebTablesTests#deleteRecordRemovesItFromTable` — Automated
- **TC-UI-TABLE-004** → `com.flamingo.qa.ui.webtables.WebTablesTests#searchFiltersTableToMatchingRecords` — Automated
- **TC-UI-TABLE-N01** → `com.flamingo.qa.ui.webtables.WebTablesTests#clickingColumnHeaderDoesNotReorderRows` — Automated

**Coverage summary:** 7 manual test cases, 7 automated (100%).
