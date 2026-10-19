import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Server extends UnicastRemoteObject implements ServerInterface {

    List<UserInterface> users = new ArrayList<>();

    protected Server() throws RemoteException {
    }

    public void entrarNoChat(String nick, UserInterface usr) throws RemoteException {
        users.add(usr);
        enviarMensagem("Usuário: " + nick + " entrou no chat", "Sistema");
    }



    public void sairDoChat(String nick, UserInterface usr) throws RemoteException {
        users.remove(usr);
        enviarMensagem("Usuário: " + nick + " saiu no chat", "Sistema");
    }

    @Override
    public void enviarMensagem(String msg, String nick) throws RemoteException {
        try {
            for (UserInterface usr : users){
                usr.exibirMensagem(msg);
            }
        } catch (Exception e) {
            System.out.println("Erro na trasmissão da mensagem" + e);

        }
    }

    public static void main(String[] args) {
        try {
            ServerInterface s = new Server();
            Registry registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("Servidor", s);
            System.out.println("Servidor de Chat RMI iniciado com sucesso na porta 1099.");
        } catch (Exception e) {
            System.out.println ("Erro no servidor: " + e.getMessage());
        }
    }
}
