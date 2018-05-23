package cz.zcu.students.kiwi.snmp;

import java.io.IOException;
import java.io.PrintStream;

public abstract class SnmpRoutine {
    public abstract int run(SnmpClient client, PrintStream stream) throws IOException;
}
