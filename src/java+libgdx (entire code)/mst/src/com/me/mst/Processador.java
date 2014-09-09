package com.me.mst;

import java.util.ArrayList;
import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Processador extends Thread {
	public volatile static ArrayList<Processador> processadores; //arraylist apontando para os processadores
	//public static ArrayList<Grafo> subgrafos = new ArrayList<Grafo>(); //todas as threads vão adicionar valores aqui, array de escrita compartilhada, cada thread com seu elemento específico de memória
	private Grafo grafo; //grafo que a thread conterá/trabalhará em cima
	public boolean finished = true; //booleano disponível pra saber se as threads já terminaram e quais terminaram
	private ArrayList<Vector2> arestasmarcadas = new ArrayList<Vector2>();
	
	public Processador(int verticeid){ //verticeid é o vértice inicial que o grafo será criado/conterá
		this.grafo = new Grafo(verticeid); //cria um novo grafo pro processador conter
		this.grafo.sincronizarVertice(verticeid);
	}
	
	public void run(){
		this.finished = false;
		Grafo g = Grafo.grafoentrada;
		Vector3 menorcusto = new Vector3(-1, -1, 999999); //vertice1, vertice2 e custo da aresta
		
		Set<Integer> keysv = this.grafo.vertices.keySet();		
		Set<Vector2> keysa = g.arestas.keySet();
        for(Integer keyv: keysv){//todos os vértices do grafo manipulado pelo processador em questão
	        for(Vector2 keya: keysa){//loop para todas as arestas do grafo de entrada
	        	if (!this.grafo.vertices.contains(keya.y) || !this.grafo.vertices.contains(keya.x)){//se o grafo do processador não contém algum dos outros vértices da aresta, se ocntém os dois, não vai entrar na verificação
		        	if (!arestasmarcadas.contains(keya)){ //se a aresta ainda não foi marcada
		        		if (keyv == keya.x){//o vértice é igual ao primeiro vértice da aresta da iteração
			            		if (menorcusto.z > g.arestas.get(keya)) menorcusto = new Vector3(keyv, keya.y, g.arestas.get(keya)); //se o custo da aresta encontrada é menor que o custo previamente armazenado
			            }else if (keyv == keya.y){//o vértice é igual ao segundo vértice da aresta da iteração
			            		if (menorcusto.z > g.arestas.get(keya)) menorcusto = new Vector3(keya.x, keyv, g.arestas.get(keya)); //se o custo da aresta encontrada é menor que o custo previamente armazenado
			            }
	        		}
	        	}
	        }
		}
        
        //depois de encontrar a menor aresta, adicionar/marcar ao grafo do processador
        if (menorcusto.x != -1) {
        	this.grafo.adicionarAresta((int)menorcusto.x, (int)menorcusto.y);
        	arestasmarcadas.add(new Vector2(menorcusto.x, menorcusto.y));
        }
        this.finished = true;
	}
	
	/*//AJEITAR ESSE MÉTODO \/
	//operação paralela para marcar as arestas de menor custo
	public void run(){
		this.finished = false;
		int tamanhoinicial = this.grafo.vertices.size();
		for (int v=0; v<tamanhoinicial; v++){ //pra todo vértice do grafo que o processador contém
			Vector3 menorcusto = new Vector3(-1, v, 99999); //vetor que vai armazenar o vértice origem, destino, e o custo da aresta entre os dois
			for (int i=0; i<Grafo.grafoentrada.vertices.size(); i++){ //para todo vértice do grafo de entrada
				if (!this.grafo.vertices.contains(i)){ //o grafo associado ao processador não pode conter o vértice que irá adicionar, deve estar em um grafo diferente
					int custoaresta = Grafo.getCustoAresta(v, i); //pegar o custo da aresta entre v e i
					if (custoaresta < menorcusto.z && (custoaresta != -1)){ //se houver aresta entre os dois e o custo for menor que o previamente armazenado
						menorcusto.x = v; //atualizar valores
						menorcusto.y = i;
						menorcusto.z = custoaresta;
					}
				}
			}
			if (menorcusto.x != -1){
				if (Grafo.getCustoAresta((int)menorcusto.x, (int)menorcusto.y) != -1 &&
						(this.grafo.vertices.containsKey((int)menorcusto.x) || (this.grafo.vertices.containsKey((int)menorcusto.y)))){
					System.out.println((int)menorcusto.x + "<->" +(int)menorcusto.y + "Vertices: " + this.grafo.vertices.toString());
					this.grafo.adicionarVertice((int)menorcusto.y);
					this.grafo.adicionarAresta((int)menorcusto.x, (int)menorcusto.y); //marcar ou criar aresta no novo subgrafo
				}
			}
		}
		
		//subgrafos.add(this.grafo); //adiciona o grafo do processador à uma lista de subgrafos momentâneos
		this.finished = true; //marcar em uma flag que o processamento desse processador específico acabou
		//adicionarGrafo();	
	}
	
	
	public synchronized void adicionarGrafo(){
		subgrafos.add(this.grafo);
	}
	public void setarGrafo(Grafo grafo){
		this.grafo = grafo;
		this.finished = true;
	}*/
	
	public static boolean todosTerminaram(){
		for (int i=0; i < processadores.size(); i++){
			if (!processadores.get(i).finished) return false;
		}
		return true;
	}
	
	public Grafo getGrafo(){return this.grafo;}
}
