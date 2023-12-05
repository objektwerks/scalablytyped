package objektwerks

import com.raquo.laminar.api.L.*

import org.scalajs.dom

import scala.scalajs.js
import scala.util.Random

/**
 * See: https://github.com/sjrd/scalajs-sbt-vite-laminar-chartjs-example for the original code.
 */
@main def runChartApp(): Unit = renderOnDomContentLoaded(dom.document.querySelector("#app"), renderApp())

final class Id

final case class DataItem(id: Id = Id(),
                          label: String = "?",
                          value: Double = f"${Random.between(1.1, 9.1)}%1.1f".toDouble)

val dataItemsVar = Var[List[DataItem]](List(DataItem(Id(), "one", 1.1)))
val dataItemsSignal = dataItemsVar.signal
val doubleSignal = dataItemsSignal.map(_.map(_.value))

val chartTypes = List("bar", "line", "pie")
val chartBus = EventBus[HtmlElement]()

def renderApp(): HtmlElement =
  div(
    h1("Data"),
    renderDataItemTable(),
    ul(
      li("Sum - ", child.text <-- doubleSignal.map(doubles => f"${doubles.sum}%2.2f")),
      li("Avg - ", child.text <-- doubleSignal.map(doubles => f"${doubles.sum / doubles.size}%2.2f"))
    ),
    h1("Chart"),
    label("Select chart type: "),
    select(
      option(selected(true), disabled(true), hidden(true), ""),
      children <-- Var(chartTypes.map(item => option(item))).signal
    ).amend(
      onChange.mapToValue --> { value => chartBus.emit( renderDataItemChart(value) ) },
    ),
    div(
      child <-- chartBus.events
    )
  )

def renderDataItemTable(): HtmlElement =
  table(
    thead(
      tr(th("Label"), th("Value"), th("Action")),
    ),
    tbody(
      children <-- dataItemsSignal.split(_.id) { (id, initial, signal) => renderDataItem(id, signal) }
    ),
    tfoot(
      tr(td(button("➕", onClick --> (_ => dataItemsVar.update(dataItems => dataItems :+ DataItem()))))),
    ),
  )

def renderDataItem(id: Id,
                   signal: Signal[DataItem]): HtmlElement =
  val labelTextInputUpdater = dataItemsVar.updater[String] { (dataItems, newLabel) =>
    dataItems.map(dataItem => if dataItem.id == id then dataItem.copy(label = newLabel) else dataItem)
  }

  val valueTextInputUpdater = dataItemsVar.updater[Double] { (dataItems, newValue) =>
    dataItems.map(dataItem => if dataItem.id == id then dataItem.copy(value = newValue) else dataItem)
  }

  tr(
    td(labelTextInput(signal.map(_.label), labelTextInputUpdater)),
    td(valueTextInput(signal.map(_.value), valueTextInputUpdater)),
    td(button("🗑️", onClick --> (_ => dataItemsVar.update(data => data.filter(_.id != id))))),
  )

def labelTextInput(labelSignal: Signal[String],
                   labelObserver: Observer[String]): Input =
  input(
    typ("text"),
    controlled(
      value <-- labelSignal,
      onInput.mapToValue --> labelObserver,
    ),
  )

def valueTextInput(valueSignal: Signal[Double],
                   valueObserver: Observer[Double]): Input =
  val stringVar = Var[String]("")
  input(
    typ("text"),
    controlled(
      value <-- stringVar.signal,
      onInput.mapToValue --> stringVar,
    ),
    valueSignal --> stringVar.updater[Double] { (oldDoubleAsString, newDouble) =>
      if oldDoubleAsString.toDoubleOption.contains(newDouble) then oldDoubleAsString
      else newDouble.toString
    },
    stringVar.signal --> { string =>
      string.toDoubleOption.foreach(valueObserver.onNext)
    },
  )

def renderDataItemChart(typeOfChart: String): HtmlElement =
  import scala.scalajs.js.JSConverters.*
  import typings.chartJs.mod.*
  import registrar.ChartRegistrar.{
    ArcElement, BarController, BarElement, CategoryScale, LinearScale, LineController, LineElement, PieController, PointElement, Chart => ChartJs
  }

  ChartJs.register(
    ArcElement, BarController, BarElement, CategoryScale, LinearScale, LineController, LineElement, PieController, PointElement
  )

  var optionalChart: Option[Chart] = None
  val chartType = typeOfChart match
                    case "bar" => ChartType.bar
                    case "line" => ChartType.line
                    case "pie" => ChartType.pie

  /*
  val xTitle = js.Dynamic.literal(display = true, text = "Date")
  val xMajor = js.Dynamic.literal(enabled = true)
  val xTicks = js.Dynamic.literal(major = xMajor)
  val xAxes = js.Dynamic.literal(type = "time", display = true, title = xTitle, ticks = xTicks)

  val yTitle = js.Dynamic.literal(display = true, text = "Value")
  val yAxes = js.Dynamic.literal(display = true, title = yTitle)

  val scales = js.Dynamic.literal(x = xAxes, y = yAxes)
  val options = js.Dynamic.literal(scales = scales)
  */

  canvasTag(
    width("100%"),

    onMountUnmountCallback(
      mount = { mountContext =>
        val canvas = mountContext.thisNode.ref
        val chart = Chart.apply.newInstance2(canvas, new ChartConfiguration {
          `type` = chartType
          data = new ChartData { datasets = js.Array(new ChartDataSets {}) }
          options = new ChartOptions {
            scales = new ChartScales {
              xAxes = js.Array(new ChartXAxe { // This is Chart.js V2 code. Why? It compiles but fails at runtime! See readme!
                title = new ChartTitleOptions {
                  display = true
                  text = "Time"
                }
              })
              yAxes = js.Array(new ChartYAxe { // This is Chart.js V2 code. Why? It compiles but fails at runtime! See readme!
                title = new ChartTitleOptions {
                  display = true
                  text = "Series"
                }
              })
            }
          }
        })
        optionalChart = Some(chart)
      },
      unmount = { _ =>
        optionalChart.foreach(chart => chart.destroy())
        optionalChart = None
      }
    ),

    dataItemsSignal --> { dataItems =>
      optionalChart.foreach { chart =>
        chart.data.labels = dataItems.map(_.label).toJSArray
        chart.data.datasets.get(0).data = dataItems.map(_.value).toJSArray
        chart.update()
      }
    },
  )