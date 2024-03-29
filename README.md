ScalablyTyped
-------------
>ScalaJs app using ScalablyTyped, Chart.js, Laminar and Scala 3.

>This project is ***inspired*** by: [ScalaJs-Laminar-ChartJS Github](https://github.com/sjrd/scalajs-sbt-vite-laminar-chartjs-example)

WARNING
-------
>***sbt clean compile fastLinkJS*** produces:
* [2024.3.28] **multiple** type errors.
* [2023.12.13] **5** type errors.
>ScalaJs ***needs*** a dedicated typed chart library.

Install
-------
1. brew install node
2. npm install
3. npm install npm-check-updates
>See **package.json**.

NPM
---
>To update package.json dependencies.
1. ncu

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

Chart.js
--------
>Initially, the Chart.js chart ***failed*** to render!

>So I ***opened*** this issue: [Chart Rendering Fails](https://github.com/sjrd/scalajs-sbt-vite-laminar-chartjs-example/issues/6)

>As a ***solution***, I built registrar.Registrar, allowing for ***required*** Chart.js component registration.
>My hack is based on ***Nikita Gazarov's*** work:

* [Weather Chart](https://demo.laminar.dev/app/weather/gradient/squamish)

>See the code:

* [Weather Gradient View](https://github.com/raquo/laminar-full-stack-demo/blob/master/client/src/main/scala/com/raquo/app/weather/WeatherGradientView.scala#L18-L191)
* [Weather Gradient Chart](https://github.com/raquo/laminar-full-stack-demo/blob/master/client/src/main/scala/com/raquo/app/weather/WeatherGradientChart.scala)
* [Chart](https://github.com/raquo/laminar-full-stack-demo/blob/master/client/src/main/scala/vendor/chartjs/Chart.scala)

>To accommodate ***all*** Chart.js registrable components, registrar.Registrar will ***require*** additional work.

>Currently, this project supports ChartType.**bar**, ChartType.**line** and ChartType.**pie**.

Mappings
--------
>ScalablyTyped mappings for setting chart x and y axes labels fails with these errors:
1. Invalid scale configuration for scale: xAxes
2. Invalid scale configuration for scale: yAxes
>The ScalablyTyped mapping appears to support Chart.js V2 for xAxes and yAxes, where a
>js.Array[ChartXAxe] or js.Array[ChartYAxe] is required. Chart.js V3+ require a ChartXAxe or ChartYAxe.

>***Also Chart.js 4.4.2 breaks typings!***

>It would appear ScalaJs needs it's own custom chart component.

JS Solution
-----------
>Using Chart.js V3+ json:
```
  options: {
    scales: {
      x: {
        type: 'time',
        display: true,
        title: {
          display: true,
          text: 'Date'
        },
        ticks: {
          major: {
            enabled: true
          },
        }
      },
      y: {
        display: true,
        title: {
          display: true,
          text: 'Value'
        }
      }
    }
  }
```

ScalaJs Solution
----------------
>I attempted to replace the current chart code with this literal code - but the typing won't allow it.
```
  import scala.scalajs.js.Dynamic.literal

  val xAxes = literal(
    `type` = "time",
    display = true,
    title = literal(display = true, text = "Time"),
    ticks = literal(major = literal(enabled = true))
  )
  val yAxes = literal
    (display = true,
    title = literal(display = true, text = "Series")
  )
  val scales = literal(x = xAxes, y = yAxes)
  val options = literal(scales = scales)
```

JS Code
-------
>The ```Invalid scale configuration for scale: xAxes | yAxes``` error is thrown in the code below:
```
function mergeScaleConfig(config, options) {
  const chartDefaults = overrides[config.type] || {scales: {}};
  const configScales = options.scales || {};
  const chartIndexAxis = getIndexAxis(config.type, options);
  const scales = Object.create(null);

  // First figure out first scale id's per axis.
  Object.keys(configScales).forEach(id => {
    const scaleConf = configScales[id];
    if (!isObject(scaleConf)) {
      return console.error(`Invalid scale configuration for scale: ${id}`); // Error occurs here!!!
    }
    if (scaleConf._proxy) {
      return console.warn(`Ignoring resolver passed as options for scale: ${id}`);
    }
    const axis = determineAxis(id, scaleConf, retrieveAxisFromDatasets(id, config), defaults.scales[scaleConf.type]);
    const defaultId = getDefaultScaleIDFromAxis(axis, chartIndexAxis);
    const defaultScaleOptions = chartDefaults.scales || {};
    scales[id] = mergeIf(Object.create(null), [{axis}, scaleConf, defaultScaleOptions[axis], defaultScaleOptions[defaultId]]);
  });
```

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
