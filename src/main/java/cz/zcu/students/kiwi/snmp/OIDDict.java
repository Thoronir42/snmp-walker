package cz.zcu.students.kiwi.snmp;

import cz.zcu.students.kiwi.snmp.routine.TableWalk;
import org.snmp4j.smi.OID;

public class OIDDict {
    public static TableWalk RoutingTable() {
        return new TableWalk(new OID("1.3.6.1.2.1.4.21.1"), "Routing table");
    }
}
