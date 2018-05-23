package cz.zcu.students.kiwi;

import cz.zcu.students.kiwi.snmp.OIDDict;
import cz.zcu.students.kiwi.snmp.SnmpClient;
import cz.zcu.students.kiwi.snmp.SnmpColumn;
import cz.zcu.students.kiwi.snmp.SnmpTableWalk;

import java.io.IOException;

public class SnmpDemoMain {
    public static void main(String[] args) {

        try (SnmpClient client = new SnmpClient("udp:127.0.0.1/161", true)) {
            SnmpColumn[] columns = {
                    new SnmpColumn(2, "I-face", 10),
                    new SnmpColumn(8, "ipRouteType"),
                    new SnmpColumn(1, "ipDest", 15),
                    new SnmpColumn(11, "ipRouteMask", 15),
            };

            SnmpTableWalk tableWalk = new SnmpTableWalk(client, OIDDict.RoutingTable());
            tableWalk.setColumns(columns);

            tableWalk.walk(System.out);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
