IP=$1

echo "Deleting telemetry index on $IP <br>"
echo 
curl -XDELETE http://$IP:9200/telemetry

echo 
echo "<br>Apply mapping template on telemetry index on $IP <br>"
echo 
curl -XPUT http://$IP:9200/_template/template_1 -d '
{
      "template":"telemetry*",
      "settings":{
         "index.number_of_shards":3,
	     "index.number_of_replicas": 0
      },
      "mappings":{
	  "events" : {
        "properties" : {
          "InstallId" : {
            "type" : "string","index":"not_analyzed"
          },
          "ProductId" : {
            "type" : "string","index":"not_analyzed"
          },
          "SesStart" : {
            "type" : "date",
            "format" : "strict_date_optional_time||epoch_millis"
          },
          "Sid" : {
            "type" : "string","index":"not_analyzed"
          },
          "Sidn" : {
            "type" : "string","index":"not_analyzed"
          },
          "evt_C" : {
             "type" : "integer"
          },
          "evt_N" : {
            "type" : "string","index":"not_analyzed"
          },
          "evt_T" : {
            "type" : "date",
            "format" : "strict_date_optional_time||epoch_millis"
          },
          "prop_binding" : {
            "type" : "string","index":"not_analyzed"
          },
          "prop_country" : {
            "type" : "string","index":"not_analyzed"
          },
          "prop_language" : {
            "type" : "string","index":"not_analyzed"
          },
          "prop_machine" : {
            "type" : "string","index":"not_analyzed"
          },
          "prop_os" : {
            "type" : "string","index":"not_analyzed"
          },
          "prop_geo" : {
            "type" : "geo_point"
          },         
          "prop_osver" : {
            "type" : "string","index":"not_analyzed"
          }
        }
      }
	  
	  }
}

'

echo 
echo "<br>Creating telemetry index on $IP <br>"
echo 
curl -XPUT http://$IP:9200/telemetry
echo 
