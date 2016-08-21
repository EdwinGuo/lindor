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

// experimental google map
// todo
//s2.toList.groupBy(w => w._7).map(g => (g._1, g._2.size))

@JSExport
object Weather extends js.JSApp {
  val renderHtml = new WeatherFrag(scalatags.Text)
  dom.document.body.innerHTML = renderHtml.htmlFrag.render

  @JSExport
  var toDisplay = "all"
  //  latitude, longitude, vid, dir_tag, route_tag, heading, status
  var allData = List[(Double, Double, String, String, String, String, String)]()
  var delayData = List[(Double, Double, String, String, String, String, String)]()
  var runningData = List[(Double, Double, String, String, String, String, String)]()
  var terminalData = List[(Double, Double, String, String, String, String, String)]()
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
        case _ => imaPink
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
      case "question" => displayData = terminalData
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
            case _ => imaPink
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

    //updateBus
    // dom.window.setInterval(() => {
    //   // clear out the old marker
    //   markers.foreach { w =>
    //     {
    //       w.setMap(null)
    //     }
    //   }

    //   // get the new bus info
    //   polulateBus

    //   // repopulate the map
    //   markers = busData.map(w =>
    //     (jsnew(g.google.maps.Marker))(lit(
    //       map = gogleMap,
    //       position = (jsnew(g.google.maps.LatLng)(w._1, w._2))
    //     //, animation = g.google.maps.Animation.DROP
    //     )))
    // }, 50000)

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
          case _ => imaPink
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
