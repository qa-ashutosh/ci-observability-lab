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

echo "IGNORED_TESTS=$(echo "${TEST_RESULTS_STRING}" | awk -F'"' '{ print $2 }')"
echo "TOTAL_TESTS=$(echo "${TEST_RESULTS_STRING}" | awk -F'"' '{ print $4 }')"
echo "PASSED_TESTS=$(echo "${TEST_RESULTS_STRING}" | awk -F'"' '{ print $6 }')"
echo "FAILED_TESTS=$(echo "${TEST_RESULTS_STRING}" | awk -F'"' '{ print $8 }')"
echo "SKIPPED_TESTS=$(echo "${TEST_RESULTS_STRING}" | awk -F'"' '{ print $10 }')"