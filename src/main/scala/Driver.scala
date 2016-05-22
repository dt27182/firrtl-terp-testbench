import Chisel._
import firrtl._
import firrtl_interpreter._
import Chisel.iotesters._
import java.io._
/*
object genFirrtlIRString {
  def apply(dutGen: ()=> Chisel.Module, dutName: String): String = {
    val dutModule = Chisel.Driver.emit(dutGen)
    /*val rootDir = new File(".").getCanonicalPath()
    val buildDir = s"${rootDir}"
    val verilogFilePath = s"${buildDir}/${dutName}.v"

    // Parse circuit into FIRRTL
    val circuit = firrtl.Parser.parse(dutModule.split("\n"))

    val writer = new PrintWriter(new File(verilogFilePath))
    // Compile to verilog
    firrtl.VerilogCompiler.run(circuit, writer)
    writer.close()

    verilogFilePath*/
    dutModule
  }
}
*/

object genCpp {
  def apply(dutGen: ()=> Chisel.Module, dutName: String) = {
    val rootDirPath = new File(".").getCanonicalPath()
    val testDirPath = s"${rootDirPath}/test_run_dir"

    val circuit = Chisel.Driver.elaborate(dutGen)
    // Dump FIRRTL for debugging
    val firrtlIRFilePath = s"${testDirPath}/${circuit.name}.ir"
    Chisel.Driver.dumpFirrtl(circuit, Some(new File(firrtlIRFilePath)))
    // Parse FIRRTL
    //val ir = firrtl.Parser.parse(Chisel.Driver.emit(dutGen) split "\n")
    // Generate Verilog
    val verilogFilePath = s"${testDirPath}/${circuit.name}.v"
    //val v = new PrintWriter(new File(s"${dir}/${circuit.name}.v"))
    firrtl.Driver.compile(firrtlIRFilePath, verilogFilePath, new firrtl.VerilogCompiler())

    val verilogFileName = verilogFilePath.split("/").last
    val cppHarnessFileName = "classic_tester_top.cpp"
    val cppHarnessFilePath = s"${testDirPath}/${cppHarnessFileName}"
    val cppBinaryPath = s"${testDirPath}/V${dutName}"
    val vcdFilePath = s"${testDirPath}/${dutName}.vcd"

    copyVerilatorHeaderFiles(testDirPath)

    genVerilatorCppHarness(dutGen, verilogFileName, cppHarnessFilePath, vcdFilePath)
    Chisel.Driver.verilogToCpp(verilogFileName.split("\\.")(0), new File(testDirPath), Seq(), new File(cppHarnessFilePath)).!
    Chisel.Driver.cppToExe(verilogFileName.split("\\.")(0), new File(testDirPath)).!
    cppBinaryPath
  }
}

object Driver extends App {
  val cppFilePath = genCpp(() => new Accumulator(), "Accumulator")
  runClassicTesterWithVerilatorBinary(() => new Accumulator(), cppFilePath) {(c, p) => new AccumulatorTests(c,p)}
  chiselMainTest(Array("--test", "--backend", "c", "--compile", "--genHarness"), () => new Accumulator()) {(c) => new AccumulatorTests(c)}
  assert(runClassicTester("verilator", () => new Accumulator()) {(c, p) => new AccumulatorTests(c,p)})
  assert(runClassicTester("firrtl", () => new Accumulator()) {(c, p) => new AccumulatorTests(c,p)})
  assert(runClassicTester("firrtl", () => new Hello()) {(c, p) => new HelloTests(c,p)})
}
