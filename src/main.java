import com.fazecast.jSerialComm.SerialPort;

public class main {
    public static void main(String[] args) {
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Total Serial Ports Found: " + ports.length);
        for (SerialPort port : ports ){
            System.out.println("- Detected Port: " +  port.getSystemPortName() + "(" + port.getDescriptivePortName());
        }

    }
}
