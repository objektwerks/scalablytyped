package objektwerks

import org.scalajs.dom
import org.scalajs.dom.HTMLDocument
import org.scalajs.dom.document

@main def app() =
  document.addEventListener("DOMContentLoaded", { (_: dom.Event) =>
    build(document)
  })

  def build(document: HTMLDocument): Unit =
    document.body // Build chart!