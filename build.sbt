lazy val scalablytyped = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalablyTypedConverterExternalNpmPlugin)
  .settings(
    scalaVersion := "3.3.1-RC6",
    scalacOptions ++= Seq(
      "-Wunused:all"
    ),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "2.6.0",
    ),
    externalNpm := {
      baseDirectory.value
    }
  )
