import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnecter {
    public void socket() {
        while (true) {
            try {
                ServerSocket serverSocket = null;
                serverSocket = new ServerSocket(8112);
                System.out.println("접속 대기중 ~~~");
                Socket socket = serverSocket.accept();
                System.out.println("클라이언트 접속");

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(),"UTF-8")
                );

                String rev = br.readLine();
                System.out.println(rev);

                br.close();
                socket.close();
                serverSocket.close();
                System.out.println("종료");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
