package objektwerks

import com.raquo.laminar.api.L.*

import org.scalajs.dom

import scala.scalajs.js
import scala.util.Random

/**
 * See: https://github.com/sjrd/scalajs-sbt-vite-laminar-chartjs-example for original code.
 */

final class DataItemID

final case class DataItem(id: DataItemID,
                          label: String,
                          value: Double)

object DataItem:
  def apply(): DataItem = DataItem(DataItemID(), "?", f"${Random.between(1.1,10.1)}%1.1f".toDouble)

@main def runApp(): Unit = renderOnDomContentLoaded(dom.document.querySelector("#app"), appElement())

val dataItemVar = Var[List[DataItem]](List(DataItem(DataItemID(), "one", 1.1)))
val dataItemSignal = dataItemVar.signal
val dataItemValues = dataItemSignal.map(_.map(_.value))

def appElement(): HtmlElement =
  div(
    h1("Data"),
    renderDataItemTable(),
    p("Sum of values: ", child.text <-- dataItemValues.map(vs => f"${vs.sum}%2.2f")),
    p("Average value: ", child.text <-- dataItemValues.map(vs => f"${vs.sum / vs.size}%2.2f")),
    h1("Chart"),
    renderDataItemChart()
  )

def renderDataItemTable(): HtmlElement =
  table(
    thead(
      tr(th("Label"), th("Value"), th("Action")),
    ),
    tbody(
      children <-- dataItemSignal.split(_.id) { (id, initial, itemSignal) => renderDataItem(id, itemSignal) }
    ),
    tfoot(
      tr(td(button("➕", onClick --> (_ => dataItemVar.update(data => data :+ DataItem()))))),
    ),
  )

def renderDataItem(id: DataItemID,
                   item: Signal[DataItem]): HtmlElement =
  val labelTextInputUpdater = dataItemVar.updater[String] { (dataItems, newLabel) =>
    dataItems.map(item => if item.id == id then item.copy(label = newLabel) else item)
  }

  val valueTextInputUpdater = dataItemVar.updater[Double] { (dataItems, newValue) =>
    dataItems.map(item => if item.id == id then item.copy(value = newValue) else item)
  }

  tr(
    td(labelTextInput(item.map(_.label), labelTextInputUpdater)),
    td(valueTextInput(item.map(_.value), valueTextInputUpdater)),
    td(button("🗑️", onClick --> (_ => dataItemVar.update(data => data.filter(_.id != id))))),
  )

def labelTextInput(valueSignal: Signal[String],
                   valueUpdater: Observer[String]): Input =
  input(
    typ := "text",
    controlled(
      value <-- valueSignal,
      onInput.mapToValue --> valueUpdater,
    ),
  )

def valueTextInput(valueSignal: Signal[Double],
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

def renderDataItemChart(): HtmlElement =
  import scala.scalajs.js.JSConverters.*
  import typings.chartJs.mod.*
  import registrar.Registrar.{BarController, BarElement, CategoryScale, LinearScale, Chart => ChartJs}

  ChartJs.register(BarController, BarElement, CategoryScale, LinearScale)

  var optionalChart: Option[Chart] = None

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
        })
        optionalChart = Some(chart)
      },
      unmount = { thisNode =>
        for (chart <- optionalChart) chart.destroy()
        optionalChart = None
      }
    ),

    dataItemSignal --> { dataItems =>
      for (chart <- optionalChart)
        chart.data.labels = dataItems.map(_.label).toJSArray
        chart.data.datasets.get(0).data = dataItems.map(_.value).toJSArray
        chart.update()
    },
  )