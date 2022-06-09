echo '================================================================================='
echo Running Tests
echo '================================================================================='
if [[ -z $test_option ]]; then
  mvn clean test
else
  mvn clean test "-Dsuite=$test_option"
fi
