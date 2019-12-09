Goal:
- Demo copy dependencies to target/libs folder and executable jar file

The pom uses a “manual” way to copy dependencies. There are other helper plugins to achieve the same goal.

Note:
If you start a fresh Maven project in Eclipse, you need to pay attention to the following:
- Project properties: build with JDK not JRE, set compatible running mode (ex. java 1.8)

If you clone this project, just do a maven install.

The most important part of this exercise is the omission of &ltpluginManagement&gt!! It won’t copy anything if you encapsulate the plugins in &ltpluginManagement&gt.

See comments in the pom for details.


