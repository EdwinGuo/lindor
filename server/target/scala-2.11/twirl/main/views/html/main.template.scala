
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._

object main_Scope0 {
  import models._
  import controllers._
  import play.api.i18n._
  import views.html._
  import play.api.templates.PlayMagic._
  import play.api.mvc._
  import play.api.data._

  class main extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable, Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template2[String, Html, play.twirl.api.HtmlFormat.Appendable] {

    /*
 * This template is called from the `index` template. This template
 * handles the rendering of the page header and body tags. It takes
 * three arguments, a `String` for the title of the page and an `Html`
 * object to insert into the body of the page and an `Int` for refreshing the
 * page after a set interval.
 */
    def apply /*8.2*/ (title: String, content: Html): play.twirl.api.HtmlFormat.Appendable = {
      _display_ {
        {

          Seq[Any](format.raw /*8.32*/ ("""

"""), format.raw /*10.1*/ ("""<!DOCTYPE html>
<html lang="en">
  <head>
    <title>"""), _display_( /*13.13*/ title), format.raw /*13.18*/ ("""</title>
    <link rel="stylesheet" media="screen" href=""""), _display_( /*14.50*/ routes /*14.56*/ .Assets.versioned("stylesheets/bootstrap.css")), format.raw /*14.102*/ ("""">
  </head>
  <body>
    <script type="text/javascript" src="http://code.jquery.com/jquery-1.10.2.js"></script>
    <script src="gmaps.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js"></script>
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAStmPtrbXppbP2QAB3PdNpvf-R1vKLB50"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/d3/3.4.11/d3.js"></script>
    <script type="text/javascript" src="//cdnjs.cloudflare.com/ajax/libs/c3/0.1.29/c3.js"></script>
    <link href="//cdnjs.cloudflare.com/ajax/libs/c3/0.1.29/c3.css" rel="stylesheet" type="text/css">
    <script src="https://maps.googleapis.com/maps/api/js"></script>
    <script type="text/javascript">

     function loadPie(ggg) """), format.raw /*27.28*/ ("""{"""), format.raw /*27.29*/ ("""
       """), format.raw /*28.8*/ ("""c3.generate("""), format.raw /*28.20*/ ("""{"""), format.raw /*28.21*/ ("""
         """), format.raw /*29.10*/ ("""bindto: '#edwinTest',
         data: """), format.raw /*30.16*/ ("""{"""), format.raw /*30.17*/ ("""
           """), format.raw /*31.12*/ ("""columns: ggg,
           type : 'donut'
         """), format.raw /*33.10*/ ("""}"""), format.raw /*33.11*/ (""",
         donut: """), format.raw /*34.17*/ ("""{"""), format.raw /*34.18*/ ("""
           """), format.raw /*35.12*/ ("""title: "Bus stats:",
         """), format.raw /*36.10*/ ("""}"""), format.raw /*36.11*/ ("""
       """), format.raw /*37.8*/ ("""}"""), format.raw /*37.9*/ (""");
     """), format.raw /*38.6*/ ("""}"""), format.raw /*38.7*/ ("""
    """), format.raw /*39.5*/ ("""</script>


    <script type="text/javascript" src="../example-fastopt.js"></script>
    <script type="text/javascript" src="/workbench.js"></script>

    <script>
     com.knoldus.weather.Weather().main();
    </script>

    """), _display_( /*49.6*/ content), format.raw /*49.13*/ ("""
    """), _display_( /*50.6*/ playscalajs /*50.17*/ .html.scripts("client")), format.raw /*50.40*/ ("""
  """), format.raw /*51.3*/ ("""</body>
</html>
"""))
        }
      }
    }

    def render(title: String, content: Html): play.twirl.api.HtmlFormat.Appendable = apply(title, content)

    def f: ((String, Html) => play.twirl.api.HtmlFormat.Appendable) = (title, content) => apply(title, content)

    def ref: this.type = this

  }

}

/*
 * This template is called from the `index` template. This template
 * handles the rendering of the page header and body tags. It takes
 * three arguments, a `String` for the title of the page and an `Html`
 * object to insert into the body of the page and an `Int` for refreshing the
 * page after a set interval.
 */
object main extends main_Scope0.main
/*
                  -- GENERATED --
                  DATE: Tue Aug 16 20:40:45 GMT 2016
                  SOURCE: /Users/EdwinGuo/datallite/lindor/server/app/views/main.scala.html
                  HASH: 689a306ae306765021721d7c782c479297ece41d
                  MATRIX: 847->323|972->353|1001->355|1082->409|1108->414|1193->472|1208->478|1276->524|2090->1310|2119->1311|2154->1319|2194->1331|2223->1332|2261->1342|2326->1379|2355->1380|2395->1392|2472->1441|2501->1442|2547->1460|2576->1461|2616->1473|2674->1503|2703->1504|2738->1512|2766->1513|2801->1521|2829->1522|2861->1527|3114->1754|3142->1761|3174->1767|3194->1778|3238->1801|3268->1804
                  LINES: 26->8|31->8|33->10|36->13|36->13|37->14|37->14|37->14|50->27|50->27|51->28|51->28|51->28|52->29|53->30|53->30|54->31|56->33|56->33|57->34|57->34|58->35|59->36|59->36|60->37|60->37|61->38|61->38|62->39|72->49|72->49|73->50|73->50|73->50|74->51
                  -- GENERATED --
              */
