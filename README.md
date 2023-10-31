ScalablyTyped
-------------
>ScalaJs using ScalablyTyped, Chartjs and Scala 3.

Note
----
>Chartjs (```4.3.1, @types/chart.js": "^2.9.37```) types don't work as advertised. See App for details.

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
1. [ScalaJs-Vite Tutorial](https://www.scala-js.org/doc/tutorial/scalajs-vite.html)
2. [Chartjs](https://www.chartjs.org/docs/latest/)
3. [ScalaJs-ChartJs](https://www.scala-js.org/doc/tutorial/scalablytyped.html)
4. [ScalaJs-Laminar-ChartJS Github](https://github.com/sjrd/scalajs-sbt-vite-laminar-chartjs-example)