lazy val scalablytyped = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalablyTypedConverterExternalNpmPlugin)
  .settings(
    scalaVersion := "3.3.1",
    scalacOptions ++= Seq(
      "-Wunused:all"
    ),
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % "16.0.0",
      "org.scala-js" %%% "scalajs-dom" % "2.8.0",
    ),
    externalNpm := baseDirectory.value
  )