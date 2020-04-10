## AWS-CLI Cheat Sheet

Here are some basic commands to use when verifying topics, queues, subscriptions, or messages through the aws-cli.

**List Topics:**

`aws --endpoint-url=http://localhost:4575 sns list-topics`

**List Queues:**

`aws --endpoint-url=http://localhost:4576 sqs list-queues`

**List Topic Subscriptions:**

`aws --endpoint-url=http://localhost:4575 sns list-subscriptions`

**Recieve a Message from a Queue:**

`aws --endpoint-url=http://localhost:4576 --queue-url=http://localhost:4576/queue/sub_new_queue sqs receive-message`