package TrabalhoHashExtensivel;

import java.util.Scanner;

public class Programa {

	public static <T> void main(String[] args) {
		// pega o hash
		HashExtensivel hashExtensivel;
		// cria um scanner pra receber os dados
		Scanner console = new Scanner(System.in);
		// instancia a classe prontuario no construtor padrão
		Prontuario prontuario = new Prontuario();
		// instancia o arquivo mestre que vai ser passado por parametro quando preciso
		ArquivoMestre arquivoMestre = new ArquivoMestre();
		try {
			// instansia o hash
			hashExtensivel = new HashExtensivel(4, "diretorio.hash.db", "cestos.hash.db");
			// menu
			int opcao;
			do {
				System.out.println("\n\n ------------------------------");
				System.out.println("|             MENU             |");
				System.out.println(" ------------------------------");
				System.out.println("|       1 - Inserir            |");
				System.out.println("|       2 - Editar             |");
				System.out.println("|       3 - Buscar             |");
				System.out.println("|       4 - Excluir            |");
				System.out.println("|       5 - Imprimir           |");
				System.out.println("|       6 - Simulação          |");
				System.out.println("|       0 - Sair               |");
				System.out.println(" ------------------------------");
				try {
					opcao = Integer.valueOf(console.nextLine());
				} catch (NumberFormatException e) {
					opcao = -1;
				}

				switch (opcao) {
				// opção 1 do menu
				case 1: {
					// printa que entrou na opção de inserção
					System.out.println("\nVocê entrou na opção de inserir novo prontuário");
					// recebe valor de chave e enquanto chave não for um valor de 1 a 999999999 não
					// sai da opção do while
					System.out.println("\nDigite seu CPF (de 1 a 999999999)");
					int chave = Integer.valueOf(console.nextLine());
					while (!(chave >= 1 && chave <= 999999999)) {
						System.out.println("\ndeve ser um valor de 1 a 999999999 (digite novamente)");
						chave = Integer.valueOf(console.nextLine());
					}
					// recebe o nome
					System.out.println("\nDigite seu nome");
					String nome = console.nextLine();
					// recebe a data de nascimento
					System.out.println("\nDigite sua data de nascimento ");
					String dataDeNascimento = console.nextLine();
					// recebe o sexo e não sai do while enquanto não for só o caractere f ou m, pode
					// ser maiusculo ou minusculo
					System.out.println("\nDigite seu sexo digite (f) para feminino ou (m) para masculino");
					String sexo = console.nextLine();
					while (!((sexo.equals("f")) || (sexo.equals("m")) || (sexo.equals("F")) || (sexo.equals("M")))) {
						System.out.println(
								"\ndigite o seu sexo novamente deve ser (f) para feminino ou (m) para masculino");
						sexo = console.nextLine();
					}
					// passa os dados para o objeto prontuario
					prontuario.setCpf(chave);
					prontuario.setNome(nome);
					prontuario.setDataDeNascimento(dataDeNascimento);
					prontuario.setSexo(sexo);
					// pega o tempo Iinicial antes do metodo
					long tempoInicial = System.currentTimeMillis();
					// envia os dados para o metodo create do hash
					hashExtensivel.create(chave, arquivoMestre, prontuario);
					// pega o tempo após terminar o metodo
					long tempoFinal = System.currentTimeMillis();
					// printa o tempo em milisegundos que o programa demorou pra executar (tempo
					// final - tempo inicial)
					System.out.println("\nO tempo em milisegundos que o metodo de insersão demorou foi: "
							+ (tempoFinal - tempoInicial));
				}
					break;
				// opção 2 do menu
				case 2: {
					// printa que entrou na opção de edição
					System.out.println("\nVocê entrou na opção de Editar o campo anotação");
					// recebe valor de chave e enquanto chave não for um valor de 1 a 999999999 não
					// sai da opção do while
					System.out.println("\nDigite seu CPF (de 1 a 999999999) para efetuar a busca no arquivo");
					int chave = Integer.valueOf(console.nextLine());
					while (!(chave >= 1 && chave <= 999999999)) {
						System.out.println("\ndeve ser um valor de 1 a 999999999 (digite novamente)");
						chave = Integer.valueOf(console.nextLine());
					}
					// pega o tempo Iinicial antes do metodo
					long tempoInicial = System.currentTimeMillis();
					// envia os dados para o metodo ler (só que esse metodo que ele chama é o metodo
					// ler que só é usado para a edição do arquivo)
					hashExtensivel.readEditar(chave, arquivoMestre, console, hashExtensivel);
					// pega o tempo após terminar o metodo
					long tempoFinal = System.currentTimeMillis();
					// printa o tempo em milisegundos que o programa demorou pra executar (tempo
					// final - tempo inicial)
					System.out.println("\nO tempo em milisegundos que o metodo de insersão demorou foi: "
							+ (tempoFinal - tempoInicial));

				}
					break;
				// opção 3 do menu
				case 3: {
					// printa que entrou na opção de busca, o trabalho não pedeia opção de busca mas
					// achei que era uma opção interessante de se ter
					System.out.println("\nVocê entrou na opção de BUSCA");
					// recebe valor de chave e enquanto chave não for um valor de 1 a 999999999 não
					// sai da opção do while
					System.out.println("\nDigite seu CPF (de 1 a 999999999) para efetuar a busca no arquivo");
					int chave = Integer.valueOf(console.nextLine());
					while (!(chave >= 1 && chave <= 999999999)) {
						System.out.println("\ndeve ser um valor de 1 a 999999999 (digite novamente)");
						chave = Integer.valueOf(console.nextLine());
					}

					// chama o metodo de ler do hash
					hashExtensivel.read(chave, arquivoMestre);

				}
					break;
				// opção 4 do menu
				case 4: {
					// printa que entrou na opção de exclusão
					System.out.println("\nVocê entrou na opção de EXCLUSAO");
					// recebe valor de chave e enquanto chave não for um valor de 1 a 999999999 não
					// sai da opção do while
					System.out.println("\nDigite seu CPF (de 1 a 999999999) para efetuar a busca no arquivo");
					int chave = Integer.valueOf(console.nextLine());
					while (!(chave >= 1 && chave <= 999999999)) {
						System.out.println("\ndeve ser um valor de 1 a 999999999 (digite novamente)");
						chave = Integer.valueOf(console.nextLine());
					}
					// chama o metodo de delete do hash
					hashExtensivel.delete(chave, arquivoMestre);
				}
					break;
				// opção 5 do menu
				case 5: {
					// printa que entrou na opção de impressão
					System.out.println("\nVocê entrou na opção de Impressão");
					System.out.println("-------------impressão do indece hash:--------------");
					// printa os cestos, diretório com profundidade etc...
					hashExtensivel.print();
					System.out.println("\n\n------------impressão do arquivo mestre:-------------");
					// printa os itens salvos (que não sejam com cpf = 0, pois cpf 0 é um item que
					// foi excluido anteriormente)
					arquivoMestre.imprimeTudoDoArquivo();
				}
					break;
				// opção 6 do menu
				case 6: {
					// limpa os arquivos
					arquivoMestre.limpaArquivo();
					hashExtensivel.limpaTudo(4, "diretorio.hash.db", "cestos.hash.db");
					Prontuario p; // insere 1.350.000 prontuario
					// insere 1.350.000 prontuarios
					long tempoInicial1 = System.currentTimeMillis();
					for (int i = 1; i <= 1350000; i++) {
						p = new Prontuario(i, "teste", "teste", "teste");
						System.out.println("impressão número: " + i);
						hashExtensivel.create(i, arquivoMestre, p);
					}
					long tempoFinal1 = System.currentTimeMillis();
					
					// pesquisa os 1.350.000 prontuarios
					long tempoInicial2 = System.currentTimeMillis();
					for (int i = 1; i <= 1350000; i++) {
						System.out.println("pesquisa número: " + i);
						hashExtensivel.read(i, arquivoMestre);
					}
					long tempoFinal2 = System.currentTimeMillis();

					

					// limpa os arquivos
					arquivoMestre.limpaArquivo();
					hashExtensivel.limpaTudo(4, "diretorio.hash.db", "cestos.hash.db");
					// insere 10.000 prontuarios
					long tempoInicial3 = System.currentTimeMillis();
					for (int i = 1; i <= 10000; i++) {
						p = new Prontuario(i, "teste", "teste", "teste");
						System.out.println("impressão número: " + i);
						hashExtensivel.create(i, arquivoMestre, p);
					}
					long tempoFinal3 = System.currentTimeMillis();
					
					// pesquisa os 10.000 prontuarios
					long tempoInicial4 = System.currentTimeMillis();
					for (int i = 1; i <= 10000; i++) {
						System.out.println("pesquisa número: " + i);
						hashExtensivel.read(i, arquivoMestre);
					}
					long tempoFinal4 = System.currentTimeMillis();
					
					// limpa os arquivos
					arquivoMestre.limpaArquivo();
					hashExtensivel.limpaTudo(4, "diretorio.hash.db", "cestos.hash.db");

					// insere 5.000 prontuarios
					long tempoInicial5 = System.currentTimeMillis();
					for (int i = 1; i <= 5000; i++) {
						p = new Prontuario(i, "teste", "teste", "teste");
						System.out.println("impressão número: " + i);
						hashExtensivel.create(i, arquivoMestre, p);
					}
					long tempoFinal5 = System.currentTimeMillis();

					// pesquisa os 5.000 prontuarios
					long tempoInicial6 = System.currentTimeMillis();
					for (int i = 1; i <= 5000; i++) {
						System.out.println("pesquisa número: " + i);
						hashExtensivel.read(i, arquivoMestre);
					}
					long tempoFinal6 = System.currentTimeMillis();

					// limpa os arquivos
					arquivoMestre.limpaArquivo();
					hashExtensivel.limpaTudo(4, "diretorio.hash.db", "cestos.hash.db");

					// insere 1.000 prontuarios
					long tempoInicial7 = System.currentTimeMillis();
					for (int i = 1; i <= 1000; i++) {
						p = new Prontuario(i, "teste", "teste", "teste");
						System.out.println("impressão número: " + i);
						hashExtensivel.create(i, arquivoMestre, p);
					}
					long tempoFinal7 = System.currentTimeMillis();

					// pesquisa os 1.000 prontuarios
					long tempoInicial8 = System.currentTimeMillis();
					for (int i = 1; i <= 1000; i++) {
						System.out.println("pesquisa número: " + i);
						hashExtensivel.read(i, arquivoMestre);
					}
					long tempoFinal8 = System.currentTimeMillis();

					// printa o tempo que levou na tela o arquivo de 1.350.000 mil de registros
					System.out.println("\nO tempo em milisegundos que o metodo de insersão de 1.350.000 arquivos demorou foi: "+ (tempoFinal1 - tempoInicial1) + " milisegundos");
					System.out.println("\nO tempo em milisegundos que o metodo de pesquisa de 1.350.000 demorou foi: "+ (tempoFinal2 - tempoInicial2) + " milisegundos");
					// printa o tempo que levou na tela o arquivo de 10.000 mil de registros
					System.out.println("\nO tempo em milisegundos que o metodo de insersão de 10.000 arquivos demorou foi: "+ (tempoFinal3 - tempoInicial3) + " milisegundos");
					System.out.println("\nO tempo em milisegundos que o metodo de pesquisa de 10.000 demorou foi: "+ (tempoFinal4 - tempoInicial4) + " milisegundos");
					// printa o tempo que levou na tela o arquivo de 5.000 mil de registros
					System.out.println("\nO tempo em milisegundos que o metodo de insersão de 5.000 arquivos demorou foi: "+ (tempoFinal5 - tempoInicial5) + " milisegundos");
					System.out.println("\nO tempo em milisegundos que o metodo de pesquisa de 5.000 demorou foi: "+ (tempoFinal6 - tempoInicial6) + " milisegundos");
					// printa o tempo que levou na tela o arquivo de 1.000 mil de registros
					System.out.println("\nO tempo em milisegundos que o metodo de insersão de 1.000 arquivos demorou foi: "+ (tempoFinal7 - tempoInicial7) + " milisegundos");
					System.out.println("\nO tempo em milisegundos que o metodo de pesquisa de 1.000 demorou foi: "+ (tempoFinal8 - tempoInicial8) + " milisegundos");
				}
					break;
				// opção 0 do menu que fecha o programa
				case 0:
					break;
				// caso não caia em nenhuma opção printa no console (opção invalida)
				default:
					System.out.println("Opcao invalida");
				}
			} while (opcao != 0);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Integer extracted() {
		return (Integer) null;
	}
}
