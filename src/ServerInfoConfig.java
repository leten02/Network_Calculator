import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ServerInfoConfig {
    private String serverIP;
    private int serverPort;

    public ServerInfoConfig() {
        // Set default values
        serverIP = "localhost";
        serverPort = 9999;
    }

    public void loadConfigFromFile(String configFilePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(configFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ServerIP:")) {
                    serverIP = line.substring(9).trim();
                } else if (line.startsWith("Port:")) {
                    serverPort = Integer.parseInt(line.substring(5).trim());
                }
            }
        } catch (IOException e) {
            System.out.println("Failed to read the configuration file. Using default server IP and port.");
        }
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public static void main(String[] args) {
        ServerInfoConfig config = new ServerInfoConfig();
        config.loadConfigFromFile("server_info.txt");

        String serverIP = config.getServerIP();
        int serverPort = config.getServerPort();

        System.out.println("Server IP: " + serverIP);
        System.out.println("Server Port: " + serverPort);
    }
}
