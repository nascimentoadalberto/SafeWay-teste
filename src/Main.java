import entity.*;
import java.util.*;

/*
* CÓDIGO
— Muito código de print sem necessidade
— Sem verificação de input (caso colocar letras no menu o programa é encerrado)
— Switches sem default case
— MUITOS ifs, dificultando o entendimento
— Nomes de variáveis e indexes não descritivos
— Métodos com muitas responsabilidades

EXPERIÊNCIA DO USUÁRIO
— Ao inserir um usuário ou senha incorretos, o programa é finalizado
— Alguns erros de ortografia, como "Relizar" ao invés de "Realizar"
— Após ver as compras, é solicitado o login novamente
— Não há confirmação do produto ser adicionado ao carrinho

BOAS PRÁTICAS
— Não existem pacotes separando as classes
— o método "executar" tem muita responsabilidade, pode ser separado em métodos auxiliares

ESTRUTURA DE DADOS
— Listagem de produtos no menu está fora de ordem

REGRAS DE NEGÓCIO
— Saldo da empresa não é atualizado conforme as taxas do sistema
*/

public class Main {

    public static void main(String[] args) throws InputMismatchException {

        // SIMULANDO BANCO DE DADOS

        List<Produto> carrinho = new ArrayList<Produto>();
        List<Venda> vendas = new ArrayList<Venda>();

        Empresa empresa = new Empresa(1, "SafeWay Padaria", "30021423000159", 0.15, 0.0);
        Empresa empresa2 = new Empresa(2, "Level Varejo", "53239160000154", 0.05, 0.0);
        Empresa empresa3 = new Empresa(3, "SafeWay Restaurante", "41361511000116", 0.20, 0.0);

        Produto produto = new Produto(1, "Pão Francês", 5, 3.50, empresa);
        Produto produto2 = new Produto(2, "Coturno", 10, 400.0, empresa2);
        Produto produto3 = new Produto(3, "Jaqueta Jeans", 15, 150.0, empresa2);
        Produto produto4 = new Produto(4, "Calça Sarja", 15, 150.0, empresa2);
        Produto produto5 = new Produto(5, "Prato feito - Frango", 10, 25.0, empresa3);
        Produto produto6 = new Produto(6, "Prato feito - Carne", 10, 25.0, empresa3);
        Produto produto7 = new Produto(7, "Suco Natural", 30, 10.0, empresa3);
        Produto produto8 = new Produto(8, "Sonho", 5, 8.50, empresa);
        Produto produto9 = new Produto(9, "Croissant", 7, 6.50, empresa);
        Produto produto10 = new Produto(10, "Chá Gelado", 4, 5.50, empresa);

        Cliente cliente = new Cliente("07221134049", "Allan da Silva", "cliente", 20);
        Cliente cliente2 = new Cliente("72840700050", "Samuel da Silva", "cliente2", 24);

        Usuario usuario1 = new Usuario("admin", "1234", null, null);
        Usuario usuario2 = new Usuario("empresa", "1234", null, empresa);
        Usuario usuario3 = new Usuario("cliente", "1234", cliente, null);
        Usuario usuario4 = new Usuario("cliente2", "1234", cliente2, null);
        Usuario usuario5 = new Usuario("empresa2", "1234", null, empresa2);
        Usuario usuario6 = new Usuario("empresa3", "1234", null, empresa3);


        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2, usuario3, usuario4, usuario5, usuario6);
        List<Cliente> clientes = Arrays.asList(cliente, cliente2);
        List<Empresa> empresas = Arrays.asList(empresa, empresa2, empresa3);
        List<Produto> produtos = Arrays.asList(produto, produto2, produto3, produto4, produto5, produto6, produto7,
                produto8, produto9, produto10);
        executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
    }

    public static void executar(
            List<Usuario> usuarios, List<Cliente> clientes, List<Empresa> empresas,
            List<Produto> produtos, List<Produto> carrinho, List<Venda> vendas) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Entre com seu usuário e senha:");
        System.out.print("Usuário: ");
        String username = sc.next();
        System.out.print("Senha: ");
        String senha = sc.next();

        List<Usuario> usuariosSearch = usuarios.stream().filter(x -> x.getUsername().equals(username)).toList();

        if (!usuariosSearch.isEmpty())
            System.out.println("Usuário não encontrado");
        else {
            Usuario usuarioLogado = usuariosSearch.get(0);
            if (!usuarioLogado.getSenha().equals(senha))
                System.out.println("Senha incorreta");
            else {
                if (usuarioLogado.IsEmpresa())
                    menuEmpresa(usuarios, clientes, empresas, produtos, carrinho, vendas, usuarioLogado);
                else
                    menuCliente(usuarios, clientes, empresas, produtos, carrinho, vendas, usuarioLogado);
            }
        }
    }

    public static void menuEmpresa(
            List<Usuario> usuarios, List<Cliente> clientes, List<Empresa> empresas,
            List<Produto> produtos, List<Produto> carrinho, List<Venda> vendas, Usuario usuarioLogado) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escolha uma opção para iniciar");
        System.out.println("1 - Listar vendas");
        System.out.println("2 - Ver produtos");
        System.out.println("0 - Deslogar");
        int escolha = sc.nextInt();

        switch (escolha) {
            case 1 -> {
                System.out.println();
                System.out.println("************************************************************\n" +
                        "VENDAS EFETUADAS");
                double saldo = usuarioLogado.getEmpresa().getSaldo();

                vendas.forEach(venda -> {
                    if (venda.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId())) {
                        System.out.println("************************************************************\n" +
                                "Venda de código: " + venda.getCódigo() + " no CPF " + venda.getCliente().getCpf() + ":");
                        venda.getItens().forEach(produto ->
                            System.out.println(produto.getId() + " - " + produto.getNome() + "    R$" + produto.getPreco())
                        );
                        System.out.println("Total Venda: R$" + venda.getValor());
                        System.out.println("Total Taxa a ser paga: R$" + venda.getComissaoSistema());
                        System.out.println("Total Líquido para empresa " + (venda.getValor() - venda.getComissaoSistema()));
                        System.out.println("************************************************************");

                        usuarioLogado.getEmpresa().setSaldo(saldo - venda.getComissaoSistema());
                    }
                });
                System.out.println("Saldo Empresa: " + saldo);
                System.out.println("************************************************************");

                executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
            }
            case 2 -> {
                System.out.println();
                System.out.println("************************************************************");
                System.out.println("MEUS PRODUTOS");
                produtos.forEach(produto -> {
                    if (produto.getEmpresa().getId().equals(usuarioLogado.getEmpresa().getId())) {
                        System.out.println("************************************************************");
                        System.out.println("Código: " + produto.getId());
                        System.out.println("Produto: " + produto.getNome());
                        System.out.println("Quantidade em estoque: " + produto.getQuantidade());
                        System.out.println("Valor: R$" + produto.getPreco());
                        System.out.println("************************************************************");
                    }

                });
                System.out.println("Saldo Empresa: " + usuarioLogado.getEmpresa().getSaldo());
                System.out.println("************************************************************");

                executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
            }
            case 0 -> executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
            default -> System.out.println("Opção inválida.");
        }
    }

    public static void menuCliente(
            List<Usuario> usuarios, List<Cliente> clientes, List<Empresa> empresas, List<Produto> produtos,
            List<Produto> carrinho, List<Venda> vendas, Usuario usuarioLogado) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escolha uma opção para iniciar:\n1 - Realizar Compras\n2 - Ver Compras \n0 - Deslogar");
        int escolha = sc.nextInt();
        switch (escolha) {
            case 1 -> {
                opcaoUmCliente(clientes, empresas, produtos, carrinho, vendas, usuarioLogado);
                carrinho.clear();
                executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
            }
            case 2 -> {
                System.out.println("************************************************************\nCOMPRAS EFETUADAS");
                vendas.forEach(venda -> {
                    if (venda.getCliente().getUsername().equals(usuarioLogado.getUsername())) {
                        System.out.println("************************************************************");
                        System.out.println("Compra de código: " + venda.getCódigo() + " na empresa "
                                + venda.getEmpresa().getNome() + ": ");
                        venda.getItens().forEach(x -> {
                            System.out.println(x.getId() + " - " + x.getNome() + "    R$" + x.getPreco());
                        });
                        System.out.println("Total: R$" + venda.getValor() +
                                "\n************************************************************");
                    }
                });
                executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
            }
            case 0 -> executar(usuarios, clientes, empresas, produtos, carrinho, vendas);
            default -> System.out.println("Opção inválida.");
        }
    }

    public static void opcaoUmCliente(
            List<Cliente> clientes, List<Empresa> empresas,
            List<Produto> produtos, List<Produto> carrinho,
            List<Venda> vendas, Usuario usuarioLogado) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Para realizar uma compra, escolha a empresa onde deseja comprar: ");
        empresas.forEach(x -> System.out.println(x.getId() + " - " + x.getNome()));
        Integer escolhaEmpresa = sc.nextInt();
        int escolhaProduto;

        do {
            System.out.println("Escolha os seus produtos: ");
            produtos.forEach(x -> {
                if (x.getEmpresa().getId().equals(escolhaEmpresa)) {
                    System.out.println(x.getId() + " - " + x.getNome());
                }
            });
            System.out.println("0 - Finalizar compra");
            escolhaProduto = sc.nextInt();

            for (Produto produtoSearch : produtos) {
                if (produtoSearch.getId().equals(escolhaProduto)) {
                    carrinho.add(produtoSearch);
                    System.out.println("Produto adicionado ao carrinho! O total de produtos é de " +
                            carrinho.size() + ". Continue comprando ou finalize a compra.");
                }
            }
        } while (escolhaProduto != 0);

        System.out.println("************************************************************");
        System.out.println("Resumo da compra: ");
        carrinho.forEach(x -> {
            if (x.getEmpresa().getId().equals(escolhaEmpresa)) {
                System.out.println(x.getId() + " - " + x.getNome() + "    R$" + x.getPreco());
            }
        });
        Empresa empresaEscolhida = empresas.stream().filter(x -> x.getId().equals(escolhaEmpresa)).toList().get(0);
        Cliente clienteLogado = clientes.stream().filter(x -> x.getUsername().equals(usuarioLogado.getUsername())).toList().get(0);

        Venda venda = criarVenda(carrinho, empresaEscolhida, clienteLogado, vendas);
        System.out.println("Total: R$" + venda.getValor());
        System.out.println("************************************************************");
    }

    public static Venda criarVenda(List<Produto> carrinho, Empresa empresa, Cliente cliente, List<Venda> vendas) {
        Double total = carrinho.stream().mapToDouble(Produto::getPreco).sum();
        Double comissaoSistema = total * empresa.getTaxa();
        int idVenda = vendas.isEmpty() ? 1 : vendas.get(vendas.size() - 1).getCódigo() + 1;
        Venda venda = new Venda(idVenda, carrinho.stream().toList(), total, comissaoSistema, empresa, cliente);
        empresa.setSaldo(empresa.getSaldo() + total);
        vendas.add(venda);
        return venda;
    }
}
