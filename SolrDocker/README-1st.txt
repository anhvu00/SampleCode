
1. Original files:
docker-compose.yml
README.md

2. New files:
SolrDockerContainer.docx = Full document about this effort
Taxi_top50-dsname.json = sample data file, already in Gitlab, can be removed.
cleanup.sh = helper script to remove containers+images while developing
Dockerfile = build solr container, can be removed (see SolrDockerContainer.docx)
docker-compose-external-volume.yml = copy this to docker-compose before trying it out (see doc)
docker-compose-standalone.yml = copy this to docker-compose before trying it out (see doc)
venom_docs.tar = sample core/collection with Taxi data in it (see doc)
