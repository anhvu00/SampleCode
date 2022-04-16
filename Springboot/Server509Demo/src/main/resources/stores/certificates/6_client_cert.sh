# ref 1 = https://wstutorial.com/rest/spring-boot-client-certificate.html


# create Bob private key, get a sign request csr
openssl req -new -newkey rsa:4096 -nodes -keyout clientBob.key -out clientBob.csr

# sign it with our rootCA.key, get clientBob.crt
openssl x509 -req -CA rootCA.crt -CAkey rootCA.key -in clientBob.csr -out clientBob.crt -days 365 -CAcreateserial

# package Bob private key + signed certificate to pkcs12
# ? how to use this file in our program? curl with this doesn't work: curl (58) could not load PEM client certificate
# perhaps curl only works with PEM (DER/PEM/ENG) which concatenates client key, certs, and intermediate CAs
openssl pkcs12 -export -out clientBob.p12 -name "clientBob" -inkey clientBob.key -in clientBob.crt

# this works:
curl "https://localhost:8443/hello" --cert clientBob.crt --key clientBob.key --cacert rootCA.crt

# this also works: (question is why does it still need rootCA.crt?)
curl "https://localhost:8443/hello" --cert-type P12 --cert clientBob.p12 --cacert rootCA.crt