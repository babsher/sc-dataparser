package gmu

import akka.actor.ActorSystem
import com.redis.RedisClient
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class TestRedisDeserialization extends App with RedisSerializer {

  implicit val system = ActorSystem("redis-test")

  val redis = new RedisClient()

//  val futurePong = redis.ping()
//  println("Ping sent!")
//  futurePong.map(pong => {
//    println(s"Redis replied with a $pong")
//  })
//  Await.result(futurePong, 5 seconds)
//
//  redis.keys("*") onSuccess {
//    case txt => print(txt.size)
//  }

  system.shutdown()
}
