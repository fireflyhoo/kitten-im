# kitten-im

### Service invoking architecture

![service invoking architectur](./docs/images/Service-Architecture.png)


### Message Sender Flow

![send message to user flow](./docs/images/send_message_to_user_flow.jpg)

### Messge Ack flow

![ack message flow](./docs/images/act-msg-flow.png)

### Ping flow
![ping flow](./docs/images/ping-pong.jpg)


### connert rpc

 1. grpc
 
 
 ### deploy resource
 Index	Size	quantity	Service
 1	4Core  8G	6	connector-gateway,business-gateway(1/2)
 2	4Core  16G	3	store-service(tikv)
 3	4Core  8G	3	session-service(redis)
 4	4Core  8G	3	register-service(Raft in memory)
