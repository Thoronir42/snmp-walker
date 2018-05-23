package cz.zcu.students.kiwi.snmp;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

public class SnmpClient implements AutoCloseable {

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

        ResponseEvent event = snmp.send(pdu, getTarget(), null);
        if (event == null) {
            throw new RuntimeException("GET timed out");
        }

        return event;
    }

    public ResponseEvent getNext(OID... oids) throws IOException {
        PDU pdu = createPDU(PDU.GETNEXT, oids);

        ResponseEvent event = snmp.send(pdu, getTarget(), null);
        if (event == null) {
            throw new RuntimeException("GETNEXT timed out");
        }

        return event;
    }

    public void close() throws IOException {
        this.snmp.close();
    }


    private PDU createPDU(int pduType, OID... oids) {
        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
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
