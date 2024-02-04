import org.scalajs.linker.interface.ModuleSplitStyle

lazy val scalablytyped = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin, ScalablyTypedConverterExternalNpmPlugin)
  .settings(
    version := "0.5-SNAPSHOT",
    scalaVersion := "3.4.0-RC4",
    scalacOptions ++= Seq(
      "-Wunused:all"
    ),
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("objektwerks")))
    },
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % "16.0.0"
    ),
    externalNpm := baseDirectory.value
  )
