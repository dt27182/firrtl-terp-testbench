import Chisel._
import Chisel.iotesters._

class Hello extends Module {
  val io = new Bundle { 
    val out = UInt(OUTPUT, width = 8)
    val in = UInt(INPUT, width = 8)
  }
  val accumulator = Reg(UInt(), init=UInt(42, width=8))
  io.out := accumulator
  accumulator := io.in
}

class HelloTests(c: Hello, b: Option[Backend] = None) extends ClassicTester(c, _backend = b) {
  expect(c.io.out, 42)
  poke(c.io.in, 1)
  step(1)
  expect(c.io.out, 1)
}

