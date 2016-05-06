lazy val buildSettings = Seq (
    scalaVersion := "2.11.7"
)
lazy val root = (project in file(".")).settings(buildSettings).dependsOn(chisel, firrtl, chiselTesters)

lazy val chisel = project in file("chisel3")
lazy val firrtl = project in file("firrtl")
lazy val chiselTesters = project in file("chisel-testers")
