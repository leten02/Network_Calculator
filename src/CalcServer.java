import java.io.*;
import java.net.*;


public class CalcServer {
    public static String calc(String exp) {
        String[] tokens = exp.split(" ");
        if (tokens.length != 3){
            if(tokens. length > 3)
                return "Error: Too many arguments";
            return "Error: Invalid expression";
        }
        try {
            int operand1 = Integer.parseInt(tokens[0]);
            int operand2 = Integer.parseInt(tokens[2]);
            String operator = tokens[1];

            int result = 0;
            switch (operator) {
                case "+":
                    result = operand1 + operand2;
                    break;
                case "-":
                    result = operand1 - operand2;
                    break;
                case "*":
                    result = operand1 * operand2;
                    break;
                case "/":
                    if (operand2 == 0)
                        return "Error: Division by zero";
                    result = operand1 / operand2;
                    break;
                default:
                    return "Error: Unknown operator";
            }
            return "Answer: " + result;
        } catch (NumberFormatException e) {
            return "Error: Invalid operands";
        }
    }

    public static void main(String[] args) {
        ServerSocket listener = null;

        try {
            listener = new ServerSocket(9999); // 서버 소켓 생성
            System.out.println("연결을 기다리고 있습니다.....");

            while (true) {
                Socket socket = listener.accept(); // 클라이언트로부터 연결 요청 대기
                System.out.println("연결되었습니다.");

                // 새로운 스레드를 생성하여 클라이언트 요청을 처리
                Thread clientHandlerThread = new Thread(new ClientHandler(socket));
                clientHandlerThread.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (listener != null)
                    listener.close(); // 서버 소켓 닫기
            } catch (IOException e) {
                System.out.println("서버 소켓 닫기 중 오류가 발생했습니다.");
            }
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                while (true) {
                    String inputMessage = in.readLine();
                    if (inputMessage == null || inputMessage.equalsIgnoreCase("bye")) {
                        System.out.println("클라이언트에서 연결을 종료하였음");
                        break; // "bye"를 받으면 연결 종료
                    }
                    System.out.println("Received: " + inputMessage); // 받은 메시지를 화면에 출력
                    String res = calc(inputMessage); // 계산. 계산 결과는 res
                    out.write(res + "\n"); // 계산 결과 문자열 전송
                    out.flush();
                }
            } catch (IOException e) {
                System.out.println("클라이언트와 채팅 중 오류가 발생했습니다.");
            }
        }
    }
}
