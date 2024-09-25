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
            System.out.println("\n--------------- RESTAURANTE FOGO NA BRASA ---------------");
            System.out.println("1 - Ver cardápio");
            System.out.println("2 - Criar cadastro");
            System.out.println("3 - Fazer pedido");
            System.out.println("4 - Sair do Programa");
            System.out.println("-".repeat(57));
            System.out.print("Digite o número da opção: ");
            opcao = leitura.nextInt();
            System.out.println("*".repeat(57) + "\n\n\n");

            switch (opcao){
                // Ver menu
                case 1:
                    String sql = "Select * from cardapio;";
                    PreparedStatement smtm = conn.conectarBancoDeDados().prepareStatement(sql);
                    smtm.execute();
                    ResultSet rs = smtm.executeQuery();

                    System.out.println("\n--------------- CARDÁPIO FOGO NA BRASA ---------------");
                    while (rs.next()){
                        int idItens = rs.getInt("ID_itens");
                        String nome = rs.getString("Nome_item");
                        double preco = rs.getDouble("Preco_item");

                        System.out.printf("Número: %d | Nome: %s | Preço: R$%.2f\n", idItens, nome, preco);
                    }
                    System.out.println("-".repeat(57) + "\n\n\n");

                    break;

                case 2:
                    System.out.println("\n------------------ CADASTRO DE CLIENTE ------------------");
                    System.out.print("Digite seu nome: ");
                    leitura.nextLine();
                    String nome = leitura.nextLine();
                    System.out.println("*".repeat(57) + "\n\n\n");

                    sql = "insert into cliente (nome) values (?)";
                    smtm = conn.conectarBancoDeDados().prepareStatement(sql);
                    smtm.setString(1, nome);
                    smtm.execute();
                    smtm.close();
                    break;

                case 3:

                    String opcao1;
                    int comanda = 1;

                    //SQL para pegar último id da comanda para ir incrementando se cliente fechar o programa e fizer outra comanda
                    String sqlUltimaComanda = "SELECT MAX(ID_Comanda) AS ultimaComanda FROM comanda;";
                    PreparedStatement stmtUltimaComanda = conn.conectarBancoDeDados().prepareStatement(sqlUltimaComanda);
                    ResultSet rsUltimaComanda = stmtUltimaComanda.executeQuery();

                    //Aqui ele vai verificar ss existe uma ultima comanda
                    if (rsUltimaComanda.next()) {
                        //se tiver uma última comanda, ele vai pegar a ultima comanda com a query do sql
                        //e incrementar com 1, formando uma nova comanda
                        comanda = rsUltimaComanda.getInt("ultimaComanda") + 1; // Incrementa o último ID_Comanda
                    } else {
                        comanda = 1; // Se não houver nenhuma comanda, começa com 1
                    }

                    System.out.println("\n------------------------ COMANDA ------------------------");
                    do {

                        System.out.print("Digite o item do cardápio que deseja inserir: ");
                        int num = leitura.nextInt();

                        //SQL para consultar faturamento
                        String sqlFaturamento = "select * from cardapio where ID_itens = ?";
                        PreparedStatement stmtFaturamento = conn.conectarBancoDeDados().prepareStatement(sqlFaturamento);
                        stmtFaturamento.setInt(1, num);
                        stmtFaturamento.execute();
                        ResultSet rsFaturamento = stmtFaturamento.executeQuery();
                        double precoFatura = 0.0;

                        if (rsFaturamento.next()) {
                            precoFatura = rsFaturamento.getDouble("Preco_item");
                        }

                        //SQL para inserir pedido
                        sql = "insert into pedido (fk_Cliente_ID_cliente) values (?)";
                        smtm = conn.conectarBancoDeDados().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                        smtm.setInt(1, 1); //Colocando o cliente sempre o mesmo
                        smtm.execute();

                        //SQL para inserir pedido na comanda
                        String sql2 = "insert into comanda (ID_Comanda, ID_mesa, Valor_Faturamento, fk_Cardapio_ID_itens, fk_Pedido_ID_pedido) values (?, ?, ?, ?, ?)";
                        PreparedStatement smtm2 = conn.conectarBancoDeDados().prepareStatement(sql2);
                        smtm2.setInt(1, comanda); // Definindo ID da comanda
                        smtm2.setInt(2, 1); // Definindo ID da mesa como 1
                        smtm2.setDouble(3, precoFatura); //Definindo o faturamento do item pedido
                        smtm2.setInt(4, num); // Número do item escolhido

                        //SQL para pegar último ID do pedido
                        int ultimoPedido = 0;
                        ResultSet rsUltimoId = smtm.getGeneratedKeys();

                        if(rsUltimoId.next()){
                            //pega o ultimo pedido e incrementa 1
                            ultimoPedido = rsUltimoId.getInt(1);
                        } else {
                            //Se não tiver nenhum pedido, inicia com 1
                            ultimoPedido = 1;
                        }

                        smtm2.setInt(5, ultimoPedido); // Definindo o ID do pedido como o ultimo ID do banco de dados

                        smtm2.execute();


                        //Pergurtando se o cliente deseja adicionar mais um item na sua comanda
                        System.out.println("\nDeseja adicionar mais um pedido? (SIM/ NAO)");
                        leitura.nextLine();
                        opcao1 = leitura.nextLine();
                        System.out.println("~".repeat(57) + "\n");

                        if (opcao1.equalsIgnoreCase("nao")) {
                            comanda++; // Incrementar o ID da comanda para a próxima
                            System.out.println("\n\n");
                            break; // Encerra o loop
                        }

                    } while (opcao1.equalsIgnoreCase("sim"));

                case 4:
                    break;

                //caso nenhuma opções do switch
                default:
                    System.out.println("Digite um número válido");
                    break;
            }

        } while (opcao != 4);

    }
}
