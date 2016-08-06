
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
    """), _display_( /*20.6*/ content), format.raw /*20.13*/ ("""
    """), _display_( /*21.6*/ playscalajs /*21.17*/ .html.scripts("client")), format.raw /*21.40*/ ("""
  """), format.raw /*22.3*/ ("""</body>
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
                  DATE: Sat Aug 06 18:24:29 GMT 2016
                  SOURCE: /Users/EdwinGuo/datallite/lindor/server/app/views/main.scala.html
                  HASH: 6054410e45124f617a6edd75480f78791f814018
                  MATRIX: 847->323|972->353|1001->355|1082->409|1108->414|1193->472|1208->478|1276->524|1525->747|1553->754|1585->760|1605->771|1649->794|1679->797
                  LINES: 26->8|31->8|33->10|36->13|36->13|37->14|37->14|37->14|43->20|43->20|44->21|44->21|44->21|45->22
                  -- GENERATED --
              */
