package objektwerks

import com.raquo.laminar.api.L.*

import org.scalajs.dom

import scala.scalajs.js
import scala.util.Random

/**
 * See: https://github.com/sjrd/scalajs-sbt-vite-laminar-chartjs-example for original code.
 */
@main def runApp(): Unit = renderOnDomContentLoaded(dom.document.querySelector("#app"), appElement())

final class Id

final case class DataItem(id: Id = Id(),
                          label: String = "?",
                          value: Double = f"${Random.between(1.1, 9.1)}%1.1f".toDouble)

val dataItemsVar = Var[List[DataItem]](List(DataItem(Id(), "one", 1.1)))
val dataItemsSignal = dataItemsVar.signal
val doubleSignal = dataItemsSignal.map(_.map(_.value))

def appElement(): HtmlElement =
  div(
    h1("Data"),
    renderDataItemTable(),
    ul(
      li("Sum - ", child.text <-- doubleSignal.map(doubles => f"${doubles.sum}%2.2f")),
      li("Avg - ", child.text <-- doubleSignal.map(doubles => f"${doubles.sum / doubles.size}%2.2f"))
    ),
    h1("Chart"),
    renderDataItemChart()
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
    td(doubleTextInput(signal.map(_.value), valueTextInputUpdater)),
    td(button("🗑️", onClick --> (_ => dataItemsVar.update(data => data.filter(_.id != id))))),
  )

def labelTextInput(stringSignal: Signal[String],
                   stringObserver: Observer[String]): Input =
  input(
    typ := "text",
    controlled(
      value <-- stringSignal,
      onInput.mapToValue --> stringObserver,
    ),
  )

def doubleTextInput(doubleSignal: Signal[Double],
                    doubleObserver: Observer[Double]): Input =
  val stringVar = Var[String]("")
  input(
    typ := "text",
    controlled(
      value <-- stringVar.signal,
      onInput.mapToValue --> stringVar,
    ),
    doubleSignal --> stringVar.updater[Double] { (previousString, newDouble) =>
      if previousString.toDoubleOption.contains(newDouble) then previousString
      else newDouble.toString
    },
    stringVar.signal --> { string =>
      string.toDoubleOption.foreach(doubleObserver.onNext)
    },
  )

def renderDataItemChart(): HtmlElement =
  import scala.scalajs.js.JSConverters.*
  import typings.chartJs.mod.*
  import registrar.Registrar.{
    ArcElement, BarController, BarElement, CategoryScale, LineController, LineElement, LinearScale, PieController, PointElement, Chart => ChartJs
  }

  ChartJs.register(
    ArcElement, BarController, BarElement, CategoryScale, LineController, LineElement, LinearScale, PieController, PointElement
  )

  var optionalChart: Option[Chart] = None

  canvasTag(
    width := "100%",
    height := "500px",

    onMountUnmountCallback(
      mount = { mountContext =>
        val canvas = mountContext.thisNode.ref
        val chart = Chart.apply.newInstance2(canvas, new ChartConfiguration {
          `type` = ChartType.bar
          // `type` = ChartType.line
          // `type` = ChartType.pie
          data = new ChartData {
            datasets = js.Array(new ChartDataSets {
              label = "Value"
              borderWidth = 1
            })
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