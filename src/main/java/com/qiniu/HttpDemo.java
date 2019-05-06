package com.qiniu;

import org.xbill.DNS.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpDemo {

    public static void main(String[] args) throws Exception {
        Map<String, String> map = new HashMap<>();
        System.out.println(map.get(null));
//         query();
//        transferZone();
//        addRR();
    }

    // 查询
    static void query() throws IOException {
        Resolver resolver = new SimpleResolver("8.8.8.8");
        resolver.setPort(53);
        Lookup lookup = new Lookup("www.baidu.com", Type.A);
        lookup.setResolver(resolver);
        lookup.run();
        if (lookup.getResult() == Lookup.SUCCESSFUL) {
            System.out.println(lookup.getAnswers()[0].rdataToString());
        }
    }

    static void transferZone() throws Exception {
        ZoneTransferIn xfr = ZoneTransferIn.newAXFR(new Name("test.com."), "192.168.36.54", null);
        List records = xfr.run();
        Message response = new Message();
        response.getHeader().setFlag(Flags.AA);
        response.getHeader().setFlag(Flags.QR);
        // response.addRecord(query.getQuestion(),Section.QUESTION);
        Iterator it = records.iterator();
        while (it.hasNext()) {
            response.addRecord((Record) it.next(), Section.ANSWER);
        }
        System.out.println(response);
    }

    static void addRR() throws Exception {
        Name zone = Name.fromString("baidu.com.");
        Name host = Name.fromString("host", zone);
        Update update = new Update(zone, DClass.IN);
        Record record = new ARecord(host, DClass.IN, 3600, InetAddress.getByName("192.0.0.2"));
        update.add(record);
        Resolver resolver = new SimpleResolver("8.8.8.8");
        resolver.setPort(53);
        TSIG tsig = new TSIG("test_key", "epYaIl5VMJGRSG4WMeFW5g==");
        resolver.setTSIGKey(tsig);
        resolver.setTCP(true);
        Message response = resolver.send(update);
        System.out.println(response);
    }
}
