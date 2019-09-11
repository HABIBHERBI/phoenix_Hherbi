package phenix.containers

import java.nio.file.Paths

import org.scalatest.{FlatSpec, Matchers}

class ArgumentsModelSpec extends FlatSpec with Matchers {
  val inputFolderPath : String = Paths.get("data/input/example").toAbsolutePath.toString
  val outputFolderPath : String = Paths.get("data/output").toAbsolutePath.toString

  "The Folder arguments" should "should be correctly constructed with ALL CLI arguments" in {
    // PREPARE
    val arguments = Seq(
      "-i", inputFolderPath,
      "-o", outputFolderPath
    )

    // EXECUTE
    val argumentsConf = new ArgumentsConfig(arguments)
    val folderArguments = new FolderArguments(argumentsConf)

    // ASSERT
    folderArguments.inputFolder.toAbsolutePath.toString shouldBe inputFolderPath
    folderArguments.outputFolder.toAbsolutePath.toString shouldBe outputFolderPath
    folderArguments.simpleCalc shouldBe true
  }

  "The Folder arguments" should "should have simple calculations disbaled by default" in {
    // PREPARE
    val arguments = Seq(
      "-i", inputFolderPath,
      "-o", outputFolderPath)

    // EXECUTE
    val argumentsConf = new ArgumentsConfig(arguments)
    val folderArguments = new FolderArguments(argumentsConf)

    // ASSERT
    folderArguments.simpleCalc shouldBe true
  }

  "The Folder arguments" should "should have phenix-output folder as default output" in {
    // PREPARE
    val arguments = Seq(
      "-i", inputFolderPath)

    // EXECUTE
    val argumentsConf = new ArgumentsConfig(arguments)
    val folderArguments = new FolderArguments(argumentsConf)

    // ASSERT
    folderArguments.outputFolder.getFileName.toString shouldBe "phenix-output"
  }
}
