package org.pitest.mutationtest.config;

import org.pitest.testapi.TestUnit;
import org.pitest.testapi.TestUnitFinder;

import java.util.Collections;
import java.util.List;

class PrioritisingTestUnitFinder implements TestUnitFinder {
    private final List<TestUnitFinder> orderedChildren;

    PrioritisingTestUnitFinder(List<TestUnitFinder> orderedChildren) {
        this.orderedChildren = orderedChildren;
    }

    @Override
    public List<TestUnit> findTestUnits(Class<?> clazz) {
       for (TestUnitFinder each : orderedChildren) {
           List<TestUnit> found = each.findTestUnits(clazz);
           if (!found.isEmpty()) {
               return found;
           }
       }
       return Collections.emptyList();
    }
}
