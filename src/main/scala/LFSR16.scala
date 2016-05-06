import Chisel._
import Chisel.hwiotesters.AdvInterpretiveTester
import firrtl.interpreter._

class LFSR16 extends Module {
  val io = new Bundle {
    val inc = Bool(INPUT)
    val out = UInt(OUTPUT, 16)
  }
  val res = Reg(UInt(), init = UInt(1, 16))
  when (io.inc) { 
    val nxt_res = Cat(res(0)^res(2)^res(3)^res(5), res(15,1)) 
    res := nxt_res
  }
  io.out := res
}

class LFSR16Tests(c: () => LFSR16) {
  val x = new AdvInterpretiveTester(c) {
    var res = 1
    val rnd = scala.util.Random
    for (t <- 0 until 16) {
      val inc = rnd.nextInt(2)
      poke("io_inc", inc)
      step(1)
      if (inc == 1) {
        val bit = ((res >> 0) ^ (res >> 2) ^ (res >> 3) ^ (res >> 5)) & 1;
        res = (res >> 1) | (bit << 15);
      }
      expect("io_out", res)
    }
  }
}
