import org.scalajs.linker.interface.ModuleSplitStyle

lazy val scalablytyped = project
  .in(file("."))
  .enablePlugins(ScalaJSPlugin, ScalablyTypedConverterExternalNpmPlugin)
  .settings(
    version := "0.5-SNAPSHOT",
    scalaVersion := "3.6.2-RC1",
    scalacOptions ++= Seq(
      "-Wall"
    ),
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= {
      _.withModuleKind(ModuleKind.ESModule)
        .withModuleSplitStyle(ModuleSplitStyle.SmallModulesFor(List("objektwerks")))
    },
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % "17.1.0"
    ),
    externalNpm := baseDirectory.value
  )
