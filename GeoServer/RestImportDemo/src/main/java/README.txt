5/15/20
PURPOSE:
- CRUD operations using GeoServer REST API.
You can upload images from outside of localhost.
- On GitHub to modify/test code between different VMs easier
NEEDED:
- GeoServer 2.16.2 on localhost OR another VM (no Docker please)
- Importer Extension (plugin) for GeoServer
- curl for command line testing
- Require Java 8
OVERVIEW:
- Emphasize on import GeoTiff (raster) image
- CRUD operations on layer with XML (JSON not recommended)
There are 2 kinds of tests:
1. simplehttp/ folder contains the test updating layer (XML).
It requires a GeoServer in a Docker container, running on localhost:8600
and the workspace:store BlueMarble:BlueMarble. 
2. The GeoServerUtils.java uses the geoserver-manager library 
to upload a GeoTiff to a GeoServer running on another VM (10.211.5.7:8600)
ISSUE:
- Parameter "async=true&execute=true" does not work as instructed by GeoServer doc.