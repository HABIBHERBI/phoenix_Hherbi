package phenix.containers

import java.nio.file.{Path, Paths}

import org.rogach.scallop.{ScallopConf, ScallopOption}


//CLI arg parsing

class ArgumentsConfig(arguments: Seq[String]) extends ScallopConf(arguments) {



  val inputFolder: ScallopOption[String] = opt[String](
    default = Some( Paths.get("","src/main/resources/data"  ).toAbsolutePath.toString  ),
    descr = "The folder which contains the data files. FAILS if it does not exist",
    required = true,
    validate = { checkFolderExistence }
  )

  val outputFolder: ScallopOption[String] = opt[String](default = Some(  Paths.get("","phenix-output"  ).toAbsolutePath.toString), descr = "The folder where you want the results files to go. If not mentioned, will put the results in ./phenix-output")

  val simpleCalc: ScallopOption[Boolean] = opt[Boolean](default = Some(true), descr = "Week calculations won't be triggered if this flag is set")

  def checkFolderExistence(path: String): Boolean = {
    Paths.get(path).toAbsolutePath
      .toFile
      .isDirectory
  }

  verify()
}




case class FolderArguments(inputFolder: Path, outputFolder: Path, simpleCalc: Boolean) {
  /**
    * Alternative constructor using argumentsConfig directly.
    * @param argumentsConfig VALIDATED scallop arguments config
    * @return
    */
  def this(argumentsConfig: ArgumentsConfig) = this (
    Paths.get(argumentsConfig.inputFolder()),
    Paths.get(argumentsConfig.outputFolder()),
    argumentsConfig.simpleCalc()
  )
}