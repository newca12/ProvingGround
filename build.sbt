import sbt.Project.projectToRef

val scalaV = "2.12.2"

val ammV = "0.9.4"

// scalaOrganization in ThisBuild := "org.typelevel"

// inThisBuild(Seq(
//   scalaOrganization := "org.typelevel",
//   scalaVersion := "2.12.2-bin-typelevel-4"
// ))

scalaVersion in ThisBuild := scalaV
scalafmtOnCompile in ThisBuild := true
scalafmtVersion in ThisBuild := "1.0.0-RC3"

classpathTypes += "maven-plugin"

resolvers += Resolver.sonatypeRepo("releases")

// javaOptions in Universal ++= Seq(
//   // -J params will be added as jvm parameters
//   "-J-Xmx4G",
//   "-J-Xms512m"
// )

// libraryDependencies += "com.lihaoyi" % "ammonite" % ammV % "test" cross CrossVersion.full
//
// sourceGenerators in Test += Def.task {
//   val file = (sourceManaged in Test).value / "amm.scala"
//   IO.write(file, """object amm extends App { ammonite.Main("$initCommands").run() }""")
//   Seq(file)
// }.taskValue

// addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.9.3")

// addCompilerPlugin("org.typelevel" % "kind-projector" % "0.9.3" cross CrossVersion.binary)

lazy val jsProjects = Seq(client)

lazy val baseSettings = Seq(
  version := "0.1",
  organization := "in.ernet.iisc.math" //, scalaVersion := scalaV
)

lazy val commonSettings = baseSettings ++ Seq(
  resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  libraryDependencies ++= Seq(
    // "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.5",
//      "org.scala-lang.modules" %% "scala-xml" % "1.0.5",
    "org.typelevel" %% "spire"      % "0.14.1",
    "com.lihaoyi"   %% "fansi"      % "0.2.4",
    "com.lihaoyi"   %% "upickle"    % "0.4.4",
    "com.chuusai"   %% "shapeless"  % "2.3.2",
    "org.typelevel" %% "cats"       % "0.9.0",
    "io.monix"      %% "monix"      % "2.3.0",
    "io.monix"      %% "monix-cats" % "2.3.0",
    "com.lihaoyi"   % "ammonite"    % ammV cross CrossVersion.patch
  ),
  scalacOptions in Compile ++= Seq(
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-encoding",
    "utf-8", // Specify character encoding used by source files.
    "-explaintypes", // Explain type errors in more detail.
    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
    "-language:experimental.macros", // Allow macro definition (besides implementation and application)
    "-language:higherKinds", // Allow higher-kinded types
    "-language:implicitConversions", // Allow definition of implicit functions called views
    "-language:postfixOps", // Allow postfix operator notation
    "-unchecked",           // Enable additional warnings where generated code depends on assumptions.
    //"-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
    //"-Xfatal-warnings", // Fail the compilation if there are any warnings.
    "-Xfuture", // Turn on future language features.
    "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
    "-Xlint:by-name-right-associative", // By-name parameter of right associative operator.
    "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
    "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
    "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
    "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
    "-Xlint:nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
    "-Xlint:option-implicit", // Option.apply used implicit view.
    "-Xlint:package-object-classes", // Class or object defined in package object.
    "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
    "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
    "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
    "-Xlint:unsound-match",         // Pattern match may not be typesafe.
    //"-Yno-adapted-args",    // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
    "-Ypartial-unification", // Enable partial unification in type constructor inference Somme issues with akka-http route
    "-Ywarn-dead-code", // Warn when dead code is identified.
    "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined. 2.12.2 only
    "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
    "-Ywarn-infer-any", // Warn when a type argument is inferred to be `Any`.
    "-Ywarn-nullary-override", // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Ywarn-nullary-unit", // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen", // Warn when numerics are widened.
    "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused. 2.12.2 only
    "-Ywarn-unused:imports", // Warn if an import selector is not referenced. 2.12.2 only
    "-Ywarn-unused:locals", // Warn if a local definition is unused. 2.12.2 only
    "-Ywarn-unused:params", // Warn if a value parameter is unused. 2.12.2 only
    "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused. 2.12.2 only
    "-Ywarn-unused:privates", // Warn if a private member is unused. 2.12.2 only
    "-Ywarn-value-discard" // Warn when non-Unit expression results are unused.
  ),
  scalacOptions in (Compile, doc) ++= Seq("-diagrams",
                                          "-implicits",
                                          "-implicits-show-all"),
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oD")
)

val akkaV = "2.4.17"

assemblyMergeStrategy in assembly := {
  case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
}

lazy val jvmSettings = Seq(
  libraryDependencies ++= Seq(
    "com.lihaoyi"            % "ammonite"       % ammV % "test" cross CrossVersion.patch,
    "com.lihaoyi"            %% "ammonite-ops"  % ammV,
    "com.github.nscala-time" %% "nscala-time"   % "2.16.0",
    "org.reactivemongo"      %% "reactivemongo" % "0.12.1",
    "com.typesafe.akka"      %% "akka-actor"    % akkaV,
    "com.typesafe.akka"      %% "akka-slf4j"    % akkaV,
    // "de.heikoseeberger"      %% "akka-sse"      % "2.0.0",
    "org.scalactic" %% "scalactic" % "3.0.1",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
//    "ch.qos.logback" % "logback-classic" % "1.0.9",
    "com.typesafe" % "config" % "1.3.0",
    // "org.mongodb"  %% "casbah" % "3.1.1",
//    "org.mongodb.scala" %% "mongo-scala-driver" % "1.0.0",
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http"   % "10.0.5",
    // "com.typesafe.akka" %% "akka-http" % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.5",
//    "com.lihaoyi" %% "upickle" % "0.3.4",
//    "com.lihaoyi" %% "ammonite-ops" % ammV,
//    "com.lihaoyi" %% "ammonite-shell" % ammV,
    // "org.scala-lang.modules" %% "scala-pickling" % "0.10.1",
    "org.slf4j"   % "slf4j-api"    % "1.7.16",
    "org.slf4j"   % "slf4j-simple" % "1.7.16",
    "com.lihaoyi" %% "pprint"      % "0.5.1",
    // Last stable release
    "org.scalanlp" %% "breeze" % "0.13.+"
    // Native libraries are not included by default. add this if you want them (as of 0.7)
    // Native libraries greatly improve performance, but increase jar sizes.
    // It also packages various blas implementations, which have licenses that may or may not
    // be compatible with the Apache License. No GPL code, as best I know.
    // "org.scalanlp" %% "breeze-natives" % "0.12",
    // The visualization library is distributed separately as well.
    // It depends on LGPL code
    // "org.scalanlp" %% "breeze-viz" % "0.12"
  ),
  resources in Compile += (fastOptJS in (client, Compile)).value.data
)

//lazy val logback = "ch.qos.logback" % "logback-classic" % "1.0.9"

lazy val serverSettings = Seq(
  libraryDependencies ++= Seq(
    // "org.scala-lang" % "scala-compiler" % scalaVersion.value,
    // ws,
    // "org.reactivemongo" %% "play2-reactivemongo" % "0.11.2.play24",
    // "com.vmunier" %% "play-scalajs-scripts" % "0.3.0",
    "org.webjars" % "jquery" % "1.11.1"
  ),
  scalaJSProjects := jsProjects,
  pipelineStages := Seq(scalaJSProd),
  initialCommands in console := """import provingground._ ; import HoTT._; /*import pprint.Config.Colors._; import pprint.pprintln*/"""
)

lazy val nlpSettings = Seq(
  libraryDependencies ++= Seq(
    "com.lihaoyi"         % "ammonite"         % ammV % "test" cross CrossVersion.patch,
    "com.lihaoyi"         %% "ammonite-ops"    % ammV,
    "edu.stanford.nlp"    % "stanford-corenlp" % "3.7.0",
    "edu.stanford.nlp"    % "stanford-corenlp" % "3.7.0" classifier "models",
    "com.google.protobuf" % "protobuf-java"    % "2.6.1"
  )
)

lazy val digressionSettings = Seq(
  name := "ProvingGround-Digressions",
  libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-actor" % akkaV),
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")
)

lazy val acSettings = Seq(
  name := "AndrewsCurtis",
  libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-actor" % akkaV),
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
  initialCommands in console := """import provingground.andrewscurtis._"""
)

lazy val nfSettings = Seq(
  name := "NormalForm",
  libraryDependencies ++= Seq("org.typelevel" %% "spire" % "0.14.1"),
  scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature"),
  initialCommands in console := """import provingground.normalform._ ; import provingground.normalform.NormalForm._"""
)

lazy val client = project
  .settings(
    name := "ProvingGround-JS",
    // scalaVersion := scalaV,
    coverageEnabled := false,
    persistLauncher := true,
    persistLauncher in Test := false,
    // sourceMapsDirectories += coreJS.base / "..",
    unmanagedSourceDirectories in Compile := Seq(
      (scalaSource in Compile).value),
    resolvers += "amateras-repo" at "http://amateras.sourceforge.jp/mvn/",
    resolvers += "jitpack" at "https://jitpack.io",
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1",
      "com.lihaoyi"  %%% "scalatags"   % "0.6.3",
      "com.lihaoyi"  %%% "upickle"     % "0.4.4",
      // "com.github.karasiq" %%% "scalajs-marked" % "1.0.2",
      "com.scalawarrior" %%% "scalajs-ace" % "0.0.4" //,
      //  "com.github.kindlychung" % "sjs-katex" % "0.1"
    )
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(coreJS)

lazy val core = (crossProject.crossType(CrossType.Pure) in file("core"))
  .settings(commonSettings: _*)
  .settings(name := "ProvingGround-Core")
  .settings(
    libraryDependencies ++= Seq(
//      "com.lihaoyi" %%% "upickle" % "0.3.4"
    ))
  .jsConfigure(_ enablePlugins ScalaJSWeb)
//  .jsSettings(sourceMapsBase := baseDirectory.value / "..")

lazy val coreJVM = core.jvm
lazy val coreJS  = core.js

lazy val server = (project in file("server"))
  .settings(commonSettings: _*)
  .settings(
    name := "provingground-server",
    // scalaVersion := scalaV,
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    // triggers scalaJSPipeline when using compile or continuous compilation
    // compile in Compile <<= (compile in Compile) dependsOn scalaJSPipeline,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"       % "10.0.0",
      "com.vmunier"       %% "scalajs-scripts" % "1.1.0",
      "com.simianquant"   %% "ammonite-kernel" % "0.3.0",
      "de.heikoseeberger" %% "akka-sse"        % "2.0.0",
      "com.typesafe.akka" %% "akka-actor"      % akkaV,
      "com.typesafe.akka" %% "akka-stream"     % akkaV,
      "com.github.scopt"  %% "scopt"           % "3.5.0"
    ),
    resources in Compile += (fastOptJS in (client, Compile)).value.data
    // WebKeys.packagePrefix in Assets := "public/",
    // managedClasspath in Runtime += (packageBin in Assets).value,
    // Compile the project before generating Eclipse files, so that generated .scala or .class files for Twirl templates are present

    // EclipseKeys.preTasks := Seq(compile in Compile)
  )
  .enablePlugins(JavaAppPackaging, UniversalPlugin)
  .dependsOn(coreJVM)

// lazy val functionfinder = project
//   .settings(commonSettings: _*)
//   .settings(name := "ProvingGround-FunctionFinder")
//   .dependsOn(coreJVM)

lazy val mizar = project
  .settings(commonSettings: _*)
  .settings(jvmSettings: _*)
  .settings(name := "Mizar-Parser",
            libraryDependencies += "com.lihaoyi" %% "fastparse" % "0.4.3")

val initCommands =
  """import provingground._, HoTT._, induction._, ammonite.ops._, translation.FansiShow._; repl.pprinter.bind(fansiPrint)"""

lazy val mantle = (project in file("mantle"))
  .settings(
    name := "ProvingGround-mantle",
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline)
    //  libraryDependencies += "com.lihaoyi" % "ammonite" % ammV cross CrossVersion.full
  )
  .settings(commonSettings: _*)
  .settings(jvmSettings: _*)
//        .settings(serverSettings : _*)
  .settings(sourceGenerators in Test += Def.task {
    val file = (sourceManaged in Test).value / "amm.scala"
    IO.write(
      file,
      s"""object amm extends App { ammonite.Main("$initCommands").run() }""")
    Seq(file)
  }.taskValue)
  .dependsOn(coreJVM)
  // .dependsOn(functionfinder)
  .dependsOn(server)
  // .dependsOn(translation)
  .enablePlugins(SbtWeb, TutPlugin)
//        dependsOn(deepwalk).
//        dependsOn(exploring)

lazy val exploring = project
  .settings(name := "ProvingGround-exploring",
            libraryDependencies += "com.lihaoyi" %% "ammonite-ops" % ammV)
  .dependsOn(coreJVM)
  .dependsOn(mantle)
  .enablePlugins(JavaAppPackaging, UniversalPlugin)

val nlpInitCommands =
  "import scala.collection.JavaConversions._, provingground._, PennTrees._, cats._, cats.implicits._, translation._, Functors._, SubTypePattern._, TreeToMath._, StanfordParser._"

lazy val nlp = (project in file("nlp"))
  .settings(name := "ProvingGround-NLP")
  .settings(commonSettings: _*)
  .settings(nlpSettings: _*)
  .settings(sourceGenerators in Test += Def.task {
    val file = (sourceManaged in Test).value / "amm.scala"
    IO.write(
      file,
      s"""object amm extends App { ammonite.Main("$nlpInitCommands").run() }""")
    Seq(file)
  }.taskValue)
//        .settings(jvmSettings : _*)
  .dependsOn(coreJVM)

// lazy val translation = (project in file("translation"))
//   .settings(name := "ProvingGround-Translation",
//             libraryDependencies += "com.lihaoyi" %% "ammonite-ops" % ammV)
//   .settings(baseSettings: _*)
//   .dependsOn(coreJVM)

// lazy val deepwalk = (project in file("deepwalk"))
//   .settings(
//     name := "DeepWalk4s",
//     classpathTypes += "maven-plugin",
//     libraryDependencies ++= Seq(
//       "org.deeplearning4j" % "deeplearning4j-core"  % "0.7.2",
//       "org.deeplearning4j" % "deeplearning4j-graph" % "0.7.2",
//       "org.nd4j"           % "nd4j-native-platform" % "0.7.2",
//       "org.deeplearning4j" % "deeplearning4j-nlp"   % "0.7.2",
// //              "org.deeplearning4j" % "deeplearning4j-ui" % "0.7.2",
//       "org.nd4j"    % "nd4j-native"   % "0.7.2",
//       "com.lihaoyi" % "ammonite"      % ammV % "test" cross CrossVersion.full,
//       "com.lihaoyi" %% "ammonite-ops" % ammV
//     )
//   )
//   .settings(baseSettings: _*)
//   .settings(initialCommands in (Test, console) :=
//     s"""ammonite.Main("import scala.collection.JavaConversions._").run() """)

// lazy val playServer = (project in file("play-server"))
//   .enablePlugins(PlayScala)
//   .settings(name := "ProvingGround-Play-Server")
//   .settings(commonSettings: _*)
//   .settings(jvmSettings: _*)
//   .settings(serverSettings: _*)
//   .settings(
//     libraryDependencies += specs2 % Test,
//     resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
//     TwirlKeys.templateImports += "controllers._")
//   .aggregate(jsProjects.map(projectToRef): _*)
//   .dependsOn(coreJVM)
//   .dependsOn(functionfinder)
//   .dependsOn(andrewscurtis)
//   .dependsOn(mantle)
//   .dependsOn(nlp)
//   .enablePlugins(SbtWeb)

lazy val realfunctions = (project in file("realfunctions"))
  .settings(commonSettings: _*)
  .settings(jvmSettings: _*)
  .settings(
    //       libraryDependencies  ++= Seq(
    // // other dependencies here
    // //"org.scalanlp" %% "breeze" % "0.11.2",
    // // native libraries are not included by default. add this if you want them (as of 0.7)
    // // native libraries greatly improve performance, but increase jar sizes.
    // //"org.scalanlp" %% "breeze-natives" % "0.11.2"
    // ),
    // resolvers ++= Seq(
    //   // other resolvers here
    //   // if you want to use snapshot builds (currently 0.12-SNAPSHOT), use this.
    //   "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    //   "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
    //   ),
    name := "RealFunctions")

// lazy val digressions = (project in file("digressions"))
//   .settings(commonSettings: _*)
//   .settings(digressionSettings: _*)
//   .dependsOn(coreJVM)
//   .dependsOn(playServer)
//   .dependsOn(functionfinder)

EclipseKeys.skipParents in ThisBuild := false

// unmanagedBase in Compile <<= baseDirectory(_ / "scalalib")

lazy val andrewscurtis = (project in file("andrewscurtis"))
  .settings(commonSettings: _*)
  .settings(jvmSettings: _*)
  .settings(acSettings: _*)
  .dependsOn(coreJVM)
  .dependsOn(mantle)

lazy val normalform = (project in file("normalform"))
  .settings(commonSettings: _*)
  .settings(jvmSettings: _*)
  .settings(nfSettings: _*)

fork in run := true
