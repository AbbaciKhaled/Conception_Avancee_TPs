import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

public class P2 {


	public static ServerSocket SocketServeur;
	public static Socket connection;
	public static ObjectOutputStream out;
	public static ObjectInputStream in;


	public static InterfaceRMI rmiServer;
	public static Registry registry;
	public static String Adress = "127.0.0.1";
	public static int port = 1099;
	
	public static InterfaceRMI rmiServer2;
	public static Registry registry2;
	public static String Adress2 = "127.0.0.1";
	public static int port2 = 1100;


	public static void main(String[] args) {

		try {

			SocketServeur = new ServerSocket(2004);
			System.out.println("P2 : Waiting for connection");
			connection = SocketServeur.accept();
			System.out.println("P2 : Connection received And Accepted");


			out = new ObjectOutputStream(connection.getOutputStream());
			in = new ObjectInputStream(connection.getInputStream());


			registry = LocateRegistry.getRegistry("127.0.0.1",port);
			System.out.println("P3 : RMI Registry found on port 1099");
			rmiServer = (InterfaceRMI) (registry.lookup("rmiServer"));
			
			registry2 = LocateRegistry.getRegistry("127.0.0.1",port2);
			System.out.println("P3 : RMI Registry found on port 1100");
			rmiServer2 = (InterfaceRMI) (registry2.lookup("rmiServer"));

			while(true) {

				
				String a[] = ((String) in.readObject()).split(",");
				System.out.println("Reception donn�es de P1");
				out.writeObject("Bien Re�u From P2");

				
				switch(Integer.parseInt(a[0])) {

				case 1:
				{
					int x = rmiServer.update("insert into student values ('"+a[1]+"','"+a[2]+"','"+a[3]+"');");
					int x2 = rmiServer2.update("insert into student (matricule, nom, prenom) values ('"+a[1]+"','"+a[2]+"','"+a[3]+"');");
					
					if (x==1 && x2==1)
						out.writeObject("Insertion termin�e avec succ�s");
					else if(x==0 && x2==0)
						out.writeObject("Insertion �chou�e dans les 2 bases");
					else if(x==0)
						out.writeObject("Insertion �chou�e dans la base MySQL");
					else
						out.writeObject("Insertion �chou�e dans la base MS Access");	
					
					break;
				}


				case 2:
				{
					int x = rmiServer.update("delete from student where matricule = "+a[1]);
					int x2 = rmiServer2.update("delete from student where matricule = "+a[1]);


					if (x==1 && x2==1)
						out.writeObject("Suppression termin�e avec succ�s");
					else if(x==0 && x2==0)
						out.writeObject("Suppression �chou�e dans les 2 bases");
					else if(x==0)
						out.writeObject("Suppression �chou�e dans la base MySQL");
					else
						out.writeObject("Suppression �chou�e dans la base MS Access");

					break;
				}

				case 3:
				{
					System.out.println("select nom, prenom from student where matricule = "+a[1]);

					String x = rmiServer.select("select nom, prenom from student where matricule = "+a[1]);
					out.writeObject(x);
					
					String x2 = rmiServer2.select("select nom, prenom from student where matricule = "+a[1]);
					out.writeObject(x2);
					break;
				}
				
				case 4:
				{
					String x = rmiServer.list("select nom, prenom from student");
					out.writeObject(x);
					
					String x2 = rmiServer2.list("select nom, prenom from student");
					out.writeObject(x2);
					break;
				}

				}

			}

		} catch(Exception e){
			e.printStackTrace();
		}


	}


}
