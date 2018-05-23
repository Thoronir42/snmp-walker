package cz.zcu.students.kiwi.snmp;

import org.snmp4j.smi.OID;

public class OIDDict {
    public static OID RoutingTable() {
        return new OID("1.3.6.1.2.1.4.21.1");
    }
}
