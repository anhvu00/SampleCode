# We'll use the PKCS 12 archive, to package our server's private key (i.e. localhost.key) 
# together with the signed certificate (i.e. localhost.crt)
# pass phrase = changeit (as in Step 1 and 2)
# You don't need "export password". It's the password for the export p12 file. 
# The receiver must provide this password when importing the file
# output = localhost.p12
# we now have the localhost.key and the localhost.crt bundled in the single localhost.p12 file
openssl pkcs12 -export -out localhost.p12 -name "localhost" -inkey localhost.key -in localhost.crt
