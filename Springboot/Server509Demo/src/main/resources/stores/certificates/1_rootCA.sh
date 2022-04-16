# From https://www.baeldung.com/x-509-authentication-in-spring-security
# Step 1 - create our own self-signed root CA certificate first. This way we'll act as our own certificate authority.
# pass phrase = changeit
# common name = KYRON.COM
# output = rootCA.key and rootCA.crt
openssl req -x509 -sha256 -days 3650 -newkey rsa:4096 -keyout rootCA.key -out rootCA.crt
