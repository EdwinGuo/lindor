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
import org.scalajs.dom.{ html => _, _ }

import Constants._

@JSExport
object Weather extends js.JSApp {
  val renderHtml = new WeatherFrag(scalatags.Text)
  dom.document.body.innerHTML = renderHtml.htmlFrag.render

  @JSExport
  var allRoutes = Set("5", "6", "7", "8", "9", "10", "11", "12", "14", "15", "16", "17", "20", "21", "22", "23", "24", "25", "26", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "94", "95", "96", "97", "98", "99", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "115", "116", "118", "120", "121", "122", "123", "124", "125", "126", "127", "129", "130", "131", "132", "133", "134", "135", "143", "144", "145", "160", "161", "162", "165", "168", "169", "171", "185", "190", "191", "192", "195", "196", "198", "199", "300", "301", "302", "304", "306", "310", "315", "317", "320", "322", "324", "325", "329", "334", "335", "336", "337", "339", "341", "343", "353", "354", "384", "385", "395", "396", "501", "502", "503", "504", "505", "506", "509", "510", "511", "512", "514")

  var toDisplay = "all"
  var routeId = "all"
  //  latitude, longitude, vid, dir_tag, route_tag, heading, status
  var allData = List[(Double, Double, String, String, String, String, String)]()
  var delayData = List[(Double, Double, String, String, String, String, String)]()
  var runningData = List[(Double, Double, String, String, String, String, String)]()
  var terminalData = List[(Double, Double, String, String, String, String, String)]()
  var unavailableData = List[(Double, Double, String, String, String, String, String)]()
  var questionData = List[(Double, Double, String, String, String, String, String)]()
  // to display the info at the map, will be used as default
  var displayData = List[(Double, Double, String, String, String, String, String)]()

  val opts = google.maps.MapOptions(
    center = new LatLng(43.768050, -79.371202),
    zoom = 10,
    panControl = false,
    streetViewControl = false,
    mapTypeControl = false
  )

  val gmap = new google.maps.Map(document.getElementById("map_canvas"), opts)

  polulateBus

  renderDisplayData()

  var markers = displayData.map(w => {
    val marker = new google.maps.Marker(google.maps.MarkerOptions(
      position = new google.maps.LatLng(w._1, w._2),
      map = gmap,
      icon = w._7 match {
        case "delayed" => imaRed;
        case "running" => imaGreen;
        case "terminal" => imaYellow;
        case "unavailable" => imaPink;
        case _ => imaBlue;
      },
      title = "Marker"
    ))

    val infowindow = new google.maps.InfoWindow(google.maps.InfoWindowOptions(
      content = f"""Id: ${w._3},
                      DirTag: ${w._4},
                      RouteTag: ${w._5},
                      Heading: ${w._6}
                   """
    ))

    google.maps.event.addListener(marker, "mouseover", () => {
      infowindow.open(gmap, marker)
    })

    google.maps.event.addListener(marker, "mouseout", () => {
      infowindow.close()
    })

    marker
  })

  @JSExport
  def updateInternalState(d: String) = {
    toDisplay = d
  }

  @JSExport
  def renderDisplayData() = {
    toDisplay match {
      case "all" => displayData = allData
      case "delayed" => displayData = delayData
      case "running" => displayData = runningData
      case "terminal" => displayData = terminalData
      case "unavailable" => displayData = unavailableData
      case "question" => displayData = questionData
      case _ => displayData = allData
    }
  }

  @JSExport
  def polulateBus() {
    //  latitude, longitude, vid, dir_tag, route_tag, heading, status
    allData = List[(Double, Double, String, String, String, String, String)]()
    delayData = List[(Double, Double, String, String, String, String, String)]()
    runningData = List[(Double, Double, String, String, String, String, String)]()
    terminalData = List[(Double, Double, String, String, String, String, String)]()
    unavailableData = List[(Double, Double, String, String, String, String, String)]()
    questionData = List[(Double, Double, String, String, String, String, String)]()

    jQuery.ajax(js.Dynamic.literal(
      `type` = "GET",
      `async` = false,
      `timeout` = 3000,
      url = "/bus", success = { (data: String, textStatus: String, jqXHR: JQueryXHR) =>
      data.split("\\|").map(w => { val g = w.split("\\^"); (g(2).toDouble, g(3).toDouble, g(0), g(1), g(4), g(5), g(6)) }).foreach { f =>
        {
          // clear the bus data

          allData = allData :+ f
          if (f._7 == "running") {
            runningData = runningData :+ f
          } else if (f._7 == "delayed") {
            delayData = delayData :+ f
          } else if (f._7 == "terminal") {
            terminalData = terminalData :+ f
          } else if (f._7 == "unavailable") {
            unavailableData = unavailableData :+ f
          } else {
            questionData = questionData :+ f
          }
        }
      }
    }
    ).asInstanceOf[JQueryAjaxSettings])
  }

  @JSExport
  def polulateChartNew() {
    // clear the bus data
    //println("Chart stuff :" + displayData.groupBy(w => w._7).map(g => g._1 + ":" + g._2.size + ",").mkString(","))
    js.Dynamic.global.loadPie(displayData.groupBy(w => w._7).map(g => js.Array(g._1, g._2.size)).toSeq.toJSArray)
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

  import google.maps.Data.Feature
  import google.maps.LatLng

  @JSExport
  def showReport(): Unit = {

    //start here

    //google.maps.event.addDomListener(window, "load", initializeEdwin)
    //end here

    dom.window.setInterval(() => {
      // clear out the old marker
      markers.foreach { w =>
        {
          w.setMap(null)
        }
      }
      //repopulate bus
      polulateBus

      renderDisplayData

      // repopulate the map
      markers = displayData.map(w => {

        val marker = new google.maps.Marker(google.maps.MarkerOptions(
          position = new google.maps.LatLng(w._1, w._2),
          map = gmap,
          icon = w._7 match {
            case "delayed" => imaRed;
            case "running" => imaGreen;
            case "terminal" => imaYellow;
            case "unavailable" => imaPink;
            case _ => imaBlue;
          },
          title = "Marker"
        ))

        val infowindow = new google.maps.InfoWindow(google.maps.InfoWindowOptions(
          content = f"""Id: ${w._3},
                      DirTag: ${w._4},
                      RouteTag: ${w._5},
                      Heading: ${w._6}
                   """
        ))

        google.maps.event.addListener(marker, "mouseover", () => {
          infowindow.open(gmap, marker)
        })

        google.maps.event.addListener(marker, "mouseout", () => {
          infowindow.close()
        })

        marker
      })
    }, 10000)

    showDetail

    //val data = js.Array(js.Array("idle", 3), js.Array("running", 85), js.Array("Malfunctional", 10), js.Array("Misc", 2))
    polulateChartNew()
    dom.window.setInterval(() => {
      polulateChartNew()
    }, 50000)

    //end here

    dom.window.setInterval(() => showDetail, 50000)
    //dom.window.setInterval(() => updateBus, 3000)
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

  private def populateMyWork(data: String) = {
    jQuery("#tempDetail").attr("style", "display:block;")
    jQuery("#cityName").append("Twitter Board")
    data.split("\\|").map(g =>
      //jQuery("#twitterTable").appendChild(tr(td(ReportStyles.td, g)))
      jQuery("#twitterTable").append(f"""<tr class="child" align="left"><td>$g</td></tr>"""))
  }

  @JSExport
  def getActionData(data: String): Unit = {

    updateInternalState(data)
    renderDisplayData

    markers.foreach { w =>
      {
        w.setMap(null)
      }
    }

    markers = displayData.map(w => {
      val marker = new google.maps.Marker(google.maps.MarkerOptions(
        position = new google.maps.LatLng(w._1, w._2),
        map = gmap,
        icon = w._7 match {
          case "delayed" => imaRed;
          case "running" => imaGreen;
          case "terminal" => imaYellow;
          case "unavailable" => imaPink;
          case _ => imaBlue;
        },
        title = "Marker"
      ))

      val infowindow = new google.maps.InfoWindow(google.maps.InfoWindowOptions(
        content = f"""Id: ${w._3},
                      DirTag: ${w._4},
                      RouteTag: ${w._5},
                      Heading: ${w._6}
                   """
      ))

      google.maps.event.addListener(marker, "mouseover", () => {
        infowindow.open(gmap, marker)
      })

      google.maps.event.addListener(marker, "mouseout", () => {
        infowindow.close()
      })

      marker
    })
  }

  @JSExport
  def selectSpecificRoute() {
    val route = jQuery("#route").value()
    if (allRoutes.contains(route.toString)) {
      println("I have route: " + route)
      displayData = allData.filter(_._5 == route)

      delayData = displayData.filter(_._7 == "delayed")
      runningData = displayData.filter(_._7 == "running")
      terminalData = displayData.filter(_._7 == "terminal")
      unavailableData = displayData.filter(_._7 == "unavailable")

    } else {
      println("route not found: " + route)
    }
  }

  @JSExport
  def main(): Unit = {
    println("start miracle!!!")
    showReport
  }
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
          id := "route", name := "route", placeholder := "Enter a route name",
          `type` := "text", value := "", size := 15
        ),
        button(
          ReportStyles.bootstrapButton,
          `type` := "button", name := "submit", id := "submit",
          onclick := "com.knoldus.weather.Weather().selectSpecificRoute();", "Search"
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
