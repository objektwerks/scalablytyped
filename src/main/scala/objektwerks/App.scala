package objektwerks

import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.HTMLDocument

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
    ()