package phenix

import phenix.containers.{ArgumentsConfig, FolderArguments}
import phenix.pilot.Orchestrator


// main function : use sbt run -i inputFolder -o outputFolder


object Main extends App {
  val argumentsConf = new ArgumentsConfig(args)

  val folderArguments = new FolderArguments(argumentsConf)

  Orchestrator.launchProcess(folderArguments)

}

