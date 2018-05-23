package cz.zcu.students.kiwi;

import cz.zcu.students.kiwi.snmp.OIDDict;
import cz.zcu.students.kiwi.snmp.SnmpClient;
import cz.zcu.students.kiwi.snmp.routine.Column;
import cz.zcu.students.kiwi.snmp.routine.TableWalk;
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

            Column[] columns = {
                    new Column(2, "I-face", 10),
                    new Column(8, "ipRouteType"),
                    new Column(1, "ipDest", 15),
                    new Column(11, "ipRouteMask", 15),
            };

            long start = System.currentTimeMillis();
            TableWalk tableWalk = new TableWalk(OIDDict.RoutingTable());
            tableWalk.setColumns(columns);

            int itemsWalked = tableWalk.run(client, System.out);
            log.info("Total items walked: " + itemsWalked + " in " + (System.currentTimeMillis() - start) + "ms");
        } catch (IOException ex) {
            log.warn("Snmp walk failed" + ex.toString());
        }

    }
}
