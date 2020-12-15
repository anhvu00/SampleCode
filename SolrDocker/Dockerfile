# This Dockerfile creates a container, copy our core (including schema.xml and sample Taxi data)
# It still needs help to run (e.g. create core, map port 8983)
# Build command: "docker build -t sme-solr:1.0 ."
# Sample run command: "docker run -p 8983:8983 sme-solr:1.0"
# Warning: "docker run -p 8983:8983 sme-solr:1.0 solr-precreate venom_docs" will overwrite venom_docs/ folder

# Base solr image
FROM solr:8.6

# Default dir when you login (optional)
WORKDIR /var/solr/data

# TODO: schema and schemaless configuration...
#RUN rm -rf /var/solr/data/conf/managed-schema

# Copy our test core with data to the container
# TODO: sudo root to delete the tar OR copy venom_docs/ folder instead
COPY ./venom_docs.tar /var/solr/data
RUN cd /var/solr/data && tar -xvf ./venom_docs.tar && rm ./venom_docs.tar 

