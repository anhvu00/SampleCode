# Step 3 - sign the request with our rootCA.crt certificate and its private key
# a. create some extra parameters used in signing and save to a file (ex. localServerCSRParams.ext)
# authorityKeyIdentifier=keyid,issuer
# basicConstraints=CA:FALSE
# subjectAltName = @alt_names
# [alt_names]
# DNS.1 = localhost
# b. use rootCA.crt, rootCA.key, and localServerCSRParams.ext to sign the CSR request
# pass phrase for rootCA.key = changeit (same as in Step 1)
# output = localhost.crt and rootCA.srl
# The localhost.crt is the certificate signed by our own certificate authority.
# To print our certificate's details in a human-readable form we can use the following command:
# openssl x509 -in localhost.crt -text
openssl x509 -req -CA rootCA.crt -CAkey rootCA.key -in localhost.csr -out localhost.crt -days 3650 -CAcreateserial -extfile localServerCSRParams.ext
