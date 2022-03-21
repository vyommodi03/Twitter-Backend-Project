
# Twitter-Backend-Project
___
It provides API which gives recent interesting tweets from Twitter.
And also provides API that gives conversation for any interesting tweets.

# API Instructions
___

## Interesting Tweets API
### 1. Get interesting tweets

```GET:  http://localhost:8080/api/v1/interesting_tweet```

| Parameter | Type        | Description                      |
|:----------|:------------|:---------------------------------|
| `after`   | `timestamp` | **Optional**. Default value: all |
| `before`  | `timestamp` | **Optional**. Default value: all |
| `size`    | `int`       | **Optional**. Default value: 10  |
output:
```
[
    {
        "id": "1500540303329366016",
        "text": "These people are courageous.  Facing down an out-of-control want to be dictator to the whole world. Like Daniel be… https://t.co/IAOy1MoftW",
        "created_at": "Sun Mar 06 18:34:21 +0000 2022",
        "timestamp_ms": 1646591661661,
        "author": {
            "id": "272477805",
            "name": "AU",
            "screen_name": "1AJU13"
        }
    },...
]
```
#

### 2. Get interesting tweet from id (for check is it present in interesting tweets or not)

```GET http://localhost:8080/api/v1/interesting_tweet/{tweet_id}```

example:

```GET http://localhost:8080/api/v1/interesting_tweet/1500540303329366016 ```

output:
```
{
    "id": "1500540303329366016",
    "text": "These people are courageous.  Facing down an out-of-control want to be dictator to the whole world.  Like Daniel be… https://t.co/IAOy1MoftW",
    "created_at": "Sun Mar 06 18:34:21 +0000 2022",
    "timestamp_ms": 1646591661661,
    "author": {
        "id": "272477805",
        "name": "AU",
        "screen_name": "1AJU13"
    }
}
```
#
### 3. Get interesting tweets for particular topic

```GET http://localhost:8080/api/v1/interesting_tweet/specific```

| Parameter | Type     | Description                        |
|:----------|:---------|:-----------------------------------|
| `keyword` | `string` | **Required** Topic name for result |
| `size`    | `int`    | **Optional**. Default value: 10    |

example:

```GET http://localhost:8080/api/v1/interesting_tweet/specific?keyword=ipl```

output:
```
[
    {
        "id": "1500420694382833671",
        "text": "IPL 2022 complete schedule.✨🏆 https://t.co/roXmdmUz9M",
        "created_at": "Sun Mar 06 10:39:04 +0000 2022",
        "timestamp_ms": 1646563144666,
        "author": {
            "id": "1444343461499572233",
            "name": "ShaYan Vfc",
            "screen_name": "ShaYanVK18"
        }
    },...
]
```
#
### 4. Get interesting tweets of some user

```GET http://localhost:8080/api/v1/interesting_tweet/author```

| Parameter | Type     | Description                     |
|:----------|:---------|:--------------------------------|
| `name`    | `string` | **Required** Author name        |
| `size`    | `int`    | **Optional**. Default value: 10 |

example:

```GET http://localhost:8080/api/v1/interesting_tweet/author?name=cricket```

output:
```
[
    {
        "id": "1500424565717344261",
        "text": "@daniel86cricket it must be saddest day for you.. your cricket team is literally fucked by indian cricket team... Y… https://t.co/r988StiGIP",
        "created_at": "Sun Mar 06 10:54:27 +0000 2022",
        "timestamp_ms": 1646564067664,
        "author": {
            "id": "957718471013408768",
            "name": "Cricket Sarcasm",
            "screen_name": "cricket_sarcas"
        }
    }
]
```
#

### 5. Get top hashtags from last 1000 tweets.

```GET http://localhost:8080/api/v1/interesting_tweet/hashtags```

example:

```GET http://localhost:8080/api/v1/interesting_tweet/hashtags```

output:
```
{
  "ukraine": 35,
  "russia": 17,
  "ukraine️": 13,
  "ukrainerussianwar": 11,
  "stopputinnow": 6,
  "putin": 5,
  "bsg": 5,
  "stopwar": 4,
  "directlnc": 4,
  "standwithukraine": 4,
  "russianukrainianwar": 4,
  "lncmonde": 4,
  "standwithukraine️": 4,
  "mytwitteranniversary": 3,
  "stoprussia": 3,
  "rt": 3,
  "putinwarcrimes": 3,
  "ukraineunderattaсk": 3,
  "news": 3,
  "war": 3,
  "china": 3,
  "ukriane": 3,
  "ukrainerussiawar": 3,
  "nato": 3,
  "usa": 3
}
```
#

## Tweet conversation API
### 1. Get tweet conversation for specific interesting tweet

```GET http://localhost:8080/api/v1/tweet_conversation/{Interesting_tweet_id}```

example:

```GET http://localhost:8080/api/v1/tweet_conversation/1500561874626420736```

output:
```
{
    "interestingTweet": {
        "id": "1500561874626420736",
        "text": "But why cant these boundaries be break in IPL ? #ipl #psl #bcci #icc #pcb @IPL https://t.co/mGNOYftG9M",
        "created_at": "Sun Mar 06 20:00:04 +0000 2022",
        "timestamp_ms": 1646596804659,
        "author": {
            "id": "52174941",
            "name": "Nabeel Qureshi",
            "screen_name": "nabeelqureshi"
        }
    },
    "tweetReplyList": [
        {
            "id": "1500760296268484614",
            "text": "@nabeelqureshi @IPL https://t.co/bWkFQMZc8M",
            "parentId": "1500561874626420736",
            "created_at": "Mon Mar 07 09:08:32 +0000 2022",
            "timestamp_ms": 1646644112065,
            "author": {
                "id": "1500755590511046660",
                "name": "aroosa Rauf",
                "screen_name": "aroosaRauf1"
            }
        },...
    ]
}
```
#

### 2. Get tweet conversation for random interesting tweet

```GET http://localhost:8080/api/v1/tweet_conversation/```

example:

```GET http://localhost:8080/api/v1/tweet_conversation/```

output:
```
{
    "interestingTweet": {
        "id": "1500561874626420736",
        "text": "But why cant these boundaries be break in IPL ? #ipl #psl #bcci #icc #pcb @IPL https://t.co/mGNOYftG9M",
        "created_at": "Sun Mar 06 20:00:04 +0000 2022",
        "timestamp_ms": 1646596804659,
        "author": {
            "id": "52174941",
            "name": "Nabeel Qureshi",
            "screen_name": "nabeelqureshi"
        }
    },
    "tweetReplyList": [
        {
            "id": "1500760296268484614",
            "text": "@nabeelqureshi @IPL https://t.co/bWkFQMZc8M",
            "parentId": "1500561874626420736",
            "created_at": "Mon Mar 07 09:08:32 +0000 2022",
            "timestamp_ms": 1646644112065,
            "author": {
                "id": "1500755590511046660",
                "name": "aroosa Rauf",
                "screen_name": "aroosaRauf1"
            }
        },...
    ]
}
```
#

# Installation
___
### Database setup
#### Kafka setup
* Run Kafka server commands
    ```
    bin/zookeeper-server-start.sh config/zookeeper.properties
    bin/kafka-server-start.sh config/server.properties
    ```
* Create kafka topic
    ```
    bin/kafka-topics.sh --create --bootstrap-server {KAFKA_SERVER_URL,ex: localhost:9092} --replication-factor {NO_OF_REPLICA} --partitions {NO_OF_PARTITIONS} --topic {TOPIC_NAME}
    ```
#### Redis Server setup
* Run redis server
    ```
    redis-server
    ```
#### MongoDb Server setup
* Run mongodb server
    ```
    brew services start mongodb-community@5.0
    ```
* Store regex in Mongodb in this format (Database name: "Backend-Project" , Collection name: "tweets-regex")
    ```
    {
        "_id":ObjectId("621de0e926e090a63e8cb140"),
        "regex" : "(.*)dhoni(.*)"
    }
    ```
#### Run Elasticsearch setup
* Run elastic search server
    ```
    bin/elasticsearch
    ```
* Make schema for interesting_tweets index in elasticsearch
    ```
    PUT interesting_tweets
    {
      "mappings": {
        "properties": {
          "id":{
            "type": "keyword"
          },
          "text":{
            "type": "text"
          },
          "parentId":{
            "type": "keyword"
          },
          "created_at":{
            "type": "keyword"
          },
          "timestamp_ms":{
            "type": "date",
            "format": "epoch_millis"
          },
          "author":{
            "properties": {
              "id":{
                "type":"keyword"
              },
              "name":{
                "type":"text"
              },
              "screen_name":{
                "type":"text"
              } 
            } 
          }
        }
      }  
    }
    ```
* Make schema for tweet_conversation indices
    ```
    PUT tweet_conversation
    {
      "mappings": {
        "properties": {
          "id":{
            "type": "keyword"
          },
          "text":{
            "type": "text"
          },
          "parentId":{
            "type": "keyword"
          },
          "created_at":{
            "type": "keyword"
          },
          "timestamp_ms":{
            "type": "date",
            "format": "epoch_millis"
          },
          "author":{
            "properties": {
              "id":{
                "type":"keyword"
              },
              "name":{
                "type":"text"
              },
              "screen_name":{
                "type":"text"
              } 
            } 
          }
        }
      }  
    }
    ```
### Configuration
#### elasticsearch.properties
```
INTERESTING_TWEET_INDEX = interesting_tweets //name of interesting tweets index
TWEET_CONVERSATION_INDEX = tweet_conversation // name of tweet conversation index
```

#### kafka.properties
```
BOOTSTRAPSERVERS  = 127.0.0.1:9092 // Bootstrap server url
TOPIC = kafka-testing // kafka topic name
```

#### mongodb.properties
```
MONGO_URL = mongodb://localhost:27017 // Mongodb URL
DATABASE_NAME = Backend-Project // Database name
COLLECTION_NAME = tweets-regex // Collection name
```

#### redis.properties
```
INTERESTING_TWEET_KEY = interesting_tweet // Hashset key for interesting tweets
NON_INTERESTING_TWEET_KEY = non_interesting_tweet // Hashset key for non_interesting tweets
```
#### twitter.properties
```
CONSUMER_KEYS = // Twitter API key 
CONSUMER_SECRETS = // Twitter API key secret 
TOKEN = // Access Token 
TOKEN_SECRET = // Access token secret
BEARER_TOKEN = // Bearer token
```

# Run command
Run this application from terminal
```
./gradlew bootrun 
```
