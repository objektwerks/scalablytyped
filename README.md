ScalablyTyped
-------------
>ScalaJs using ScalablyTyped, Chart.js and Scala 3.

Note
----
>Initially the Chart.js chart failed to render. So I opened this issue:
>[Chart Rendering Fails](https://github.com/sjrd/scalajs-sbt-vite-laminar-chartjs-example/issues/6)

>See registrar.Registrar for Chart.js component registration.

Install
-------
1. brew install node
2. npm install
>See **package.json**.

Build
-----
1. npm install ( only when package.json changes )
2. sbt clean compile fastLinkJS
>See **target/public** directory.

Dev
---
>Edits are reflected in both sessions.
1. sbt ( new session )
2. ~ fastLinkJS
3. npm run dev ( new session )

Package
-------
1. sbt clean test fullLinkJS
2. npm run build
>See **dist** directory.

Browser
-------
1. sbt clean compile fastLinkJS
2. npm run dev

Reference
---------
* [ScalaJs-Vite Tutorial](https://www.scala-js.org/doc/tutorial/scalajs-vite.html)
* [Chartjs](https://www.chartjs.org/docs/latest/)
* [ScalaJs-ChartJs](https://www.scala-js.org/doc/tutorial/scalablytyped.html)
* [ScalaJs-Laminar-ChartJS Github](https://github.com/sjrd/scalajs-sbt-vite-laminar-chartjs-example)
