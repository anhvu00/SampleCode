4/25/22 Work in progress (WIP)

Source code is a mix of the Bouncy Castle example and my springboot REST/Web client/server using the BC library.
- When mvn install, my BCDemoTest will use the BC library and the test/resources/application.properties
to create client.p12, server.jks, and trustStore.jks.
After a successful build, you can run the following server client pairs:
1. SSLServerExample (server) and SSLClientExample (client)
2. SSLServerWithClientAuthExample (server) and SSLCilentWithClientAuthExample (client) - see "hello world" on console
3. SSLServerWithClientAuthIdExample (server) and SSLCilentWithClientAuthTrustExample (client) - see "hello world" on console
4. BouncyCastleDemoApplication (server) and SSLClient (client) - see "hello" on console

TODO:
- Update SSLClient to use the values in application.properties
- The key/trust stores are created by the unit test with the src/test/resources/app.properties.
If something change there, we have to change the src/main/resources/app.properties (and possibly the SSLClient) as well.
This is messy and needed to be clean up.
- Clean up unused classes/codes.
- Clean up hard coded values