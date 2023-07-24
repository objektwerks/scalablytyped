enablePlugins(ScalaJSPlugin)

scalaVersion := "3.3.1-RC4"
libraryDependencies ++= {
  Seq(
    "org.scala-js" %%% "scalajs-dom" % "2.6.0",
    "com.outr" %%% "scribe" % "3.11.8",
    "com.lihaoyi" %%% "utest" % "0.8.1" % Test
  )
}
jsEnv := new org.scalajs.jsenv.jsdomnodejs.JSDOMNodeJSEnv()
testFrameworks += new TestFramework("utest.runner.Framework")
scalaJSUseMainModuleInitializer := true