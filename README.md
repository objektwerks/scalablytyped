ScalablyTyped
-------------
>ScalaJs app using ScalablyTyped, Chart.js and Scala 3.

Note
----
>This project is ***inspired*** by: [ScalaJs-Laminar-ChartJS Github](https://github.com/sjrd/scalajs-sbt-vite-laminar-chartjs-example)

>Initially, though, the Chart.js chart ***failed*** to render.

>So I ***opened*** this issue: [Chart Rendering Fails](https://github.com/sjrd/scalajs-sbt-vite-laminar-chartjs-example/issues/6)

>As a ***solution***, I built registrar.Registrar, allowing for ***required*** Chart.js component registration.
>My hack, using advanced copy-n-paste techniques, is based on ***Nikita Gazarov's*** work:

* [Weather Chart](https://demo.laminar.dev/app/weather/gradient/squamish)

>See the code:

* [Weather Gradient View](https://github.com/raquo/laminar-full-stack-demo/blob/master/client/src/main/scala/com/raquo/app/weather/WeatherGradientView.scala#L18-L191)
* [Chart](https://github.com/raquo/laminar-full-stack-demo/blob/master/client/src/main/scala/vendor/chartjs/Chart.scala)

>Clearly, to accommodate ***all*** Chart.js registrable components, registrar.Registrar would ***require*** additional work.

>Currently, this project supports ChartType.bar, ChartType.line and ChartType.pie. Simply edited the ChartType in App.

>I suspect the ScalaJs community ***needs*** a custom Chart.js component - to help developers avoid these and other Chart.js pitfalls.

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