#!/usr/bin/bash

###################################################################################################
#     This is extracting the values for each test suite description.                              #
#     It uses awk to print the value between two quotes.                                          #
#     The columns printed is based on the following example string:                               #
#     <testng-results ignored="0" total="23" passed="22" failed="1" skipped="0">                  #
###################################################################################################

TEST_RESULTS_LOCATION="${1:-target/surefire-reports}"
TEST_RESULTS_FILE="${TEST_RESULTS_LOCATION}/testng-results.xml"

if [[ ! -f "${TEST_RESULTS_FILE}" ]]; then
    echo "ERROR: Test results file not found: ${TEST_RESULTS_FILE}"
    exit 1
fi

TEST_RESULTS_STRING=$(grep "<testng-results" "${TEST_RESULTS_FILE}")

if [[ -z "${TEST_RESULTS_STRING}" ]]; then
    echo "ERROR: <testng-results> element not found in ${TEST_RESULTS_FILE}"
    exit 1
fi

# Extract test results
IGNORED_TESTS=$(echo "${TEST_RESULTS_STRING}" | awk -F'"' '{ print $2 }')
TOTAL_TESTS=$(echo "${TEST_RESULTS_STRING}" | awk -F'"' '{ print $4 }')
PASSED_TESTS=$(echo "${TEST_RESULTS_STRING}" | awk -F'"' '{ print $6 }')
FAILED_TESTS=$(echo "${TEST_RESULTS_STRING}" | awk -F'"' '{ print $8 }')
SKIPPED_TESTS=$(echo "${TEST_RESULTS_STRING}" | awk -F'"' '{ print $10 }')

# Push test metrics to Prometheus Pushgateway
cat <<EOF | curl --fail --silent --show-error \
  --data-binary @- \
  "${PUSHGATEWAY_URL}/metrics/job/github_actions"
github_actions_ignored_tests{action_id="${GITHUB_RUN_NUMBER}",commit="${GITHUB_SHA}",actor="${GITHUB_ACTOR}",branch="${GITHUB_REF_NAME}"} ${IGNORED_TESTS}
github_actions_total_tests{action_id="${GITHUB_RUN_NUMBER}",commit="${GITHUB_SHA}",actor="${GITHUB_ACTOR}",branch="${GITHUB_REF_NAME}"} ${TOTAL_TESTS}
github_actions_passed_tests{action_id="${GITHUB_RUN_NUMBER}",commit="${GITHUB_SHA}",actor="${GITHUB_ACTOR}",branch="${GITHUB_REF_NAME}"} ${PASSED_TESTS}
github_actions_failed_tests{action_id="${GITHUB_RUN_NUMBER}",commit="${GITHUB_SHA}",actor="${GITHUB_ACTOR}",branch="${GITHUB_REF_NAME}"} ${FAILED_TESTS}
github_actions_skipped_tests{action_id="${GITHUB_RUN_NUMBER}",commit="${GITHUB_SHA}",actor="${GITHUB_ACTOR}",branch="${GITHUB_REF_NAME}"} ${SKIPPED_TESTS}
EOF

# Add test results to Honeycomb build events
echo "gha.maven.test.ignored=${IGNORED_TESTS}" >> "$BUILDEVENT_FILE"
echo "gha.maven.test.total=${TOTAL_TESTS}" >> "$BUILDEVENT_FILE"
echo "gha.maven.test.passed=${PASSED_TESTS}" >> "$BUILDEVENT_FILE"
echo "gha.maven.test.failed=${FAILED_TESTS}" >> "$BUILDEVENT_FILE"
echo "gha.maven.test.skipped=${SKIPPED_TESTS}" >> "$BUILDEVENT_FILE"
