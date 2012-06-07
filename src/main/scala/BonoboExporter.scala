package com.novoda.bonobo.exporter

import org.specs2.main.Arguments
import org.specs2.specification.{ExecutedSpecification, ExecutingSpecification}

class BonoboExporter extends org.specs2.reporter.Exporter {

  def export(implicit args: Arguments) = (e: ExecutingSpecification) => {
    e.executed
  }

}
