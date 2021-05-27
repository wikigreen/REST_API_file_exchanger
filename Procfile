web: java $JAVA_OPTS -jar target/dependency/webapp-runner.jar --port $PORT target/*.war

release: java -cp target/classes;target/dependency/* com.wikigreen.flyway.Migration