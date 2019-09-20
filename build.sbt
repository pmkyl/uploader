name := "select_and_upload"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.25",
  "com.microsoft.sqlserver" % "mssql-jdbc" % "7.2.2.jre8",
  "mysql" % "mysql-connector-java" % "8.0.17",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.decodified" %% "scala-ssh" % "0.9.0",
  // Start with this one
  "org.tpolecat" %% "doobie-core"      % "0.8.2",
  "io.monix" %% "monix" % "3.0.0",
  // And add any of these as needed
  "org.tpolecat" %% "doobie-h2"        % "0.8.2",          // H2 driver 1.4.199 + type mappings.
  "org.tpolecat" %% "doobie-hikari"    % "0.8.2",          // HikariCP transactor.
  "org.tpolecat" %% "doobie-postgres"  % "0.8.2",          // Postgres driver 42.2.8 + type mappings.
  "org.tpolecat" %% "doobie-quill"     % "0.8.2",          // Support for Quill 3.4.4
  "org.tpolecat" %% "doobie-specs2"    % "0.8.2" % "test", // Specs2 support for typechecking statements.
  "org.tpolecat" %% "doobie-scalatest" % "0.8.2" % "test"  // ScalaTest support for typechecking statements.

)
