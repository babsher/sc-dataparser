package gmu

object TestMain extends App {
  val p = new Persister("sc")
  p.getReplays().map(println)
}
