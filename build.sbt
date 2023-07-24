enablePlugins(ScalaJSPlugin)

scalaVersion := "3.3.1-RC4"
libraryDependencies ++= {
  Seq(
    "org.scala-js" %%% "scalajs-dom" % "2.6.0"
  )
}
scalaJSUseMainModuleInitializer := true