import Chisel._
import firrtl._
import firrtl.interpreter._
import java.io._

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
object Driver extends App {
  //successful tests
  //new SampleDUTTester(Chisel.Driver.elaborateModule(() => new SampleDUT()), () => new SampleDUT())
  //new IOTestDUTTester(Chisel.Driver.elaborateModule(() => new IOTestDUT()), () => new IOTestDUT())

  //failed tests
  //new AccumulatorTests(Chisel.Driver.elaborateModule(() => new Accumulator()), () => new Accumulator())
  new LFSR16Tests(Chisel.Driver.elaborateModule(() => new LFSR16()), () => new LFSR16())
}
