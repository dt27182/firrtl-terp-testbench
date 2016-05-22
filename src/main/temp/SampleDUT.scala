import Chisel._
import Chisel.hwiotesters.AdvInterpretiveTester

class SampleDUT extends Module {
  val io = new Bundle {
    val in = UInt(INPUT, width=65)
    val out = UInt(OUTPUT, width=65)
    val delayed_out = UInt(OUTPUT, width=65)
  }
  io.out := io.in
  val delayed_in = Reg(init=UInt(0))
  delayed_in := io.in
  io.delayed_out := delayed_in
}

class SampleDUTTester(c: SampleDUT, dutGenFunc: () => SampleDUT) extends AdvInterpretiveTester(dutGenFunc) {
  poke(c.io.in, 1)
  expect(c.io.out, 1)
  peek(c.io.delayed_out)
  poke(c.io.in, 2)
  step(1)
  expect(c.io.out, 2)
  expect(c.io.delayed_out, 2)
}
