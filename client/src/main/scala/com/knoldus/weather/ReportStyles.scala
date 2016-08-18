package com.knoldus.weather

import scalacss.Defaults._

object ReportStyles extends StyleSheet.Inline {

  import dsl._

  val mainDiv = style(
    //position := "fixed",
    addClassName("col-md-12"),
    borderBottom := "1px solid #eee",
    backgroundColor(Color("#000000"))
  )

  val heading = style(
    marginBottom(5 px),
    color.rgb(220, 208, 192),
    textAlign.center
  )

  val firstSpan = style(
    marginLeft(20 px),
    textTransform.uppercase,
    textShadow := "2px 2px 4px #000"
  )

  val secondSpan = style(
    textShadow := "1px 1px 1px #000"
  )

  val thirdSpan = style(
    textShadow := "1px 1px 1px #000",
    marginTop(80 %%)
  )

  val imageCommon = mixin(
    height(60 px),
    width(60 px)
  )

  val firstImg = style(imageCommon)

  val secondImg = style(
    imageCommon,
    marginLeft(20 px)
  )

  val secondDiv = style(
    addClassName("col-md-12"),
    marginTop(10 %%)
  )

  val thirdDiv = style(
    addClassName("col-md-12"),
    marginTop(20 %%)
  )

  val search = style(
    width(60 %%),
    height(35 px),
    margin := "0% 0px 0px 16%",
    borderRadius(0 px),
    paddingLeft(5 px)
  )

  val bootstrapButton = style(
    addClassName("btn btn-info"),
    height(35 px),
    margin := "-1px 0px 0px 0%",
    borderRadius(0 px)
  )

  val mainContainer = style(
    addClassName("col-md-12 maincontainer"),
    marginTop(30 px),
    borderTop := "2px solid #ccc",
    paddingTop(30 px),
    borderBottom := "2px solid #ccc",
    paddingBottom(30 px),
    display.none
  )

  val innerDiv = style(
    width(620 px),
    height(200 px),
    marginLeft(60 px),
    overflow.auto
  )

  val city = style(
    marginLeft(60 px),
    fontSize(28 px),
    color := "#67890a",
    fontWeight.bold
  )

  val table = style(
    addClassName("table-bordered table-striped"),
    width(620 px),
    textAlign.center,
    marginTop(10 px)
  )

  val commonTD = mixin(
    padding(0 px),
    fontWeight.bold
  )

  val td = style(
    padding(20 px),
    textAlign.left
  )

  val firstTd = style(
    commonTD,
    fontSize(22 px)
  )

  val secondTd = style(commonTD)

  val mapCanvas = style(
    height(830 px),
    width(912 px)
  )

  val innerDivEdwin = style(
    width(700 px),
    height(600 px),
    marginTop(30 px),
    marginLeft(5 px),
    float := "right"
  //outline := "#00FF00 dotted thick"
  )

}
