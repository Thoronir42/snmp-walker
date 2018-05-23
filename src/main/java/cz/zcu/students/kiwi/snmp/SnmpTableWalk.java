package cz.zcu.students.kiwi.snmp;

import org.apache.log4j.Logger;
import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;

import java.io.IOException;
import java.io.PrintStream;

public class SnmpTableWalk {
    private static Logger log = Logger.getLogger(SnmpTableWalk.class);

    private static final int MAX_TABLE_VALUES = 32;

    private final SnmpClient snmp;
    private final OID tableOid;
    private final int tableOidLength;

    private SnmpColumn[] columns;

    public SnmpTableWalk(SnmpClient snmp, OID tableOid) {
        this.snmp = snmp;
        this.tableOid = tableOid;
        this.tableOidLength = tableOid.getValue().length;

        log.info("Initialized table with OID: " + tableOid.toDottedString());
    }

    public SnmpTableWalk setColumns(SnmpColumn... columns) {
        this.columns = columns;
        log.info("Assigned " + columns.length + " columns");
        for (SnmpColumn column : columns) {
            log.debug("- Column OID:" + column.getChildOid() + ", caption: " + column.getCaption());
        }

        return this;
    }

    public int walk(PrintStream out) throws IOException {
        log.info("walk() starting");

        log.debug("printing table head");
        out.print("|  #|");
        OID[] colOids = new OID[columns.length];
        for (int c = 0; c < columns.length; c++) {
            colOids[c] = new OID(tableOid).append(columns[c].getChildOid());
            out.format(" " + columns[c].format(columns[c].getCaption()) + " |");
        }
        out.println();

        OID breakerOID = colOids[0];

        int n = 0;
        log.info("Starting to query and list table items");
        while (n < MAX_TABLE_VALUES) {
            ResponseEvent responseEvent = snmp.getNext(colOids);
            PDU responsePdu = responseEvent.getResponse();
            if (responseEvent == null || responsePdu.get(0) == null) {
                log.warn("Response PDU is empty");
                break;
            }

            OID respOID = responsePdu.get(0).getOid();

            // table = 1.2.4 (tableOidLength)
            // desired oid = 1.2.4.X
            // if result oid does not equal expected oid, the value does not exist
            if (breakerOID.leftMostCompare(this.tableOidLength + 1, respOID) != 0) {
                log.info("Response OID is not same as expected");
                break;
            }

            printRow(out, colOids, n, responsePdu);

            n++;
        }

        return n;
    }

    private void printRow(PrintStream out, OID[] colOids, int n, PDU responsePdu) {
        log.info("Printing row " + n);

        out.format("|%3d|", n);

        for (int c = 0; c < columns.length; c++) {
            String value = responsePdu.get(c).getVariable().toString();
            out.print(" " + columns[c].format(value) + " |");

            // assign OID to get nextValue
            colOids[c] = responsePdu.get(c).getOid();
        }

        out.println();
    }
}
