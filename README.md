# molgenis-selenium

[![Build Status](http://www.molgenis.org/jenkins/buildStatus/icon?job=selenium-molgenis-test)](http://www.molgenis.org/jenkins/job/selenium-molgenis-test/)

This project uses selenium and the molgenis REST client to test a running molgenis instance.
You can configure which instance should be tested in a property file.

The property file can have any name.
A sample property file that tests on your local system is in `src/test/resources/local.properties`.

To run the tests, you run maven with the target `test`.
The configuration file should be specified by setting the environment variable

    jenkins_propsfile=/path/to/the/properties

![](doc/run-configuration-eclipse.png "Eclipse run configuration")
