package objektwerks

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.{Element, HTMLDocument}

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import typings.chartJs.mod.*

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

    val canvas = document.createElement("canvas")
    canvas.id = "chart"
    document.body.appendChild(canvas)
    ()
/*
  def buildChart(canvas: Element): Element =
    val chartDataSets = js.Array(
      new ChartDataSets {
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
      `type` = ChartType.line
      date = chartData
      options = chartOptions
    }

    new Chart(canvas, chartConfig)
    */