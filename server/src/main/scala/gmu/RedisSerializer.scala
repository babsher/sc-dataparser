package gmu

import akka.util.ByteString

trait RedisSerializer {

//  implicit val unitFrameSerializer = new ByteStringFormatter[ReplayUnit] with ReplayPickles {
//
//    override def deserialize(bs: ByteString): ReplayUnit = {
//      bs.toArray.unpickle[ReplayUnit]
//    }
//
//    override def serialize(data: ReplayUnit): ByteString = {
//      ByteString(data.pickle.value)
//    }
//  }
//
//  implicit val replayFrameSerializer = new ByteStringFormatter[ReplayPlayers] with ReplayPickles {
//
//    override def deserialize(bs: ByteString): ReplayPlayers = {
//      bs.toArray.unpickle[ReplayPlayers]
//    }
//
//    override def serialize(data: ReplayPlayers): ByteString = {
//      ByteString(data.pickle.value)
//    }
//  }
}