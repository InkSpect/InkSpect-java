package com.github.inkspect.java_cli.domain.queries

// import io.joern.scanners.*
import io.shiftleft.semanticcpg.language.*
import io.joern.console.*
import io.joern.macros.QueryMacros.*

object DangerousFunctions extends QueryBundle {

  implicit val resolver: ICallResolver = NoResolve

  @q
  def execUsed(): Query =
    Query.make(
      name = "call-to-exec",
      author = "0Xdbe",
      title = "Dangerous function 'java.lang.Runtime.exec:java.lang.Process(java.lang.String)' used",
      description = """
        | A call to the function `java.lang.Runtime.exec:java.lang.Process(java.lang.String)` 
        | could result in a potential remote code execution.
        |""".stripMargin,
      score = 8,
      withStrRep({ cpg =>

        println("=====================")
        println("execUsed:")

        cpg.call
          .name("exec")
          .foreach(println)

        cpg.call
          .name("exec")

        // cpg.method("java.lang.Runtime.exec").foreach(println)
        // cpg.method("java.lang.Runtime.exec").callIn
      }),
      tags = List("badfn", "default")
    )
}
