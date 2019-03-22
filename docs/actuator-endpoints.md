#  Prometheus Metrics access via Grafana

## Capability (Metrics Monitoring : Prometheus and Grafana)
- Prometheus and Grafana can be used to monitor a vast range of applications. For collecting metrics, we shall be targeting the REST application. This application has exposed a metrics endpoint at "http://localhost:8080/actuator/prometheus" using Spring Boot Actuator.

## Endpoints

- Prometheus metrics can be accessed at: http://localhost:8080/actuator/prometheus

- Prometheus server can be accessed at: http://localhost:9090/graph

- Grafana can be accessed at: http://localhost:3000/?orgId=1. Default username and password are "admin/admin" 


## Add/View Prometheus as a Data Source in Grafana

- Log into Grafana with the username and password configured (the default is admin/admin)

- Click the gear icon in the left sidebar and from the menu select "Data Sources." 

- Click on "Add Data Source." This will open a page to add a data source. Prometheus should have been defined as a datasource already and looks like docs/images/Grafana-Datasources.jpg

- Give a suitable name to this new data source, as this will be used while creating visualizations. Please see docs/images/Grafana-Datasource-Details.jpg
	a. Select Prometheus in the "type" drop down.
	b. The URL shall be "http://localhost:9090" as we have Prometheus running on local host on port 9090.
	c. Fill other details if you have any security or HTTP related settings.
	d. Click "Save & Test."
	e. If Grafana is able to make connections to Prometheus instance with the details provided, then you will get a message saying "Data source is working." If you get any errors, review your values.
	
## Dashboard

- We can access the home page for the dashboard as shown in: docs/images/Grafana-Dashboard.jpeg. JVM Micrometer and Hystrix dashboards are available by default

- A new dashboard can be created based on the requirement using below steps:
	a. Click on "Create your first dashboard" tab. Then click on one of the options like Graph or Singlestat or Table etc as shown in 	   docs/images/Grafana-NewDashBoard.jpeg 
	b. Select a particular option as above to look at the required stats.