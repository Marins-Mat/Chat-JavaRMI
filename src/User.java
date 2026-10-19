import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class User extends UnicastRemoteObject implements UserInterface {

    protected User() throws RemoteException {
    }

    private String nick;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public void exibirMensagem(String msg) throws RemoteException {
        System.out.println(msg);
    }

    public static void main(String[] args){
        try {
            System.out.println("Digite o seu nome de usuario: ");
            Scanner sc = new Scanner(System.in);
            String nick = sc.next();

            UserInterface usr = new User();
            String ip = "localhost";

            Registry registry = LocateRegistry.getRegistry(ip, 1099);
            ServerInterface server = (ServerInterface) Naming.lookup("Servidor");
            server.entrarNoChat(nick, usr);

            System.out.println("--- Conectado ao Chat! (Digite 'sair' para encerrar) ---");
            System.out.print("> ");

            while (true) {
                String input = sc.nextLine();

                if (input.equalsIgnoreCase("sair")) {
                    server.sairDoChat(nick, usr);
                    UnicastRemoteObject.unexportObject(usr, true);
                    System.out.println("Você saiu do chat.");
                    break;
                }

                if (!input.isBlank()) {
                    server.enviarMensagem(input, nick);
                }
            }

            sc.close();
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Erro no Cliente: " + e.getMessage());
        }
    }
}


