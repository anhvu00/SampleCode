5/12/20
PURPOSE:
- CRUD operations using GeoServer REST API
- On GitHub to modify/test code between different VMs easier
NEEDED:
- GeoServer 2.16.2 on localhost
- Importer Extension (plugin) for GeoServer
- curl for command line testing
- Require Java 8
OVERVIEW:
- Emphasize on import GeoTiff (raster) image
- CRUD operations on layer with XML (JSON not recommended)
ISSUES:
- Cannot upload images from outside of localhost. Images must be on same host as GeoServer.
- Parameter "async=true&execute=true" does not work as instructed by GeoServer doc.