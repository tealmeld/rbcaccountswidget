name := "rbcwidget"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.6"

lazy val dashboard = (project in file(".")).enablePlugins(PlayScala)

pipelineStages := Seq(uglify, digest, gzip)

pipelineStages in Assets := Seq()

DigestKeys.algorithms += "sha1"

UglifyKeys.uglifyOps := { js =>
  Seq((js.sortBy(_._2), "concat.min.js"))
}

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  "javax.inject" % "javax.inject" % "1",
  "org.scaldi" %% "scaldi-play" % "0.5.8",
  "org.webjars" % "bootstrap" % "3.3.4",
  "org.webjars" % "angularjs" % "1.3.15",
  "org.webjars" % "angular-ui-bootstrap" % "0.13.0",
  "org.mockito" % "mockito-core" % "1.10.19" % "test"
)

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "reactivemongo" % "0.11.6",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.6.play24"
)

resolvers ++= Seq(
  "Java.net Maven2 Repository"     at "http://download.java.net/maven/2/",
  "Sonatype scala-tools repo"      at "https://oss.sonatype.org/content/groups/scala-tools/",
  "Sonatype scala-tools releases"  at "https://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
    "org.specs2"                 %% "specs2"               % "2.4"             % "test",
    "org.scalatest"              %% "scalatest"            % "2.2.1"           % "test",
    "ch.qos.logback"             %  "logback-classic"      % "1.1.3",
    "org.slf4j"                  %  "slf4j-nop"            % "1.6.4"
)

libraryDependencies ++= {
  val akkaVersion = "2.3.9"
  Seq(
    "com.typesafe.akka"          %% "akka-actor"           % akkaVersion,
    "com.typesafe.akka"          %% "akka-remote"          % akkaVersion,
    "com.typesafe.akka"          %% "akka-slf4j"           % akkaVersion,
    "com.typesafe.akka"          %% "akka-testkit"         % akkaVersion       % "test"
  )
}

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.typesafe.play"   %% "play-slick"            % "1.1.0",
  "com.typesafe.play"   %% "play-slick-evolutions" % "1.1.0",
  "mysql"               %  "mysql-connector-java"  % "5.1.35",
  "org.flywaydb"        %  "flyway-core"           % "3.2.1"
)

libraryDependencies ++= {
  val slickVersion = "3.1.0"
  Seq(
    "com.typesafe.slick"         %% "slick"                % slickVersion      % "compile",
    "com.typesafe.slick"         %%  "slick-hikaricp"      % slickVersion      % "compile"
  )
}