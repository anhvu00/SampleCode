3/18/21 - Metrics Demo
Goal:
- Learn how Springboot produce metrics for Prometheus
Description:
- Springboot web/metrics REST server
- Pom.xml includes spring-actuator and micrometer-prometheus
- Application.properties turns on/off the target metrics
Usage:
- localhost:8080/demo/hello = verify the server is running
- localhost:8080/actuator = show a list of URLs available on this server
- For example: localhost:8080/actuator/health = status is up
- http://localhost:8080/actuator/prometheus = shows a list of metrics from cpu usage, jvm stats, logback to request metrics.
TODO:
- Configure Prometheus to "scrape" the above endpoints (prometheus.yml)
Example:
scrape_configs:
- job_name: 'spring'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['HOST:PORT']
- Configure Grafana to display those metrics (i.e. new datasource)

3/29/21 
- Containerize & deploy on local kube cluster
https://www.oreilly.com/content/how-to-manage-docker-containers-in-kubernetes-with-java/
- Wire everything together (myapp, Prometheus, Grafana). How helm chart configure Prometheus scrape target here?
The real kirby work is more involved: Gitlab pipeline

