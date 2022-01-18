package TrabalhoHashExtensivel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Prontuario {
	// nome, data de nascimento, sexo e uma área de m caracteres/bytes para
	// anotações do médico
	private int cpf;
	private String nome;
	private String dataDeNascimento;
	private String sexo;
	private char[] anotacao;

	// construtor padrão
	public Prontuario() {
		String aux = "não existe anotações no momento";
		// cpf começa como 0
		this.cpf = 0;
		// aceita até 400 caracteres em anotações e começa escrevendo no vetor de char
		// que não existe anotações no momento
		anotacao = new char[400];
		/*
		 * o for escreve no char o valor de uma estring examinando o tamanho dela e caso
		 * o vetor termine de escrever a string ele deixa todo o resto em branco pra
		 * caso tivesse algo escrito antes
		 */
		for (int i = 0; i < anotacao.length; i++) {
			if (i < aux.length()) {
				anotacao[i] = aux.charAt(i);
				continue;
			}
			if (anotacao.length >= aux.length() && i < anotacao.length) {
				anotacao[i] = ' ';
			}
		}

	}

	// contrutor com parametros para acessar os atributos privados da classe e
	// atribuir valores (anotações não recebe valor pelo construtor pois começa com
	// valor de não tem anotações no momento até o médico editar")
	public Prontuario(int cpf, String nome, String dataDeNascimento, String sexo) {
		String aux = "não existe anotações no momento";
		this.setCpf(cpf);
		this.setNome(nome);
		this.setDataDeNascimento(dataDeNascimento);
		this.setSexo(sexo);
		// aceita até 400 caracteres em anotações e começa escrevendo no vetor de char
		// que não existe anotações no momento
		anotacao = new char[400];
		/*
		 * o for escreve no char o valor de uma estring examinando o tamanho dela e caso
		 * o vetor termine de escrever a string ele deixa todo o resto em branco pra
		 * caso tivesse algo escrito antes
		 */
		for (int i = 0; i < anotacao.length; i++) {
			if (i < aux.length()) {
				anotacao[i] = aux.charAt(i);
				continue;
			}
			if (anotacao.length >= aux.length() && i < anotacao.length) {
				anotacao[i] = ' ';
			}
		}
	}

	public String toString() {
		// cria uma string para auxiliar no retorno que começa como sem nada
		String aux = "";
		System.out.println("\n");
		// o for percorre todo o char e vai inserindo na string cada caractere
		for (int i = 0; i < anotacao.length; i++) {
			aux += anotacao[i];
		}
		// para retornar o que estava guardado no char retorna a string que recebeu os
		// caracteres do char
		return "\nCPF: " + this.cpf + "\nNome: " + this.nome + "\nData de nascimento: " + this.dataDeNascimento
				+ "\nSexo: " + this.sexo + "\nAnotações: " + aux;
	}

	// metodo usado pra inserir no arquivo
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		// escreve os dados
		dos.writeInt(cpf);
		dos.writeUTF(nome);
		dos.writeUTF(dataDeNascimento);
		dos.writeUTF(sexo);
		int i = 0;
		// insere todos caracteres presentes no vetor de char
		while (i < anotacao.length) {
			dos.writeChar(anotacao[i]);
			i++; //
		}

		return baos.toByteArray();
	}

	// metodo usado para ler no arquivo
	public void fromByteArray(byte[] ba) throws IOException {
		ByteArrayInputStream bais = new ByteArrayInputStream(ba);
		DataInputStream dis = new DataInputStream(bais);
		// recebe cpf
		cpf = dis.readInt();
		//recebe nome
		nome = dis.readUTF();
		//recebe data de nascimento
		dataDeNascimento = dis.readUTF();
		//recebe o sexo
		sexo = dis.readUTF();
		//recebe todos caracteres do vetor de char que estão armazenados
		for (int i = 0; i < 400; i++) {
			anotacao[i] = dis.readChar();
		}
	}

	// metodos get e set
	public int getCpf() {
		return cpf;
	}

	public void setCpf(int cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDataDeNascimento() {
		return dataDeNascimento;
	}

	public void setDataDeNascimento(String dataDeNascimento) {
		this.dataDeNascimento = dataDeNascimento;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public char[] getAnotacao() {
		return anotacao;
	}

	public void setAnotacao(char[] anotacao) {
		this.anotacao = anotacao;
	}

}
