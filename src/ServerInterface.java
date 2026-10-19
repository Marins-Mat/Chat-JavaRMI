import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    void enviarMensagem(String msg, String nick) throws RemoteException;
    void entrarNoChat(String nick, UserInterface usr) throws  RemoteException;
    void sairDoChat(String nick, UserInterface usr) throws  RemoteException;
}
