package cz.zcu.students.kiwi;

import cz.zcu.students.kiwi.snmp.OIDDict;
import cz.zcu.students.kiwi.snmp.SnmpClient;
import cz.zcu.students.kiwi.snmp.SnmpColumn;
import cz.zcu.students.kiwi.snmp.SnmpTableWalk;
import org.apache.log4j.Logger;

import java.io.IOException;

public class SnmpDemoMain {
    private static final Logger log = Logger.getLogger(SnmpDemoMain.class);

    public static void main(String[] args) {
        MainLog.configureLogging();

        String ip = "127.0.0.1";
        String connectString = "udp:" + ip + "/161";
        String community = "public";

        log.info("Connecting to: " + connectString + ", community: " + community);
        try (SnmpClient client = new SnmpClient(connectString, community, true)) {
            log.info("Connected");

            SnmpColumn[] columns = {
                    new SnmpColumn(2, "I-face", 10),
                    new SnmpColumn(8, "ipRouteType"),
                    new SnmpColumn(1, "ipDest", 15),
                    new SnmpColumn(11, "ipRouteMask", 15),
            };

            long start = System.currentTimeMillis();
            SnmpTableWalk tableWalk = new SnmpTableWalk(client, OIDDict.RoutingTable());
            tableWalk.setColumns(columns);

            int itemsWalked = tableWalk.walk(System.out);
            log.info("Total items walked: " + itemsWalked + " in " + (System.currentTimeMillis() - start) + "ms");
        } catch (IOException ex) {
            log.warn("Snmp walk failed" + ex.toString());
        }

    }
}
