12/22/2014
This is a test project showing how to generate java source code from xsd using jaxb2-maven-plugin version 1.6.

1. Run as-is:
Import maven project into Eclipse, right click Test1.java | Run-As JUnit.

2. Recompile:
Delete folder src/main/java/com, then right click pom.xml | Run As | Maven clean
Note that you might have to delete Test1.java as well. This unit test is so simple that you can rewrite it later.
You'll see the source is generated again under src/main/java/com
To test, right click Test1.java | Run As | JUnit

See AccountDetails.xsd and pom.xml for detail

TO DO:
Common practice is to have all generated source in a separate project.
Then include this project as a dependency to your main project.
This is left undone for your exercise.