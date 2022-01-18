package TrabalhoHashExtensivel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Diretorio {

		byte profundidadeGlobal;
		long[] enderecos;

		public Diretorio() {
			profundidadeGlobal = 0;
			enderecos = new long[1];
			enderecos[0] = 0;
		}

		public boolean atualizaEndereco(int p, long e) {
			if (p > Math.pow(2, profundidadeGlobal))
				return false; // Caso a posicao p seja maior que o tamanho do diretorio.
			enderecos[p] = e; // Atualiza o endereco da posicao p por e.
			return true;
		}

		public byte[] toByteArray() throws IOException {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeByte(profundidadeGlobal);
			int quantidade = (int) Math.pow(2, profundidadeGlobal); // define a qtd de espacos no diretorio.
			int i = 0;
			while (i < quantidade) {
				dos.writeLong(enderecos[i]); // escreve os enderecos nos espacos do diretorio.
				i++;
			}
			return baos.toByteArray();
		}

		public void fromByteArray(byte[] ba) throws IOException {
			ByteArrayInputStream bais = new ByteArrayInputStream(ba);
			DataInputStream dis = new DataInputStream(bais);
			profundidadeGlobal = dis.readByte();
			int quantidade = (int) Math.pow(2, profundidadeGlobal);
			enderecos = new long[quantidade]; // aloca vetor de enderecos pro diretorio.
			int i = 0;
			while (i < quantidade) {
				enderecos[i] = dis.readLong();
				i++;
			}
		}

		public String toString() {
			String s = "\nProfundidade global: " + profundidadeGlobal;
			int i = 0;
			int quantidade = (int) Math.pow(2, profundidadeGlobal);
			while (i < quantidade) {
				s += "\n" + i + ": " + enderecos[i];
				i++;
			}
			return s;
		}

		protected long endereco(int p) {
			if (p > Math.pow(2, profundidadeGlobal))
				return -1;
			return enderecos[p];
		}

		protected boolean duplica() {
			if (profundidadeGlobal == 127)
				return false; // nao duplica o diretorio caso tamanho limite.
			profundidadeGlobal++;
			int q1 = (int) Math.pow(2, profundidadeGlobal - 1); // qtd de espacos no diretorio antes.
			int q2 = (int) Math.pow(2, profundidadeGlobal); // nova qtd de espacos no diretorio.
			long[] novosEnderecos = new long[q2];
			int i = 0;
			while (i < q1) {
				novosEnderecos[i] = enderecos[i];
				i++;
			}
			while (i < q2) {
				novosEnderecos[i] = enderecos[i - q1]; // 'copia' os enderecos pras posicoes restantes(duplica).
				i++;
			}
			enderecos = novosEnderecos; // o array antigo de enderecos passa a ser o novo.
			return true;
		}

		protected int hash(int chave) {
			return chave % (int) Math.pow(2, profundidadeGlobal);
		}

		protected int hash2(int chave, int pl) { // cálculo do hash para uma dada profundidade local
			return chave % (int) Math.pow(2, pl);
		}

	}
