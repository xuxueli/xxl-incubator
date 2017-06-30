package com.tmp.demo.ssh;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.util.io.NoCloseInputStream;
import org.apache.sshd.common.util.io.NoCloseOutputStream;

import java.net.InetSocketAddress;
import java.util.Arrays;

public class Test {
    public static String host = "127.0.0.1";
    public static int port = 22;
    public static String username = "root";
    public static String password = "xuxueli";

    public static void main(String[] args) {
        try {
            testSsh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testSsh() throws Exception {
        SshClient client = SshClient.setUpDefaultClient();
        client.start();

        ClientSession session = client.connect(username, new InetSocketAddress(host, port)).getSession();
        session.addPasswordIdentity(password);
        session.auth();

        ClientChannel channel = session.createExecChannel("ifconfig");
        System.out.println(channel.getStreaming());
        //ClientChannel channel =session.createShellChannel();
        channel.setOut(new NoCloseOutputStream(System.out));
        channel.setErr(new NoCloseOutputStream(System.err));
        channel.setIn(new NoCloseInputStream(System.in));
        channel.open();

        channel.waitFor(Arrays.asList(ClientChannelEvent.CLOSED), 0);
        channel.close(false);
        session.close(false);
        client.stop();
    }
}
