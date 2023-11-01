package objektwerks

import com.raquo.laminar.api.L.*

import org.scalajs.dom

import scala.scalajs.js

/**
 * See: https://github.com/sjrd/scalajs-sbt-vite-laminar-chartjs-example for original code.
 */

final class DataItemID

final case class DataItem(id: DataItemID,
                          label: String,
                          value: Double)

object DataItem:
  def apply(): DataItem = DataItem(DataItemID(), "?", Math.random())

@main def runApp(): Unit =
  renderOnDomContentLoaded(dom.document.querySelector("#app"), appElement())

val dataVar = Var[List[DataItem]](List(DataItem(DataItemID(), "one", 1.0)))
val dataSignal = dataVar.signal
val dataValues = dataSignal.map(_.map(_.value))

def appElement(): HtmlElement =
  div(
    h1("ChartJs"),
    renderDataTable(),
    ul(
      li("Sum of values: ", child.text <-- dataValues.map(_.sum)),
      li("Average value: ", child.text <-- dataValues.map(vs => vs.sum / vs.size)),
    ),
    renderDataGraph(),
  )

def renderDataTable(): HtmlElement =
  table(
    thead(
      tr(th("Label"), th("Value"), th("Action")),
    ),
    tbody(
      children <-- dataSignal.split(_.id) { (id, initial, itemSignal) =>
        renderDataItem(id, itemSignal)
      }
    ),
    tfoot(
      tr(td(button("➕", onClick --> (_ => dataVar.update(data => data :+ DataItem()))))),
    ),
  )

def renderDataItem(id: DataItemID,
                   item: Signal[DataItem]): HtmlElement =
  val labelUpdater = dataVar.updater[String] { (data, newLabel) =>
    data.map(item => if item.id == id then item.copy(label = newLabel) else item)
  }

  val valueUpdater = dataVar.updater[Double] { (data, newValue) =>
    data.map(item => if item.id == id then item.copy(value = newValue) else item)
  }

  tr(
    td(inputForString(item.map(_.label), labelUpdater)),
    td(inputForDouble(item.map(_.value), valueUpdater)),
    td(button("🗑️", onClick --> (_ => dataVar.update(data => data.filter(_.id != id))))),
  )

def inputForString(valueSignal: Signal[String],
                   valueUpdater: Observer[String]): Input =
  input(
    typ := "text",
    controlled(
      value <-- valueSignal,
      onInput.mapToValue --> valueUpdater,
    ),
  )

def inputForDouble(valueSignal: Signal[Double],
                   valueUpdater: Observer[Double]): Input =
  val strValue = Var[String]("")
  input(
    typ := "text",
    controlled(
      value <-- strValue.signal,
      onInput.mapToValue --> strValue,
    ),
    valueSignal --> strValue.updater[Double] { (prevStr, newValue) =>
      if prevStr.toDoubleOption.contains(newValue) then prevStr
      else newValue.toString
    },
    strValue.signal --> { valueStr =>
      valueStr.toDoubleOption.foreach(valueUpdater.onNext)
    },
  )

def renderDataGraph(): HtmlElement =
  import scala.scalajs.js.JSConverters.*
  import typings.chartJs.mod.*

  var optChart: Option[Chart] = None

  canvasTag(
    width := "100%",
    height := "500px",

    onMountUnmountCallback(
      mount = { nodeCtx =>
        val canvas = nodeCtx.thisNode.ref
        val chart = Chart.apply.newInstance2(canvas, new ChartConfiguration {
          `type` = ChartType.bar
          data = new ChartData {
            datasets = js.Array(new ChartDataSets {
              label = "Value"
              borderWidth = 1
            })
          }
          options = new ChartOptions {
            scales = new ChartScales {
              yAxes = js.Array(new CommonAxe {
                ticks = new TickOptions {
                  beginAtZero = true
                }
              })
            }
          }
        })
        optChart = Some(chart)
      },
      unmount = { thisNode =>
        for (chart <- optChart) chart.destroy()
        optChart = None
      }
    ),

    dataSignal --> { data =>
      for (chart <- optChart) {
        chart.data.labels = data.map(_.label).toJSArray
        chart.data.datasets.get(0).data = data.map(_.value).toJSArray
        chart.update()
      }
    },
  )