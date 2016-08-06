
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._

object index_Scope0 {
  import models._
  import controllers._
  import play.api.i18n._
  import views.html._
  import play.api.templates.PlayMagic._
  import play.api.mvc._
  import play.api.data._

  class index extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable, Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

    /**/
    def apply(): play.twirl.api.HtmlFormat.Appendable = {
      _display_ {
        {

          def /*1.2*/ dashboardContent /*1.18*/ : play.twirl.api.HtmlFormat.Appendable = {
            _display_(

              Seq[Any](format.raw /*1.22*/ ("""
"""), format.raw /*2.1*/ ("""<script>
        com.knoldus.weather.Weather().main()
</script>
"""))
            )
          };
          Seq[Any](format.raw /*5.2*/ ("""

"""), _display_( /*7.2*/ main("Weather Information System", content = dashboardContent)))
        }
      }
    }

    def render(): play.twirl.api.HtmlFormat.Appendable = apply()

    def f: (() => play.twirl.api.HtmlFormat.Appendable) = () => apply()

    def ref: this.type = this

  }

}

/**/
object index extends index_Scope0.index
/*
                  -- GENERATED --
                  DATE: Sat Aug 06 17:47:10 GMT 2016
                  SOURCE: /Users/EdwinGuo/datallite/lindor/server/app/views/index.scala.html
                  HASH: 5c94c7dd9b08e173ac438f92cf2c7bedb2510b63
                  MATRIX: 593->1|617->17|697->21|724->22|827->87|855->90
                  LINES: 24->1|24->1|26->1|27->2|31->5|33->7
                  -- GENERATED --
              */
