Camel Router Spring Project
===========================

This project has been created using

    mvn archetype:generate                         \
    -DarchetypeGroupId=org.apache.camel.archetypes \
    -DarchetypeArtifactId=camel-archetype-spring   \
    -DarchetypeVersion=2.22.1                      \
    -DgroupId=poc.mdc                              \
    -DartifactId=test

To build this project use

    mvn install

To run this project with Maven use

    # to see the tests passing
    export CLEAR_MDC=true;mvn test
    
    # to see the tests failing
    export CLEAR_MDC=false;mvn test

For more help see the Apache Camel documentation

    http://camel.apache.org/

Notes
=====

A new MDC is used as soon as a new exchange unit of work is instantiated

This is happening when using
  - wiretap
  - split with parallel processing 
