package objektwerks

import org.scalajs.dom
import org.scalajs.dom.*
import org.scalajs.dom.document

// import scala.scalajs.js
// import scala.scalajs.js.JSConverters.*

// import typings.chartJs.mod.*

object App:
  def main(args: Array[String]): Unit =
    document.addEventListener("DOMContentLoaded", { (_: dom.Event) =>
      build(document)
    })

  def build(document: HTMLDocument): Unit = ??? // Rebuild!!!