package com.github.inkspect

import io.shiftleft.codepropertygraph.generated.{Cpg, NodeTypes}
import io.shiftleft.codepropertygraph.generated.nodes.{Method, Mynodetype}
import io.shiftleft.semanticcpg.language.*
import flatgraph.help.{Doc, DocSearchPackages, Traversal, TraversalSource}

import scala.jdk.CollectionConverters.IteratorHasAsScala
import io.joern.dataflowengineoss.queryengine.EngineContext

package object java_cli {

  // provides package names to search for @Doc annotations etc
  implicit val docSearchPackages: DocSearchPackages =
    Cpg.defaultDocSearchPackage
      .withAdditionalPackage(this.getClass.getPackageName)

  /** Example of a custom language step
    */
  implicit class MynodetypeSteps(val traversal: Iterator[Mynodetype]) extends AnyVal {
    def myCustomStep: Iterator[Mynodetype] = {
      println("custom step executed")
      traversal
    }
  }

  @Traversal(elementType = classOf[Method])
  implicit class CustomMethodSteps(val traversal: Iterator[Method]) extends AnyVal {
    @Doc(info = "custom step on method as an example", longInfo = "a veeery long description again")
    def customMethodStep: Iterator[String] =
      traversal.flatMap(_.parameter.name)
  }

  /** Example implicit conversion that forwards to the `StandaloneStarters` class
    */
  implicit def toStandaloneStarters(cpg: Cpg): StandaloneStarters =
    new StandaloneStarters(cpg)

  /** Example of custom node type starters */
  @TraversalSource
  class StandaloneStarters(cpg: Cpg) {
    @Doc(info = "custom starter step as an example", longInfo = "a veeery long description")
    def customStarterStep: Iterator[String] =
      cpg.method.parameter.name
  }

  implicit def context(implicit cpg: Cpg): EngineContext = EngineContext()

}
