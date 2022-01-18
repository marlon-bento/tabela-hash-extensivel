package TrabalhoHashExtensivel;

/*********
 * TABELA HASH EXTENSIVEL
 * int chave, long dado
 * 
 * Os nomes dos métodos foram mantidos em ingles
 * apenas para manter a coerencia com o resto da
 * disciplina:
 * - boolean create(int chave, long dado)
 * - long read(int chave)
 * - boolean update(int chave, long dado)
 * - boolean delete(int chave)
 * 
 * Implementado pelo Prof. Marcos Kutova
 * v1.0 - 2019
 * OBS: Alguns nomes no codigo foram editados por erro de codificacao -alunos.
 */


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class HashExtensivel {

	String nomeArquivoDiretorio;
	String nomeArquivoCestos;
	RandomAccessFile arqDiretorio;
	RandomAccessFile arqCestos;
	int quantidadeDadosPorCesto;
	Diretorio diretorio;
	
	
	public HashExtensivel(int n, String nomeArqDiretorio, String nomeArqCesto) throws Exception {
		quantidadeDadosPorCesto = n;
		nomeArquivoDiretorio = nomeArqDiretorio;
		nomeArquivoCestos = nomeArqCesto;

		arqDiretorio = new RandomAccessFile(nomeArquivoDiretorio, "rw");
		arqCestos = new RandomAccessFile(nomeArquivoCestos, "rw");

		// Se o diretorio ou os cestos estiverem vazios, cria um novo diretorio e lista
		// de cestos
		if (arqDiretorio.length() == 0 || arqCestos.length() == 0) {

			// Cria um novo diretorio, com profundidade de 0 bits (1 unico elemento)
			diretorio = new Diretorio();
			byte[] bd = diretorio.toByteArray();
			arqDiretorio.write(bd);

			// Cria um cesto vazio, ja apontado pelo unico elemento do diretorio
			Cesto c = new Cesto(quantidadeDadosPorCesto);
			bd = c.toByteArray();
			arqCestos.seek(0);
			arqCestos.write(bd);
		}
	}
	//sobrecarga do metodo create para criar o arquivo e o hash ao mesmo tempo
	public boolean create(int chave, ArquivoMestre arquivoMestre, Prontuario prontuario) throws Exception {
		// retorna true se inseriu sem colisao. false se colidiu e inseriu.
		// Carrega o diretorio
		byte[] bd = new byte[(int) arqDiretorio.length()];
		arqDiretorio.seek(0);
		arqDiretorio.read(bd);
		diretorio = new Diretorio();
		diretorio.fromByteArray(bd);

		// Identifica a hash do diretorio,
		int i = diretorio.hash(chave);

		// Recupera o cesto
		long enderecoCesto = diretorio.endereco(i);
		Cesto c = new Cesto(quantidadeDadosPorCesto);
		byte[] ba = new byte[c.size()];
		arqCestos.seek(enderecoCesto);
		arqCestos.read(ba);
		c.fromByteArray(ba);

		// Testa se a chave ja nao existe no cesto
		if (c.read(chave) != -1)
			throw new Exception("Chave ja existe");

		// Testa se o cesto ja nao esta cheio
		// Se nao estiver, create o par de chave e dado
		if (!c.full()) {
			// Insere a chave no cesto e o atualiza (já criando esse prontuario tambem no arquivo e retornando sua posição que foi inserida)
			c.create(chave, arquivoMestre.escreveNoArq(prontuario));
			arqCestos.seek(enderecoCesto);
			arqCestos.write(c.toByteArray());
			return true;
		}
		//chama o metodo create (que não foi sobrecarregado) e já passa o metodo q cria no arquivo e que retorna a posição que foi inserida
		create(chave, arquivoMestre.escreveNoArq(prontuario));// insere novamente o dado colidido.
		return false;

	}
	public boolean create(int chave, long dado) throws Exception {
        //retorna true se inseriu sem colisao. false se colidiu e inseriu.
        //Carrega o diretorio
        byte[] bd = new byte[(int)arqDiretorio.length()];
        arqDiretorio.seek(0);
        arqDiretorio.read(bd);
        diretorio = new Diretorio();
        diretorio.fromByteArray(bd);        
        
        // Identifica a hash do diretorio,
        int i = diretorio.hash(chave);
        
        // Recupera o cesto
        long enderecoCesto = diretorio.endereco(i);
        Cesto c = new Cesto(quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);
        
        // Testa se a chave ja nao existe no cesto
        if(c.read(chave)!=-1)
            throw new Exception("Chave ja existe");     

        // Testa se o cesto ja nao esta cheio
        // Se nao estiver, create o par de chave e dado
        if(!c.full()) {
            // Insere a chave no cesto e o atualiza 
            c.create(chave, dado);
            arqCestos.seek(enderecoCesto);
            arqCestos.write(c.toByteArray());
            return true;        
        }
        
        // Duplica o diretorio
        byte pl = c.profundidadeLocal;
        if(pl>=diretorio.profundidadeGlobal)
            diretorio.duplica();
        byte pg = diretorio.profundidadeGlobal;

        // Cria os novos cestos, com os seus dados no arquivo de cestos
        Cesto c1 = new Cesto(quantidadeDadosPorCesto, pl+1);
        arqCestos.seek(enderecoCesto); //volta ponteiro pro inicio do 'cesto' no arquivo dos cestos.
        arqCestos.write(c1.toByteArray());//escreve o novo cesto 1.

        Cesto c2 = new Cesto(quantidadeDadosPorCesto, pl+1);
        long novoEndereco = arqCestos.length(); //novo endereco no final do arquivo de cestos.
        arqCestos.seek(novoEndereco);
        arqCestos.write(c2.toByteArray()); //escreve novo cesto no final do arquivo, como na tabela hash da aula.
        
        // Atualiza os dados no diretorio
        int inicio = diretorio.hash2(chave, c.profundidadeLocal);
        int deslocamento = (int)Math.pow(2,pl);
        int max = (int)Math.pow(2,pg);
        boolean troca = false;
        for(int j=inicio; j<max; j+=deslocamento) {
            if(troca)
                diretorio.atualizaEndereco(j,novoEndereco); //coloca na posicao j o novo cesto criado.
            troca=!troca;
        }
        
        // Atualiza o arquivo do diretorio
        bd = diretorio.toByteArray();
        arqDiretorio.seek(0);
        arqDiretorio.write(bd);
        
        // Reinsere as chaves
        for(int j=0; j<c.quantidade; j++) {
            create(c.chaves[j], c.dados[j]); //reinsere cada chave no cesto correto.
        }
        create(chave,dado);//insere novamente o dado colidido.
        return false;   

    }

    public long read(int chave) throws Exception {
        
        //Carrega o diretorio
        byte[] bd = new byte[(int)arqDiretorio.length()];
        arqDiretorio.seek(0);
        arqDiretorio.read(bd);
        diretorio = new Diretorio();
        diretorio.fromByteArray(bd);
        // Identifica a hash do diretorio,
        int i = diretorio.hash(chave);
        
        // Recupera o cesto
        long enderecoCesto = diretorio.endereco(i);
        Cesto c = new Cesto(quantidadeDadosPorCesto);
        byte[] ba = new byte[c.size()];
        arqCestos.seek(enderecoCesto);
        arqCestos.read(ba);
        c.fromByteArray(ba);
        
        return c.read(chave);
    }
    // sobrecarga do metodo de ler que retorna valor, esse metodo é usado para ler o arquivo
	public void read(int chave, ArquivoMestre arquivoMestre) throws Exception {
		// Carrega o diretorio
		byte[] bd = new byte[(int) arqDiretorio.length()];
		arqDiretorio.seek(0);
		arqDiretorio.read(bd);
		diretorio = new Diretorio();
		diretorio.fromByteArray(bd);

		// Identifica a hash do diretorio,
		int i = diretorio.hash(chave);

		// Recupera o cesto
		long enderecoCesto = diretorio.endereco(i);
		Cesto c = new Cesto(quantidadeDadosPorCesto);
		byte[] ba = new byte[c.size()];
		arqCestos.seek(enderecoCesto);
		arqCestos.read(ba);
		c.fromByteArray(ba);
	    if(c.read(chave) == -1) {
	    	System.out.println("\nNão existe este dado buscado");
	    }else {
	    	arquivoMestre.lerNoArq(c.read(chave));
	    }
	}
	// metodo usado para ler o hash e manda a posição do prontuario no arquivo para poder fazer a alteração
	public void readEditar(int chave, ArquivoMestre arquivoMestre,Scanner console,HashExtensivel hashExtensivel) throws Exception {
		// Carrega o diretorio
		byte[] bd = new byte[(int) arqDiretorio.length()];
		arqDiretorio.seek(0);
		arqDiretorio.read(bd);
		diretorio = new Diretorio();
		diretorio.fromByteArray(bd);

		// Identifica a hash do diretorio,
		int i = diretorio.hash(chave);
		
		// Recupera o cesto
		long enderecoCesto = diretorio.endereco(i);
		Cesto c = new Cesto(quantidadeDadosPorCesto);
		byte[] ba = new byte[c.size()];
		arqCestos.seek(enderecoCesto);
		arqCestos.read(ba);
		c.fromByteArray(ba);
	    if(c.read(chave) == -1) {
	    	//caso não exista o cpf que foi buscado
	    	System.out.println("\nNão existe este dado buscado");
	    }else {
	    	//cria um vetor de 400 posições
	    	char[] aux = new char[400];
	    	//string que vai receber o que o usuario digitar
	    	String anotacao;
	    	//recebe o que o usuario digitou (só pode ser até 400 caracteres)
	    	System.out.println("\nO prontuario foi encontrado, digite uma anotação para editar este arquivo a anotação pode ter até 400 Caracteres");
	    	anotacao = console.nextLine();
	    	//while usado para ver se a string possui mais de 400 caracteres e se possuir não sai da estrutura de repetição até enviar uma anotação com menos de 400 caracteres
	    	while(!(anotacao.length() <= 400)) {
	    		System.out.println("\nDigite novamente a anotação só pode conter no maximo 400 caracteres");
		    	anotacao = console.nextLine();
	    	}
	    	// for que escreve no vetor cada posição da string enquanto não ultrapassou seu tamanho, e se ultrapassar quer dizer que escreveu ela toda e assim insere posições vazias nas próximas posições
	    	for(int j = 0; j < aux.length;j++) {
				if(j < anotacao.length()) {
					aux[j] = anotacao.charAt(j);
					continue;
				}
				if(aux.length >= anotacao.length() &&  j < aux.length ){
					aux[j] = ' ';
				}
			}
	    	//chama o metodo de editar o arquivo passando a posição do prontuario, vetor de char, e a hash 
	    	arquivoMestre.editarNoArquivo(c.read(chave),aux,hashExtensivel);
	    }
	}
	public boolean update(int chave, long novoDado) throws Exception {

		// Carrega o diretorio
		byte[] bd = new byte[(int) arqDiretorio.length()];
		arqDiretorio.seek(0);
		arqDiretorio.read(bd);
		diretorio = new Diretorio();
		diretorio.fromByteArray(bd);

		// Identifica a hash do diretorio,
		int i = diretorio.hash(chave);

		// Recupera o cesto
		long enderecoCesto = diretorio.endereco(i);
		Cesto c = new Cesto(quantidadeDadosPorCesto);
		byte[] ba = new byte[c.size()];
		arqCestos.seek(enderecoCesto);
		arqCestos.read(ba);
		c.fromByteArray(ba);

		// atualiza o dado
		if (!c.update(chave, novoDado))
			return false; // se nao conseguiu atualizar.

		// Atualiza o cesto
		arqCestos.seek(enderecoCesto);
		arqCestos.write(c.toByteArray()); // re-escreve o cesto no arquivo de cestos.
		return true;

	}

	public boolean delete(int chave) throws Exception {

		// Carrega o diretorio
		byte[] bd = new byte[(int) arqDiretorio.length()];
		arqDiretorio.seek(0);
		arqDiretorio.read(bd);
		diretorio = new Diretorio();
		diretorio.fromByteArray(bd);

		// Identifica a hash do diretorio, procura a posicao que aponta pro cesto onde a
		// chave esta.
		int i = diretorio.hash(chave);

		// Recupera o cesto
		long enderecoCesto = diretorio.endereco(i);
		Cesto c = new Cesto(quantidadeDadosPorCesto);
		byte[] ba = new byte[c.size()];
		arqCestos.seek(enderecoCesto);
		arqCestos.read(ba);
		c.fromByteArray(ba);

		// delete a chave
		if (!c.delete(chave))
			return false; // se nao conseguiu deletar(dado nao encontrado ou bucket vazio).

		// Atualiza o cesto
		arqCestos.seek(enderecoCesto);
		arqCestos.write(c.toByteArray()); // re-escreve o cesto no arquivo de cestos.
		return true;
	}
	//metodo sobrecarregado(possui dois metodos com mesmo nome para parametros diferentes) usado para deletar no arquvio e no hash
	public boolean delete(int chave,ArquivoMestre arquivoMestre) throws Exception {

		// Carrega o diretorio
		byte[] bd = new byte[(int) arqDiretorio.length()];
		arqDiretorio.seek(0);
		arqDiretorio.read(bd);
		diretorio = new Diretorio();
		diretorio.fromByteArray(bd);

		// Identifica a hash do diretorio, procura a posicao que aponta pro cesto onde a
		// chave esta.
		int i = diretorio.hash(chave);

		// Recupera o cesto
		long enderecoCesto = diretorio.endereco(i);
		Cesto c = new Cesto(quantidadeDadosPorCesto);
		byte[] ba = new byte[c.size()];
		arqCestos.seek(enderecoCesto);
		arqCestos.read(ba);
		c.fromByteArray(ba);
		// delete a chave
		//no if chama o delete do texto passando a chave, arquivo e a posição do prontuario
		if (!c.delete(chave,arquivoMestre,c.read(chave))) {
			return false; // se nao conseguiu deletar(dado nao encontrado ou bucket vazio).
		}
		
		// Atualiza o cesto
		arqCestos.seek(enderecoCesto);
		arqCestos.write(c.toByteArray()); // re-escreve o cesto no arquivo de cestos.
		return true;
	}


	public void print() {
		try {
			byte[] bd = new byte[(int) arqDiretorio.length()]; // novo arrayByte do tamanho do diretorio.
			arqDiretorio.seek(0);
			arqDiretorio.read(bd);
			diretorio = new Diretorio(); // inicializa diretorio vazio para colar o real.
			diretorio.fromByteArray(bd);
			System.out.println("\nDIRETORIO:");
			System.out.println(diretorio);

			System.out.println("\nCESTOS:");
			arqCestos.seek(0);
			while (arqCestos.getFilePointer() != arqCestos.length()) {
				Cesto c = new Cesto(quantidadeDadosPorCesto);
				byte[] ba = new byte[c.size()]; // vetor de bytes do tamanho do cesto sendo lido.
				arqCestos.read(ba);
				c.fromByteArray(ba);
				System.out.println(c);// printa o cesto na tela.
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//limpa o diretorio e o cesto
	public void limpaTudo(int n, String nomeArqDiretorio, String nomeArqCesto) throws Exception{
		arqDiretorio.setLength(0);
		arqCestos.setLength(0);
		quantidadeDadosPorCesto = n;
		nomeArquivoDiretorio = nomeArqDiretorio;
		nomeArquivoCestos = nomeArqCesto;

		arqDiretorio = new RandomAccessFile(nomeArquivoDiretorio, "rw");
		arqCestos = new RandomAccessFile(nomeArquivoCestos, "rw");

		// Se o diretorio ou os cestos estiverem vazios, cria um novo diretorio e lista
		// de cestos
		if (arqDiretorio.length() == 0 || arqCestos.length() == 0) {

			// Cria um novo diretorio, com profundidade de 0 bits (1 unico elemento)
			diretorio = new Diretorio();
			byte[] bd = diretorio.toByteArray();
			arqDiretorio.write(bd);

			// Cria um cesto vazio, ja apontado pelo unico elemento do diretorio
			Cesto c = new Cesto(quantidadeDadosPorCesto);
			bd = c.toByteArray();
			arqCestos.seek(0);
			arqCestos.write(bd);
		}
		System.out.println("\nO diretório e cesto foram limpos");
	}


}
