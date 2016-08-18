package controllers

import play.api.mvc._

import scalaj.http.Http
import com.typesafe.config.ConfigFactory

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

  //id,dirTag,lat,lon,routeTag
  val busInfo = List(
    "8084,40_0_40,43.665283,-79.461632,40",
    "1406,52_1_52B,43.717266,-79.438965,52",
    "8179,97_1_yk97B,43.643101,-79.375298,97",
    "7913,168_0_168,43.670315,-79.455551,168",
    "1344,512_1_512bus,43.687733,-79.392616,512",
    "8126,81_1_81,43.701965,-79.344498,81",
    "4140,504_1_br504rq,43.645718,-79.393936,504",
    "7698,86_0_86E,43.733566,-79.261948,86",
    "1010,52_0_52B,43.721767,-79.640587,52",
    "7433,34_1_34,43.725983,-79.296448,34",
    "4061,501_1_501HULO,43.591568,-79.544746,501",
    "4171,504_1_504,43.64325,-79.405716,504",
    "9085,53_0_53Epm,43.828117,-79.287552,53",
    "7792,135_0_135,43.686832,-79.286613,135",
    "1551,52_0_52A,43.693233,-79.562698,52",
    "1629,47_1_47A,43.650051,-79.438484,47",
    "4023,506_0_506strcon,43.6817319,-79.308868,506",
    "1069,39_0_39B,43.792965,-79.331581,39",
    "7620,68_0_68B,43.854218,-79.333664,68",
    "1222,129_0_129A,43.856415,-79.281601,129",
    "4078,505_1_505rush,43.65255,-79.399582,505",
    "8306,196_0_196Bpm,43.749298,-79.46212,196",
    "9005,29_1_29C,43.657532,-79.4344179,29",
    "4034,506_1_506strcon,43.688351,-79.301987,506",
    "7558,54_0_54A,43.707268,-79.39547,54",
    "7549,116_0_116C,43.7682,-79.186096,116",
    "8206,103_1_103,43.705582,-79.399467,103",
    "1117,38_1_38A,43.772469,-79.251518,38",
    "4068,506_1_506strcon,43.6624679,-79.376503,506",
    "8314,51_1_51,43.789715,-79.3677979,51",
    "1827,38_0_38A,43.773834,-79.2519,38",
    "1715,39_1_39C,43.803116,-79.336403,39",
    "4244,501_0_501A,43.650433,-79.388313,501",
    "4413,510_0_510B,43.666668,-79.403519,510",
    "1398,199_1_199B,43.784916,-79.39267,199",
    "9044,29_1_29C,43.646683,-79.430283,29",
    "1278,132_0_132,43.807835,-79.215431,132",
    "7833,87_0_87 C,43.690517,-79.342834,87",
    "1207,53_1_53Bpm,43.820202,-79.322502,53",
    "1114,85_1_85C,43.776051,-79.346901,85",
    "1247,130_1_130A,43.8325,-79.267685,130",
    "9118,36_0_36Acon,43.760033,-79.5043029,36",
    "8081,76_0_76A,43.6225809,-79.501015,76",
    "7442,100_0_100C,43.728065,-79.328186,100",
    "8566,118_0_118p,43.727051,-79.48082,118",
    "7757,102_1_102B,43.758148,-79.22467,102",
    "7881,57_0_57,43.809399,-79.291183,57",
    "1558,41_0_41E,43.726002,-79.482101,41",
    "8157,8_1_8,43.681583,-79.357681,8",
    "8394,117_0_117R,43.754368,-79.464317,117",
    "7836,511_1_511bus,43.638599,-79.40052,511",
    "1590,63_0_63B,43.639065,-79.412003,63",
    "8533,84_1_84C,43.747417,-79.530014,84",
    "7686,25_1_25*,43.775768,-79.347153,25",
    "1375,512_1_512bus,43.683784,-79.41555,512",
    "1681,512_0_512bus,43.688232,-79.393204,512",
    "7578,185_1_185,43.74585,-79.345665,185",
    "9020,7_0_7,43.791767,-79.442131,7",
    "1721,39_0_39A,43.789967,-79.368401,39",
    "8052,110_0_110A,43.633816,-79.519035,110",
    "1566,32_0_32C,43.699551,-79.436485,32",
    "7947,60_0_60C,43.7809979,-79.414665,60",
    "4155,501_1_501HULO,43.630867,-79.47892,501",
    "7426,67_0_67A,43.700199,-79.286819,67",
    "7840,9_1_9rush,43.7387159,-79.238182,9",
    "7942,84_0_84D,43.751766,-79.482803,84",
    "1747,95_1_95A,43.759201,-79.309898,95",
    "7464,185_1_185,43.713615,-79.3358839,185",
    "7691,70_1_dc70C,43.683681,-79.323715,70",
    "1538,52_0_52A,43.715698,-79.443336,52",
    "8381,512_1_512bus,43.685982,-79.40522,512",
    "7490,100_0_100B,43.730015,-79.328331,100",
    "8032,111_0_111,43.655201,-79.5644,111",
    "8023,188_1_188pm,43.618935,-79.525887,188",
    "7759,102_1_102D,43.893967,-79.26445,102",
    "8065,111_1_111,43.637268,-79.539185,111",
    "8125,24_1_24A,43.791965,-79.330765,24",
    "8333,169_1_169B,43.7756649,-79.346931,169",
    "1200,32_0_32A,43.699032,-79.434052,32",
    "1060,52_1_52F,43.7183,-79.4340129,52",
    "7725,62_0_62,43.689152,-79.301102,62",
    "1617,89_0_89,43.698666,-79.512314,89",
    "8469,60_1_60C,43.796017,-79.429764,60",
    "4058,504_1_504,43.651031,-79.369583,504",
    "1605,32_1_32A,43.693199,-79.461632,32",
    "4187,509_1_509A,43.645485,-79.379349,509",
    "9026,29_0_29C,43.659782,-79.435402,29",
    "1650,52_0_52B,43.713135,-79.458633,52",
    "1653,89_0_89,43.703968,-79.527336,89",
    "4178,504_1_504,43.665298,-79.352585,504",
    "8164,143_1_143,43.648651,-79.393547,143",
    "8132,121_0_121C,43.636299,-79.40712,121",
    "1281,79_0_79B,43.692749,-79.517769,79",
    "4075,505_0_505,43.652065,-79.4070969,505",
    "7772,102_1_102A,43.757484,-79.224503,102",
    "1096,199_0_199C,43.810848,-79.252701,199",
    "7501,86_1_86E,43.809483,-79.17112,86",
    "1413,32_1_32A,43.667366,-79.587067,32",
    "8124,91_0_91B,43.696518,-79.317566,91"
  )

  val busStats = List(List("idle_2", "running_80", "Malfunctional_10", "Mics_8"), List("idle_22", "running_50", "Malfunctional_10", "Mics_18"), List("idle_90", "running_10", "Malfunctional_0", "Mics_10"), List("idle_100", "running_0", "Malfunctional_0", "Mics_0"))

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
    val ss = (0 to 10).map(_ =>
      busInfo(rand.nextInt(busInfo.length)))
    Ok(ss.mkString("|"))
  }

  def busStatus() = Action { implicit request =>
    Ok(busStats(rand.nextInt(busStats.length)).mkString("|"))
  }

}
