package cz.zcu.students.kiwi.snmp;

import org.apache.log4j.Logger;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

public class SnmpClient implements AutoCloseable {
    private static final Logger log = Logger.getLogger(SnmpClient.class);

    private Snmp snmp;

    private final String connectString;
    private OctetString community;

    public SnmpClient(String connectString, boolean autoConnect) throws IOException {
        this(connectString, "public", autoConnect);
    }

    public SnmpClient(String connectString, String community, boolean autoConnect) throws IOException {
        this.connectString = connectString;
        this.community = new OctetString(community);
        if (autoConnect) {
            this.connect();
        }
    }

    public void connect() throws IOException {
        DefaultUdpTransportMapping transport = new DefaultUdpTransportMapping();
        this.snmp = new Snmp(transport);


        transport.listen();
    }


    public ResponseEvent get(OID... oids) throws IOException {
        PDU pdu = createPDU(PDU.GET, oids);

        return send(pdu, getTarget(), null, "GET");
    }

    public ResponseEvent getNext(OID... oids) throws IOException {
        PDU pdu = createPDU(PDU.GETNEXT, oids);

        return send(pdu, getTarget(), null, "GETNEXT");
    }

    public void close() throws IOException {
        log.info("Closing connection");
        this.snmp.close();
        log.debug("Closed");
    }

    private ResponseEvent send(PDU pdu, Target target, TransportMapping mappings, String method) throws IOException {
        log.info(method + " with " + pdu.size() + " OIDs");

        long start = System.currentTimeMillis();
        ResponseEvent event = snmp.send(pdu, target, mappings);
        long stop = System.currentTimeMillis();
        log.debug(method + " response received after " + (stop - start) + "ms");
        if (event == null) {
            throw new IOException(method + " timed out");
        }

        return event;
    }

    private PDU createPDU(int pduType, OID... oids) {
        log.info("Creating PDU with type " + pduType + " for " + oids.length + " OIDs");
        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
            log.debug("- " + oid.toDottedString());
        }
        pdu.setType(pduType);

        return pdu;
    }

    private Target getTarget() {
        CommunityTarget target = new CommunityTarget();

        target.setAddress(GenericAddress.parse(this.connectString));
        target.setCommunity(community);

        target.setRetries(2);
        target.setTimeout(1500);

        target.setVersion(SnmpConstants.version2c);

        return target;
    }

}
