# Step 2 - Create a server side certificate
# a. creating a certificate signing request (CSR)
# pass phrase = changeit
# common name = localhost
# You don't need a "challenge password"
# output = localhost.csr and localhost.key
openssl req -new -newkey rsa:4096 -out localhost.csr -keyout localhost.key
