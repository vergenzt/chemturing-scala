chemturing-scala
================

A Scala version of the ChemTuring chemical operating system simulator.

## Running

Currently the only way to interact with the simulator is through an interactive Scala REPL. To run this, run:

```
$ cd chemturing-scala
$ ./gradlew installApp

(gradle output...)

BUILD SUCCESSFUL

Total time: ...
```

```
$ ./build/install/chemturing/bin/chemturing
Welcome to ChemTuring v3.0!
 [Using Scala version 2.11.1 on Java HotSpot(TM) 64-Bit Server VM, Java 1.6.0_65]
Type in expressions to have them evaluated.
Type :help for more information.

The following classes are imported automatically:
  State
  Mode
    EXEC, KILL
  Operation
    READ, WRITE, SKIP, MSKIP

==>
==> // You can instantiate a State here. The only required argument is the
==> // first one, data, which is a Seq[Boolean]. There is an implicit utility
==> // class in scope that allows you to generate a Seq[Boolean] from a binary
==> // string by typing seq"101101...". All other properties of State have
==> // default values.
==>
==> var state = State(seq"100101011100")
state: com.timvergenz.chemturing.core.State = State(100101011100, progPtr=00, dataPtr=00, mem=0, mode=EXEC, prep=false, m=1)
==>
==> // You can also specify the other values with keywords.
==>
==> state = State(seq"100101011100", dataPtr=1)
state: com.timvergenz.chemturing.core.State = State(100101011100, progPtr=00, dataPtr=01, mem=0, mode=EXEC, prep=false, m=1)
==>
==> // You can get the next state by calling .next on a State variable.
==>
==> state.next
res0: com.timvergenz.chemturing.core.State = State(100101011100, progPtr=02, dataPtr=02, mem=0, mode=EXEC, prep=true, m=1)
==> state.next.next
res1: com.timvergenz.chemturing.core.State = State(100101011100, progPtr=04, dataPtr=03, mem=0, mode=EXEC, prep=false, m=1)
==>
==> // You can also just start the line with a dot to use the previous result value
==>
==> .next // == state.next.next.next
res2: com.timvergenz.chemturing.core.State = State(100101011100, progPtr=06, dataPtr=04, mem=0, mode=EXEC, prep=false, m=1)
```

## Eclipse setup

Clone the project and run `./gradlew eclipse` to generate an Eclipse project that you can import into an existing workspace.

## Tests

To run tests, run `./gradlew test`.

