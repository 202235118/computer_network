package HW1;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HW1_Server {
    private static final int PORT = 1115;

    public static void main(String[] args) {
        try (ServerSocket listener = new ServerSocket(PORT)) {
            System.out.println("The calculator server is running...");
            ExecutorService pool = Executors.newFixedThreadPool(20);

            while (true) {
                Socket clientSocket = listener.accept();
                pool.execute(new CalculatorHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// 각 클라이언트의 연결을 처리하는 스레드
class CalculatorHandler implements Runnable {
    private Socket clientSocket;

    public CalculatorHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String expression;
            while ((expression = reader.readLine()) != null) {
                String result = calculate(expression);
                writer.println(result);
            }

        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // calculate 메서드는 클라이언트로부터 받은 연산식을 처리하고 결과를 반환
    private String calculate(String expression) {
        String[] tokens = expression.split(" ");
        if (tokens.length != 3) {
            return "Incorrect : Too many arguments"; // 피연산자 개수가 부적절한 경우
        }

        try {
            int operand1 = Integer.parseInt(tokens[1]);
            int operand2 = Integer.parseInt(tokens[2]);

            switch (tokens[0]) {
                case "ADD":
                    return "Answer:" + (operand1 + operand2);
                case "SUB":
                    return "Answer:" + (operand1 - operand2);
                case "MUL":
                    return "Answer:" + (operand1 * operand2);
                case "DIV":
                    if (operand2 == 0) {
                        return "Error: divided by zero";
                    }
                    return "Answer:" + (operand1 / operand2);
                default:
                    return "Incorrect : Unknown operation"; // 알 수 없는 연산인 경우
            }
        } catch (NumberFormatException e) {
            return "Error: Invalid operands"; // 숫자로 변환할 수 없는 피연산자인 경우
        }
    }
}
