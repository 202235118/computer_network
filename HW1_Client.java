package HW1;

import java.io.*;
import java.net.*;
import java.util.*;

public class HW1_Client {
    public static void main(String[] args) {
    	// Configuration 객체를 생성하여 서버 정보 가져옴
    	Configuration config = new Configuration("server_info.dat");

        try (Socket socket = new Socket(config.getServerIP(), config.getServerPort());
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            while (true) {
            	// 사용자로부터 산술 표현식 입력 받기
                System.out.print("Enter arithmetic expression (e.g., ADD 10 20): ");
                String expression = reader.readLine();

                // 서버로 표현식 전송
                writer.println(expression);

             // 서버로부터의 응답 받기
                String response;
                while ((response = serverReader.readLine()) != null) {
                    System.out.println(response);
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Configuration {
    private String serverIP;
    private int serverPort;

    public Configuration(String configFile) {
    	// 파일을 읽어와 서버 정보 가져옴
        loadConfiguration(configFile);
    }

    private void loadConfiguration(String configFile) {
        File file = new File(configFile);

        // 파일이 존재하지 않으면 기본값 사용
        if (!file.exists()) {
            serverIP = "localhost";
            serverPort = 1115;
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
        	// 파일에서 IP와 포트 번호 읽기
            serverIP = scanner.nextLine();
            serverPort = Integer.parseInt(scanner.nextLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }
}
