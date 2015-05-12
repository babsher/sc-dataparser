package gmu

import com.mongodb.MongoClient

object TestMain extends App {
  val mongo = new MongoClient()
  val p = new MongoPersistence(mongo, "sc")
//  p.findReplays().foreach(println)
  p.findMaps().foreach(println)
}
