package cz.zcu.students.kiwi;

import cz.zcu.students.kiwi.snmp.OIDDict;
import cz.zcu.students.kiwi.snmp.SnmpClient;
import cz.zcu.students.kiwi.snmp.routine.Column;
import cz.zcu.students.kiwi.snmp.routine.TableWalk;
import org.apache.log4j.Logger;

import java.io.IOException;

public class SnmpDemoMain {
    private static final Logger log = Logger.getLogger(SnmpDemoMain.class);

    private static void printHelp(String err) {
        if (err != null) {
            System.out.println(err);
        }

        System.out.println("Usage: program host [community]");
        System.out.println("  host = ip or hostname of SNMP server");
        System.out.println("  community = SNMP group of objects, default='public' ");
    }

    public static void main(String[] args) {
        MainLog.configureLogging();

        // todo: enhance CLI
        if (args.length < 1) {
            printHelp("Not enough arguments");
            return;
        }

        String ip = args[0];
        String protocol = "udp";
        int port = 161;
        String community = (args.length >= 2) ? args[1] : "public";

        String connectString = protocol + ":" + ip + "/" + port;

        log.info("Connecting to: " + connectString + ", community: " + community);
        try (SnmpClient client = new SnmpClient(connectString, community, true)) {
            log.info("Connected");

            Column[] columns = {
                    new Column(2, "iface", 10, "Interface number"),
                    new Column(8, "type", "ipRouteType"),
                    new Column(1, "dest", 15, "ipDest"),
                    new Column(11, "mask", 15, "ipRouteMask"),
            };

            long start = System.currentTimeMillis();
            TableWalk tableWalk = OIDDict.RoutingTable();
            tableWalk.setColumns(columns);

            int itemsWalked = tableWalk.run(client, System.out);
            log.info("Total items walked: " + itemsWalked + " in " + (System.currentTimeMillis() - start) + "ms");
        } catch (IOException ex) {
            log.warn("Snmp walk failed" + ex.toString());
        }

    }
}
