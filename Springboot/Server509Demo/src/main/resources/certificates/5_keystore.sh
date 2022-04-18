# This step is optional. 
# KEYSTORE = multiple keys, each has a unique "alias" or format (ex. storetype=PKCS12, keystore=JKS)
# PKCS12 = (Public Key Cryptographic Standards) is a password protected format that can contain multiple certs and keys and is the industry standard. 
# JKS: Java KeyStore is similar to PKCS12; it's a proprietary format and is limited to the Java environment.
# You can convert JKS to PKCS12 with keytool (shipped with JRE)
# (We'll have to provide the source keystore password and also set a new keystore password. 
# The alias and keystore password will be needed later.)
# From https://www.geeksforgeeks.org/difference-between-truststore-and-keystore-in-java/
# TRUSTSTORE = CA certs, public info, the CA that you trust
# KEYSTORE = private info, your application cert (private)
# use keytool to create a keystore.jks repository and import the localhost.p12 file with a single command:
# Both destination and source keystore password = changeit (same as before to remember but this is the password to access the keystore, different purpose,
# it'll be included in application.properties later)
# keytool -importkeystore -srckeystore localhost.p12 -srcstoretype PKCS12 -destkeystore keystore.jks -deststoretype JKS
keytool -importkeystore -srckeystore localhost.p12 -srcstoretype PKCS12 -destkeystore keystore.jks -deststoretype pkcs12
