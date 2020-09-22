export SPRING_DATASOURCE_URL=$(cat /secrets/oracle/config/jdbc_url)
export SPRING_DATASOURCE_USERNAME=$(cat /secrets/oracle/tp-registeret/username)
export SPRING_DATASOURCE_PASSWORD=$(cat /secrets/oracle/tp-registeret/password)
