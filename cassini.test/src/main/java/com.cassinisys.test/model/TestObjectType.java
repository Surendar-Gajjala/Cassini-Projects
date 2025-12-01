package com.cassinisys.test.model;

import java.io.Serializable;

/**
 * Created by Suresh Cassini on 02-Jul-18.
 */
public enum TestObjectType implements Serializable {
    TESTSCENARIO,
    TESTPLAN,
    TESTSUITE,
    TESTCASE,
    TESTRUN,
    TESTRUNCONFIGURATION,
    RCSUITE,
    RCRUN,
    RCCASE,
    RCPLAN,
    RCSCENARIO,
    RCRUNCONFIGURATION,
    RUNSCENARIO,
    RUNPLAN,
    RUNSUITE,
    RUNCASE,
    RUNEXECUTION,
    FILE
}
