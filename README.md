[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.pitest/pitest/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/org.pitest/pitest)
![Build Statis](https://github.com/hcoles/pitest/workflows/CI/badge.svg?branch=master)
[![Build Status](https://dev.azure.com/henrycoles/pitest/_apis/build/status/hcoles.pitest?branchName=master)](https://dev.azure.com/henrycoles/pitest/_build/latest?definitionId=3&branchName=master)

Pitest (aka PIT) is a state of the art mutation testing system for Java and the JVM.

Read all about it at http://pitest.org

## Releases

### 1.8.1-SNAPSHOT

* #637  - Support different input and output encodings (thanks @qxo)
* #705  - Allow + in file paths (thanks @ali-ghanbari)
* #903  - Filter mutants in singleton constructors
* #1025 - Rework String Switch filtering
* #1027 - Rework assert filtering and remove legacy filter mechanism
* #1030 - Filter enum switch junk mutations

### 1.8.0

* #1017 - Improve static infinite loop detection
* #1020 - Rework NFA matching library and try with resource filtering
* #1022 - Support quarkus test

### 1.7.6

* #1008 Upgrade ASM for initial Java 19 support
* #1016 Fail cleanly when no working test plugin supplied
* #1015 Move TestNG support out of main codebase

As a result of #1015 the [TestNG plugin](https://github.com/pitest/pitest-testng-plugin) must now be configured when using pitest in codebases tested by TestNG. In earlier releases support was included automatically.

### 1.7.5

* #625 / #983 Fixed report aggregation (thanks @therealryan and @aurelien-baudet)
* #985 Handle empty surefire excludes
* #993 Move rv mutators to external plugin
* #994 / #995 Filter equivalent boxed return true mutants in try blocks
* #968 / 997 Improve filtering of equivalent empty return mutants
* #999 / 1003 New parameter to locate root of multi module projects

As a result of #993, anyone wishing to use the research orientated RV operators must configure the [pitest-rv plugin](https://github.com/pitest/pitest-rv-plugin), however these mutators are not recommended for general use. 

### 1.7.4

* #965 - BigDecimal Mutator (thanks @MarcinNowak-codes)
* #750 - Support -DskipTests from command line (thanks @marwin1991)

### 1.7.3

* #952 Mutate map return to `emptyMap` instead of null
* #954 Allow mutators to be excluded
* #957 Filter equivalent mutations to Boolean.TRUE and Boolean.FALSE

### 1.7.2 

* #943 Change default mutators - replace negate conditional with remove conditional
* #946 Mutate stream returns to empty stream instead of null

### 1.7.1

* #932 Improve switch mutation descriptions
* #934 Configure console output verbosity
* #940 Hide internal features
* #942 Fix for 3rd party mutation operators not added to client classpath
* #939 Improve caching of bytecode retrieval

### 1.7.0

* #923 Internal interface changes
* #930 Pluggable mutators

Due to internal changes some third party plugins maybe incompatible with this release.

All history files should be deleted before upgrading.

The names of the remove conditionals mutators have changed slightly as a result of #930 and
may need to be updated in build scripts if explicitly activated.

### 1.6.9

* #922 Filter equivalent stream.empty mutants in flatMap calls
* #921 Guarantee order of mutation operators
* #919 Filter junk mutations in java records

### 1.6.8

* #917 - Add method to retrieve all mutator ids for pitclipse and other tooling
* #913 - Bump ASM to 9.2

### 1.6.7

* #897 Fix description when replacing value with empty set
* #900 Support multiple test engines
* #822 Fix regression of NPE when reading classpath manifest
* #798 Fix regression of jvmArgs parameter support via maven
* #797 Fix regression of line coverage on console

As a result of #900 the `testPlugin` parameter is now defunct, but pitest will continue to accept it without error for this release. Pitest will use any test-engines supplied on the classpath, falling back to the built in JUnit4 support when other test engines cannot handle the class.

### 1.6.6

* #891 - Fix history performance
* #889 - Do not scan classpath for history when option not supplied
* #887 - Fix css for timeouts and memory errors
* #888 - Remove defunct max mutants per class perameter
* #890 - Remove defunct mutate static initalizers parameter

### 1.6.5

* #882 Avoid running tests when no mutants possible

### 1.6.4

* #862 Update ASM for Java 16

### 1.6.3

* #853 Fix case insensitive feature check
* #855 Make coverage data available to interceptors
* #857 Pass stderr/out data by line (thanks @kgeilmann)
* #858 Fix test not failing when maxSurviving is 0 (thanks @alexkoltz)
* #860 Fix junk mutations in try with resources in java 11+

### 1.6.2

* #770 and #746 Fix NPE during coverage stage (thanks @LaurentTho3)
* #849 Make feature names case insensitive
* #844 Extend feature system to work with listeners
* #842 Make report options available to listeners

### 1.6.1

* Automate release to maven central
* #774 Test strength statistic (thanks @alex859)
* #798 Enable jvm args to be passed from command line (thanks @yfrolov)
* #797 Add line coverage to console (thanks @qxo)
* #822 Mitigate NPE on accidental dependency (thanks @szpak)

### 1.5.2

* #749 ANT support for the fullMutationMatrix option (thanks @ayaankazerouni)
* #752, #755, #757 Version bumps and legacy code improvements (thanks @AlexElin)
* #758 Correctly can for test packages (thanks @nicerloop)
* #765 Fix incremental analysis for TestNG (thanks @StefanPenndorf)     

### 1.5.1

* #737 Add skipFailingTests flag to command line tool (thanks @szpak)
* #739 Fix typo in error message (thanks @vmellgre)
* #745 Improve error message when junit not on classpath (thanks @szpak)
* #748 Bump asm to 8.0 for Java 14 support

### 1.5.0

* #556 - Do not mutate enum constructors
* #726 - Ensure static initializer coverage is recorded 
* #734/#735/#736 - Replace legacy interfaces with Java API (thanks @AlexElin)

<details>
    <summary>Older versions</summary>

### 1.4.11

* #683 - Filter try-with-resources before filtering inlined code (thanks @Vampire)
* #686 - Do not print the class name twice for unsplittable test units (thanks @Vampire)
* #672 - Do not include the current directory to the minion class path (thanks @Vampire)
* #692 - Add property='skipPitest' to skip attribute inn maven plugin (thanks @cjgwhite)
* #697 - TestNG 7.0.0 compatibility (thanks @kris-scheibe)
* #666 - UOI4 reports mutated field name (thanks @LaurentTho3)
* #716 - Bump asm to 7.3.1 (required for Java 14)
* #710 - Use the new mutator set by default

### 1.4.10

* #534 Smaller blocks for more precise test targeting (thanks @jon-bell) 
* #664 Fix A0D2 map key (thanks @Vampire) 
* #656 Escape characters in init methods for html report (thanks @Vampire) 
* #404 Filter junk mutations to compiler generated Objects.requireNonNull calls

### 1.4.9

* #613 / #623 - Fix for powermock issues on (thanks @jon-bell)
* #614 - Improved error message when no test plugin (thanks @szpak)
* #620 - Support annotation processors such as Micronaut that do not set debug filename 

### 1.4.8

* #597 Fix for bug in coverage when large number of classes (thanks @jon-bell)
* #601 Avoid stealing keyboard focus on macos (thanks @maxgabut)

### 1.4.7

* #545 - Faster coverage calculation (thanks @jon-bell)

### 1.4.6

* #580 - upgrade to ASM 7.1
* #573 - Ant support for `testPlugin` and `failWhenNoMutations` (thanks @mduggan)
* #555 - Allow empty elements in maven configuration (thanks @maxgabut)

### 1.4.5

* #557 - Issues running on Java 8

### 1.4.4
 
* #518 - Experimental BigInteger mutator (thanks @ripdajacker)
* #513 - Sort mutators in html report (thanks @ThLeu)
* #553 - Classic mutators from literature (thanls @LaurentTho3)
* #528 - Added skipFailingTests option from maven plugin (thanks @nicerloop)

### 1.4.3

* #510 - Compute mutation test matrix (thanks @nrainer)
* #519 - Java 11 support

### 1.4.2

* #500 - Support for large classpaths with new `useClasspathJar` option (thanks @jqhan)

### 1.4.1

* #446 - Ignore empty directories when aggregating report (thanks @maxgabut)
* #457 - Fix base dir in multi module projects (thanks @sarahBuisson and @giggluigg)
* #471 - Basic Java 11 support
* #477 - Null byte in xml (thanks @maxgabut)
* #480 - Allow aggregation of timestamped reports (thanks @maxgabut)
* #495 - Move summary to end of console output (thanks @pedrorijo91)
* #499 - Fix for equivalent mutants in new return val mutators

### 1.4.0

* #445 - Raise minimum supported java version to 8
* Switch to ASM 6.1.1
* #448 Aggregate report based on module (thanks @sarahBuisson)
* #462 Escape test names in coverage xml (thanks @maxgabut) 

### 1.3.2

* #433 - Aggregate reports via maven (thanks @rchargel)
* #438 - Filter tests at method level (thanks @ftrautsch)
* #439 - Primitives return mutator doesn't handle bytes
* #380 - Remove reliance on xstream for improved java 9 support
* #414 - Replace xstream history store (thanks @kiftio)

Note when upgrading that history files generated by previous releases are not compatible with 1.3.2 an above.

### 1.3.1

* #434 - XStream security warning

### 1.3.0

* #196 - Raise minimum java version to 7
* #425 - Broaden for-each detection
* #428 - Add `excludedTestClasses` parameter
* #379 - Remove support for mutating static initializers
* #432 - Add export plugin functionality into pitest
* #427 - Better return values mutators
* #429 - Simplify test api

428 changes the existing behaviour of the excludedClasses filter. Previously this excluded classes from both being mutated and run as tests which may require changes for some people when upgrading.

As a result of 429 TestNG tests will no longer be automatically detected - the new `testPlugin` parameter must be explicitly set to "testng".

### 1.2.5

* #407 - SCM goal cannot be run from within module (thanks @sbuisson)
* #256 - Inline style violate Content Security Policy (thanks @kiftio)
* #403 - No css in the html report viewed from jenkins (thanks @kiftio)
* #409 - Mutate based on changes across branched (thanks @sbuisson) 
* #418 - Avoid for loop increments
* #424 - Avoid for each code
* #389 - Widen matching of implicit null checks

### 1.2.4

* #397 - Report reasons maven modules skipped
* #389 - Filter junk getClass null checks in lambdas
* #400 - Update to ASM 6.0

### 1.2.3

* Update to ASM 6 for Java 9 support
* #388 - Mark maven plugin as threadsafe
* #362 - Suppress common equivalent mutant in equals methods

### 1.2.2

* #367 - Static analysis cannot find core classes in some projects

### 1.2.1

* #347 - Avoid autogenerated code (e.g by lombok) anotated with @Generated
* #285 - Avoid compiler generated conditionals in switch on string. (thanks @Kwaq)
* #218 - New "naked receiver" / method chain mutator. (thanks @UrsMetz)
* #354/#356 - New extension point plus changes to reduce cost of random access to bytecode for static analysis
* #353 - Improved static initializer code detection
* #364 - Filter infinite loops via static analysis
* #365 - Configuration system for plugins

(Note #347 will **not** detect `javax.annotaion.Generated` as it has only source retention.) 

### 1.2.0

* #318 - Separate main process code from code sharing client classpath
* #295 - Ignore abstract TestNG tests
* #215 - Automatic selection of target classes for maven
* #314 - Do not fail for maven modules without tests or code
* #306 - Do not fail when only interfaces are in changeset
* #325 - Anonymous class in Spock test causing error
* #334 - Compatibility with recent TestNG releases
* #335 - Incorrect coverage with powermock

Note - as of this release the maven plugin will automatically determine which classes to mutate instead of assuming that the package names match the group id.

### 1.1.11

* #269 - Not possible to break build on 1 mutant
* #271 - Include method descriptor in line coverage
* #170 - Exclusion by JUnit runner
* #291 - Handle empty excludes
* #287 - Check class hierarchy for annotations
* #289 - Option to supply classpath from file


### 1.1.10

* #260 - Initial support for mutating Kotlin code
* #264 - Support for yatspec runner (thanks @theangrydev)
* Break build when more than `maxSurviving` mutants survive

### 1.1.9

* #132 - Allow analysis of only files touched in last commit (thanks Tomasz Luch)

### 1.1.8

* #239 - Provide a shortcut to set history files via maven
* #240 - Support for regexes (thanks sebi-hgdata)
* #243 - Use ephemeral ports to communicate with minions

### 1.1.7

* #196 - Raise minimum java version to 1.6
* #231 - Process hangs

### 1.1.6

* #10  - Add maven report goal (thanks jasonmfehr) 
* #184 - Remove undocumented project file feature
* #219 - Performance improvement for report generation (thanks tobiasbaum) 
* #190 - Allow custom properties for plugins

Note this release contains a known issue (#231). Please upgrade.

### 1.1.5

* Fix for #148 - Stackoverflow with TestNG data providers when using JMockit
* Fix for #56 - Not reporting junit incompatibilities
* Fix for #174 - Invalid linecoverage.xml with static initializers 
* Fix for #183 - Can't run GWTMockito tests
* Fix for #179 - Broken `includeLaunchClasspath=false` on Windows
* #173 - Read exclusions and groups from maven surefire config

### 1.1.4

* #157         - Support maven -DskipTests flag (thanks lkwg82)
* Fix for #163 - Should not include test tree in coverage threshold
* #166         - Allow classpath exclusions for maven plugin (thanks TomRK1089)
* #155         - Restore Java 5 compatibility
* Fix for #148 - Issue with JMockit + TestNG (thanks estekhin and KyleRogers)

### 1.1.3

* Fix for #158 - Tests incorrectly excluded from mutants
* Fix for #153 - SCM plugin broken for maven 2
* Fix for #152 - Does not work with IBM jdk

### 1.1.2

* Fix for #150 - line coverage under reported

### 1.1.1

* Block based coverage (fixes 79/131)
* End support for running on Java 5 (java 5 bytecode still supported)
* Skip flag for maven modules (#106)
* Stop declaring TestNG as a dependency 
* New parameter propagation mutator (thanks UrsMetz)

### 1.1.0

* Change scheme for identifying mutants (see https://github.com/hcoles/pitest/issues/125)
* Support alternate test apis via plugin system
* Report error when supplied mutator name does not match (thanks artspb)
* Report exit codes from coverage child process (thanks KyleRogers)
* Treat JUnit tests with ClassRule annotation as one unit (thanks devmop)

Please note that any stored history files or sonar results are invalidated by this release.

### 1.0.0

* Switch version numbering scheme
* Upgrade to ASM 5.0.2
* Fix for #114 - fails to run for java 8 when -parameters flag is set
* #99 Support additionalClasspathElements property in maven plugin (thanks artspb)
* #98 Do not mutate java 7 try with resources (thanks @artspb)
* #109 extended remove conditional mutator (thanks @vrthra)


### 0.33

* Move to Github
* Upgrade of ASM to support Java 8 bytecode (thanks to "iirekm") 
* Partial support for JUnit categories (thanks to "chrisr")
* New Remove Increments Mutator (thanks to Rahul Gopinath)
* Minor logging improvements (thanks to Kyle Rogers aka Stephan Penndorf)
* Fix for #92 - broken maven 2 support
* Fix for #75 - incorrectly ignored tests in classes with both @Ignore and @BeforeClass / @AfterClass

### 0.32

* restores java 7 compatibility
* new remove conditionals mutator
* support for mutating static initializers with TestNG
* properly isolate classpaths when running via Ant
* break builds on coverage threshold
* allow JVM to be specified 
* support user defined test selection strategies
* support user defined output format
* support user defined test prioritisation
* fix for issue blocking usage with [Robolectric](http://robolectric.org/)

Note, setup for Ant based projects changes in this release. See [ant setup](http://pitest.org/quickstart/ant/) for details on usage.

### 0.31

* Maven 2 compatibility restored
* Much faster line coverage calculation
* Fix for #78 - Error when PowerMockito test stores mock as member

This release also changes a number of internal implementation details, some of which may be of interest/importance to those maintaining tools that
integrate with PIT.

Mutations are now scoped internally as described in [https://groups.google.com/forum/#!topic/pitusers/E0-3QZuMYjE](https://groups.google.com/forum/#!topic/pitusers/E0-3QZuMYjE)

A new class (org.pitest.mutationtest.tooling.EntryPoint) has been introduced that removes some of the duplication that existed in the various ways of launching mutation analysis. 

### 0.30

* Support for parametrized [Spock](http://code.google.com/p/spock/) tests
* Support for [JUnitParams](http://code.google.com/p/junitparams/) tests
* Fix for #73 - JUnit parameterised tests calling mutee during setup failing during mutation phase
* Fix to #63 - ant task fails when empty options supplied
* Ability to override maven options from command line
* Ability to fail a build if it does not achieve a given mutation score
* Performance improvement when tests use @BeforeClass or @AfterClass annotations
* Slightly improved scheduling over multiple threads
* Improved maven multi project support
* Integration with source control for maven users


### 0.29

* Incremental analysis (--historyInputLocation and --historyOutputLocation)
* Inlined code detection turned on by default
* Quieter loggging by default
* Improved Java 7 support
* Upgrade of ASM from 3.3 to 4
* Fix for concurrency issues during coverage collection
* Fix for #53 - problems with snapshot junit versions
* Fix for #59 - duplicate dependencies set via maven


### 0.28

* Inlined finally block detection (--detectInlinedCode)
* New experimental switch statement mutator (contributed by Chris Rimmer)
* Do not mutate Groovy classes
* Fix for #33 - set user.dir to match surefire
* Fix for #43 - optionally suppress timestamped folders (--timestampedReports=true/false)
* Fix for #44 - concurrent modification exception when gathering coverage
* Fix for #46 - incorrect setting of flags by ant task
* Smaller memory footprint for main process
* Faster coverage gathering for large codebases
* Faster classpath scanning for large codebases
* Support for JUnit 3 suite methods
* Fixes for incorrect detection of JUnit 3 tests

**Known issue** - Fix for #33 may not resolve issue for maven 2 users.

Detection of Groovy code has not yet been tested with Groovy 2 which may generate substantially different
byte code to earlier versions.

### 0.27

* Much prettier reports
* Now avoids mutating assert statements
* Removed inScopeClasses option - use targetClasses and targetTests instead
* Fix for 100% CPU usage when child JVM crashes
* Fix for #35 #38 - experimental member variable mutator now corrects stack
* Fix for #39 - order of classpath elements now maintained when running from maven

**Upgrading users may need to modify their build due to removal of the inScopeClasses parameter**

### 0.26

* Ant support
* New experimental mutator for member variables 
* Fix for #12 #27 - no longer hangs when code under test launches non daemon threads
* Fix for #26 - now warns when no test library found on classpath
* Fix for #30 - now errors if mutated classes have no line or source debug
* Fix for #32 - now correctly handles of JUnit assumptions 

**Known issue** - The new member variable mutator may cause errors in synchronized errors. The mutator is
however disabled by default, and the generated errors are correctly handled by PIT.

### 0.25

* TestNG support (experimental)
* Fix for issue where mutations in nested classes not isolated from each other
* Fix for broken classpath isolation for projects using xstream
* Improved handling of JUnit parametrized tests
* Ability to limit mutations to specific classpath roots (--mutableCodePaths)
* Ability to add non launch classpath roots (--classPath) (experimental)
* Read configuration values from XML (experimental)
* Option to not throw error when no mutations found
* Consistent ordering of classes in HTML report
* Statistics written to console
* Classes no longer loaded during initial classpath scanning
* New syntax to easily enable all mutation operations

### 0.24

* JMockit support
* Option to output results in XML or CSV
* Fix for #11
* Improved INLINE_CONSTS mutator

### 0.23

* Fix for issue 7 - source files not located

### 0.22

* Upgrade of Xstream to 1.4.1 to enable OpenJDK 7 support
* Fix for #5 - corruption of newline character in child processes
* Ability to set child process launch arguments

### 0.21

* Significant performance improvements
* Support for powermock via both classloader (requires PowerMockIgnore annotation) and java agent
* Minor error reporting and usability improvements
* Fix for major defect around dependency analysis
* PIT dependencies no longer placed on classpath when running via maven
* Support for excluding certain classes or tests
* Support for verbose logging

### 0.20

* Limit number of mutations per class
* Upgrade xstream to 1.3.1
* Make available from maven central

### 0.19

* Built in enum methods now excluded from mutation
* Fixed bug around reporting of untested classes
* Support for excluding tests greater than a certain distance from class
* Support for excluding methods from mutation analysis
* Performance improvements
* Removed support for launching mutation reports from JUnit runner

### 0.18

* First public release
</details>

## Issues

Please consult our [issue management rules](ISSUES.md) before creating or working on issues. 

## Credits

Pitest is mainly the work of [me](https://twitter.com/0hjc) but has benefited from contributions from many others. 

Notable contributions not visible [here](https://github.com/hcoles/pitest/graphs/contributors) as they were made before this code was migrated to github include 

* Nicolas Rusconi - Ant Task
* Struan Kerr-Liddell - Improvements to html report
* Stephan Pendorf - Multiple improvments including improved mutators
 
Although PIT does not incorporate any code from the Jumble project (http://jumble.sourceforge.net/), the Jumble codebase was used as a guide when developing some aspects of PIT.

## Other stuff

The codebase is checked up on in a few places that give slower feedback than the github hooks.

[maven2 on IBM JDK check](https://hjc.ci.cloudbees.com/job/maven2_triangle_example/)




