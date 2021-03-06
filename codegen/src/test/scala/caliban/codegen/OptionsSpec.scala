package caliban.codegen

import caliban.codegen.Options.Header
import zio.test.Assertion._
import zio.test._
import zio.test.environment.TestEnvironment

object OptionsSpec extends DefaultRunnableSpec {
  override def spec: ZSpec[TestEnvironment, Any] =
    suite("OptionsSpec")(
      test("full arguments") {
        val input  = List("schema", "output", "--scalafmtPath", "fmtPath", "--headers", "header1:value1,header2:value2")
        val result = Options.fromArgs(input)
        assert(result)(
          equalTo(
            Some(
              Options(
                "schema",
                "output",
                Some("fmtPath"),
                Some(List(Header("header1", "value1"), Header("header2", "value2"))),
                None
              )
            )
          )
        )
      },
      test("full arguments (--headers option first)") {
        val input  = List("schema", "output", "--headers", "header1:value1,header2:value2", "--scalafmtPath", "fmtPath")
        val result = Options.fromArgs(input)
        assert(result)(
          equalTo(
            Some(
              Options(
                "schema",
                "output",
                Some("fmtPath"),
                Some(List(Header("header1", "value1"), Header("header2", "value2"))),
                None
              )
            )
          )
        )
      },
      test("minimum arguments") {
        val input  = List("schema", "output")
        val result = Options.fromArgs(input)
        assert(result)(
          equalTo(
            Some(
              Options("schema", "output", None, None, None)
            )
          )
        )
      },
      test("not enough arguments") {
        val input  = List("schema")
        val result = Options.fromArgs(input)
        assert(result)(equalTo(None))
      },
      test("--scalafmtPath value missing") {
        val input  = List("schema", "output", "--scalafmtPath", "--headers", "header1:value1,header2:value2")
        val result = Options.fromArgs(input)
        assert(result)(equalTo(None))
      },
      test("empty list") {
        val result = Options.fromArgs(Nil)
        assert(result)(equalTo(None))
      },
      test("provide package name") {
        val input  = List("schema", "output", "--packageName", "com.github.ghostdogpr")
        val result = Options.fromArgs(input)
        assert(result)(
          equalTo(
            Some(
              Options(
                "schema",
                "output",
                None,
                None,
                Some("com.github.ghostdogpr")
              )
            )
          )
        )
      }
    )
}
