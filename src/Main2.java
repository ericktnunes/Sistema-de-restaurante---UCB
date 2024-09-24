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
            System.out.println("--------------- RESTAURANTE FOGO NA BRASA ---------------");
            System.out.println("1 - Ver menu");
            System.out.println("2 - Criar cadastro");
            System.out.println("3 - Fazer pedido");
            System.out.println("-".repeat(57));
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

                    String opcao1;
                    int comanda = 1;

                    do {
                        System.out.println("Digite o item do cardápio que deseja inserir: ");
                        int num = leitura.nextInt();

                        sql = "insert into pedido (fk_Cliente_ID_cliente) values (?)";
                        smtm = conn.conectarBancoDeDados().prepareStatement(sql);
                        smtm.setInt(1, 1);
                        smtm.execute();

                        String sql2 = "insert into comanda (ID_Comanda, ID_mesa, Valor_Faturamento, fk_Cardapio_ID_itens, fk_Pedido_ID_pedido) values (?, ?, ?, ?, ?)";
                        PreparedStatement smtm2 = conn.conectarBancoDeDados().prepareStatement(sql2);

                        smtm2.setInt(1, comanda);
                        smtm2.setInt(2, 1);
                        smtm2.setInt(3, 500);
                        smtm2.setInt(4, num);
                        smtm2.setInt(5, 4);
                        smtm2.execute();

                        System.out.println("Deseja adicionar mais um pedido? (SIM/ NAO)");
                        leitura.nextLine();
                        opcao1 = leitura.nextLine();

                        if (opcao1.equalsIgnoreCase("nao")) {
                            comanda++; // Incrementar o ID da comanda para a próxima
                            System.out.println("Nova comanda criada: " + comanda);
                            break; // Encerra o loop
                        }

                    } while (opcao1.equalsIgnoreCase("sim"));

                    break;
            }

        } while (opcao != 0);

    }
}
