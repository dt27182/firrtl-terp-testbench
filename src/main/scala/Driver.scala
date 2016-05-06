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
  new SampleDUTTester(Chisel.Driver.elaborateModule(() => new SampleDUT()), () => new SampleDUT())
  //new LFSR16Tests(() => new LFSR16())
  //println(genFirrtlIRString(() => new SampleDUT(), "SampleDUT"))
  //runClassicTester(() => new SampleDUT(), verilogFilePath) {c => new SampleDUTTester(c)}
}
