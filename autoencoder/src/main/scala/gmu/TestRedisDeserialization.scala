package gmu

import redis.RedisClient

class TestRedisDeserialization extends App with RedisSerializer {

  val client = RedisClient(db = Option(25))

  client.
}
