package controllers

import play.api.mvc._

import scalaj.http.Http
import com.typesafe.config.ConfigFactory
import org.json4s._
import org.json4s.jackson.JsonMethods._

class WeatherController extends Controller {
  val ttcInfo = List(
    """REMINDER: Service suspended,Line 2 St George -Pape due to scheduled track upgrades.Alternate route: Shuttle buses. https://t.co/vA0SjfGxUh"""",
    """REMINDER: Service suspended,Line 2 St George -Pape due to scheduled track upgrades.Alternate route: Shuttle buses. https://t.co/YLKE8Eps94"""",
    """REMINDER: Service suspended,Line 2 St George -Pape due to scheduled track upgrades.Alternate route: Shuttle buses. https://t.co/yoqpbOW8xU"""",
    """REMINDER: Service suspended,Line 2 St George -Pape due to scheduled track upgrades.Alternate route: Shuttle buses. https://t.co/yrALGHQl5I"""",
    """ALL CLEAR: 64 Main has returned to scheduled service, however the roadway on Main at Kingston remains closed.#TTC"""",
    """REMINDER: Service suspended on Line 2 from St George to Pape due to scheduled track upgrades.Alternate route: Shuttle buses operating.#TTC""",
    """64 Main diverting both ways via Kingston, Woodbine, Queen, Neville Loop due to construction on Main at Kingston.#TTC"""",
    """REMINDER: Service suspended on Line 2 from St George to Pape due to scheduled track upgrades. Alternate route: Shuttle buses operating.#TTC""",
    """ALL CLEAR: The delay at Dupont Station has now cleared. Service has resumed on Line 1.#TTC""",
    """Trains holding northbound on Line 1 at Dupont Station due to mechanical difficulties on board a train.#TTC""",
    """The collector booth at Ossington Stn will be closed from Aug 6 to 28. During this time you won’t be able to buy #TTC fares.""",
    """No service on Line 2 from St George to Pape due to scheduled track upgrades. Alternate route: Shuttle buses operating.#TTC""",
    """395 York Mills buses diverting eastbound via Birchmount, Lawrence, Kennedy due to a collision at Ellesmere and Wye Valley. #TTC"""",
    """301 Queen service turning back at Humber Loop due to a collision at Lakeshore and Parklawn. #TTC""",
    """ALL CLEAR: The delay both ways at Morningside and Kingston has now cleared and full service on the 116/198 routes have resumed. #TTC""",
    """112 West Mall buses diverting westbound via Eringate, Wellesworth due to a collision at Gentian and Renforth. #TTC"""",
    """198 U of T Scarb diverting NB via Kingston, Amiens, Teft, SB via Theft, Amiens, Kingston due to a collision at Morningside &amp; Kingston. #TTC"""",
    """ALL CLEAR: The delay at Queen and Yonge is clear. 501 Queen has returned to scheduled routing.""",
    """ALL CLEAR: The delay at Queen and Yonge is Clear. 501 Queen has returned to scheduled routing. #TTC""",
    """ALL CLEAR: The delay westbound at Royal York Station has cleared and regular service has resumed on Line 2 (BD). #TTC""",
    """Trains are currently turning back at Jane and Islington Stns due to a medical emergency on board a train westbound at Royal York Stn. #TTC""",
    """UPDATE: 501 Queen diverting both ways via Shaw, King, Church due to a police investigation at Queen and Yonge. #TTC""",
    """Trains are holding on Line 2, westbound at Royal York Station, due to a medical emergency on board a train. #TTC""",
    """501 Queen route holding eastbound on Queen at Yonge with a police investigation on board a streetcar. #TTC""",
    """ALL CLEAR: Rogers and Sellers has re-opened. 161 Rogers has resumed regular routing. #TTC""",
    """ALL CLEAR: Evans and Islington has re-opened. 15 Evans has resumed regular routing. #TTC""",
    """15 Evans route diverting westbound via Islington, Queensway, Kipling, due to autos in collision at Evans and Islington. #TTC""",
    """ALL CLEAR: The delay at Ossington Station has now cleared and regular service has resumed on Line 2. #TTC""",
    """Trains are holding on Line 2, westbound at Ossington Station, due to a medical emergency on board a train. #TTC""",
    """161 Rogers route diverting both ways via Dufferin, St. Clair, Caledonia, due to autos in collision at Rogers and Sellers. #TTC""",
    """ALL CLEAR: Dundas and Bay has re-opened. 505 Dundas has returned to regular routing. #TTC""",
    """ALL CLEAR: The delay at Pape and Strathcona is now clear. 72 Pape has resumed regular routing. #TTC""",
    """72 Pape route diverting northbound via Gerrard, Jones, Danforth, due to a Toronto Fire Investigation at Pape and Strathcona. #TTC""",
    """72 Pape route diverting southbound via Cavell, Carlaw, due to a Toronto Fire Investigation at Pape and Strathcona. #TTC""",
    """ALL CLEAR: The delay at Milner and McCowan is now clear. 132 Milner has resumed regular routing. #TTC""",
    """132 Milner route is diverting westbound via McCowan, Pitfield, Brimley, Triton, due to a collision at Milner and McCowan. #TTC"""
  )

  //val busStats = List(List("idle_2", "running_80", "Malfunctional_10", "Mics_8"), List("idle_22", "running_50", "Malfunctional_10", "Mics_18"), List("idle_90", "running_10", "Malfunctional_0", "Mics_10"), List("idle_100", "running_0", "Malfunctional_0", "Mics_0"))

  def getLastBustData() = {
    val response = Http("https://api.salaam.io/last_vehicle_status").asString
    val s1 = org.json4s.jackson.JsonMethods.parse(response.body).values
    val s2 = s1.asInstanceOf[List[Map[String, Any]]]

    s2.map(w => w.getOrElse("vid", 0).asInstanceOf[BigInt].toString + "^" +
      w.getOrElse("dir_tag", "").asInstanceOf[String] + "^" +
      {
        if (w.getOrElse("latitude", 0.0).isInstanceOf[Double])
          w.getOrElse("latitude", 0.0).asInstanceOf[Double].toString
        else
          w.getOrElse("latitude", 0.0).asInstanceOf[BigInt].toString
      }
      + "^" +
      {
        if (w.getOrElse("longitude", 0.0).isInstanceOf[Double])
          w.getOrElse("longitude", 0.0).asInstanceOf[Double].toString
        else
          w.getOrElse("longitude", 0.0).asInstanceOf[BigInt].toString
      }
      + "^" +
      w.getOrElse("route_tag", "").asInstanceOf[String] + "^" +
      w.getOrElse("heading", 0).asInstanceOf[BigInt].toString + "^" +
      w.getOrElse("status", "").asInstanceOf[String]

    //w.getOrElse("vid", 0).asInstanceOf[BigInt].toString + "," +
    )

  }

  import java.util.Random

  val rand = new Random(System.currentTimeMillis());

  /**
   * Display Weather Information
   */
  def index: Action[AnyContent] = Action {
    Ok(views.html.index())
  }

  /**
   * This action is used to handle Ajax request
   *
   * @return
   */
  def ajaxCall(city: String) = Action { implicit request =>
    val key = ConfigFactory.load().getString("appId")
    val response = Http("http://api.openweathermap.org/data/2.5/weather").params(Map("q" -> city, "appid" -> key)).asString
    Ok(response.body)
  }

  def twitterCall() = Action { implicit request =>
    val ss = (0 to 10).map(_ =>
      ttcInfo(rand.nextInt(ttcInfo.length)))
    Ok(ss.mkString("|"))
  }

  def busCall() = Action { implicit request =>
    val data = getLastBustData()
    Ok(data.mkString("|"))
  }

  def busStatus() = Action { implicit request =>
    Ok("deprecated")
  }

}
