package TrabalhoHashExtensivel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Cesto {

		byte profundidadeLocal; // profundidade local do cesto
		short quantidade; // quantidade de pares presentes no cesto
		short quantidadeMaxima; // quantidade maxima de pares que o cesto pode conter
		int[] chaves; // sequencia de chaves armazenadas no cesto
		long[] dados; // sequencia de dados correspondentes as chaves
		short bytesPorPar; // size fixo de cada par de chave/dado em bytes
		short bytesPorCesto; // size fixo do cesto em bytes

		public Cesto(int qtdmax) throws Exception {
			this(qtdmax, 0);
		}

		public Cesto(int qtdmax, int pl) throws Exception {
			if (qtdmax > 32767)
				throw new Exception("Quantidade maxima de 32.767 elementos");
			if (pl > 127)
				throw new Exception("Profundidade local maxima de 127 bits");
			profundidadeLocal = (byte) pl;
			quantidade = 0;
			quantidadeMaxima = (short) qtdmax;
			chaves = new int[quantidadeMaxima];
			dados = new long[quantidadeMaxima];
			bytesPorPar = 12; // int + long
			bytesPorCesto = (short) (bytesPorPar * quantidadeMaxima + 3);
		}

		public byte[] toByteArray() throws IOException {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			dos.writeByte(profundidadeLocal);
			dos.writeShort(quantidade);
			int i = 0;
			while (i < quantidade) {
				dos.writeInt(chaves[i]);
				dos.writeLong(dados[i]);
				i++; // escreve os pares chave-endereço no byteArray
			}
			while (i < quantidadeMaxima) {
				dos.writeInt(0);
				dos.writeLong(0);
				i++; // preenche os espaços vazios se existirem no byteArray(ate a qtdMax)
			}
			return baos.toByteArray();
		}

		public void fromByteArray(byte[] ba) throws IOException {
			ByteArrayInputStream bais = new ByteArrayInputStream(ba);
			DataInputStream dis = new DataInputStream(bais);
			profundidadeLocal = dis.readByte();
			quantidade = dis.readShort();
			int i = 0;
			while (i < quantidadeMaxima) {
				chaves[i] = dis.readInt();
				dados[i] = dis.readLong();
				i++; // lê do byteArray os dados do bucket.
			}
		}

		public boolean create(int c, long d) {
			if (full())
				return false; // aparentemente se bucket cheio, retorna falso.
			int i = quantidade - 1;
			while (i >= 0 && c < chaves[i]) {
				chaves[i + 1] = chaves[i];
				dados[i + 1] = dados[i];
				i--; // aparentemente procurando o lugar de c no array, do final pro inicio(i=0).
			}
			i++;
			chaves[i] = c;
			dados[i] = d;
			quantidade++; // guarda c e d na posiçao correta. incrementa qtd de pares no bucket.
			return true;
		}

		public long read(int c) {
			if (empty())
				return -1;
			int i = 0;
			while (i < quantidade && c > chaves[i])
				i++;
			if (i < quantidade && c == chaves[i])
				return dados[i]; // retorna endereço do cesto, se encontrado.
			else
				return -1;
		}

		public boolean update(int c, long d) {
			if (empty())
				return false;
			int i = 0;
			while (i < quantidade && c > chaves[i])
				i++;
			if (i < quantidade && c == chaves[i]) {
				dados[i] = d; // substitui o endereço pelo atualizado.
				return true;
			} else
				return false;
		}

		public boolean delete(int c) {
			if (empty())
				return false;
			int i = 0;
			while (i < quantidade && c > chaves[i])
				i++;
			if (c == chaves[i]) {
				while (i < quantidade - 1) {
					chaves[i] = chaves[i + 1];
					dados[i] = dados[i + 1]; // arrasta os dados para 'trás' do dado sendo excluído.
					i++;
				}
				quantidade--; // reduz a quantidade de pares no cesto. 'exclui' a posicao do dado deletado.
				return true;
			} else
				return false; // se nao encontrado o dado.
		}
		// delete que é usado para deletar o hash e o arquivo (esse é uma sobrecarga do metodo delete do hash para poder deleta no arquivo ao mesmo tempo)
		public boolean delete(int c, ArquivoMestre arquivoMestre,long aux) throws IOException {
			if (empty()) {
				System.out.println("\nDado não existe");
				return false;
			}
			int i = 0;
			while (i < quantidade && c > chaves[i])
				i++;
			if (c == chaves[i]) {
				while (i < quantidade - 1) {
					chaves[i] = chaves[i + 1];
					dados[i] = dados[i + 1]; // arrasta os dados para 'trás' do dado sendo excluído.
					i++;
				}
				quantidade--; // reduz a quantidade de pares no cesto. 'exclui' a posicao do dado deletado.
				
				//atualiao arquivo que foi deletado
				arquivoMestre.excluiNoArquivo(aux);
				return true;
			} else {
				System.out.println("\nDado não existe");
				return false; // se nao encontrado o dado.
			}
		}
		public boolean empty() {
			return quantidade == 0;
		}

		public boolean full() {
			return quantidade == quantidadeMaxima;
		}

		public String toString() {
			String s = "\nProfundidade Local: " + profundidadeLocal + "\nQuantidade: " + quantidade + "\n| ";
			int i = 0;
			while (i < quantidade) {
				s += chaves[i] + ";" + dados[i] + " | "; // concatena em s os pares do bucket.
				i++;
			}
			while (i < quantidadeMaxima) {
				s += "-;- | "; // caso tenham espaços sobrando, concatena em s.
				i++;
			}
			return s;
		}

		public int size() {
			return bytesPorCesto;
		}

	}