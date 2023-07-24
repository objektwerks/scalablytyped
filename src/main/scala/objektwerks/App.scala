package objektwerks

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.{CanvasRenderingContext2D, Element, HTMLDocument}

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import typings.chartJs.anon.*
import typings.chartJs.distTypesIndexMod.{ChartConfiguration, ChartData, ChartDataset}

object App:
  def main(args: Array[String]): Unit =
    document.addEventListener("DOMContentLoaded", { (_: dom.Event) =>
      build(document)
    })

  def build(document: HTMLDocument): Unit =
    val chartDataSets = js.Array[ChartDataset](
      // import warning: importer.ImportType#apply Failed type conversion: chart.js.anon.keyinChartTypetypekeyChar[TType]
      new ChartDataset {
        data = js.Array[Int](1, 2, 3, 4, 5, 6, 7, 8, 9, 10) // Just a guess!
      }
    )
    
    val chartData = new ChartData {
      datasets = chartDataSets
    }

    val chartConfig = new ChartConfiguration {
      data = chartData
    }

    val canvas = document.getElementById("chart").asInstanceOf[CanvasRenderingContext2D]
    val chart = new Chart { // No property for chartConfig!
      ctx = canvas
      `type` = "line"
    }

    ()