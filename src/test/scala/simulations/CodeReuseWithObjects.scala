package simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

class CodeReuseWithObjects extends Simulation {

  val httpConf = http.baseUrl("http://localhost:8080/app/")
    .header("Accept", "application/json")

  //val scn = scenario("Video Game DB - 3 calls")

    //.exec(http("Get all video games - 1st call")
      //.get("videogames"))
    //.pause(5)

    //.exec(http("Get specific game")
      //.get("videogames/1"))
    //.pause(1, 20)

    //.exec(http("Get all Video games - 2nd call")
      //.get("videogames"))

  def getAllVideoGames(): ChainBuilder = {
    repeat(3) {
      exec(http("Get all video games - 1st call")
        .get("videogames")
        .check(status.is(200)))
    }
  }

  def getSpecificVideoGame(): ChainBuilder = {
    repeat(5) {
      exec(http("Get specific game")
        .get("videogames/1")
        .check(status.in(200 to 210)))
    }
  }

  val scn = scenario("Code reuse")
    .exec(getAllVideoGames())
    .pause(5)
    .exec(getSpecificVideoGame())
    .pause(5)
    .exec(getAllVideoGames())

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpConf)


}
