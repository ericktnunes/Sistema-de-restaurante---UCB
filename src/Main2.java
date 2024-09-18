import Connection.ConexaoBD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) throws SQLException {
        ConexaoBD conn = new ConexaoBD();
        conn.conectarBancoDeDados();

        Scanner leitura = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("1 - Ver menu");
            System.out.println("2 - Criar cadastro");
            System.out.println("3 - Fazer pedido");
            opcao = leitura.nextInt();

            switch (opcao){
                // Ver menu
                case 1:
                    String sql = "Select * from cardapio;";
                    PreparedStatement smtm = conn.conectarBancoDeDados().prepareStatement(sql);
                    smtm.execute();
                    ResultSet rs = smtm.executeQuery();

                    while (rs.next()){
                        int idItens = rs.getInt("ID_itens");
                        String nome = rs.getString("Nome_item");
                        double preco = rs.getDouble("Preco_item");

                        System.out.printf("Número: %d | Nome: %s | Preço: R$%.2f\n", idItens, nome, preco);
                    }
                    break;

                case 2:
                    System.out.println("Digite seu nome: ");
                    leitura.nextLine();
                    String nome = leitura.nextLine();

                    sql = "insert into cliente (nome) values (?)";
                    smtm = conn.conectarBancoDeDados().prepareStatement(sql);
                    smtm.setString(1, nome);
                    smtm.execute();
                    smtm.close();
                    break;

                case 3:
                        sql = "INSERT INTO pedido (fk_Mesa_ID_Mesa, fk_Cliente_ID_cliente) VALUES (?, ?)";
                        smtm = conn.conectarBancoDeDados().prepareStatement(sql);
                        smtm.setInt(1, 1);
                        smtm.setInt(2, 1);
                        smtm.execute();

                        System.out.println("Qual o número do cardápio deseja adicionar?");
                        int num = leitura.nextInt();
                        sql = "INSERT INTO comanda (fk_Cardapio_ID_Itens, fk_Pedido_ID_pedido) VALUES (?, ?)";
                        smtm = conn.conectarBancoDeDados().prepareStatement(sql);
                        smtm.setInt(1, num);
                        smtm.setInt(2, 1);
                        smtm.execute();
                        break;
            }

        } while (opcao != 0);

    }
}
