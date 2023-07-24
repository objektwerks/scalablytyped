package objektwerks

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.{CanvasRenderingContext2D, Element, HTMLDocument}

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import typings.chartJs.anon.*
import typings.chartJs.distTypesIndexMod.{ChartConfiguration, ChartData}

object App:
  def main(args: Array[String]): Unit =
    document.addEventListener("DOMContentLoaded", { (_: dom.Event) =>
      build(document)
    })

  def build(document: HTMLDocument): Unit =  
    val chartDataSets = js.Array(
      new ChartDataSets {
        label = "Data"
        borderWidth = 1
        backgroundColor = "navy"
      }
    )
    
    val chartData = new ChartData {
      datasets = chartDataSets
    }

    val chartOptions = new ChartOptions {
    }

    val chartConfig = new ChartConfiguration {
      `type` = ChartType.line
      date = chartData
      options = chartOptions
    }

    val canvas = document.getElementById("chart").asInstanceOf[CanvasRenderingContext2D]
    val chart = new Chart {
      ctx = canvas
    }

    ()