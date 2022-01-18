package TrabalhoHashExtensivel;

import java.io.RandomAccessFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ArquivoMestre {
	private RandomAccessFile arq;

	// metodo que cria o arquivo
	public void criarArq() throws IOException {
		arq = new RandomAccessFile("arquivoMestre.db", "rw");
	}

	// metodo que encerra o arquivo
	public void encerrarArq() throws IOException {
		arq.close();
	}

	// metodo usado pra escrever no arquivo (primeiro insere no hash para ver se a
	// chave já existe e se não existir é inserido no hash e mandado para inserir
	// aqui também)
	public long escreveNoArq(Prontuario p1) throws Exception {
		// cria o arquivo
		criarArq();
		// cria um vetor de byte
		byte ba[];
		// pega o tamanho do arquivo, como o arquivo funciona como um vetor e começa
		// pela posição 0, o tamanho do arquivo será sempre a posição que receberá o
		// novo dado
		long posicao = arq.length();
		// posiciona o ponteiro no final do arquivo onde vai ser inserido o novo dado
		arq.seek(posicao);
		// passa o que deve ser inserido para o vetor de bytes
		ba = p1.toByteArray();
		// insere no arquivo
		arq.writeInt(ba.length);
		arq.write(ba);
		// encerra o arquivo
		encerrarArq();
		// printa que foi inserido
		System.out.println("O prontuario foi inserido");
		return posicao;
	}

	// metodo para ler
	public void lerNoArq(long posicao) throws IOException {
		// cria um vetor de byte
		byte ba[];
		// cria o arquivo
		criarArq();
		// LEITURA
		// instancia um novo objeto prontuario com o construtor padrão
		Prontuario p = new Prontuario();
		int tam;
		/*
		 * usa a posição recebida por parametro para posicionar o ponteiro do arquivo no
		 * local onde deseja ler (primeiro é buscado no hash pelo cpf e se achar, chama
		 * esse metodo e passa a posição que está inserido no arquivo)
		 */
		arq.seek(posicao);
		tam = arq.readInt();
		ba = new byte[tam];
		// lê o arquivo e passa os valores para o objeto prontuario
		arq.read(ba);
		p.fromByteArray(ba);
		// printa os valores
		System.out.println(p);
		// encerra o arquivo
		encerrarArq();
	}

	// metodo que edita a anotação do médico
	public void editarNoArquivo(long posicao, char[] anotacao, HashExtensivel hashExtensivel) throws Exception {
		byte ba[];
		// cria o arquivo
		criarArq();
		// LEITURA
		Prontuario p = new Prontuario();
		int tam;
		arq.seek(posicao);
		tam = arq.readInt();
		ba = new byte[tam];
		arq.read(ba);
		p.fromByteArray(ba);
		p.setAnotacao(anotacao);

		// escreve a alteração
		arq.seek(posicao);
		ba = p.toByteArray();
		arq.writeInt(ba.length);
		arq.write(ba);
		System.out.println("\nA anotação foi alterada");
		// encerra o arquivo
		encerrarArq();
	}

	public void imprimeTudoDoArquivo() throws Exception {
		// cria o arquivo
		criarArq();
		Prontuario p = new Prontuario();
		byte ba[];
		int tam;
		// percorre todo o arquivo com uma posição i que começa com valor 0 e a cada
		// interação pega a próxima posição que será lida até que a posição i seja igual
		// ao tamanho do arquivo
		for (long i = 0; i < arq.length(); i = arq.getFilePointer()) {
			arq.seek(i);
			tam = arq.readInt();
			ba = new byte[tam];
			arq.read(ba);
			p.fromByteArray(ba);
			// se o cpf for igual a 0 quer dizer que foi excluido então não é imprimido
			if (p.getCpf() != 0) {
				System.out.println(p);
			}

		}
		if(arq.length() == 0) {
			System.out.println("\nO arquivo mestre está vazio");
		}
		// encerra o arquivo
		encerrarArq();
	}

	public void excluiNoArquivo(long posicao) throws IOException {
		byte ba[];
		// cria o arquivo
		criarArq();
		// LEITURA
		Prontuario p = new Prontuario();
		int tam;
		arq.seek(posicao);
		tam = arq.readInt();
		ba = new byte[tam];
		arq.read(ba);
		p.fromByteArray(ba);
		// se cpf for 0 quer dizer que foi excluido
		p.setCpf(0);

		// escreve a alteração
		arq.seek(posicao);
		ba = p.toByteArray();
		arq.writeInt(ba.length);
		arq.write(ba);
		System.out.println("\nO prontuario foi deletado");
		// encerra o arquivo
		encerrarArq();
	}

	//limpa o arquivo
	public void limpaArquivo() throws IOException {

		criarArq();
		// vamos excluir todo o conteúdo do arquivo
		arq.setLength(0);
		System.out.println("\nArquivo Mestre foi limpo");
		encerrarArq();
	}

}
