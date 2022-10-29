**1) Instructions on how to run the application:**

Run "docker-compose up" command, and all dependencies and the spring app will boot automatically. 
The spring boot app will run on port 8080.

The image name for "Account Spring Boot app" is novruz21/account.
It was built and pushed to DockerHub.

Dockerfile and docker-compose.yaml files are in the root folder of the project.
There is also init_db.sql file in order to initiate the database tables of PostgreSQL.

**2) Estimate on how many transactions can handle per second**

In order to estimate that, JMeter was used to make requests to create transactions
From results, it can be estimated that 230-250 requests to proceed under 1 second are 
successful. The "Thread Group.jmx" file could be opened in JMeter software to be analyzed 
for configurations.

**3) Describe what do you have to consider to be able to scale applications horizontally**

First, you have to design you spring application to be completely stateless (It is also the requirement for REST API). 
Then, more than one instance of your app could be available in different servers.
You can use front proxy or reverse proxy to divide the requests from client and send them to different instances and thus 
reducing load on the app (load balancing). Thanks to Docker and Kubernates, these procedures are not so headache these days. 
Also there are db technologies like Cassandra which give developers a straightforward ways to solve scaling issues.

**4) Explanation of important choices in the solution**

Error handling in the app was designed in a way that RestControllerAdvice is used and catches each exception as a middleware. 
To customize that custom Runtime Exceptions are created for different error messages.
However, it could also be designed without throwing Exceptions at all. For example, 
returning error messages in metadata of the response.

In MyBatis mapper classes (repository), annotations are used. There is also dynamic sql and xml based ways to handle sql commands.

Ideally, there should be another environment for integration tests (test). However, for simplicity, there is only one environment (dev) in the app.
