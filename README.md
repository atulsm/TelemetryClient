# TelemetryClient
This is a sample java project to export telemetry data to elastic search


How to use:

1) Setup an ES 2.2 cluster
2) Run the script to apply mapping es_reset_telemetry_index.sh IP
3) Configure your IP address in elasticsearch.properties file
4) Configure Kibana and export the dashboards Kibana4.export.json
5) Download your telemetry data files to a folder
6) Edit TelemetryFolderUploadES.java to use that folder and run that class.
7) Data will be flattened and exported to ES and can be visualized in kibana

TODO:
1) Support other types like metrices and messages
2) Do an IP to country Map
