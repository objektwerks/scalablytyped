package objektwerks

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.{HTMLCanvasElement, Element, HTMLDocument}

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import typings.chartJs.mod.*
import typings.chartJs.anon.ChartType
import typings.chartJs.distTypesIndexMod.{ChartConfiguration, ChartData, ChartDataset}

object App:
  def main(args: Array[String]): Unit =
    document.addEventListener("DOMContentLoaded", { (_: dom.Event) =>
      build(document)
    })

  def build(document: HTMLDocument): Unit =
    val chartType = new ChartType { `type` = "line" }
    val dataArray = js.Array[Int](1, 2, 3)
    val chartDatasetProperties = new ChartDataset[ChartType, js.Array[Int]]()
    chartDatasetProperties.set("type", chartType)
    chartDatasetProperties.set("data", dataArray)

    val chartDatasets = js.Array[ChartDataset](chartDatasetProperties.asInstanceOf[ChartDataset])
    
    val chartData = new ChartData {
      datasets = chartDatasets
    }

    val chartConfig = new ChartConfiguration {
      data = chartData
    }

    val canvas = document.getElementById("chart").asInstanceOf[HTMLCanvasElement]
    val chart = Chart(item = canvas, config = chartConfig)
    ()