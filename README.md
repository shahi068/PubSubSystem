HOW TO COMPILE AND RUN CODE ON CSE LAB MACHINES:

- Open a terminal window and navigate to the directory where the project files are located.
- Extract the tar.gz file using the following command: tar -xvzf project2.tar.gz
- Change directory to the extracted folder using the command: cd project2
- Create a bin directory for storing the compiled files: mkdir bin
- Compile the source code using the command: javac -d bin src/*.java

To run the project on the CSE lab machines:

- Start the server coordinator by running the command: java -cp bin BulletinBoardServerCoordinator. This will start the Bulletin Board Server Coordinator on port 6000.
- In a separate terminal window, start the server by running the command: java -cp bin BulletinBoardServer. This will start the Bulletin Board Server on port 5000.
- In another terminal window, start the client by running the command: java -cp bin BulletinBoardClient. This will connect to the server and allow you to perform operations on the bulletin board.

How to use the Bulletin Board:

- When the client is running, enter the operation you want to perform (POST, READ, REPLY, or QUIT) and follow the prompts to enter the necessary information.
- For the POST operation, enter the title and content of the article you want to post. The server will assign an ID to the article and store it in memory.
- For the READ operation, the server will retrieve all the articles that have been posted and display them on the screen.
- For the REPLY operation, enter the ID of the article you want to reply to, as well as the title and content of your reply. The server will assign an ID to the reply and store it as a child of the parent article.
- To quit the client, enter the QUIT operation.


DESIGN DOCUMENT

This project is a Bulletin Board System consisting of four components: BulletinBoardClient, BulletinBoardServer, BulletinBoardServerCoordinator, and Article.

The Article class is responsible for representing a bulletin board article, storing its ID, title, content, and replies. It also provides methods to add a reply to an article and to print an article and its replies with indentation.

The BulletinBoardServerCoordinator acts as a coordinator for the servers, responsible for registering and coordinating them. It listens for incoming connections from servers, receives requests to register, and assigns IDs to new articles. It also keeps a list of backup servers for each primary server.

The BulletinBoardServer is the main server component of the system. It listens for incoming connections from clients, receives requests to post, read, and reply to articles, and responds accordingly. It maintains a list of articles and their replies, assigns IDs to new articles, and propagates updates to backup servers.

The BulletinBoardClient is the user interface for the system. It prompts the user for an operation to perform (post, read, reply, or quit) and the necessary information for each operation (title and content for posting an article). It then sends the request to the server and receives the response.

Performance Graphs and Analysis

As this project involves network communication, performance is heavily dependent on network latency and bandwidth. The performance of the system can be evaluated by measuring the time it takes to perform each operation. As the number of concurrent clients increases, the average time taken for each operation also increases. This is expected, as more clients means more network traffic and more processing required by the servers. However, the increase in time is relatively small, even with 25+ concurrent clients. Overall, the system performs well in a local network environment, even with a moderate number of concurrent clients. However, it is predicted that the performance may degrade significantly in a wide-area network or under heavy load. In these cases, the system may require optimizations such as load balancing or caching to improve performance.


TEST CASES:

Test Case: Register Server
Description: Verify server registration with the coordinator.
Steps: Start the coordinator and a Bulletin Board server. Check server isn't registered, then register it. Verify registration.
Expected Result: Server successfully registers with the coordinator.

Test Case: Post Article
Description: Verify posting an article.
Steps: Start servers, register, connect using client, Post and verify article, Read and verify article listing.
Expected Result: Article posted and listed successfully.

Test Case: Read Articles
Description: Verify retrieving article list.
Steps: Start servers, register, connect using client, Read and verify article list.
Expected Result: Article list retrieved successfully.

Test Case: Reply to Article
Description: Verify adding a reply.
Steps: Start servers, register, connect using client, Read and note an article ID, Reply and verify, Read and verify reply listing.
Expected Result: Reply added and listed successfully.

Test Case: Multiple Servers
Description: Verify updates across multiple servers.
Steps: Start coordinator, two Bulletin Board servers, register both, connect, Post and verify article, Read and verify on both servers.
Expected Result: Updates propagate to all registered servers.

Test Case: Reply to a Reply
Description: Verify replying to a reply.
Steps: Start servers and client, Post article, create and verify nested replies.
Expected Result: Nested replies added and listed successfully.

Potential Deadlocks/Race Conditions:
Race condition with multiple clients POSTing/REPLYing simultaneously may result in duplicate article IDs.


GROUP WORKLOAD DOCUMENTATION:

Adam Shahin:

- Implemented the Article class, including the fields and methods for creating and manipulating articles and their replies, and also aided in creating the BulletinBoardServerCoordinator and BulletinBoardClient class.
- Collaborated with the rest of the group on the design of the server-client architecture and the communication protocols.
- Contributed to the overall design document, including performance and analysis, and also worked on the test cases and the process of compiling the code with other group members.

Rayhaan Khan:

- Implemented the BulletinBoardServerCoordinator class, which coordinates the communication between the primary and backup servers, and also worked on the BulletinBoardServer class.
- Collaborated with the rest of the group on the design of the server-client architecture and the communication protocols.
- Contributed to the README.md file, including writing instructions on how to run and compile the code and test cases.

Farhan Khan:

- Implemented the BulletinBoardClient class, which allows users to interact with the bulletin board server by posting articles, reading articles, and replying to articles, and also helped with the remaining classes.
- Collaborated with the rest of the group on the design of the server-client architecture and the communication protocols.
- Contributed to the README.md file, including writing instructions on how to run and compile the code and test cases.