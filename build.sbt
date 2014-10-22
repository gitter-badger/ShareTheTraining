name := """ShareTheTraining"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.3"

libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "4.3.6.Final",
  cache,
  javaWs,
  "org.xerial" % "sqlite-jdbc" % "3.7.2",
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "org.apache.solr" % "solr-solrj" % "4.10.1",
  "com.typesafe.play.plugins" %% "play-plugins-mailer" % "2.3.0",
  "mysql" % "mysql-connector-java" % "5.1.33",
  "ws.securesocial" %% "securesocial" % "master-SNAPSHOT"
)

resolvers += Resolver.sonatypeRepo("snapshots")

javaOptions in Test ++= Seq(
  "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=9998",
  "-Xms512M",
  "-Xmx1536M",
  "-Xss1M",
  "-XX:MaxPermSize=384M"
)