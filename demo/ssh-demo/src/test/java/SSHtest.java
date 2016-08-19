import net.schmizz.keepalive.KeepAliveProvider;
import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;

public class SSHtest {
 
    public static void main(String[] args) throws Exception {

        // init
        DefaultConfig defaultConfig = new DefaultConfig();
        defaultConfig.setKeepAliveProvider(KeepAliveProvider.KEEP_ALIVE);
        SSHClient ssh = new SSHClient(defaultConfig);

        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        ssh.connect(Test.host, Test.port);
        ssh.getConnection().getKeepAlive().setKeepAliveInterval(5); // every 60sec
        ssh.authPassword(Test.username, Test.password);


        // run
        {
            Session session = ssh.startSession();
            Command cmd = session.exec("pwd");
            System.out.println("host " + ssh.getRemoteHostname() + " out put:\n" + IOUtils.readFully(cmd.getInputStream()).toString());
            session.close();
        }
        {
            Session session = ssh.startSession();
            Command cmd = session.exec("ls");
            System.out.println("host " + ssh.getRemoteHostname() + " out put:\n" + IOUtils.readFully(cmd.getInputStream()).toString());
            session.close();
        }

        // close
        ssh.close();
    }
 
}