package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {

   //Isso aqui são os dados que usamos para conectar com o banco de dados
    String url = "jdbc:mysql://localhost:3306/restaurante";
    String usuario = "root";
    String senha = "admin";

    public Connection conectarBancoDeDados(){
        Connection conn = null;

        try {
            //Carregando Driver do banco de dados
            Class.forName("com.mysql.cj.jdbc.Driver");
            //Criando conexão com banco de dados
            conn = DriverManager.getConnection(url, usuario, senha);
        } catch(ClassNotFoundException | SQLException e){
            System.out.println("Driver não encontrado " + e.getMessage());
        }

        return conn;
    }

}
