package phenix.containers

import java.nio.file.Path

abstract class FileOutputName {
  val outputName: String
}

case class FileOutput(outputName: String, fileContentOutput: Stream[String]) extends FileOutputName

case class KpiOutput(outputPath: Path, fileOutputs: Stream[FileOutput])