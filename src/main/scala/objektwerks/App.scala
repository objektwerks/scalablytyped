package objektwerks

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.{Element, HTMLCanvasElement, HTMLDocument}

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import typings.chartJs.mod.*
import typings.chartJs.anon.ChartOptions
import typings.chartJs.chartJsStrings.line
import typings.chartJs.distTypesIndexMod.{ChartConfiguration, ChartData, ChartDataset, ChartType}

object App:
  def main(args: Array[String]): Unit =
    document.addEventListener("DOMContentLoaded", { (_: dom.Event) =>
      build(document)
    })

  def build(document: HTMLDocument): Unit =
    val paragraph = document.createElement("p")
    paragraph.id = "pid"
    paragraph.textContent = "Scalajs-ScalablyTyped-Chartjs web app!"
    document.body.appendChild(paragraph)

    val canvas = document.createElement("canvas").asInstanceOf[HTMLCanvasElement]
    canvas.id = "chart"
    document.body.appendChild(canvas)
    ()

  def buildChart(canvas: Element): Element =
    val chartDataSets = js.Array(
      new ChartDataSet {
        label = "Data"
        borderWidth = 1
        backgroundColor = "navy"
      }
    )
    
    val chartData = new ChartData {
    }

    val chartOptions = new ChartOptions {
    }

    val chartConfig = new ChartConfiguration {
      `type` = line
      date = chartData
      options = chartOptions
    }

    new Chart(canvas, chartConfig)
