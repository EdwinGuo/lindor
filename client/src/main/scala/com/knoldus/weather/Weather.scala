package com.knoldus.weather

import org.scalajs.dom
import org.scalajs.dom.{ XMLHttpRequest, document }
import org.scalajs.jquery.{ JQuery, jQuery, JQueryAjaxSettings, JQueryXHR }
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js
import scala.scalajs.js.Dynamic.{ global => g, literal => lit, newInstance => jsnew }
import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.{ Array, Date, JSON }
import scalacss.Defaults._
import scalacss.ScalatagsCss._
import scalatags.Text._
import scalatags.Text.all._
import js.JSConverters._

import js.annotation._
import google.maps.LatLng
import google._

trait DataGenerator {

  // def initialize(lat: Double, long: Double) = {
  //   val map_canvas = document.getElementById("map_canvas")
  //   val map_options = lit(center = (jsnew(g.google.maps.LatLng))(lat, long), zoom = 3, mapTypeId = g.google.maps.MapTypeId.ROADMAP)
  //   val gogleMap = (jsnew(g.google.maps.Map))(map_canvas, map_options)
  //   val marker = (jsnew(g.google.maps.Marker))(lit(map = gogleMap, position = (jsnew(g.google.maps.LatLng)(lat, long))))
  // }

  def initialize(data: List[(Double, Double)]) = {
    val map_canvas = document.getElementById("map_canvas")
    val map_options = lit(center = (jsnew(g.google.maps.LatLng))(43.768050, -79.371202), zoom = 10, mapTypeId = g.google.maps.MapTypeId.ROADMAP)
    val gogleMap = (jsnew(g.google.maps.Map))(map_canvas, map_options)

    val marker =
      data.map(w =>
        (jsnew(g.google.maps.Marker))(lit(
          map = gogleMap,
          position = (jsnew(g.google.maps.LatLng)(w._1, w._2)) //, animaton = g.google.maps.Animation.DROP
        )))
  }

  def msToTime(unix_timestamp: Long): String = {
    val date = new Date(unix_timestamp * 1000);
    val hrs = date.getHours();
    val mins = date.getMinutes();
    val secs = date.getSeconds();
    hrs + ":" + mins + ":" + secs
  }
}

class WeatherReport extends DataGenerator {
  @JSExport
  var busData = List[(Double, Double)]()

  @JSExport
  def polulateBus() {
    // clear the bus data
    busData = List[(Double, Double)]()

    jQuery.ajax(js.Dynamic.literal(
      `type` = "GET",
      `async` = false,
      `timeout` = 3000,
      url = "/bus", success = { (data: String, textStatus: String, jqXHR: JQueryXHR) =>
      data.split("\\|").map(w => { val g = w.split(","); (g(2).toDouble, g(3).toDouble) }).foreach { f =>
        {
          busData = busData :+ f
        }
      }
    }
    ).asInstanceOf[JQueryAjaxSettings])
  }

  @JSExport
  def polulateChart() {
    // clear the bus data

    jQuery.ajax(js.Dynamic.literal(
      `type` = "GET",
      `async` = false,
      `timeout` = 3000,
      url = "/chart", success = { (data: String, textStatus: String, jqXHR: JQueryXHR) =>
      js.Dynamic.global.loadPie(data.split("\\|").map(w => { val dd = w.split("_"); js.Array(dd(0), dd(1).toDouble) }).toSeq.toJSArray)
    }
    ).asInstanceOf[JQueryAjaxSettings])
  }

  // def loadMarker() {
  //   val marker =
  //     busData.map(w =>
  //       (jsnew(g.google.maps.Marker))(lit(
  //         map = gogleMap,
  //         position = (jsnew(g.google.maps.LatLng)(w._1, w._2))
  //       //, animation = g.google.maps.Animation.DROP
  //       )))
  // }

  @JSExport
  def showReport(): Unit = {
    val renderHtml = new WeatherFrag(scalatags.Text)
    dom.document.body.innerHTML = renderHtml.htmlFrag.render

    val map_canvas = document.getElementById("map_canvas")
    val map_options = lit(center = (jsnew(g.google.maps.LatLng))(43.768050, -79.371202), zoom = 10, mapTypeId = g.google.maps.MapTypeId.ROADMAP)
    val gogleMap = (jsnew(g.google.maps.Map))(map_canvas, map_options)

    polulateBus

    //println("I have bus: " + busData.toString)

    // load the initial data
    var markers = busData.map(w =>
      (jsnew(g.google.maps.Marker))(lit(
        map = gogleMap,
        position = (jsnew(g.google.maps.LatLng)(w._1, w._2))
      //, animation = g.google.maps.Animation.DROP
      )))

    showDetail

    //start here

    //val data = js.Array(js.Array("idle", 3), js.Array("running", 85), js.Array("Malfunctional", 10), js.Array("Misc", 2))
    polulateChart()
    dom.window.setInterval(() => {
      polulateChart()
    }, 50000)

    //end here

    //updateBus
    //initialize(43.856415, -79.281601)
    dom.window.setInterval(() => {
      // clear out the old marker
      markers.foreach { w =>
        {
          w.setMap(null)
        }
      }

      // get the new bus info
      polulateBus

      // repopulate the map
      markers = busData.map(w =>
        (jsnew(g.google.maps.Marker))(lit(
          map = gogleMap,
          position = (jsnew(g.google.maps.LatLng)(w._1, w._2))
        //, animation = g.google.maps.Animation.DROP
        )))
    }, 50000)
    dom.window.setInterval(() => showDetail, 50000)
    //dom.window.setInterval(() => updateBus, 3000)
  }

  def updateBus() {
    //clearBusMap
    jQuery.ajax(js.Dynamic.literal(
      `type` = "GET",
      url = "/bus", success = { (data: String, textStatus: String, jqXHR: JQueryXHR) =>
      initialize(data.split("\\|").map(w => { val g = w.split(","); (g(2).toDouble, g(3).toDouble) }).toList)
    }
    ).asInstanceOf[JQueryAjaxSettings])
  }

  //@JSExport
  def showDetail() {
    clearTwitterBoard

    val name = jQuery("#name").value()
    jQuery.ajax(js.Dynamic.literal(
      `type` = "GET",
      url = "/twitter", success = { (data: String, textStatus: String, jqXHR: JQueryXHR) =>
      populateMyWork(data)
    }
    ).asInstanceOf[JQueryAjaxSettings])
  }

  private def clearTwitterBoard: JQuery = {
    jQuery("#cityName").empty()
    jQuery("#twitterTable").empty()
  }

  private def cleanUI: JQuery = {
    jQuery("#cityName").empty()
    jQuery("#weather").empty()
    jQuery("#pressure").empty()
    jQuery("#humidity").empty()
    jQuery("#sunrise").empty()
    jQuery("#sunset").empty()
    jQuery("#geocoords").empty()
    jQuery("#temp").empty()
  }

  // private def populateWeatherReprt(data: String) = {
  //   val result = JSON.parse(data)
  //   val weather = result.weather.asInstanceOf[Array[js.Dynamic]](0)
  //   jQuery("#tempDetail").attr("style", "display:block;")
  //   jQuery("#cityName").append("Information Board")
  //   val image = "http://openweathermap.org/img/w/" + weather.icon + ".png"
  //   jQuery("#temp").append("<img src=" + image + " >" + Math.floor(result.main.temp.toString.toDouble - 273.15))
  //   jQuery("#weather").append("" + weather.main)
  //   jQuery("#pressure").append("" + result.main.pressure + " hpa")
  //   jQuery("#humidity").append(result.main.humidity + " %")
  //   jQuery("#sunrise").append(msToTime(result.sys.sunrise.toString.toLong))
  //   jQuery("#sunset").append(msToTime(result.sys.sunset.toString.toLong))
  //   jQuery("#geocoords").append("[" + result.coord.lon + ", " + result.coord.lat + "]")

  //   initialize(43.856415, -79.281601)
  //   //initialize()
  // }

  private def populateMyWork(data: String) = {
    jQuery("#tempDetail").attr("style", "display:block;")
    jQuery("#cityName").append("Twitter Board")
    data.split("\\|").map(g =>
      //jQuery("#twitterTable").appendChild(tr(td(ReportStyles.td, g)))
      jQuery("#twitterTable").append(f"""<tr class="child" align="left"><td>$g</td></tr>"""))

    //start here

    // end here

    //initialize()
  }
}

// experimental google map

@JSExport
object Weather extends WeatherReport with js.JSApp {
  @JSExport
  def main(): Unit = {
    println("start miracle!!!")
    showReport
  }
  // def main(): Unit = {
  //   println("*************edwin*****************running**********")

  //   def initialize() = js.Function {
  //     val opts = google.maps.MapOptions(
  //       center = new LatLng(51.201203, -1.724370),
  //       zoom = 8,
  //       panControl = false,
  //       streetViewControl = false,
  //       mapTypeControl = false
  //     )
  //     val gmap = new google.maps.Map(document.getElementById("map-canvas"), opts)

  //     val marker = new google.maps.Marker(google.maps.MarkerOptions(
  //       position = gmap.getCenter(),
  //       map = gmap,
  //       title = "Marker"
  //     ))

  //     val contentString = """
  //           <div id="content">
  //           <h1 id="firstHeading" class="firstHeading">Hello World !!</h1>
  //           </div>
  //           """

  //     val infowindow = new google.maps.InfoWindow(google.maps.InfoWindowOptions(
  //       content = contentString
  //     ))

  //     google.maps.event.addListener(marker, "click", () => {
  //       println("Marker click !")
  //       infowindow.open(gmap, marker)
  //     })
  //   }
  //   google.maps.event.addDomListener(dom.window, "load", initialize)
  // }
}

class WeatherFrag[Builder, Output <: FragT, FragT](val bundle: scalatags.generic.Bundle[Builder, Output, FragT]) {

  val htmlFrag = html(
    ReportStyles.render[TypedTag[String]],
    body(
      div(
        ReportStyles.mainDiv,
        h1(
          ReportStyles.heading,
          img(ReportStyles.firstImg, src := "/assets/images/name.png"),
          //"DataLlite Present - "
          span(ReportStyles.firstSpan, "Demo -"),
          span(ReportStyles.secondSpan, "Track and Trace"),
          img(ReportStyles.secondImg, src := "/assets/images/satellite.png")
        )
      ),
      div(
        ReportStyles.secondDiv, id := "search",
        input(
          ReportStyles.search,
          id := "name", name := "name", placeholder := "Enter a route name",
          `type` := "text", value := "", size := 15
        ),
        button(
          ReportStyles.bootstrapButton,
          `type` := "button", name := "submit", id := "submit",
          onclick := "com.knoldus.weather.Weather().showDetail();", "Search"
        )
      ),
      div(ReportStyles.mainContainer, id := "tempDetail",
        div(
          div(
            `class` := "col-md-6",
            div(ReportStyles.city, id := "cityName"),
            div(
              ReportStyles.innerDiv,
              //div(ReportStyles.city, id := "cityName"),
              table(
                ReportStyles.table,
                id := "twitterTable"
              //List(
              // tr(td(ReportStyles.td, "edwin"))
              //)
              )
            ) // start here
            ,
            div(
              ReportStyles.innerDivEdwin,
              id := "edwinTest"

            )

          //end here

          ),
          div(
            `class` := "col-md-6",
            div(ReportStyles.mapCanvas, id := "map_canvas")
          )
        ))
    )
  )
}
