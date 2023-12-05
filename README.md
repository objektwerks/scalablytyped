ScalablyTyped
-------------
>ScalaJs app using ScalablyTyped, Chart.js and Scala 3.

>This project is ***inspired*** by: [ScalaJs-Laminar-ChartJS Github](https://github.com/sjrd/scalajs-sbt-vite-laminar-chartjs-example)

Note
----
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

Warning
-------
>ScalablyTyped mappings for setting chart x and y axes labels fails with these errors:
1. Invalid scale configuration for scale: xAxes
2. Invalid scale configuration for scale: yAxes
>The ScalablyTyped mapping appears to support Chart.js V2 for xAxes and yAxes, where a
>js.Array[ChartXAxe] or js.Array[ChartYAxe] is required. Chart.js V3+ require a ChartXAxe or ChartYAxe.

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
>WIP!
```
val xTitle = js.Dynamic.literal(display = true, text = "Date")
val xTicks = js.Dynamic.literal(major = xMajor)
val x = js.Dynamic.literal(type = "time", display = true, title = xTitle)
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
