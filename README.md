# Telemetry ES Uploader
This is a sample java project to export telemetry data to elastic search


How to use:

  - Setup an ES 2.2 cluster
  - Run the script to apply mapping es_reset_telemetry_index.sh IP
  - Configure your IP address in elasticsearch.properties file
  - Configure Kibana and export the dashboards Kibana4.export.json
  - Download your telemetry data files to a folder
  - Edit TelemetryFolderUploadES.java to use that folder and run that class.
  - Data will be flattened and exported to ES and can be visualized in kibana

TODO:
  - Support other types like metrices and messages
  - Do an IP to country Map
