package gmu

import com.mongodb.MongoClient

object TestMain extends App {
  val mongo = new MongoClient()
  val p = new MongoPersistence(mongo, "sc")
  p.findReplays().map(_.replay).sorted.foreach(println)
  println()
  p.findMaps().map(_.replayMap.mapName).foreach(println)
}
